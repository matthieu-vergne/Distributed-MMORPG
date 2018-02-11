package fr.vergne.dmmorpg.sample;

import static fr.vergne.dmmorpg.sample.AWTKeyConsumer.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import fr.vergne.dmmorpg.Renderer;
import fr.vergne.dmmorpg.impl.ComposedRenderer;
import fr.vergne.dmmorpg.sample.persistence.Deserializer;
import fr.vergne.dmmorpg.sample.player.Player;
import fr.vergne.dmmorpg.sample.view.impl.PlayerView;
import fr.vergne.dmmorpg.sample.view.impl.Scaler;
import fr.vergne.dmmorpg.sample.view.impl.renderer.Filler;
import fr.vergne.dmmorpg.sample.view.impl.renderer.PlayerRenderer;
import fr.vergne.dmmorpg.sample.world.World;
import fr.vergne.dmmorpg.sample.world.WorldAction;
import fr.vergne.dmmorpg.sample.world.WorldBuilder;
import fr.vergne.dmmorpg.sample.world.WorldCell;
import fr.vergne.dmmorpg.sample.world.WorldPosition;
import fr.vergne.dmmorpg.sample.world.WorldState;
import fr.vergne.dmmorpg.sample.world.WorldTransition;
import fr.vergne.dmmorpg.sample.world.action.PlayerMove;
import fr.vergne.dmmorpg.sample.world.impl.FinalWorldTransition;
import fr.vergne.dmmorpg.sample.zone.AccessPolicy;
import fr.vergne.dmmorpg.sample.zone.Zone;
import fr.vergne.dmmorpg.sample.zone.impl.ZoneBuilder;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	private final File saveDir = new File("./save");
	private final File zoneDir = new File(saveDir, "zones");
	private final File historyFile = new File(saveDir, "history.yml");
	private final File worldFile = new File(saveDir, "world.yml");
	private final PrintWriter historyWriter;
	private final String longPattern = "(-?\\d+)";
	private final Pattern delimiterPattern = Pattern
			.compile("^\\[" + longPattern + ", " + longPattern + ", " + longPattern + ", " + longPattern + "\\]$");
	private final String zonePattern = "([-_a-zA-Z0-9]+)";
	private final Pattern zoneListPattern = Pattern.compile("^\\{" + zonePattern + "(?:, " + zonePattern + ")*\\}$");

	public Gui() {
		deleteRecursively(saveDir); // TODO Remove
		/* CORE FILES */

		if (saveDir.exists() || saveDir.mkdirs()) {
			// save directory exists
		} else {
			throw new RuntimeException("Cannot create save directory");
		}

		try {
			historyWriter = new PrintWriter(new FileWriter(historyFile, true));
		} catch (IOException cause) {
			throw new RuntimeException(cause);
		}

		try {
			createZoneFiles();
			createWorldFile();
		} catch (FileAlreadyExistsException cause1) {
			// Ignore, just create if does not exist
			// TODO Extract creation & reading in dedicated class
		}

		/* WORLD INSTANCE */

		Map<String, Zone.Type> zones = loadZones();

		Deserializer<World> worldDeserializer = () -> {
			File file = worldFile;
			if (!file.exists()) {
				throw new IllegalArgumentException("Unknown world file: " + file);
			} else {
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					System.out.println("Load world");
					WorldBuilder wBuilder = new WorldBuilder();

					{
						ZoneBuilder zBuilder = new ZoneBuilder();
						String groundDefault = parseLine(reader.readLine(), "groundDefault");
						zBuilder.setDefault(zones.get(groundDefault));
						int layerCount = Integer.parseInt(parseLine(reader.readLine(), "groundLayers"));
						for (int i = 0; i < layerCount; i++) {
							String def = "ground" + i;
							String layer = parseLine(reader.readLine(), def);
							String limits = parseLine(reader.readLine(), def + "Limits");
							WorldPosition start = parseStart(limits);
							WorldPosition stop = parseStop(limits);
							zBuilder.add(zones.get(layer), p -> start.getX() <= p.getX() && p.getX() <= stop.getX()
									&& start.getY() <= p.getY() && p.getY() <= stop.getY());
						}
						wBuilder.setGround(zBuilder.build());
					}

					{
						ZoneBuilder zBuilder = new ZoneBuilder();
						String limits = parseLine(reader.readLine(), "treeLimits");
						WorldPosition start = parseStart(limits);
						WorldPosition stop = parseStop(limits);
						zBuilder.add(zones.get("tree"), p -> start.getX() <= p.getX() && p.getX() <= stop.getX()
								&& start.getY() <= p.getY() && p.getY() <= stop.getY());
						wBuilder.setTrees(zBuilder.build());
					}

					{
						String excluded = parseLine(reader.readLine(), "accessExclude");
						Matcher matcher = zoneListPattern.matcher(excluded);
						if (!matcher.find()) {
							throw new RuntimeException("Illegal access line: " + excluded);
						} else {
							Collection<Zone.Type> excludedZones = new LinkedList<>();
							while (matcher.group(2) != null) {
								String first = matcher.group(1);
								excludedZones.add(zones.get(first));
								excluded = excluded.replaceFirst(Pattern.quote(first + ", "), "");
								matcher = zoneListPattern.matcher(excluded);
								matcher.find();
							}
							excludedZones.add(zones.get(matcher.group(1)));
							AccessPolicy<Object> accessPolicy = (object, world, position, direction) -> {
								WorldCell cell = world.getCell(position);
								return !(excludedZones.contains(cell.getGround())
										|| excludedZones.contains(cell.getTree()));
							};
							wBuilder.setEnterAccessPolicy(accessPolicy);
							wBuilder.setLeaveAccessPolicy(accessPolicy);
						}
					}

					World world = wBuilder.build();

					// TODO Retrieve & apply transitions

					return world;
				} catch (IOException cause) {
					throw new RuntimeException(cause);
				}
			}
		};
		World world = worldDeserializer.load();

		/* LOCAL PLAYER */

		String playerID = "test";
		Player player = world.getPlayer(playerID);
		if (player == null) {
			player = world.addPlayer(playerID, new WorldPosition(0, 0));
		} else {
			// Player already exists
		}

		/* GUI */

		Scaler scaler = new Scaler(32, 32);
		PlayerView worldView = new PlayerView(player, scaler);
		configureKeyboard(world, playerID, scaler);

		Renderer<WorldCell, Graphics> cellRenderer;
		{
			ComposedRenderer<Graphics> renderer = new ComposedRenderer<>();
			for (String name : zones.keySet()) {
				Zone.Type zone = zones.get(name);
				renderer.put(zone, createZoneRenderer(zone));
			}
			renderer.put(player, new PlayerRenderer(new File("res/avatar.png")));
			cellRenderer = (cell, graphics) -> {
				renderer.render(cell.getGround(), graphics);
				renderer.render(cell.getTree(), graphics);
				cell.getPlayers().forEach(p -> renderer.render(p, graphics));
			};
		}
		ViewComponent worldViewComponent = new ViewComponent(world, worldView, cellRenderer);
		scaler.listenUpdate(u -> worldViewComponent.fireRepaint());

		setTitle("DMMORPG");
		setMinimumSize(new Dimension(64, 64));
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(1, 1));
		getContentPane().add(worldViewComponent);
		pack();
	}

	private Map<String, Zone.Type> loadZones() {
		Map<String, Zone.Type> zones = new HashMap<>();
		try (DirectoryStream<Path> paths = Files.newDirectoryStream(zoneDir.toPath())) {
			paths.forEach(path -> {
				File file = path.toFile();
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String name = parseLine(reader.readLine(), "id");
					zones.put(name, new Zone.Type(file));
				} catch (IOException cause) {
					throw new RuntimeException(cause);
				}
			});
		} catch (IOException cause) {
			throw new RuntimeException(cause);
		}
		return zones;
	}

	private void deleteRecursively(File file) {
		if (file.isDirectory()) {
			try (DirectoryStream<Path> paths = Files.newDirectoryStream(file.toPath())) {
				paths.forEach(path -> deleteRecursively(path.toFile()));
			} catch (IOException cause) {
				throw new RuntimeException(cause);
			}
		} else {
			// Nothing to browse
		}
		file.delete();
	}

	private WorldPosition parseStart(String limits) {
		Matcher matcher = delimiterPattern.matcher(limits);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Invalid delimiter: " + limits);
		} else {
			long x = Long.parseLong(matcher.group(1));
			long y = Long.parseLong(matcher.group(2));
			return new WorldPosition(x, y);
		}
	}

	private WorldPosition parseStop(String limits) {
		Matcher matcher = delimiterPattern.matcher(limits);
		if (!matcher.find()) {
			throw new IllegalArgumentException("Invalid delimiter: " + limits);
		} else {
			long x = Long.parseLong(matcher.group(3));
			long y = Long.parseLong(matcher.group(4));
			return new WorldPosition(x, y);
		}
	}

	private void createZoneFiles() throws FileAlreadyExistsException {
		createZoneFile("water", Color.BLUE);
		createZoneFile("earth", Color.ORANGE);
		createZoneFile("snow", Color.WHITE);
		createZoneFile("tree", Color.GREEN);
	}

	private void createWorldFile() throws FileAlreadyExistsException {
		File file = worldFile;
		ensureDirExists(file.getParentFile());
		if (file.exists()) {
			throw new FileAlreadyExistsException("World file already exists: " + file);
		} else {
			try (PrintWriter writer = new PrintWriter(file)) {
				writer.println("groundDefault: water");
				writer.println("groundLayers:  2");
				writer.println("ground0:       earth");
				writer.println("ground0Limits: [-9, -9, 9, 9]");
				writer.println("ground1:       snow");
				writer.println("ground1Limits: [-2, -1, 2, 1]");
				writer.println("treeLimits:    [-2, -2, -2, -2]");
				writer.println("accessExclude: {water, tree}");
			} catch (FileNotFoundException cause) {
				throw new RuntimeException(cause);
			}
		}
	}

	private void createZoneFile(String name, Color color) throws FileAlreadyExistsException {
		File file = new File(zoneDir, name + ".yml");
		ensureDirExists(file.getParentFile());
		if (file.exists()) {
			throw new FileAlreadyExistsException("Zone file already exists: " + file);
		} else {
			try (PrintWriter writer = new PrintWriter(file)) {
				writer.println("id:       " + name);
				writer.println("renderer: filler");
				writer.println("color:    " + color.getRGB());
			} catch (FileNotFoundException cause) {
				throw new RuntimeException(cause);
			}
		}
	}

	private Filler<Zone.Type> createZoneRenderer(Zone.Type type) {
		File file = type.getDescriptor();
		if (!file.exists()) {
			throw new IllegalArgumentException("Unknown zone file: " + file);
		} else {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				System.out.println("Load zone: " + parseLine(reader.readLine(), "id"));
				String renderer = parseLine(reader.readLine(), "renderer");
				if (!renderer.equals("filler")) {
					throw new RuntimeException("Unmanaged renderer: " + renderer);
				} else {
					String color = parseLine(reader.readLine(), "color");
					return new Filler<>(Color.decode(color));
				}
			} catch (IOException cause) {
				throw new RuntimeException(cause);
			}
		}
	}

	private String parseLine(String line, String key) {
		Matcher matcher = Pattern.compile("^" + key + ": *(.*)$").matcher(line);
		if (!matcher.find()) {
			throw new RuntimeException("Unrecognized " + key + " line: " + line);
		} else {
			return matcher.group(1);
		}
	}

	private void ensureDirExists(File dir) {
		if (dir.exists() || dir.mkdirs()) {
			// Use it
		} else {
			throw new RuntimeException("Cannot create directory: " + dir);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (historyWriter != null) {
			historyWriter.close();
		} else {
			// Closed abruptly
		}
		super.finalize();
	}

	private void configureKeyboard(World world, String player, Scaler scaler) {
		// TODO Similarly make a MouseConsumer
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		AWTKeyConsumer consumer = new AWTKeyConsumer();
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_LEFT),
				event -> execute(world, new PlayerMove(player, Direction.LEFT)));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_RIGHT),
				event -> execute(world, new PlayerMove(player, Direction.RIGHT)));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_UP),
				event -> execute(world, new PlayerMove(player, Direction.TOP)));
		consumer.addConsumer(whenKeyPressed(KeyEvent.VK_DOWN),
				event -> execute(world, new PlayerMove(player, Direction.BOTTOM)));
		consumer.addConsumer(whenCharTyped('+'), event -> scaler.increaseScale());
		consumer.addConsumer(whenCharTyped('-'), event -> scaler.decreaseScale());
		focusManager.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
				return consumer.consume(event);
			}
		});
	}

	public void execute(World world, WorldAction action) {
		WorldState origin = () -> world;
		WorldTransition transition = new FinalWorldTransition(System.currentTimeMillis(), origin, action);
		String content = transition.toString();
		historyWriter.println("---");
		historyWriter.println("time: " + transition.getTime());
		historyWriter.println("action: " + transition.getAction());
		historyWriter.flush();
		System.out.println("Saved transition: " + content);

		// TODO Make it such that we actually use the returned world instance
		action.apply(origin).create();
	}

	public static void main(String[] args) {
		new Thread(() -> new Gui().setVisible(true)).start();
	}
}

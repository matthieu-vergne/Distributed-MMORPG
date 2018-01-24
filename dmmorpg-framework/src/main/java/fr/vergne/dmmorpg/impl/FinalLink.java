package fr.vergne.dmmorpg.impl;

import fr.vergne.dmmorpg.View.Link;

public class FinalLink<WorldCell, GraphicsCell> implements Link<WorldCell, GraphicsCell> {

	private final WorldCell worldCell;
	private final GraphicsCell graphicsCell;

	public FinalLink(WorldCell worldCell, GraphicsCell graphicsCell) {
		this.worldCell = worldCell;
		this.graphicsCell = graphicsCell;
	}

	@Override
	public WorldCell getWorldCell() {
		return worldCell;
	}

	@Override
	public GraphicsCell getGraphicsCell() {
		return graphicsCell;
	}

}

package fr.vergne.dmmorpg;

import java.util.Iterator;

public interface View<World, WCell, Graphics, GCell> {

	public Iterator<Link<WCell, GCell>> map(World world, Graphics graphics);

	public interface Link<WCell, GCell> {
		public WCell getWorldCell();

		public GCell getGraphicsCell();
	}
}

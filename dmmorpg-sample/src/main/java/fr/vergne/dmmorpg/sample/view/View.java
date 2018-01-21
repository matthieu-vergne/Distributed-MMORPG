package fr.vergne.dmmorpg.sample.view;

import fr.vergne.dmmorpg.Renderable;
import fr.vergne.dmmorpg.Updatable;
import fr.vergne.dmmorpg.sample.world.WorldUpdate;;

public interface View<Graphics> extends Renderable<Graphics>, Updatable<WorldUpdate> {

}

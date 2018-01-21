package fr.vergne.dmmorpg.impl;

import fr.vergne.dmmorpg.Updatable.Update;

public class BasicUpdate<Source, Property, Value> implements Update<Source, Property, Value> {

	private final Source source;
	private final Property property;
	private final Value oldValue;
	private final Value newValue;

	public BasicUpdate(Source source, Property property, Value oldValue, Value newValue) {
		this.source = source;
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public Source getSource() {
		return source;
	}

	@Override
	public Property getProperty() {
		return property;
	}

	@Override
	public Value getOldValue() {
		return oldValue;
	}

	@Override
	public Value getNewValue() {
		return newValue;
	}
}

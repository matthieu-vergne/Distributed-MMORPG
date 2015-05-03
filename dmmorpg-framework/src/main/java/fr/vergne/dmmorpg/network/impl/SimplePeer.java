package fr.vergne.dmmorpg.network.impl;

import fr.vergne.dmmorpg.network.Peer;

public class SimplePeer implements Peer {

	private final String host;
	private final int port;

	public SimplePeer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof Peer) {
			Peer p = (Peer) obj;
			return getHost().equals(p.getHost()) && getPort() == p.getPort();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Peer(" + host + ":" + port + ")";
	}
}

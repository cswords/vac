package edu.monash.it.student.vac;

import java.net.*;

public class NetworkService {

	InetSocketAddress socketAddress;

	protected String description;

	protected int id;

	protected Protocol baseProtocol;

	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void setSocketAddress(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Protocol getBaseProtocol() {
		return baseProtocol;
	}

	public void setBaseProtocol(Protocol baseProtocol) {
		this.baseProtocol = baseProtocol;
	}

	public enum Protocol {
		TCP, UDP
	}
}

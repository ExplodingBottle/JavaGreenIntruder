package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.net.InetAddress;
import java.net.ServerSocket;

public class ServerThread extends Thread {

	private int port;

	public ServerThread(int port) {
		this.port = port;
	}

	public void run() {
		try {
			ServerSocket socket = new ServerSocket(port, 50, InetAddress.getByName("localhost"));
			while (!interrupted()) {
				try {
					socket.accept().close();
					new BlockedDialogAsyncThread().start();
				} catch (Exception e) {
				}

			}
			socket.close();
		} catch (Exception e) {
		}
	}

}

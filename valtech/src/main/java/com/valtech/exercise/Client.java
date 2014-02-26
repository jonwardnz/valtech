package com.valtech.exercise;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	/**
	 * Reads the command line for action to be send to the server and prints the result. It takes two parameters, host and port.
	 * If not parameters are entered it will default to localhost and 8080.
	 * 
	 * @param args host and port of the server
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		final String host = args.length > 1 ? args[0] : "localhost";
		final int port = args.length >= 2 ? Integer.parseInt(args[1]) : 8080;

		System.out.println("\nValtech client connecting to server on host : "  + host + " and port : " + port + "\n");

		final Client client = new Client(host, port );
		
		System.out.println("Connected!\n");
		System.out.println("Commands : posting  : <user name> -> <message>");
		System.out.println("           reading  : <user name>");
		System.out.println("           following: <user name> follows <another user>");
		System.out.println("           wall     : <user name> wall");
		System.out.println("           quit     : quit\n");

		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		final String POST_TEXT      = " -> ";
		final String FOLLOWING_TEXT = " follows ";
		final String WALL_TEXT      = " wall";

		String line;
		
		while ((line = reader.readLine()) != null) {

			if ("quit".equals(line)) {
				break;
			}
			else if (line.contains(POST_TEXT)) {	
				client.post(line.substring(0, line.indexOf(POST_TEXT)), line.substring(line.indexOf(POST_TEXT) + POST_TEXT.length()));
			}
			else if (line.contains(FOLLOWING_TEXT)) {	
				client.follow(line.substring(0, line.indexOf(FOLLOWING_TEXT)), line.substring(line.indexOf(FOLLOWING_TEXT) + FOLLOWING_TEXT.length()));
			}
			else if (line.contains(WALL_TEXT)) {	
				System.out.print(client.wall(line.substring(0, line.indexOf(WALL_TEXT))));
			}
			else {
				System.out.print(client.read(line));
			}
		}
	}
	
	private final Socket socket;
	private final ObjectOutputStream out;
	private final ObjectInputStream  in;
	
	public Client(final String host, final int port) throws Exception {
		
		this.socket   = new Socket(host, port);
		this.out      = new ObjectOutputStream(socket.getOutputStream());
		this.in       = new ObjectInputStream(socket.getInputStream());
	}
	
	public void post(final String userName, final String message) throws Exception {
		out.writeObject(Action.post(userName, new Post(message)));
	}

	public String read(final String userName) throws Exception {
		out.writeObject(Action.read(userName));
		return (String)in.readObject();
	}

	public void follow(final String userName, final String follow) throws Exception {
		out.writeObject(Action.follow(userName, follow));
	}
	
	public String wall(final String userName) throws Exception {
		out.writeObject(Action.wall(userName));
		return (String)in.readObject();
	}
}

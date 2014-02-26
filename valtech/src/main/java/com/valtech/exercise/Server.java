package com.valtech.exercise;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    /**
     * Creates a socket server to listen for client commands. It stores the client's posts to be sent back on request.
     * It takes one parameters the port. If not parameter is entered it will default to port 8080.
     * 
     * @param args
     * @throws Exception
     */
	public static void main(String[] args) throws Exception {

		final int port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
		
		System.out.println("\nValtech server listening on port : " + port + ". Enter CTL-C to shutdown server.");
		
		@SuppressWarnings("unused")
		Server server = new Server(port);
	}

	// Listens for client requests
	final private ServerSocket listener;
	
	// Stores the messages
	final private ConcurrentHashMap<String, UserMessages> allMessages = new ConcurrentHashMap<String, UserMessages>();
	final private ExecutorService threadPool = Executors.newFixedThreadPool(10);

	
	public Server(final int port) throws Exception {

		listener = new ServerSocket(port);
		
		while (true) {

			ClientHandler clientHandler = new ClientHandler(listener.accept());
			threadPool.submit(clientHandler);
		}
	}
	
	private class ClientHandler implements Runnable {

		private final Socket socket;
		
		ClientHandler(final Socket socket) {
			
			this.socket = socket;
		}
		
		@Override
		public void run() {
			
			try {
				
				final ObjectInputStream  actionStream = new ObjectInputStream(socket.getInputStream());
				final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
	
				Action action = null;
	
				while ((action = (Action) actionStream.readObject()) != null) {
	
					UserMessages userMessages;
	
					switch (action.getActionType()) {
	
					case POST:
	
						userMessages = allMessages.get(action.getUserName());
	
						if (userMessages == null) {
	
							final UserMessages newMessages = new UserMessages(action.getUserName());
	
							userMessages = allMessages.putIfAbsent(action.getUserName(), newMessages);
	
							if (userMessages == null) {	
								userMessages = newMessages;
							}
						}
	
						userMessages.getMessages().add(action.getPost());
	
						break;
	
					case READ:
	
						userMessages = allMessages.get(action.getUserName());
	
						if (userMessages != null) {
							outputStream.writeObject(formatPosts(userMessages.userIterator()));
						}
						else {
							outputStream.writeObject("");
						}
						
						break;
						
					case FOLLOW:
						
						userMessages = allMessages.get(action.getUserName());
						UserMessages followingMessages = allMessages.get(action.getFollow());
						
						if (userMessages != null && followingMessages != null) {
							userMessages.getFollows().add(followingMessages);
						}
						break;

					case WALL:
						
						userMessages = allMessages.get(action.getUserName());
						
						if (userMessages != null) {
							outputStream.writeObject(formatPosts(userMessages.userAndFollowingIterator()));
						}
						else {
							outputStream.writeObject("");
						}
						
						break;
					}
				}
			}
			catch(SocketException|EOFException socketException) {
				
			}
			catch(Exception e){		
				e.printStackTrace();
			}
			finally {
				try {
					socket.close();
				} catch (IOException e) {}
			}
		}

		private String formatPosts(final Iterator<String> messages) throws IOException {
			
			StringBuffer output = new StringBuffer();
			
			while (messages.hasNext()) {
				output.append(messages.next() + '\n');
			}
			return output.toString();
		}
	}
}

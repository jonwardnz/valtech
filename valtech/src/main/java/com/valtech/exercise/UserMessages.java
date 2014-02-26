package com.valtech.exercise;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

class UserMessages {

	private final String userName;
	private final List<Post> messages = new CopyOnWriteArrayList<Post>();
	private final Set<UserMessages> follows  = new CopyOnWriteArraySet<UserMessages>();
	
	UserMessages(final String userName) {
		this.userName = userName;
	}
	
	String getUserName() {
		return userName;
	}

	List<Post> getMessages() {
		return messages;
	}

	Set<UserMessages> getFollows() {
		return follows;
	}
	
	Iterator<String> userIterator() {
		return new UserIterator();
	}
	
	/** 
	 * Reverse iterator to return latest messages first.
	 * 
	 * @author jward
	 *
	 */
	private class UserIterator implements Iterator<String> {

		private final ListIterator<Post> iterator = messages.listIterator(messages.size());
		
		@Override
		public boolean hasNext() {			
			return iterator.hasPrevious();
		}

		@Override
		public String next() {
			return iterator.previous().toString();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();			
		}	
	}
	
	Iterator<String> userAndFollowingIterator() {
		return new UserAndFollowingIterator();
	}
	
	/**
	 * Iterates over all message lists to return latest messages first.
	 * 
	 * @author jward
	 *
	 */
	private class UserAndFollowingIterator implements Iterator<String> {
		
		private final List<MessagesList> messageLists = new ArrayList<MessagesList>();
		private String postMessage;
		
		UserAndFollowingIterator() {
			
			messageLists.add(new MessagesList(userName, messages));
			
			for(UserMessages follow : follows){
				messageLists.add(new MessagesList(follow.getUserName(), follow.getMessages()));
			}  
		}
		
		@Override
		public boolean hasNext() {

			MessagesList nextMessageList = null;
			
			for (MessagesList messageList: messageLists){
				
				if (messageList.hasPosts()) {
					
					if (nextMessageList == null || messageList.getCurrentPostedTime() >  nextMessageList.getCurrentPostedTime()) {
						nextMessageList = messageList;
					}
				}
			}
			
			if (nextMessageList != null) {
				postMessage = nextMessageList.popPostMessage();
				return true;
			}
			else {
				postMessage = null;
				return false;
			}
			
		}

		@Override
		public String next() {
			return postMessage;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();	
		}
		
		
		/**
		 * Used to track progress through message list.
		 * 
		 * @author jward
		 *
		 */
		private class MessagesList {

			private final String userName;
			private final List<Post> messages;
			private int index;
			
			MessagesList(final String userName, final List<Post> messages){
				this.userName = userName;
				this.messages = messages;
				index = this.messages.size()-1;
			}
			
			boolean hasPosts() {
				return index >= 0;
			}
			
			long getCurrentPostedTime() {
				return messages.get(index).getPostedTime();
			}
			
			String popPostMessage() {				
				return userName + " - " + messages.get(index--).toString();
			}
		}
	}
}
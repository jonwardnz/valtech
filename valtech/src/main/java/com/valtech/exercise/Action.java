package com.valtech.exercise;

import java.io.Serializable;

/**
 * Captures the type of action entered by the user. This is sent to the server to process.
 * 
 * @author jward
 *
 */
class Action implements Serializable {

	private static final long serialVersionUID = 6739184818081238338L;

	static Action post(final String userName, final Post post) {
		return new Action(ActionType.POST, userName, null, post);
	}

	static Action read(final String userName) {
		return new Action(ActionType.READ, userName, null, null);
	}

	static Action follow(final String userName, final String follow) {
		return new Action(ActionType.FOLLOW, userName, follow, null);
	}

	static Action wall(final String userName) {
		return new Action(ActionType.WALL, userName, null, null);
	}

	public enum ActionType {
		POST, READ, FOLLOW, WALL		
	};

	
	private final ActionType actionType;
	private final String userName;
	private final String follow;
	private final Post post;

	private Action(final ActionType actionType, final String userName, final String follow, final Post post) {

		this.actionType = actionType;
		this.userName = userName;
		this.follow = follow;
		this.post = post;
	}


	/**
	 *  
	 * @return the actionType
	 */
	ActionType getActionType() {

		return actionType;
	}

	/**
	 * The name of the user that created the action. This will entered for all actions.
	 * 
	 * @return the userName
	 */
	String getUserName() {
		return userName;
	}

	/**
	 * The name of the user to follow. This is only entered for following actions.
	 * 
	 * @return the follow
	 */
	String getFollow() {
		return follow;
	}

	/**
	 * The message entered by the user. This is only entered for posting actions.
	 * 
	 * @return the post
	 */
	Post getPost() {
		return post;
	}

}

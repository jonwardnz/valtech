package com.valtech.exercise;

import java.io.Serializable;

import java.util.Date;

/**
 * Record the message and time of the post.
 * 
 * @author jward
 *
 */
class Post implements Serializable {

	private static final long serialVersionUID = -8456120597896649831L;
	
	private final long postedTime;
	private final String message;

	Post(final String message) {
		this.postedTime = new Date().getTime();
		this.message    = message;
	}

	long getPostedTime() {
		return postedTime;
	}

	@Override
	public String toString() {
		return message  + " (" + getTimeDiff() + ")";
	}
	
	private String getTimeDiff() {
		
		long diff = new Date().getTime() - postedTime;
		
	    long diffSeconds = diff / 1000;
	    long diffMinutes = diff / (60 * 1000);
	    long diffHours   = diff / (60 * 60 * 1000);
	    long diffDays    = diff / (60 * 60 * 1000 * 24);
	    long diffWeeks   = diff / (60 * 60 * 1000 * 24 * 7);
	    long diffMonths  = (long) (diff / (60 * 60 * 1000 * 24 * 30.41666666));
	    long diffYears   = diff / (60 * 60 * 1000 * 24 * 365);

	    if (diffSeconds < 1) {
	        return "less than a second ago";
	    } else if (diffMinutes < 1) {
	        return diffSeconds + " second" + (diffSeconds == 1 ? " " : "s ") + "ago";
	    } else if (diffHours < 1) {
	        return diffMinutes + " minute" + (diffMinutes == 1 ? " " : "s ") + "ago";
	    } else if (diffDays < 1) {
	        return diffHours + " hour"     + (diffHours == 1   ? " " : "s ") + "ago";
	    } else if (diffWeeks < 1) {
	        return diffDays + " day"       + (diffDays == 1    ? " " : "s ") + "ago";
	    } else if (diffMonths < 1) {
	        return diffWeeks + " week"     + (diffWeeks == 1   ? " " : "s ") + "ago";
	    } else if (diffYears < 1) {
	        return diffMonths + " month"   + (diffMonths == 1  ? " " : "s ") + "ago";
	    } else {
	        return diffYears + " year"     + (diffYears == 1   ? " " : "s ") + "agp";
	    }
	}
}

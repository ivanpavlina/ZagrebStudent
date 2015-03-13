package com.exitcode.zagrebstudent;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("data")
public class CommentPost extends ParseObject {
	ParseUser user;
	int markerId;
	String comment;
	int rating;

	public CommentPost() {
		super();
	}

	public String getComment() {
		return getString("comment");
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public int getRating() {
		return getInt("rating");
	}

	public void setComment(ParseUser user, int markerId, String comment,
			int rating) {
		put("user", user);
		put("markerId", markerId);
		put("comment", comment);
		put("rating", rating);
	}

	public static ParseQuery<CommentPost> getQuery() {
		return ParseQuery.getQuery(CommentPost.class);
	}
}

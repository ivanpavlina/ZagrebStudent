package com.exitcode.zagrebstudent;

public interface Communicator_ContentActivity {

	public int getCurrentSelectedMarkerId();

	public String getCurrentSelectedMarkerTitle();

	public void getQueryAdapter();

	public Boolean requestOpenCommentWindow();

	public void sendComment(String commentText, int commentRating);

}

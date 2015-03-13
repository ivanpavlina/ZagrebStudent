package com.exitcode.zagrebstudent;

import com.parse.ParseQueryAdapter;

public interface Communicator_FragmentSecond {

	public void newMarkerSelected();

	public void commentSent();

	public void setAdapter(ParseQueryAdapter<CommentPost> queryAdapter);

}

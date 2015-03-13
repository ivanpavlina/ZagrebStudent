package com.exitcode.zagrebstudent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseQueryAdapter;

public class FragmentSecond extends Fragment implements
		Communicator_FragmentSecond {

	View rootView;

	Communicator_ContentActivity communicator;

	ParseQueryAdapter<CommentPost> currentActiveQueryAdapter;

	View layoutF2View;
	View layoutF2Send;

	Boolean layout1Visible;

	Button btnF2Lyt1Switch, btnF2Lyt2Switch, btnF2Lyt2Send;

	ListView lstvF2Lyt1Posts;

	TextView tVF2Lyt2Message;

	EditText edttF2Lyt2Comment;

	RatingBar rtbF2Lyt2Rating;

	String unsavedCommentText;
	int unsavedCommentRating;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_second, container, false);

		communicator = (Communicator_ContentActivity) getActivity();

		layoutF2View = rootView.findViewById(R.id.lytFragment2View);

		layoutF2Send = rootView.findViewById(R.id.lytFragment2Send);

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("layout1Visible", layout1Visible);

		if (!layout1Visible) {

			outState.putInt("unsavedCommentRating",
					rtbF2Lyt2Rating.getProgress());

			if (edttF2Lyt2Comment.getText().toString() != null) {
				outState.putString("unsavedCommentText", edttF2Lyt2Comment
						.getText().toString());

			}

		}

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			layout1Visible = savedInstanceState.getBoolean("layout1Visible");

		} else {
			layout1Visible = true;
		}

		if (!layout1Visible) {
			if (savedInstanceState.getString("unsavedCommentText") != null) {
				unsavedCommentText = savedInstanceState
						.getString("unsavedCommentText");
			}
			unsavedCommentRating = savedInstanceState
					.getInt("unsavedCommentRating");
		}

		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onResume() {
		if (layout1Visible) {
			showInitializeLayout1();
		} else {
			showInitializeLayout2();
		}

		super.onResume();
	}

	public void showInitializeLayout1() {

		layoutF2View.setVisibility(View.VISIBLE);
		layoutF2Send.setVisibility(View.GONE);
		layout1Visible = true;

		lstvF2Lyt1Posts = (ListView) layoutF2View
				.findViewById(R.id.lVFragment2Lyt1Posts);

		btnF2Lyt1Switch = (Button) layoutF2View
				.findViewById(R.id.btnFragment2Lyt1Switch);

		if (communicator.getCurrentSelectedMarkerId() != 0) {
			communicator.getQueryAdapter();
		}

		initializeListenersLayout1();

	}

	public void showInitializeLayout2() {

		layoutF2Send.setVisibility(View.VISIBLE);
		layoutF2View.setVisibility(View.GONE);
		layout1Visible = false;
		btnF2Lyt2Switch = (Button) layoutF2Send
				.findViewById(R.id.btnF2Lyt2Switch);
		btnF2Lyt2Send = (Button) layoutF2Send.findViewById(R.id.btnF2Lyt2Send);
		tVF2Lyt2Message = (TextView) layoutF2Send
				.findViewById(R.id.tVFragment2Lyt2Message);
		edttF2Lyt2Comment = (EditText) layoutF2Send
				.findViewById(R.id.eTFragment2Lyt2Comment);
		rtbF2Lyt2Rating = (RatingBar) layoutF2Send
				.findViewById(R.id.rBFragment2Lyt2Rating);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Ocjenite i komentirajte ");
		stringBuilder.append(communicator.getCurrentSelectedMarkerTitle());

		tVF2Lyt2Message.setText(stringBuilder.toString());

		initializeListenersLayout2();
	}

	public void initializeListenersLayout1() {

		btnF2Lyt1Switch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (communicator.requestOpenCommentWindow()) {
					showInitializeLayout2();
				}

			}
		});
	}

	public void initializeListenersLayout2() {

		btnF2Lyt2Switch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showInitializeLayout1();
			}
		});

		btnF2Lyt2Send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkSendAllowed()) {

					communicator.sendComment(edttF2Lyt2Comment.getText()
							.toString(), (int) rtbF2Lyt2Rating.getRating());
				}

			}
		});

	}

	public Boolean checkSendAllowed() {
		if (rtbF2Lyt2Rating.getRating() < 1) {
			Toast.makeText(getActivity(), "Ostavite ocjenu", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (edttF2Lyt2Comment.getText().length() < 1) {
			Toast.makeText(getActivity(), "Upišite komentar",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void newMarkerSelected() {

		if (currentActiveQueryAdapter != null)
			currentActiveQueryAdapter.clear();

		if (!layout1Visible) {
			edttF2Lyt2Comment.setText("");
			rtbF2Lyt2Rating.setRating(0);
			showInitializeLayout1();
		}

	}

	@Override
	public void commentSent() {
		showInitializeLayout1();
		edttF2Lyt2Comment.setText("");
		rtbF2Lyt2Rating.setRating(0);
	}

	@Override
	public void setAdapter(ParseQueryAdapter<CommentPost> queryAdapter) {

		currentActiveQueryAdapter = queryAdapter;
		if (!layout1Visible) {
			showInitializeLayout1();
		} else {
			lstvF2Lyt1Posts.setAdapter(currentActiveQueryAdapter);

		}

	}

}
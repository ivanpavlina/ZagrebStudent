package com.exitcode.zagrebstudent;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class FragmentFirst extends Fragment implements
		Communicator_FragmentFirst {

	TextView textViewFragment1Address, textViewFragment1Details;
	RatingBar ratingBarFragment1Score;
	Communicator_ContentActivity communicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_first, container,
				false);

		communicator = (Communicator_ContentActivity) getActivity();

		textViewFragment1Address = (TextView) v
				.findViewById(R.id.tVFragment1Address);
		textViewFragment1Details = (TextView) v
				.findViewById(R.id.tVFragment1Details);
		textViewFragment1Details
				.setMovementMethod(new ScrollingMovementMethod());
		ratingBarFragment1Score = (RatingBar) v
				.findViewById(R.id.rBFragment1Score);

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {

		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void newMarkerSelected() {
		textViewFragment1Address.setText(R.string.empty);
		textViewFragment1Details.setText(R.string.empty);
		ratingBarFragment1Score.setProgress(0);
		ratingBarFragment1Score.setVisibility(View.GONE);
	}

	@Override
	public void setData(String address, String details) {
		textViewFragment1Address.setText(address);
		textViewFragment1Details.setText(Html.fromHtml(details));
		ratingBarFragment1Score.setVisibility(View.VISIBLE);

	}

	@Override
	public void setScore(int score) {
		ratingBarFragment1Score.setProgress(score);
		ratingBarFragment1Score.setVisibility(View.VISIBLE);

	}

}

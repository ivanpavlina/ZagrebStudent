package com.exitcode.zagrebstudent;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setIcon(R.color.transparent);

		ParseObject.registerSubclass(CommentPost.class);
		Parse.initialize(this, "i6uvJEaZaCauzb8aMIQH1q7sNCXsQjGRvgbfoKpF",
				"ANDOSdbBqsHxueVVUGnWLMt4pRX6pNy4PMsepLvz");

		if (ParseUser.getCurrentUser() != null) {
			Intent intent = new Intent(this, ContentActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
		}
	}

}

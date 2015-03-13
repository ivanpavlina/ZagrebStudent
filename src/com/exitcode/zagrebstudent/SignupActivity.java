package com.exitcode.zagrebstudent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity {

	EditText editTextSignupUsername, editTextSignupPassword,
			editTextSignupPasswordAgain, editTextSignupEmail;
	Button btnSignup, btnToLogin;
	Boolean checkSignupData, checkEmail;
	String username;
	String password;
	String passwordAgain;
	String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		getActionBar().setIcon(R.color.transparent);

		editTextSignupUsername = (EditText) findViewById(R.id.eTSignupUsername);
		editTextSignupPassword = (EditText) findViewById(R.id.eTSignupPassword);
		editTextSignupPasswordAgain = (EditText) findViewById(R.id.eTSignupPasswordAgain);
		editTextSignupEmail = (EditText) findViewById(R.id.eTSignupEmail);

		btnSignup = (Button) findViewById(R.id.btnSignupSignup);
		btnToLogin = (Button) findViewById(R.id.btnSignupToLogin);

		btnSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				username = editTextSignupUsername.getText().toString().trim();
				password = editTextSignupPassword.getText().toString().trim();
				passwordAgain = editTextSignupPasswordAgain.getText()
						.toString().trim();
				email = editTextSignupEmail.getText().toString().trim();

				checkSignupData = checkSignupData(username, password,
						passwordAgain, email);

				if (checkSignupData && checkLocationEnabled()
						&& checkInternetEnabled()) {
					signup(username, password, email);
				}
			}
		});

		btnToLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignupActivity.this,
						LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
	}

	public Boolean checkSignupData(String username, String password,
			String passwordAgain, String email) {

		Boolean usernameCheck = username.length() != 0;
		Boolean passwordCheck = password.length() != 0;
		Boolean passwordAgainCheck = passwordAgain.length() != 0;
		Boolean passwordMatch = password.equals(passwordAgain);
		Boolean emailCheck = email.length() != 0;
		Boolean emailFormatCheck = checkEmail(email);

		if (!usernameCheck) {
			Toast.makeText(this,
					getResources().getString(R.string.error_empty_username),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!passwordCheck) {
			Toast.makeText(this,
					getResources().getString(R.string.error_empty_password),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!passwordAgainCheck) {
			Toast.makeText(
					this,
					getResources().getString(
							R.string.error_empty_password_again),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!emailCheck) {
			Toast.makeText(this,
					getResources().getString(R.string.error_empty_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!passwordMatch) {
			Toast.makeText(this,
					getResources().getString(R.string.error_mismatch_password),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!emailFormatCheck) {
			Toast.makeText(this,
					getResources().getString(R.string.error_format_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (usernameCheck && passwordCheck && passwordAgainCheck
				&& passwordMatch && emailCheck && emailFormatCheck) {
			return true;
		}
		return false;
	}

	public static boolean checkEmail(String email) {

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public void signup(String username, String password, String email) {

		final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);
		dialog.setMessage(getString(R.string.progress_signup));
		dialog.show();

		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);

		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				dialog.dismiss();
				if (e != null) {
					Toast.makeText(SignupActivity.this, e.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(SignupActivity.this,
							ContentActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
	}

	public Boolean checkLocationEnabled() {

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					SignupActivity.this);
			builder.setMessage(R.string.error_gps_turned_off)
					.setPositiveButton(
							R.string.error_gps_turned_off_open_settings,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(intent);

								}
							});

			AlertDialog dialog = builder.create();
			dialog.show();

			return false;

		}

		return true;
	}

	public Boolean checkInternetEnabled() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					SignupActivity.this);
			builder.setMessage(R.string.error_internet_not_available)
					.setPositiveButton(
							R.string.error_internet_not_available_open_settings,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											Settings.ACTION_WIFI_SETTINGS);
									startActivity(intent);

								}
							});

			AlertDialog dialog = builder.create();
			dialog.show();

			return false;

		}

		return true;
	}

}
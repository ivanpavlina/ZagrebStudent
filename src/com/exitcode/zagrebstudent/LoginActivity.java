package com.exitcode.zagrebstudent;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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

public class LoginActivity extends Activity {

	EditText editTextLoginUsername, editTextLoginPassword;
	Button btnLogin, btnToSignup;
	Boolean checkLoginData;
	String username;
	String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().setIcon(R.color.transparent);

		editTextLoginUsername = (EditText) findViewById(R.id.eTLoginUsername);
		editTextLoginPassword = (EditText) findViewById(R.id.eTLoginPassword);

		btnLogin = (Button) findViewById(R.id.btnLoginLogin);
		btnToSignup = (Button) findViewById(R.id.btnLoginToSignup);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String username = editTextLoginUsername.getText().toString()
						.trim();
				String password = editTextLoginPassword.getText().toString()
						.trim();

				checkLoginData = checkLoginData(username, password);

				if (checkLoginData && checkLocationEnabled()
						&& checkInternetEnabled()) {
					login(username, password);
				}
			}
		});

		btnToSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						SignupActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	public Boolean checkLoginData(String username, String password) {

		Boolean usernameCheck = username.length() != 0;
		Boolean passwordCheck = password.length() != 0;

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

		if (usernameCheck && passwordCheck) {
			return true;
		}
		return false;
	}

	public void login(String username, String password) {

		final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage(getString(R.string.progress_login));
		dialog.show();

		ParseUser.logInInBackground(username, password, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException e) {
				dialog.dismiss();
				if (e != null) {
					Toast.makeText(LoginActivity.this, e.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(LoginActivity.this,
							ContentActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			};
		});
	}

	public Boolean checkLocationEnabled() {

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
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
					LoginActivity.this);
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

package com.exitcode.zagrebstudent;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.viewpagerindicator.TitlePageIndicator;

public class ContentActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		Communicator_ContentActivity, ConnectionCallbacks,
		com.google.android.gms.location.LocationListener,
		OnConnectionFailedListener

{

	Communicator_FragmentFirst communicatorFirst;
	Communicator_FragmentSecond communicatorSecond;

	FragmentPageAdapter fragmentPageAdapter;
	ViewPager viewPager;
	TitlePageIndicator titlePageIndicator;

	int currentSelectedMarkerId;

	Boolean filterDom = true, filterMen = true, filterSC = true;

	Boolean markerSelected = false;

	GoogleMap map;

	GoogleApiClient mGoogleApiClient;

	LocationRequest mLocationRequest;

	Boolean clientConnected = false;
	LocationManager locationManager;

	Polyline currentPolyline;

	Marker markerDomStjepanRadic, markerDomSara, markerDomCvjetno,
			markerDomLascina, markerMenSavska, markerMenStjepanRadic,
			markerMenCvjetnoNaselje, markerMenLascina, markerMenBorongaj,
			markerMenEkonomija, markerMenMedicina, markerMenVeterina,
			markerMenSumarstvo, markerMenFSB, markerMenALU, markerMenTTF,
			markerSCZagrebacki, markerSCKarlovacki, markerSCSisacki;

	Location LOCATION_CURRENT;
	LatLng LOCATION_ZAGREB = new LatLng(45.815011, 15.981919);

	String[] arrayHeading, arrayAddress, arrayURL, arraySCDetails;

	LatLng arrayLocation[] = {
			// DOMOVI
			(new LatLng(45.784893, 15.942047)),
			(new LatLng(45.796096, 15.957013)),
			(new LatLng(45.793612, 15.963287)),
			(new LatLng(45.821038, 15.999391)),

			// MENZE
			(new LatLng(45.803869, 15.965692)),
			(new LatLng(45.784878, 15.944665)),
			(new LatLng(45.793560, 15.963254)),
			(new LatLng(45.821038, 15.999391)),
			(new LatLng(45.809509, 16.022422)),
			(new LatLng(45.816002, 16.011470)),
			(new LatLng(45.818956, 15.985129)),
			(new LatLng(45.805408, 16.008567)),
			(new LatLng(45.831534, 16.029440)),
			(new LatLng(45.795287, 15.971348)),
			(new LatLng(45.812446, 15.964512)),
			(new LatLng(45.812098, 15.937294)),

			// SCI
			(new LatLng(45.803480, 15.966572)),
			(new LatLng(45.805666, 15.973192)),
			(new LatLng(45.812301, 15.963639))

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		getActionBar().setIcon(R.color.transparent);

		// Initalize fragments
		fragmentPageAdapter = new FragmentPageAdapter(
				getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(fragmentPageAdapter);
		titlePageIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		titlePageIndicator.setViewPager(viewPager);

		// Initalize map
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
				LOCATION_ZAGREB, 12);
		map.animateCamera(update);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(ContentActivity.this)
				.addOnConnectionFailedListener(this).build();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Inizilize arrays
		arrayHeading = getResources().getStringArray(R.array.marker_heading);
		arrayAddress = getResources().getStringArray(R.array.marker_address);
		arraySCDetails = getResources().getStringArray(R.array.sc_details);
		arrayURL = getResources().getStringArray(R.array.marker_url);

		// Start checkers
		checkInternetEnabled();
		checkLocationEnabled();

		initializeMarkers();
		initializeListeners();

	}

	@Override
	protected void onPause() {
		mGoogleApiClient.disconnect();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mGoogleApiClient.connect();
		super.onResume();
		initializeCommunicators();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		if (markerSelected) {
			outState.putBoolean("markerSelected", true);
			outState.putInt("currentSelectedMarkerId", currentSelectedMarkerId);
		} else {
			outState.putBoolean("markerSelected", false);
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		markerSelected = savedInstanceState.getBoolean("markerSelected");
		if (markerSelected) {
			currentSelectedMarkerId = savedInstanceState
					.getInt("currentSelectedMarkerId");

			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
					arrayLocation[currentSelectedMarkerId], 13);
			map.animateCamera(update);
			initializeCommunicators();

			getActionBar().setTitle(arrayHeading[currentSelectedMarkerId]);

			new taskGetDetails().execute();

		}

		super.onRestoreInstanceState(savedInstanceState);

	}

	private void initializeMarkers() {

		markerDomStjepanRadic = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
				.position(arrayLocation[0]).title(arrayHeading[0])
				.snippet(arrayAddress[0]));

		markerDomSara = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
				.position(arrayLocation[1]).title(arrayHeading[1])
				.snippet(arrayAddress[1]));

		markerDomCvjetno = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
				.position(arrayLocation[2]).title(arrayHeading[2])
				.snippet(arrayAddress[2]));

		markerDomLascina = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
				.position(arrayLocation[3]).title(arrayHeading[3])
				.snippet(arrayAddress[3]));

		markerMenSavska = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[4]).title(arrayHeading[4])
				.snippet(arrayAddress[4]));

		markerMenStjepanRadic = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[5]).title(arrayHeading[5])
				.snippet(arrayAddress[5]));

		markerMenCvjetnoNaselje = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[6]).title(arrayHeading[6])
				.snippet(arrayAddress[6]));

		markerMenLascina = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[7]).title(arrayHeading[7])
				.snippet(arrayAddress[7]));

		markerMenBorongaj = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[8]).title(arrayHeading[8])
				.snippet(arrayAddress[8]));

		markerMenEkonomija = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[9]).title(arrayHeading[9])
				.snippet(arrayAddress[9]));

		markerMenMedicina = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[10]).title(arrayHeading[10])
				.snippet(arrayAddress[10]));

		markerMenVeterina = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[11]).title(arrayHeading[11])
				.snippet(arrayAddress[11]));

		markerMenSumarstvo = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[12]).title(arrayHeading[12])
				.snippet(arrayAddress[12]));

		markerMenFSB = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[13]).title(arrayHeading[13])
				.snippet(arrayAddress[13]));

		markerMenALU = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[14]).title(arrayHeading[14])
				.snippet(arrayAddress[14]));

		markerMenTTF = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(arrayLocation[15]).title(arrayHeading[15])
				.snippet(arrayAddress[15]));

		markerSCZagrebacki = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
				.position(arrayLocation[16]).title(arrayHeading[16])
				.snippet(arrayAddress[16]));

		markerSCSisacki = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
				.position(arrayLocation[17]).title(arrayHeading[17])
				.snippet(arrayAddress[17]));

		markerSCKarlovacki = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
				.position(arrayLocation[18]).title(arrayHeading[18])
				.snippet(arrayAddress[18]));
	}

	public void initializeListeners() {

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				int i;

				for (i = 0; i < arrayHeading.length; i++) {
					Boolean check = arrayHeading[i].equals(marker.getTitle());

					if (check) {
						break;
					}
				}

				if (checkInternetEnabled()) {
					newMarkerSetup(i);
				}

				return true;

			}
		});

		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				if (currentPolyline != null && currentPolyline.isVisible()) {
					currentPolyline.remove();
				}

			}
		});

	}

	public void newMarkerSetup(int i) {

		markerSelected = true;
		currentSelectedMarkerId = i;
		getActionBar().setTitle(arrayHeading[currentSelectedMarkerId]);

		initializeCommunicators();
		communicatorFirst.newMarkerSelected();
		communicatorSecond.newMarkerSelected();

		new taskGetDetails().execute();

	}

	public Boolean checkMarkerSelected() {
		if (!markerSelected) {
			Toast.makeText(ContentActivity.this,
					R.string.error_no_selected_marker, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public Boolean checkEmailVerified() {

		if (!ParseUser.getCurrentUser().getBoolean("emailVerified")
				&& !ParseUser.getCurrentUser().isNew()) {
			Toast.makeText(ContentActivity.this,
					R.string.error_email_not_verified, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public Boolean checkLocationEnabled() {

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					ContentActivity.this);
			builder.setCancelable(false)
					.setMessage(R.string.error_gps_turned_off)
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
					ContentActivity.this);
			builder.setCancelable(false)
					.setMessage(R.string.error_internet_not_available)
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

	public void initializeCommunicators() {
		communicatorFirst = (Communicator_FragmentFirst) fragmentPageAdapter
				.getRegisteredFragment(0);

		communicatorSecond = (Communicator_FragmentSecond) fragmentPageAdapter
				.getRegisteredFragment(1);

	}

	private void findClosestMen() {

		int closestMenId = 0;
		if (checkCurrentLocation()) {
			double closestDistance = 999999999;
			for (int i = 3; i < 15; i++) {

				double distance = distFrom(LOCATION_CURRENT.getLatitude(),
						LOCATION_CURRENT.getLongitude(),
						arrayLocation[i].latitude, arrayLocation[i].longitude);

				if (closestDistance > distance) {

					closestDistance = distance;
					closestMenId = i;
				}

			}

			newMarkerSetup(closestMenId);
			new taskGetPolyline(closestMenId).execute();
		}

	}

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return dist * meterConversion;
	}

	private List<LatLng> decodePolyLine(final String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng(((lat / 1E5)), ((lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	@Override
	public int getCurrentSelectedMarkerId() {
		return currentSelectedMarkerId;
	}

	// FRAGMENT2

	@Override
	public Boolean requestOpenCommentWindow() {
		if (checkMarkerSelected() && checkEmailVerified()) {
			return true;
		}
		return false;

	}

	@Override
	public void sendComment(String commentText, int commentRating) {
		hideKeyboard();
		new taskSendComment(commentText, commentRating).execute();

	}

	@Override
	public void getQueryAdapter() {
		new taskGetComment().execute();

	}

	private Boolean checkCurrentLocation() {
		if (LOCATION_CURRENT == null) {
			Toast.makeText(ContentActivity.this,
					"Ne postoji trenutna lokacija, pokušajte ponovno",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		View view = this.getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private class taskGetDetails extends AsyncTask<Void, Void, Void> {

		String parsedDetails;
		ProgressDialog dialog;
		Document document;
		ParseQuery<CommentPost> parseQuery;
		int score;
		int i = 0;
		Boolean selectedSC;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ContentActivity.this);
			dialog.setTitle(getResources().getText(R.string.progress_details));
			dialog.setMessage(getResources().getText(
					R.string.progress_please_wait));
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

			if (currentSelectedMarkerId >= 16) {
				selectedSC = true;
			} else {
				selectedSC = false;
			}

		}

		@Override
		protected Void doInBackground(Void... params) {

			if (!selectedSC) {

				try {
					document = Jsoup.connect(arrayURL[currentSelectedMarkerId])
							.get();

				} catch (Exception e) {

				}
			}

			parseQuery = new ParseQuery<CommentPost>("data");

			parseQuery.whereEqualTo("markerId", currentSelectedMarkerId);
			parseQuery.findInBackground(new FindCallback<CommentPost>() {

				@Override
				public void done(List<CommentPost> objects, ParseException e) {
					if (e == null) {
						for (ParseObject scoreObject : objects) {
							score = score + scoreObject.getInt("rating");
							i++;
						}

						if (i > 0 && score > 0) {
							initializeCommunicators();
							communicatorFirst.setScore(score / i);
						}
					}

				}
			});

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			initializeCommunicators();

			if (!selectedSC) {
				if (document != null) {
					Parser parser = new Parser();
					parsedDetails = parser.Parse(currentSelectedMarkerId,
							document);
				}

				if (parsedDetails != null) {

					communicatorFirst.setData(
							arrayAddress[currentSelectedMarkerId],
							parsedDetails);
				} else {

					communicatorFirst
							.setData(
									arrayAddress[currentSelectedMarkerId],
									getResources().getText(
											R.string.error_downloading_data)
											.toString());
				}
			}

			else {
				communicatorFirst.setData(
						arrayAddress[currentSelectedMarkerId],
						arraySCDetails[currentSelectedMarkerId - 16]);
			}
			dialog.dismiss();

			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
					arrayLocation[currentSelectedMarkerId], 13);
			map.animateCamera(update);

			new taskGetComment().execute();

		}

	}

	private class taskGetComment extends AsyncTask<Void, Void, Void> {

		ParseQueryAdapter.QueryFactory<CommentPost> queryAdapterFactory;
		ParseQuery<CommentPost> parseQuery;
		ProgressDialog progressDialog;
		ParseQueryAdapter<CommentPost> queryAdapter;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ContentActivity.this);
			progressDialog.setTitle(getResources().getText(
					R.string.progress_download_comments));
			progressDialog.setMessage(getResources().getText(
					R.string.progress_please_wait));
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				queryAdapterFactory = new ParseQueryAdapter.QueryFactory<CommentPost>() {
					@Override
					public ParseQuery<CommentPost> create() {
						parseQuery = CommentPost.getQuery();
						parseQuery.whereEqualTo("markerId",
								currentSelectedMarkerId);
						parseQuery.include("user");
						parseQuery.orderByDescending("createdAt");

						return parseQuery;
					}
				};

				queryAdapter = new ParseQueryAdapter<CommentPost>(
						ContentActivity.this, queryAdapterFactory) {
					@Override
					public View getItemView(CommentPost post, View view,
							ViewGroup parent) {
						if (view == null) {
							view = View.inflate(getContext(),
									R.layout.item_comment_post, null);
						}
						TextView textViewPostUsername = (TextView) view
								.findViewById(R.id.textViewPostUsername);
						TextView textViewPostComment = (TextView) view
								.findViewById(R.id.textViewPostComment);
						RatingBar ratingBarPostRating = (RatingBar) view
								.findViewById(R.id.ratingBarPostRating);

						textViewPostUsername.setText(post.getUser()
								.getUsername());
						textViewPostComment.setText(post.getComment());
						ratingBarPostRating.setProgress(post.getRating());
						return view;
					}
				};

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			initializeCommunicators();
			communicatorSecond.setAdapter(queryAdapter);

			progressDialog.dismiss();
		}

	}

	private class taskSendComment extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		int commentRating;
		String commentText;

		public taskSendComment(String commentText, int commentRating) {
			this.commentText = commentText;
			this.commentRating = commentRating;
		}

		@Override
		protected void onPreExecute() {

			progressDialog = new ProgressDialog(ContentActivity.this);
			progressDialog.setTitle(getResources().getText(
					R.string.progress_send_comment));
			progressDialog.setMessage(getResources().getText(
					R.string.progress_please_wait));
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			CommentPost commentPost = new CommentPost();
			commentPost.setComment(ParseUser.getCurrentUser(),
					currentSelectedMarkerId, commentText, commentRating);

			ParseACL acl = new ParseACL();
			acl.setPublicReadAccess(true);

			commentPost.setACL(acl);
			commentPost.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {

				}
			});

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			communicatorSecond.commentSent();

			progressDialog.dismiss();

			new taskGetComment().execute();

			super.onPostExecute(result);
		}

	}

	private class taskGetPolyline extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		StringBuilder urlString;
		String response;
		int i;

		private taskGetPolyline(int i) {
			this.i = i;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(ContentActivity.this);
			dialog.setTitle(getResources().getText(
					R.string.progress_download_route));
			dialog.setMessage(getResources().getText(
					R.string.progress_please_wait));
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

			urlString = new StringBuilder();
			urlString
					.append("http://maps.googleapis.com/maps/api/directions/json");
			urlString.append("?origin=");
			urlString.append(LOCATION_CURRENT.getLatitude());
			urlString.append(",");
			urlString.append(LOCATION_CURRENT.getLongitude());

			urlString.append("&destination=");

			urlString.append(arrayLocation[i].latitude);
			urlString.append(",");
			urlString.append(arrayLocation[i].longitude);
			urlString.append("&sensor=false&mode=walking");

		}

		@Override
		protected Void doInBackground(Void... params)
				throws IllegalArgumentException {

			try {
				HttpClient Client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(urlString.toString());
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = Client.execute(httpGet, responseHandler);

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			JSONObject jsonResponse;
			JSONObject overviewPolilyne = new JSONObject();
			String polylinePoints;
			PolylineOptions polylineOptions;

			try {
				jsonResponse = new JSONObject(response);
				JSONArray jsonRoutesArray = jsonResponse.getJSONArray("routes");

				if (jsonRoutesArray != null && jsonRoutesArray.length() > 0) {
					JSONObject firstRoute = jsonRoutesArray.getJSONObject(0);
					if (firstRoute != null) {
						overviewPolilyne = firstRoute
								.getJSONObject("overview_polyline");
						polylinePoints = overviewPolilyne.getString("points");

						polylineOptions = new PolylineOptions().addAll(
								decodePolyLine(polylinePoints)).color(
								R.color.polyline);

						currentPolyline = map.addPolyline(polylineOptions);

						if (currentPolyline.isVisible()) {

							LatLng latLngCurrentLocation = new LatLng(
									LOCATION_CURRENT.getLatitude(),
									LOCATION_CURRENT.getLongitude());

							CameraUpdate update = CameraUpdateFactory
									.newLatLngZoom(latLngCurrentLocation, 13);
							map.animateCamera(update);

							Toast.makeText(ContentActivity.this,
									R.string.success_downloading_route,
									Toast.LENGTH_LONG).show();
						}
					}
				}

				else {
					Toast.makeText(ContentActivity.this,
							R.string.error_downloading_route,
							Toast.LENGTH_SHORT).show();

				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			dialog.dismiss();
		}
	}

	public void openSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ContentActivity.this).setTitle(R.string.action_settings);

		final AlertDialog alertDialog = builder.create();

		View alertDialogView = alertDialog.getLayoutInflater().inflate(
				R.layout.dialog_settings, null);

		alertDialog.setView(alertDialogView);

		TextView tVDialogUsername = (TextView) alertDialogView
				.findViewById(R.id.tVDialogSettingsUsername);
		TextView tvDialogEmail = (TextView) alertDialogView
				.findViewById(R.id.tvDialogSettingsEmail);
		CheckBox checkDialogDom = (CheckBox) alertDialogView
				.findViewById(R.id.cBDialogSettingsDom);
		CheckBox checkDialogMen = (CheckBox) alertDialogView
				.findViewById(R.id.cBDialogSettingsMen);
		CheckBox checkDialogSC = (CheckBox) alertDialogView
				.findViewById(R.id.cBDialogSettingsSC);
		Button btnDialogLogout = (Button) alertDialogView
				.findViewById(R.id.btnDialogSettingsLogout);

		tVDialogUsername.setText(ParseUser.getCurrentUser().getUsername());
		tvDialogEmail.setText(ParseUser.getCurrentUser().getEmail());

		checkDialogDom.setChecked(filterDom);
		checkDialogMen.setChecked(filterMen);
		checkDialogSC.setChecked(filterSC);

		OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				switch (buttonView.getId()) {

				case R.id.cBDialogSettingsDom:
					filterDom = isChecked;

					markerDomStjepanRadic.setVisible(isChecked);
					markerDomSara.setVisible(isChecked);
					markerDomCvjetno.setVisible(isChecked);
					markerDomLascina.setVisible(isChecked);

					break;

				case R.id.cBDialogSettingsMen:
					filterMen = isChecked;

					markerMenSavska.setVisible(isChecked);
					markerMenStjepanRadic.setVisible(isChecked);
					markerMenCvjetnoNaselje.setVisible(isChecked);
					markerMenLascina.setVisible(isChecked);
					markerMenBorongaj.setVisible(isChecked);
					markerMenEkonomija.setVisible(isChecked);
					markerMenMedicina.setVisible(isChecked);
					markerMenVeterina.setVisible(isChecked);
					markerMenSumarstvo.setVisible(isChecked);
					markerMenFSB.setVisible(isChecked);
					markerMenALU.setVisible(isChecked);
					markerMenTTF.setVisible(isChecked);

					break;

				case R.id.cBDialogSettingsSC:
					filterSC = isChecked;

					markerSCZagrebacki.setVisible(isChecked);
					markerSCKarlovacki.setVisible(isChecked);
					markerSCSisacki.setVisible(isChecked);

					break;

				}

			}
		};

		checkDialogDom.setOnCheckedChangeListener(checkListener);
		checkDialogMen.setOnCheckedChangeListener(checkListener);
		checkDialogSC.setOnCheckedChangeListener(checkListener);

		btnDialogLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openLogoutDialog();
				alertDialog.dismiss();

			}
		});

		alertDialog.show();
	}

	public void openLogoutDialog() {
		new AlertDialog.Builder(ContentActivity.this)
				.setTitle(R.string.settings_dialog_logout_title)
				.setPositiveButton(R.string.settings_dialog_logout_confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ParseUser.logOut();
								Intent intent = new Intent(
										ContentActivity.this,
										LoginActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);

							}
						}).show();

	}

	public void openAboutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ContentActivity.this).setTitle(R.string.action_about);

		final AlertDialog alertDialog = builder.create();

		View alertDialogView = alertDialog.getLayoutInflater().inflate(
				R.layout.dialog_about, null);

		alertDialog.setView(alertDialogView);
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.content, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_find:
			findClosestMen();

			return true;

		case R.id.action_settings:
			openSettingsDialog();
			return true;

		case R.id.action_about:
			openAboutDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public String getCurrentSelectedMarkerTitle() {
		return arrayHeading[currentSelectedMarkerId];
	}

	@Override
	protected void onStart() {
		super.onStart();

		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {

		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(5000);
		clientConnected = true;
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		clientConnected = false;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	@Override
	public void onLocationChanged(Location location) {
		LOCATION_CURRENT = location;
	}

	@Override
	public void onDisconnected() {
		clientConnected = false;

	}

}
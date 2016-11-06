package com.victorlaerte.na_onda.view.activities;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.exception.AuthenticationException;
import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.model.User;
import com.victorlaerte.na_onda.tasks.ForecastTask;
import com.victorlaerte.na_onda.tasks.LoadFacebookProfilePhoto;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.CityUtil;
import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.NaOndaUtil;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.fragments.SelectionFragment;

public class MainViewActivity extends Activity {

	private User currentUser;
	private static final String LOG_TAG = MainViewActivity.class.getName();
	private UiLifecycleHelper uiHelper;

	private String[] optionsTitles;;
	private ListView optionsList;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private City currentSelectedCity;
	private Menu menu;
	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		 * Order must to be respected
		 */
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.activity_main);

		buildDrawerLayout();
		retriveUser();

		Crashlytics.setUserIdentifier(currentUser.getId());
		Crashlytics.setUserEmail(currentUser.getUserName());
		Crashlytics.setUserName(currentUser.getName());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.empty);

		getProfilePicture();

		if (Validator.isNull(currentSelectedCity)) {

			SelectionFragment selectionFragment = new SelectionFragment();

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.add(R.id.content_frame, selectionFragment, selectionFragment.getClass().getName());
			transaction.commit();

		} else {

			setCurrentSelectedCity(currentSelectedCity);
		}

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.menu = menu;

		menu.clear();

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		MenuItem item = menu.findItem(R.id.menu_item_share);

		setmShareActionProvider((ShareActionProvider) item.getActionProvider());

		return super.onCreateOptionsMenu(menu);
	}

	public void setShareIntent(Intent shareIntent) {

		if (Validator.isNotNull(getmShareActionProvider())) {
			getmShareActionProvider().setShareIntent(shareIntent);
		}
	}

	private void buildDrawerLayout() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		optionsTitles = getResources().getStringArray(R.array.tab_options);

		optionsList = (ListView) findViewById(R.id.left_drawer);
		optionsList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, optionsTitles));

		optionsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				/*
				 * Favorites
				 */
				if (id == 0) {

					favoritesDialog();
				}
				/*
				 * Rate us
				 */
				else if (id == 1) {

					rateUs();
				}
				/*
				 * About us
				 */
				else if (id == 2) {

					aboutDialog();
				}
				/*
				 * Logout
				 */
				else if (id == 3) {

					logout();
				}

				mDrawerLayout.closeDrawers();
			}

		});

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.empty,
				R.string.empty);

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void favoritesDialog() {

		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE);
		final Set<String> stringSet = prefs.getStringSet(City.SHARED_PREFS_FAV_CITIES, new HashSet<String>());

		if (stringSet.isEmpty()) {

			Toast toast = Toast.makeText(MainViewActivity.this, R.string.noSuchFavorites, Toast.LENGTH_LONG);
			toast.show();

		} else {

			final List<City> favoriteCities = CityUtil.getFavoriteCities(MainViewActivity.this, stringSet);

			List<String> favoriteCitiesLabel = new ArrayList<String>(favoriteCities.size());

			for (City city : favoriteCities) {
				StringBuilder sb = new StringBuilder(city.getUf());
				sb.append(CharPool.SPACE);
				sb.append(CharPool.DASH);
				sb.append(CharPool.SPACE);
				sb.append(city.getName());

				favoriteCitiesLabel.add(sb.toString());
			}

			ListView listView = new ListView(this);
			listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favoriteCitiesLabel));
			final Dialog dialog = new Dialog(this);
			dialog.setTitle(R.string.select_city);
			dialog.setContentView(listView);
			dialog.show();

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					City city = favoriteCities.get(position);

					dialog.dismiss();
					setCurrentSelectedCity(city);
				}
			});

		}
	}

	private void aboutDialog() {

		Dialog dialog = new Dialog(MainViewActivity.this);

		dialog.setContentView(R.layout.dialog_about);
		dialog.setTitle(R.string.aboutTitle);

		dialog.show();
	}

	private void rateUs() {

		Uri uri = Uri.parse(Constants.MARKET_URL + MainViewActivity.this.getPackageName());

		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

		try {

			startActivity(goToMarket);

		} catch (ActivityNotFoundException e) {

			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_PLAY_URL + MainViewActivity.this
					.getPackageName())));
		}
	}

	private void logout() {

		if (currentUser.isLoggedIn()) {

			Session activeSession = Session.getActiveSession();

			if (Validator.isNull(activeSession)) {
				activeSession = Session.openActiveSessionFromCache(MainViewActivity.this);
			}

			if (Validator.isNotNull(activeSession) && activeSession.isOpened()) {
				activeSession.closeAndClearTokenInformation();
			}

			Activity activity = MainViewActivity.this;
			Intent it = new Intent(activity, LoginActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(it);
			activity.overridePendingTransition(R.anim.fadeout, R.anim.fadein);
			activity.finish();

		} else {

			finish();
		}
	}

	private void getProfilePicture() {

		if (currentUser.isLoggedIn()) {

			new LoadFacebookProfilePhoto(this).execute(currentUser.getId());
		}
	}

	public void updateProfile(Bitmap bitmap) {

		if (Validator.isNotNull(bitmap)) {

			Drawable drawable = new BitmapDrawable(getResources(), bitmap);

			getActionBar().setIcon(drawable);
			getActionBar().setTitle(currentUser.getName());
		} else {

			getActionBar().setIcon(R.drawable.com_facebook_profile_picture_blank_square);
		}
	}

	private void retriveUser() {

		try {
			currentUser = NaOndaUtil.getInstance().getUser();
			Log.d(LOG_TAG, "Logged with " + currentUser.getName());
			Log.d(LOG_TAG, "Logged with " + currentUser.getId());
		} catch (AuthenticationException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
	}

	@Override
	public void onResume() {

		super.onResume();
		uiHelper.onResume();

		AppEventsLogger.activateApp(this);
	}

	@Override
	public void onPause() {

		super.onPause();
		uiHelper.onPause();

		AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

	}

	public City getCurrentSelectedCity() {

		return currentSelectedCity;
	}

	public void setCurrentSelectedCity(City currentSelectedCity) {

		this.currentSelectedCity = currentSelectedCity;

		Log.d(LOG_TAG, "New City Selected: " + this.currentSelectedCity);

		new ForecastTask(this, this.currentSelectedCity).execute(Constants.INPE_SERVICE_BASE_URL,
				Constants.INPE_SERVICE_FORECAST_6DAYS_8HOURS_BY_DAY_SUFFIX);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {

		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);

		try {

			savedState.putParcelable(Constants.USER_KEY, NaOndaUtil.getInstance().getUser());

			savedState.putParcelable(Constants.CITY_KEY, currentSelectedCity);

		} catch (AuthenticationException e) {
			Log.d(LOG_TAG, e.getMessage());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
		uiHelper.onCreate(savedInstanceState);

		User user = savedInstanceState.getParcelable(Constants.USER_KEY);
		Session session = Session.getActiveSession();

		if (session == null) {
			session = new Session(MainViewActivity.this);
			Session.setActiveSession(session);
		}

		NaOndaUtil.getInstance().setUser(user);

		City lastSelectedCity = savedInstanceState.getParcelable(Constants.CITY_KEY);
		setCurrentSelectedCity(lastSelectedCity);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

			mDrawerLayout.closeDrawers();

		} else {

			if (Validator.isNotNull(getFragmentManager().findFragmentByTag(ForecastTask.class.getName()))) {

				getFragmentManager().popBackStack(SelectionFragment.class.getName(),
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			} else {

				super.onBackPressed();
			}
		}
	}

	public ShareActionProvider getmShareActionProvider() {

		return mShareActionProvider;
	}

	private void setmShareActionProvider(ShareActionProvider mShareActionProvider) {

		this.mShareActionProvider = mShareActionProvider;
	}
}

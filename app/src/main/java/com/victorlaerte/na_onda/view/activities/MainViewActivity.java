package com.victorlaerte.na_onda.view.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.tasks.ForecastTask;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.CityUtil;
import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.fragments.ForecastFragment;
import com.victorlaerte.na_onda.view.fragments.SelectionFragment;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.fabric.sdk.android.Fabric;

public class MainViewActivity extends AppCompatActivity {

	private static final String LOG_TAG = MainViewActivity.class.getName();

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private Menu menu;
	private ShareActionProvider shareActionProvider;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		Fresco.initialize(this);

		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		buildDrawerLayout();

		SelectionFragment selectionFragment = new SelectionFragment();

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.content_frame, selectionFragment, selectionFragment.getClass().getName());
		transaction.commit();

//		AdView mAdView = (AdView) findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		mAdView.loadAd(adRequest);
	}

	public ShareActionProvider getShareActionProvider() {
		return shareActionProvider;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.menu = menu;

		menu.clear();

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		MenuItem item = menu.findItem(R.id.menu_item_share);

		shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

		return super.onCreateOptionsMenu(menu);
	}

	public void setShareIntent(Intent shareIntent) {

		if (Validator.isNotNull(shareActionProvider)) {
			shareActionProvider.setShareIntent(shareIntent);
		}
	}

	private void buildDrawerLayout() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(
			this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(mDrawerToggle);

		NavigationView navigationDrawer = (NavigationView) mDrawerLayout.findViewById(R.id.nav_view);

		Menu menu = navigationDrawer.getMenu();

		MenuItem bookmarkItem = menu.findItem(R.id.nav_bookmark);
		bookmarkItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mDrawerLayout.closeDrawers();
				openBookmarkDialog();
				return true;
			}
		});

		MenuItem ratingItem = menu.findItem(R.id.nav_rating);
		ratingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mDrawerLayout.closeDrawers();
				rateUs();
				return true;
			}
		});

		MenuItem aboutItem = menu.findItem(R.id.nav_about);
		aboutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mDrawerLayout.closeDrawers();
				aboutDialog();
				return true;
			}
		});

	}

	private void openBookmarkDialog() {

		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE);
		final Set<String> stringSet = prefs.getStringSet(City.SHARED_PREFS_FAV_CITIES, new HashSet<String>());

		if (stringSet.isEmpty()) {

			Toast toast = Toast.makeText(MainViewActivity.this, R.string.noSuchFavorites, Toast.LENGTH_LONG);
			toast.show();

		} else {

			final List<City> favoriteCities = CityUtil.getFavoriteCities(MainViewActivity.this, stringSet);

			List<String> favoriteCitiesLabel = new ArrayList<>(favoriteCities.size());

			for (City city : favoriteCities) {
				StringBuilder sb = new StringBuilder(city.getUf());
				sb.append(CharPool.SPACE);
				sb.append(CharPool.DASH);
				sb.append(CharPool.SPACE);
				sb.append(city.getName());

				favoriteCitiesLabel.add(sb.toString());
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteCitiesLabel);

			new LovelyChoiceDialog(this)
				.setTopColorRes(R.color.colorPrimaryDark)
				.setTitle(R.string.select_city)
				.setIcon(R.drawable.ic_menu_bookmark)
				.setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
					@Override
					public void onItemSelected(int position, String item) {
						City city = favoriteCities.get(position);
						openForecastFragment(city);
					}
				})
				.show();

		}
	}

	public void openForecastFragment(City city) {
		ForecastFragment forecastFragment = new ForecastFragment();

		Bundle args = new Bundle();
		args.putParcelable(City.SELECTED_CITY, city);
		forecastFragment.setArguments(args);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.replace(R.id.content_frame, forecastFragment, forecastFragment.getClass().getName());
		transaction.addToBackStack(SelectionFragment.class.getName());
		transaction.commitAllowingStateLoss();
	}

	private void aboutDialog() {

		new LovelyInfoDialog(this)
			.setTopColorRes(R.color.colorPrimaryDark)
			.setIcon(R.drawable.ic_launcher)
			.setTitle(R.string.aboutTitle)
			.setMessage(R.string.aboutMsg)
			.show();
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
}

package com.victorlaerte.na_onda.view.activities;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.tasks.ForecastTask;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.CityUtil;
import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.fragments.SelectionFragment;

public class MainViewActivity extends AppCompatActivity {

	private static final String LOG_TAG = MainViewActivity.class.getName();

	private String[] optionsTitles;;
//	private ListView optionsList;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private Menu menu;
	private ShareActionProvider mShareActionProvider;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/*
		 * Order must to be respected
		 */
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.menu = menu;

		menu.clear();

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

//		MenuItem item = menu.findItem(R.id.menu_item_share);
//
//		setmShareActionProvider((ActionProvider) MenuItemCompat.getActionProvider(item));

		return super.onCreateOptionsMenu(menu);
	}

	public void setShareIntent(Intent shareIntent) {

		if (Validator.isNotNull(getmShareActionProvider())) {
			getmShareActionProvider().setShareIntent(shareIntent);
		}
	}

	private void buildDrawerLayout() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(
			this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

		mDrawerLayout.addDrawerListener(mDrawerToggle);

		optionsTitles = getResources().getStringArray(R.array.tab_options);

//		optionsList = (ListView) findViewById(R.id.left_drawer);
//		optionsList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, optionsTitles));
//
//		optionsList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				/*
//				 * Favorites
//				 */
//				if (id == 0) {
//
//					favoritesDialog();
//				}
//				/*
//				 * Rate us
//				 */
//				else if (id == 1) {
//
//					rateUs();
//				}
//				/*
//				 * About us
//				 */
//				else if (id == 2) {
//
//					aboutDialog();
//				}
//				/*
//				 * Logout
//				 */
//				else if (id == 3) {
//
//				}
//
//				mDrawerLayout.closeDrawers();
//			}
//
//		});
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

					//TODO: OPEN FORECAST FRAGMENT
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

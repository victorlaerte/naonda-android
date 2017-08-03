package com.victorlaerte.na_onda.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.widget.ShareActionProvider;

import com.scalified.fab.ActionButton;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.events.ForecastLoadEvent;
import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.model.CompleteForecast;
import com.victorlaerte.na_onda.model.DayForecast;
import com.victorlaerte.na_onda.model.DayOfWeek;
import com.victorlaerte.na_onda.model.Forecast;
import com.victorlaerte.na_onda.model.impl.CustomPagerAdapter;
import com.victorlaerte.na_onda.tasks.ForecastTask;
import com.victorlaerte.na_onda.util.AndroidUtil;
import com.victorlaerte.na_onda.util.CharPool;
import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.ContentTypeUtil;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.util.ViewUtil;
import com.victorlaerte.na_onda.view.activities.MainViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ForecastFragment extends Fragment {

	private static final String LOG_TAG = ForecastFragment.class.getName();
	private CompleteForecast completeForecast;
	private View view;
	private ViewPager mViewPager;
	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ActionButton actionButton;
	private MenuItem addBookmarkMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {

		view = inflater.inflate(R.layout.fragment_forecast, container, false);
		actionButton = (ActionButton) view.findViewById(R.id.action_button);

		if (state == null) {

			Bundle arguments = getArguments();
			City selectedCity = arguments.getParcelable(City.SELECTED_CITY);

			new ForecastTask(getActivity(), selectedCity).execute(Constants.INPE_SERVICE_BASE_URL,
				Constants.INPE_SERVICE_FORECAST_6DAYS_8HOURS_BY_DAY_SUFFIX);

		}

		setupLayout();
		setHasOptionsMenu(true);

		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				DayForecast dayForecast = completeForecast.getForecastByDay().get(tabLayout.getSelectedTabPosition());

				List<String> imagesUrl = dayForecast.getGraphUrlList();
				new ImageViewer.Builder(getContext(), imagesUrl)
					.setStartPosition(0)
					.show();
			}
		});

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		addBookmarkMenu = menu.findItem(R.id.menu_add_bookmark);

		if (addBookmarkMenu != null) {

			addBookmarkMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {

					SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_FILE, getActivity().MODE_PRIVATE);
					final Set<String> stringSet = prefs.getStringSet(City.SHARED_PREFS_FAV_CITIES, new HashSet<String>());

					SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.PREFS_FILE,
						getActivity().MODE_PRIVATE).edit();

					String sharedPreferencesId = completeForecast.getCity().getSharedPreferencesId();
					Set<String> newStringSet = new HashSet<>();
					newStringSet.addAll(stringSet);

					if (!newStringSet.contains(completeForecast.getCity().getSharedPreferencesId())) {
						newStringSet.add(sharedPreferencesId);
						editor.putStringSet(City.SHARED_PREFS_FAV_CITIES, newStringSet);

						updateBookmark(R.drawable.ic_menu_bookmark);
						showSnackbar(R.string.bookmark_added);

					} else {
						newStringSet.remove(sharedPreferencesId);
						editor.putStringSet(City.SHARED_PREFS_FAV_CITIES, newStringSet);

						updateBookmark(R.drawable.ic_actionbar_bookmark_border);
						showSnackbar(R.string.bookmark_removed);
					}

					editor.commit();

					return true;
				}
			});
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	private void showSnackbar(int bookmark_added) {
		Snackbar snackbar = Snackbar.make(view, bookmark_added, Snackbar.LENGTH_SHORT);
		snackbar.show();
	}

	private void setupLayout() {

		toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		toolbar.setVisibility(View.VISIBLE);

		tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
		tabLayout.setVisibility(View.VISIBLE);
	}

	private void setupTabLayout() {

		tabLayout.clearOnTabSelectedListeners();
		tabLayout.removeAllTabs();
		
		List<DayForecast> forecastByDay = completeForecast.getForecastByDay();

		for (DayForecast dayForecast : forecastByDay) {

			TabLayout.Tab tab = tabLayout.newTab();

			tab.setText(getDayOfWeek(dayForecast.getDay()).getDayAcronym());
			tab.setTag(dayForecast);

			tabLayout.addTab(tab);
		}

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				setupTableLayout();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onForecastLoadEvent(ForecastLoadEvent event) {
		Log.d(LOG_TAG, event.getCompleteForecast().toString());

		this.completeForecast = event.getCompleteForecast();
		toolbar.setTitle(getBreadcrumbText());

		setupBookmarkItem();
		setupTabLayout();
		setupTableLayout();
		setShareForecastOptions(tabLayout.getSelectedTabPosition());

	}

	private void setupBookmarkItem() {

		SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_FILE, getActivity().MODE_PRIVATE);
		final Set<String> stringSet = prefs.getStringSet(City.SHARED_PREFS_FAV_CITIES, new HashSet<String>());

		if (stringSet.contains(completeForecast.getCity().getSharedPreferencesId())) {
			updateBookmark(R.drawable.ic_menu_bookmark);
		}
	}

	private void updateBookmark(@DrawableRes int icon) {
		if (addBookmarkMenu != null) {
			addBookmarkMenu.setIcon(icon);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		super.onStop();

		EventBus.getDefault().unregister(this);
	}

	private void setShareForecastOptions(int dayIndex) {

		DayForecast dayForecast = completeForecast.getForecastByDay().get(dayIndex);

		Activity activity = getActivity();

		if (activity instanceof MainViewActivity) {

			MainViewActivity mainViewActivity = (MainViewActivity) activity;

			ShareActionProvider mShareActionProvider = mainViewActivity.getShareActionProvider();

			if (Validator.isNotNull(mShareActionProvider)) {

				String textToShare = buildTextToShare(dayForecast);

				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_SUBJECT,
					AndroidUtil.getString(getActivity(), R.string.shareSubjectSelectionFrag));
				shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
				shareIntent.setType(ContentTypeUtil.TEXT_PLAIN);

				mainViewActivity.setShareIntent(shareIntent);
			}
		}
	}

	private String buildTextToShare(DayForecast dayForecast) {

		StringBuilder sb = new StringBuilder(AndroidUtil.getString(getActivity(), R.string.forecast));

		sb.append(CharPool.SPACE);
		sb.append(completeForecast.getCity().getUf());
		sb.append(CharPool.SPACE);
		sb.append(completeForecast.getCity().getName());
		sb.append(CharPool.SPACE);
		sb.append(CharPool.DASH);
		sb.append(CharPool.SPACE);
		sb.append(getDayWithMonthAndYear(dayForecast.getDay()));
		sb.append(CharPool.NEW_LINE);
		sb.append(CharPool.NEW_LINE);
		sb.append(dayForecast.getForecast().get(2)
			.getSharebleForecast(getActivity(), AndroidUtil.getString(getActivity(), R.string.manha)));
		sb.append(CharPool.NEW_LINE);
		sb.append(CharPool.NEW_LINE);
		sb.append(dayForecast.getForecast().get(4)
			.getSharebleForecast(getActivity(), AndroidUtil.getString(getActivity(), R.string.tarde)));
		sb.append(CharPool.NEW_LINE);
		sb.append(CharPool.NEW_LINE);
		sb.append(dayForecast.getForecast().get(6)
			.getSharebleForecast(getActivity(), AndroidUtil.getString(getActivity(), R.string.final_tarde)));
		sb.append(CharPool.NEW_LINE);
		sb.append(CharPool.NEW_LINE);
		sb.append(AndroidUtil.getString(getActivity(), R.string.shareTextSelectionFrag));
		sb.append(Constants.GOOGLE_PLAY_URL + getActivity().getPackageName());

		return sb.toString();
	}

	private void addListners() {

	}

	private void setupTableLayout() {

		clearTable();

		DayForecast dayForecast = completeForecast.getForecastByDay().get(tabLayout.getSelectedTabPosition());
		List<Forecast> forecastList = dayForecast.getForecast();
		Context context = getContext();

		LinearLayout tableContent = (LinearLayout) view.findViewById(R.id.table_content);

		DecimalFormat formatter = new DecimalFormat("00");
		int marginTopBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
		int marginLeftRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

		for (Forecast forecast :forecastList) {

			LinearLayout row = ViewUtil.createLinearLayout(context, 6, marginTopBottom, marginLeftRight);
			fillRow(context, formatter, forecast, row);
			tableContent.addView(row);
		}
	}

	private void fillRow(Context context, DecimalFormat formatter, Forecast forecast, LinearLayout row) {

		row.addView(ViewUtil.createTextView(context, formatter.format(forecast.getHour())));
		row.addView(ViewUtil.createTextView(context, forecast.getWaveHeight() + "m"));
		row.addView(ViewUtil.createTextViewWithImage(
				context, forecast.getWaveDirection().getAcronym(),
				forecast.getWaveDirection().getDrawable(getActivity())));
		row.addView(ViewUtil.createTextView(context, forecast.getUnrest()));
		row.addView(ViewUtil.createTextView(context, forecast.getWindSpeed() + "\nkm/h"));
		row.addView(ViewUtil.createTextViewWithImage(
				context, forecast.getWindDirection().getAcronym(),
				forecast.getWaveDirection().getDrawable(getActivity())));
	}

	private void showGraph(int position) {

		clearGraph();

		DayForecast dayForecast = completeForecast.getForecastByDay().get(position);

		List<String> graphUrlList = dayForecast.getGraphUrlList();

		CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), graphUrlList);

		if (Validator.isNull(mViewPager)) {

//			mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		}

		mViewPager.setAdapter(mCustomPagerAdapter);

	}

	private void clearGraph() {

		if (Validator.isNotNull(mViewPager)) {

			mViewPager.setAdapter(null);
		}
	}

	private void clearTable() {
		LinearLayout tableContent = (LinearLayout) view.findViewById(R.id.table_content);
		tableContent.removeAllViews();
	}

	private String getBreadcrumbText() {

		StringBuilder sb = new StringBuilder();

		sb.append(completeForecast.getCity().getUf().toUpperCase());
		sb.append("  -  ");
		sb.append(completeForecast.getCity().getName());

		return sb.toString();
	}

	private DayOfWeek getDayOfWeek(Calendar calendar) {

		int day = calendar.get(Calendar.DAY_OF_WEEK);

		return DayOfWeek.fromInt(day);
	}

	private String getDayWithMonth(Calendar calendar) {

		StringBuilder sb = new StringBuilder();

		sb.append(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		sb.append("/");

		int month = calendar.get(Calendar.MONTH) + 1;

		String monthString = (month < 10) ? "0" + String.valueOf(month) : String.valueOf(month);

		sb.append(monthString);

		return sb.toString();
	}

	private String getDayWithMonthAndYear(Calendar calendar) {

		StringBuilder sb = new StringBuilder(getDayWithMonth(calendar));
		sb.append("/");
		sb.append(String.valueOf(calendar.get(Calendar.YEAR)));
		return sb.toString();
	}
}

package com.victorlaerte.na_onda.view.fragments;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TableRow;
import android.widget.TextView;

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
import com.victorlaerte.na_onda.util.ViewUtil;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.activities.MainViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ForecastFragment extends Fragment {

	private static final String LOG_TAG = ForecastFragment.class.getName();
	private CompleteForecast completeForecast;
	private View view;
	private ViewPager mViewPager;
	private Toolbar toolbar;
	private TabLayout tabLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {

		view = inflater.inflate(R.layout.fragment_forecast, container, false);

		if (state == null) {

			Bundle arguments = getArguments();
			City selectedCity = arguments.getParcelable(City.SELECTED_CITY);

			new ForecastTask(getActivity(), selectedCity).execute(Constants.INPE_SERVICE_BASE_URL,
				Constants.INPE_SERVICE_FORECAST_6DAYS_8HOURS_BY_DAY_SUFFIX);

		}

		setupLayout();

		return view;
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

		setupTabLayout();
		setupTableLayout();
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

	private void setShareOptions(int dayIndex) {

		DayForecast dayForecast = completeForecast.getForecastByDay().get(dayIndex);

		Activity activity = getActivity();

		if (activity instanceof MainViewActivity) {

			MainViewActivity mainViewActivity = (MainViewActivity) activity;

			ShareActionProvider mShareActionProvider = mainViewActivity.getmShareActionProvider();

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

		SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_FILE, getActivity().MODE_PRIVATE);
		final Set<String> stringSet = prefs.getStringSet(City.SHARED_PREFS_FAV_CITIES, new HashSet<String>());

//		CheckBox checkBoxFav = (CheckBox) view.findViewById(R.id.addFav);
//
//		if (stringSet.contains(completeForecast.getCity().getSharedPreferencesId())) {
//			checkBoxFav.setChecked(true);
//		}

//		checkBoxFav.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//				SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.PREFS_FILE,
//					getActivity().MODE_PRIVATE).edit();
//
//				String sharedPreferencesId = completeForecast.getCity().getSharedPreferencesId();
//
//				Set<String> newStringSet = new HashSet<String>();
//
//				newStringSet.addAll(stringSet);
//
//				if (isChecked) {
//
//					newStringSet.add(sharedPreferencesId);
//					editor.putStringSet(City.SHARED_PREFS_FAV_CITIES, newStringSet);
//
//				} else {
//
//					if (newStringSet.contains(sharedPreferencesId)) {
//						newStringSet.remove(sharedPreferencesId);
//						editor.putStringSet(City.SHARED_PREFS_FAV_CITIES, newStringSet);
//					}
//				}
//
//				editor.commit();
//			}
//		});
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

	private void addTabsToActionBar() {

//		ActionBar actionBar = getActivity().getActionBar();
//
//		actionBar.removeAllTabs();
//
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue)));
//
//		List<Tab> allTabs = getAllTabs(actionBar);
//
//		for (int i = 0; i < allTabs.size(); i++) {
//
//			if (i == 0) {
//				actionBar.addTab(allTabs.get(i), i, true);
//			} else {
//				actionBar.addTab(allTabs.get(i), i);
//			}
//		}
	}

//	private List<Tab> getAllTabs(ActionBar actionBar) {
//
//		List<Tab> tabs = new ArrayList<ActionBar.Tab>();
//
//		List<DayForecast> forecastByDay = completeForecast.getForecastByDay();
//
//		for (DayForecast dayForecast : forecastByDay) {
//
//			tabs.add(getTab(actionBar, dayForecast));
//		}
//
//		return tabs;
//	}

//	private Tab getTab(ActionBar actionBar, final DayForecast dayForecast) {
//
//		final Tab tab = actionBar.newTab();
//
//		tab.setText(getDayWithMonth(dayForecast.getDay()));
//		tab.setTabListener(new TabListener() {
//
//			@Override
//			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//
//			}
//
//			@Override
//			public void onTabSelected(Tab tab, FragmentTransaction ft) {
//
//				setShareOptions(tab.getPosition());
//				loadView(tab.getPosition());
//			}
//
//			@Override
//			public void onTabReselected(Tab tab, FragmentTransaction ft) {
//
//			}
//		});
//
//		return tab;
//	}

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

	@Override
	public void onDestroy() {

//		ActionBar actionBar = getActivity().getActionBar();
//		actionBar.removeAllTabs();
//		actionBar.setStackedBackgroundDrawable(null);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		super.onDestroy();
	}
}

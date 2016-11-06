package com.victorlaerte.na_onda.view.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.widget.LikeView;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.exception.AuthenticationException;
import com.victorlaerte.na_onda.model.City;
import com.victorlaerte.na_onda.util.AndroidUtil;
import com.victorlaerte.na_onda.util.CityUtil;
import com.victorlaerte.na_onda.util.Constants;
import com.victorlaerte.na_onda.util.ContentTypeUtil;
import com.victorlaerte.na_onda.util.ExtensionUtil;
import com.victorlaerte.na_onda.util.NaOndaUtil;
import com.victorlaerte.na_onda.util.StringPool;
import com.victorlaerte.na_onda.util.Validator;
import com.victorlaerte.na_onda.view.activities.MainViewActivity;

public class SelectionFragment extends Fragment {

	private Spinner stateSpinner;
	private Spinner citySpinner;
	private TextView selectCityLabel;
	private TextView chooseTitle;
	private static final String LOG_TAG = SelectionFragment.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_selection, container, false);

		chooseTitle = (TextView) view.findViewById(R.id.chooseTitle);

		chooseTitle.setText(Html.fromHtml(getTitleText()));

		selectCityLabel = (TextView) view.findViewById(R.id.selectCityLabel);

		stateSpinner = (Spinner) view.findViewById(R.id.stateSpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.states,
				R.layout.spinner_item);

		stateSpinner.setAdapter(adapter);

		stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

				String[] statesArray = getResources().getStringArray(R.array.states);

				String selectedStateInfo = statesArray[position];

				if (!selectedStateInfo.equals(AndroidUtil.getString(getActivity(), R.string.no_one))) {

					String[] strs = selectedStateInfo.split("-");

					String nome = strs[1].trim();

					String resourceCity = NaOndaUtil.getInstance().getStateKey(nome) + ExtensionUtil.JS;

					Map<String, City> cityList = CityUtil.findByResourceName(getActivity(), resourceCity);

					populateCitySpinner(view, cityList);

					if (Validator.isNull(citySpinner)) {

						citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
					}

					citySpinner.setVisibility(View.VISIBLE);
					selectCityLabel.setVisibility(View.VISIBLE);

				} else {

					if (Validator.isNull(citySpinner)) {

						citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
					}

					citySpinner.setAdapter(null);
					citySpinner.setVisibility(View.GONE);
					selectCityLabel.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

		try {

			if (NaOndaUtil.getInstance().getUser().isLoggedIn()) {

				LikeView likeView = (LikeView) view.findViewById(R.id.likeView);
				likeView.setLikeViewStyle(LikeView.Style.BOX_COUNT);
				likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);

				likeView.setObjectId(Constants.FACEBOOK_NA_ONDA_PAGE_URL);

				LinearLayout likeUsLayout = (LinearLayout) view.findViewById(R.id.likeUsLayout);
				likeUsLayout.setVisibility(View.VISIBLE);
			}

		} catch (AuthenticationException e) {
			Log.e(LOG_TAG, e.getMessage());
		}

		setHasOptionsMenu(true);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		setShareOptions();

		super.onCreateOptionsMenu(menu, inflater);
	}

	private void setShareOptions() {

		Activity activity = getActivity();

		if (activity instanceof MainViewActivity) {
			MainViewActivity mainViewActivity = (MainViewActivity) activity;

			ShareActionProvider mShareActionProvider = mainViewActivity.getmShareActionProvider();

			if (Validator.isNotNull(mShareActionProvider)) {

				String textToShare = AndroidUtil.getString(getActivity(), R.string.shareTextSelectionFrag) + Constants.GOOGLE_PLAY_URL + mainViewActivity
						.getPackageName();

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

	public void populateCitySpinner(View view, final Map<String, City> cityList) {

		citySpinner = (Spinner) view.findViewById(R.id.citySpinner);

		ArrayList<HashMap<String, String>> adapterList = new ArrayList<HashMap<String, String>>();

		adapterList.add(getItem(AndroidUtil.getString(getActivity(), R.string.no_one), StringPool.BLANK,
				StringPool.BLANK, StringPool.BLANK));

		for (Map.Entry<String, City> entry : cityList.entrySet()) {

			String name = entry.getValue().getName();
			String uf = entry.getValue().getUf();
			String id = entry.getValue().getId();
			String key = entry.getValue().getKey();

			adapterList.add(getItem(name, uf, id, key));
		}

		String[] from = new String[] { CityUtil.NAME_KEY };

		int[] to = new int[] { R.id.text1 };

		int layoutNativo = R.layout.spinner_item;

		citySpinner.setAdapter(new SimpleAdapter(getActivity(), adapterList, layoutNativo, from, to));

		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

				Object obj = citySpinner.getItemAtPosition(position);

				@SuppressWarnings("unchecked")
				HashMap<String, String> item = (HashMap<String, String>) obj;

				String name = item.get(CityUtil.NAME_KEY);

				if (!name.equals(AndroidUtil.getString(getActivity(), R.string.no_one))) {

					String key = item.get(CityUtil.HASH_KEY);

					City selectedCity = cityList.get(key);

					Log.d(LOG_TAG, selectedCity.getName());

					Activity activity = getActivity();

					if (activity instanceof MainViewActivity) {

						MainViewActivity mainViewActivity = (MainViewActivity) activity;
						mainViewActivity.setCurrentSelectedCity(selectedCity);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
	}

	private HashMap<String, String> getItem(String name, String uf, String id, String key) {

		HashMap<String, String> item = new HashMap<String, String>();
		item.put(CityUtil.NAME_KEY, name);
		item.put(CityUtil.UF_KEY, uf);
		item.put(CityUtil.ID_KEY, id);
		item.put(CityUtil.HASH_KEY, key);

		return item;
	}

	private String getTitleText() {

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=#ffffff>");
		sb.append(AndroidUtil.getString(getActivity(), R.string.choose_title));
		sb.append("</font>");
		sb.append(StringPool.SPACE);
		sb.append("<font color=#fdc010>");
		sb.append(AndroidUtil.getString(getActivity(), R.string.app_name_warn));
		sb.append("</font>");
		return sb.toString();
	}
}

package com.victorlaerte.na_onda.view.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.victorlaerte.na_onda.R;
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

public class SelectionFragment extends Fragment implements OnItemSelectedListener {

	private Spinner stateSpinner;
	private Spinner citySpinner;
	private static final String LOG_TAG = SelectionFragment.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_selection, container, false);

		stateSpinner = (Spinner) view.findViewById(R.id.state_spinner);
		citySpinner = (Spinner) view.findViewById(R.id.city_spinner);
		setCityAdapter(getDefaultCityAdapterList());

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.states,
				R.layout.spinner_item);

		stateSpinner.setAdapter(adapter);

		stateSpinner.setOnItemSelectedListener(this);

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

	private ArrayList<HashMap<String, String>> getDefaultCityAdapterList() {

		ArrayList<HashMap<String, String>> adapterList = new ArrayList<HashMap<String, String>>();

		adapterList.add(getItem(AndroidUtil.getString(getActivity(), R.string.select_city), StringPool.BLANK,
			StringPool.BLANK, StringPool.BLANK));

		return adapterList;
	}

	public void populateCitySpinner() {

		citySpinner.setAdapter(null);

		ArrayList<HashMap<String, String>> adapterList = getDefaultCityAdapterList();

		for (Map.Entry<String, City> entry : cityMap.entrySet()) {

			String name = entry.getValue().getName();
			String uf = entry.getValue().getUf();
			String id = entry.getValue().getId();
			String key = entry.getValue().getKey();

			adapterList.add(getItem(name, uf, id, key));
		}

		setCityAdapter(adapterList);

		citySpinner.setOnItemSelectedListener(this);
	}

	private void setCityAdapter(ArrayList<HashMap<String, String>> adapterList) {
		String[] from = new String[] { CityUtil.NAME_KEY };
		int[] to = new int[] { R.id.text1 };
		int layoutNativo = R.layout.spinner_item;

		citySpinner.setAdapter(new SimpleAdapter(getActivity(), adapterList, layoutNativo, from, to));
	}

	private HashMap<String, String> getItem(String name, String uf, String id, String key) {

		HashMap<String, String> item = new HashMap<String, String>();
		item.put(CityUtil.NAME_KEY, name);
		item.put(CityUtil.UF_KEY, uf);
		item.put(CityUtil.ID_KEY, id);
		item.put(CityUtil.HASH_KEY, key);

		return item;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		if (id == R.id.state_spinner) {

			String[] statesArray = getResources().getStringArray(R.array.states);

			String selectedStateInfo = statesArray[position];

			if (!selectedStateInfo.equals(AndroidUtil.getString(getActivity(), R.string.select_state))) {

				String[] strs = selectedStateInfo.split("-");

				String nome = strs[1].trim();

				String resourceCity = NaOndaUtil.getInstance().getStateKey(nome) + ExtensionUtil.JS;

				cityMap = CityUtil.findByResourceName(getActivity(), resourceCity);

				populateCitySpinner();

			} else {

				if (citySpinner.getAdapter().getCount() > 1) {

					setCityAdapter(getDefaultCityAdapterList());
				}
			}
		} else if (id == R.id.city_spinner) {

			Object obj = citySpinner.getItemAtPosition(position);

			HashMap<String, String> item = (HashMap<String, String>) obj;

			String name = item.get(CityUtil.NAME_KEY);

			if (!name.equals(AndroidUtil.getString(getActivity(), R.string.select_city))) {

				String key = item.get(CityUtil.HASH_KEY);

				City selectedCity = cityMap.get(key);

				Log.d(LOG_TAG, selectedCity.getName());

				Activity activity = getActivity();

				if (activity instanceof MainViewActivity) {

					MainViewActivity mainViewActivity = (MainViewActivity) activity;
					mainViewActivity.setCurrentSelectedCity(selectedCity);
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private Map<String, City> cityMap;
}

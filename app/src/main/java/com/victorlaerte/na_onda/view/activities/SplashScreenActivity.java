package com.victorlaerte.na_onda.view.activities;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.tasks.LoadAppComponentsTask;

public class SplashScreenActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

		setContentView(R.layout.activity_splash_screen);

		loadApplicationComponents(savedInstanceState);

		// printKeyHash();
	}

	private void printKeyHash() {

		// Add code to print out the key hash
		try {
			PackageInfo info = getPackageManager().getPackageInfo("com.victorlaerte.na_onda",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			Log.e("KeyHash:", e.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("KeyHash:", e.toString());
		}
	}

	private void loadApplicationComponents(Bundle savedInstanceState) {

		LoadAppComponentsTask loadAppComponentsTask = new LoadAppComponentsTask(this);

		loadAppComponentsTask.execute();
	}
}

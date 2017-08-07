package com.victorlaerte.na_onda.view.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.tasks.LoadAppComponentsTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());

		setContentView(R.layout.activity_splash_screen);

		loadApplicationComponents(savedInstanceState);
	}

	private void loadApplicationComponents(Bundle savedInstanceState) {

		LoadAppComponentsTask loadAppComponentsTask = new LoadAppComponentsTask(this);

		loadAppComponentsTask.execute();
	}
}

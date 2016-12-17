package com.victorlaerte.na_onda.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.victorlaerte.na_onda.R;
import com.victorlaerte.na_onda.model.User;
import com.victorlaerte.na_onda.model.impl.UserImpl;
import com.victorlaerte.na_onda.util.AndroidUtil;
import com.victorlaerte.na_onda.util.NaOndaUtil;
import com.victorlaerte.na_onda.util.StringPool;
import com.victorlaerte.na_onda.util.Validator;

public class LoginActivity extends FragmentActivity {

	private LoginButton loginBtn;
	private Button anonymousModeBtn;
	private ProgressBar loginProgress;
	private TextView loginLabel;
	private TextView infoLabel;
	private UiLifecycleHelper uiHelper;

	private void showLoading(boolean showLoading) {

		if (showLoading) {

			loginBtn.setVisibility(View.GONE);
			anonymousModeBtn.setVisibility(View.GONE);

			loginProgress.setVisibility(View.VISIBLE);
			loginLabel.setVisibility(View.VISIBLE);

		} else {

			loginBtn.setVisibility(View.VISIBLE);
			anonymousModeBtn.setVisibility(View.VISIBLE);

			loginProgress.setVisibility(View.GONE);
			loginLabel.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);

		loginProgress = (ProgressBar) findViewById(R.id.loginPogress);
		infoLabel = (TextView) findViewById(R.id.infoLabel);
		loginLabel = (TextView) findViewById(R.id.loginLabel);
		loginBtn = (LoginButton) findViewById(R.id.authButton);
		anonymousModeBtn = (Button) findViewById(R.id.anonymousModeBtn);

		infoLabel.setText(Html.fromHtml(getInfoText()));

		buttonsEnabled(false);
		showLoading(true);

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		addListeners();
	}

	private String getInfoText() {

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=#ffffff>");
		sb.append(AndroidUtil.getString(this, R.string.dologin));
		sb.append("</font>");
		sb.append(StringPool.SPACE);
		sb.append("<font color=#fdc010>");
		sb.append(AndroidUtil.getString(this, R.string.app_name_warn));
		sb.append("</font>");
		return sb.toString();
	}

	private void addListeners() {

		addAnonymousModeListener();

		addFacebookLoginListener();
	}

	private void addFacebookLoginListener() {

		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {

				if (Validator.isNotNull(user)) {

					Log.d(LoginActivity.class.getName(), "Logged In with " + user.getName());

					user.getUsername();
					User localUser = new UserImpl(user.getId(), user.getUsername(), user.getName(), true);
					NaOndaUtil.getInstance().setUser(localUser);

					openMainActivity();

				} else {

					Log.d(LoginActivity.class.getName(), "Something went wrong. You are not logged");
				}

				buttonsEnabled(true);
				showLoading(false);
			}
		});
	}

	private void addAnonymousModeListener() {

		anonymousModeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/* Setting Anonymous User */
				String unknow = AndroidUtil.getString(LoginActivity.this, R.string.unknow);
				User localUser = new UserImpl(unknow, unknow, unknow, false);
				NaOndaUtil.getInstance().setUser(localUser);

				openMainActivity();
			}
		});
	}

	private void openMainActivity() {

		Activity activity = LoginActivity.this;
		Intent it = new Intent(activity, MainViewActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(it);
		activity.overridePendingTransition(R.anim.fadeout, R.anim.fadein);
		activity.finish();
	}

	@Override
	public void onResume() {

		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {

		super.onPause();
		uiHelper.onPause();
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

	@Override
	public void onSaveInstanceState(Bundle savedState) {

		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}

	public void buttonsEnabled(boolean isEnabled) {

		loginBtn.setEnabled(isEnabled);
		anonymousModeBtn.setEnabled(isEnabled);
	}
}

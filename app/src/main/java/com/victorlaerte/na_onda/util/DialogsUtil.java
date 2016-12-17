package com.victorlaerte.na_onda.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.victorlaerte.na_onda.R;

public class DialogsUtil {

	public static final OnClickListener NULL_ON_CLICK_LISTENER = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		}
	};

	public static void showToastDialog(final Activity activity, int messageId) {

		showToastDialog(activity, AndroidUtil.getString(activity, messageId));
	}

	public static void showToastDialog(final Activity activity, final String message) {

		if (activity != null) {

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	public static void showDialog(Activity activity, int titleId, String message, DialogInterface.OnClickListener event) {

		showDialog(activity, AndroidUtil.getString(activity, titleId), message, event);
	}

	public static void showDialog(Activity activity, int titleId, int messageId, DialogInterface.OnClickListener event) {

		showDialog(activity, AndroidUtil.getString(activity, titleId), AndroidUtil.getString(activity, messageId),
				event);
	}

	public static void showDialog(Activity activity, String title, int messageId, DialogInterface.OnClickListener event) {

		showDialog(activity, title, AndroidUtil.getString(activity, messageId), event);
	}

	public static void showDialog(final Activity activity, final String title, final String message,
			final DialogInterface.OnClickListener okEvent) {

		if (Validator.isNotNull(activity)) {

			final DialogInterface.OnClickListener finalEvent = okEvent == null ? NULL_ON_CLICK_LISTENER : okEvent;

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_DARK).setTitle(title)
							.setMessage(message).setNeutralButton("OK", finalEvent).show();
				}
			});
		}
	}

	public static void dialogQuestion(final Activity activity, final int titleId, final String message,
			final android.content.DialogInterface.OnClickListener positiveEvent,
			final android.content.DialogInterface.OnClickListener negativeEvent) {

		dialogQuestion(activity, AndroidUtil.getString(activity, titleId), message, positiveEvent, negativeEvent);
	}

	public static void dialogQuestion(final Activity activity, final int titleId, final int messageId,
			final android.content.DialogInterface.OnClickListener positiveEvent,
			final android.content.DialogInterface.OnClickListener negativeEvent) {

		dialogQuestion(activity, AndroidUtil.getString(activity, titleId), AndroidUtil.getString(activity, messageId),
				positiveEvent, negativeEvent);
	}

	public static void dialogQuestion(final Activity activity, final String title, final int messageId,
			final android.content.DialogInterface.OnClickListener positiveEvent,
			final android.content.DialogInterface.OnClickListener negativeEvent) {

		dialogQuestion(activity, title, AndroidUtil.getString(activity, messageId), positiveEvent, negativeEvent);
	}

	public static void dialogQuestion(final Activity activity, final String title, final String message,
			final DialogInterface.OnClickListener positiveEvent, final DialogInterface.OnClickListener negativeEvent) {

		if (activity != null) {

			final DialogInterface.OnClickListener finalNegativeEvent = negativeEvent == null ? NULL_ON_CLICK_LISTENER : negativeEvent;
			final DialogInterface.OnClickListener finalPositiveEvent = positiveEvent == null ? NULL_ON_CLICK_LISTENER : positiveEvent;

			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_DARK).setTitle(title)
							.setMessage(message).setNegativeButton(R.string.no, finalNegativeEvent)
							.setPositiveButton(R.string.yes, finalPositiveEvent).show();
				}
			});
		}
	}
}

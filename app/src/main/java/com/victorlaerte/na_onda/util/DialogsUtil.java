package com.victorlaerte.na_onda.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.victorlaerte.na_onda.R;

public class DialogsUtil {

	public static final OnClickListener NULL_ON_CLICK_LISTENER = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		}
	};

	public static void showDialog(Context context, int titleId, String message, DialogInterface.OnClickListener event) {

		showDialog(context, AndroidUtil.getString(context, titleId), message, event);
	}

	public static void showDialog(Context context, int titleId, int messageId, DialogInterface.OnClickListener event) {

		showDialog(context, AndroidUtil.getString(context, titleId), AndroidUtil.getString(context, messageId),
			event);
	}

	public static void showDialog(Context context, String title, int messageId, DialogInterface.OnClickListener event) {

		showDialog(context, title, AndroidUtil.getString(context, messageId), event);
	}

	public static void showDialog(final Context context, final String title, final String message,
	                              final DialogInterface.OnClickListener okEvent) {

		if (Validator.isNotNull(context)) {

			final DialogInterface.OnClickListener finalEvent = okEvent == null ? NULL_ON_CLICK_LISTENER : okEvent;

			new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setNeutralButton("OK", finalEvent)
				.show();
		}
	}

	public static void dialogQuestion(final Context context, final int titleId, final String message,
	                                  final android.content.DialogInterface.OnClickListener positiveEvent,
	                                  final android.content.DialogInterface.OnClickListener negativeEvent) {

		dialogQuestion(context, AndroidUtil.getString(context, titleId), message, positiveEvent, negativeEvent);
	}

	public static void dialogQuestion(final Context context, final int titleId, final int messageId,
	                                  final android.content.DialogInterface.OnClickListener positiveEvent,
	                                  final android.content.DialogInterface.OnClickListener negativeEvent) {

		dialogQuestion(context, AndroidUtil.getString(context, titleId), AndroidUtil.getString(context, messageId),
			positiveEvent, negativeEvent);
	}

	public static void dialogQuestion(final Context context, final String title, final int messageId,
	                                  final android.content.DialogInterface.OnClickListener positiveEvent,
	                                  final android.content.DialogInterface.OnClickListener negativeEvent) {

		dialogQuestion(context, title, AndroidUtil.getString(context, messageId), positiveEvent, negativeEvent);
	}

	public static void dialogQuestion(final Context context, final String title, final String message,
	                                  final DialogInterface.OnClickListener positiveEvent, final DialogInterface.OnClickListener negativeEvent) {

		final DialogInterface.OnClickListener finalNegativeEvent = negativeEvent == null ? NULL_ON_CLICK_LISTENER : negativeEvent;
		final DialogInterface.OnClickListener finalPositiveEvent = positiveEvent == null ? NULL_ON_CLICK_LISTENER : positiveEvent;

		new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(message).setNegativeButton(R.string.no, finalNegativeEvent)
			.setPositiveButton(R.string.yes, finalPositiveEvent).show();
	}
}

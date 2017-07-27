package com.victorlaerte.na_onda.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author Victor Oliveira
 */
public class ViewUtil {

	public static LinearLayout createRow(Context context, int weightSum) {

//		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

		LinearLayout linearLayout = new LinearLayout(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		layoutParams.setMargins(margin, margin, margin, margin);
		layoutParams.gravity = Gravity.CENTER;
		linearLayout.setLayoutParams(layoutParams);

		linearLayout.setWeightSum(weightSum);

		return linearLayout;
	}

	public static TextView createTextViewCell(Context context, String text) {
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
		TextView textView = new TextView(context);
		textView.setGravity(Gravity.CENTER);
		TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
		layoutParams.setMargins(margin, margin, margin, margin);
		layoutParams.gravity = Gravity.CENTER;
		textView.setLayoutParams(layoutParams);
		textView.setText(text);
		return textView;
	}
}

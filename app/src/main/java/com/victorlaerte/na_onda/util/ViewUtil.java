package com.victorlaerte.na_onda.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.victorlaerte.na_onda.R;

/**
 * @author Victor Oliveira
 */
public class ViewUtil {

	public static LinearLayout createLinearLayout(Context context, int weightSum, int marginTopBottom, int marginLeftRight) {
		Drawable background = context.getDrawable(R.drawable.bottom_list_item_separator);

		LinearLayout linearLayout = new LinearLayout(context);
		LinearLayout.LayoutParams layoutParams = getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT);

		layoutParams.setMargins(marginLeftRight, 0, marginLeftRight, 0);
		linearLayout.setLayoutParams(layoutParams);
		linearLayout.setWeightSum(weightSum);
		linearLayout.setBackground(background);

		return linearLayout;
	}

	public static TextView createTextView(Context context, String text) {
		TextView textView = new TextView(context);
		textView.setGravity(Gravity.CENTER);

		TableLayout.LayoutParams layoutParams = getLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1f);

		textView.setLayoutParams(layoutParams);
		textView.setText(text);
		return textView;
	}

	public static LinearLayout createTextViewWithImage(Context context, String text, Drawable drawable) {
		drawable.setTint(Color.GRAY);

		LinearLayout.LayoutParams layoutParams = getLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT);
		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(layoutParams);
		imageView.setImageDrawable(drawable);

		TextView textView = createTextView(context, text);

		LinearLayout linearLayout = new LinearLayout(context);
		TableLayout.LayoutParams layoutParams2 = getLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(layoutParams2);

		linearLayout.addView(imageView);
		linearLayout.addView(textView);

		return linearLayout;
	}

	@NonNull
	private static LinearLayout.LayoutParams getLayoutParams(int width) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			width, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	@NonNull
	private static TableLayout.LayoutParams getLayoutParams(int width, float weight) {
		TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
			width, ViewGroup.LayoutParams.WRAP_CONTENT, weight);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

}

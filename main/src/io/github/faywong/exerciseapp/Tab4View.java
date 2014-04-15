package io.github.faywong.exerciseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import  io.github.faywong.exerciseapp.R;

public class Tab4View {
private View mTab4View;
	
	public Tab4View(Context context) {
		mTab4View = LayoutInflater.from(context).inflate(R.layout.page_tab4,
				null);
	}

	public View getView() {
		return mTab4View;
	}
}

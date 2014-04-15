package io.github.faywong.exerciseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import  io.github.faywong.exerciseapp.R;

public class Tab2View {
private View mTab2View;
	
	public Tab2View(Context context) {
		mTab2View = LayoutInflater.from(context).inflate(R.layout.page_tab2,
				null);
	}

	public View getView() {
		return mTab2View;
	}
}

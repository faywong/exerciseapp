package io.github.faywong.exerciseapp;

import  io.github.faywong.exerciseapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public class Tab1View {
	private View mTab1View;
	
	public Tab1View(Context context) {
		mTab1View = LayoutInflater.from(context).inflate(R.layout.page_tab1,
				null);
	}

	public View getView() {
		return mTab1View;
	}
}

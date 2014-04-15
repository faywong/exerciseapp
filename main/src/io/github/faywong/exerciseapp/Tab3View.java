package io.github.faywong.exerciseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import  io.github.faywong.exerciseapp.R;

public class Tab3View {
private View mTab3View;
	
	public Tab3View(Context context) {
		mTab3View = LayoutInflater.from(context).inflate(R.layout.page_tab3,
				null);
	}

	public View getView() {
		return mTab3View;
	}
}

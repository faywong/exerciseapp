package io.github.faywong.exerciseapp;

import android.app.Activity;
import android.os.Bundle;

import io.github.faywong.exerciseapp.R;

public class FreeMode extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise_free_mode);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}

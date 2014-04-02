package io.github.faywong.exerciseapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import io.github.faywong.exerciseapp.R;

/**
 * @author faywong
 *
 */
public class Main extends Activity implements View.OnClickListener {
	ImageButton freeModeBtn;
	ImageButton intelliModeBtn;
	ImageButton immersionModeBtn;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise_main);
		freeModeBtn = (ImageButton)findViewById(R.id.free_mode_btn);
		freeModeBtn.setOnClickListener(this);
		intelliModeBtn = (ImageButton)findViewById(R.id.intelli_mode_btn);
		immersionModeBtn = (ImageButton)findViewById(R.id.immersion_mode_btn);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.free_mode_btn) {
			Intent intent = new Intent();
			intent.setClass(this, FreeMode.class);
			startActivity(intent);
		} else if (v.getId() == R.id.intelli_mode_btn) {
			
		} else if (v.getId() == R.id.immersion_mode_btn) {
			
		}
	}
}

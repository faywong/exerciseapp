package io.github.faywong.exerciseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.github.faywong.exerciseapp.R;

public class FreeMode extends Activity implements View.OnClickListener {
	private RelativeLayout timeControlLayout;
	private RelativeLayout timeControlPanel;
	private Button addTimeBtn;
	private Button subTimeBtn;
	private TextView timeValueText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise_free_mode);
		timeControlLayout = (RelativeLayout) findViewById(R.id.time_control);
		timeControlLayout.setOnClickListener(this);
		timeControlPanel = (RelativeLayout) findViewById(R.id.time_selection);
		addTimeBtn = (Button) findViewById(R.id.add_time_btn);
		addTimeBtn.setOnClickListener(this);
		subTimeBtn = (Button) findViewById(R.id.sub_time_btn);
		subTimeBtn.setOnClickListener(this);
		timeValueText = (TextView) findViewById(R.id.adjusted_time_val);
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
		}
		final int viewId = v.getId();
		if (viewId == R.id.time_control) {
			if (timeControlPanel.getVisibility() == View.GONE) {
				timeControlPanel.setVisibility(View.VISIBLE);
			} else if (timeControlPanel.getVisibility() == View.VISIBLE) {
				timeControlPanel.setVisibility(View.GONE);
			}
		} else if (viewId == R.id.add_time_btn) {
			final int oldTime = Integer.parseInt(timeValueText.getText().toString());
			timeValueText.setText(String.valueOf(oldTime + 10));
		} else if (viewId == R.id.sub_time_btn) {
			final int oldTime = Integer.parseInt(timeValueText.getText().toString());
			timeValueText.setText(String.valueOf(Math.max(oldTime - 10, 0)));
		}
	}
}

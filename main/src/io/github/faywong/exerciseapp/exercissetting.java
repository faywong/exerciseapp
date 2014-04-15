package io.github.faywong.exerciseapp;

import sportsSDK.PinSDK;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class exercissetting extends Activity implements View.OnClickListener {

	Button backBtn;
	ImageButton userBtn;
	ImageButton systemBtn;
	ImageButton aboutBtn;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercis_setting);
		userBtn = (ImageButton)findViewById(R.id.user_btn);
		userBtn.setOnClickListener(this);
		systemBtn = (ImageButton)findViewById(R.id.system_btn);
		systemBtn.setOnClickListener(this);
		aboutBtn = (ImageButton)findViewById(R.id.about_btn);
		aboutBtn.setOnClickListener(this);
		backBtn = (Button)findViewById(R.id.header_leftbtn);
		backBtn.setOnClickListener(this); 
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.user_btn) {
			Intent intent = new Intent().setClass(this, userManage.class);
			startActivityForResult(intent, 0);
		} else if (v.getId() == R.id.system_btn) {
			Intent intent = new Intent().setClass(this, systemSetting.class);
			startActivityForResult(intent, 0);
		}  
		else if (v.getId() == R.id.header_leftbtn) {
			finish();
		}
	}
}



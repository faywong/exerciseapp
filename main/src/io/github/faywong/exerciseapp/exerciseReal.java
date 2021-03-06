package io.github.faywong.exerciseapp;

import sportsSDK.PinSDK;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class exerciseReal extends Activity implements View.OnClickListener {

	Button backBtn;
	ImageButton realBtn;
	ImageButton systemBtn;
	ImageButton aboutBtn;	
	ImageButton factoryBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercis_real);
		
		
		backBtn = (Button)findViewById(R.id.header_leftbtn);
		backBtn.setOnClickListener(this); 
		
		
		realBtn = (ImageButton)findViewById(R.id.real_btn);
		realBtn.setOnClickListener(this);
		
		/*systemBtn = (ImageButton)findViewById(R.id.system_btn);
		systemBtn.setOnClickListener(this);
		aboutBtn = (ImageButton)findViewById(R.id.about_btn);
		aboutBtn.setOnClickListener(this);
		
		factoryBtn = (ImageButton)findViewById(R.id.factory_btn);
		factoryBtn.setOnClickListener(this); */
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.real_btn) {
			Intent result = new Intent();
		    result.putExtra("back", FreeMode.real_mode);
		    setResult(RESULT_OK, result);
		    finish();
		} else if (v.getId() == R.id.system_btn) {
			Intent intent = new Intent().setClass(this, systemSetting.class);
			startActivityForResult(intent, 0);
		}  
		 else if (v.getId() == R.id.factory_btn) {
				Intent intent = new Intent().setClass(this, factorySetting.class);
				startActivityForResult(intent, 0);
			}  
		else if (v.getId() == R.id.header_leftbtn) {
			finish();
		}
	}
}



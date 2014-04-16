package io.github.faywong.exerciseapp;

import sportsSDK.PinSDK;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class exerciseSmart extends Activity implements View.OnClickListener {

	static public int returntolast =0;
	static public int returntomain =1;
	
	Button backBtn;
	ImageButton aerobicsBtn;
	ImageButton strengthBtn;
	ImageButton fastBtn;	
	ImageButton memorizeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercis_smart);
		
		backBtn = (Button)findViewById(R.id.header_leftbtn);
		backBtn.setOnClickListener(this); 
		
		
		aerobicsBtn = (ImageButton)findViewById(R.id.aerobics_btn);
		aerobicsBtn.setOnClickListener(this);
		
		strengthBtn = (ImageButton)findViewById(R.id.streng_btn);
		strengthBtn.setOnClickListener(this);
		
		fastBtn = (ImageButton)findViewById(R.id.fast_btn);
		fastBtn.setOnClickListener(this);
		
		memorizeBtn = (ImageButton)findViewById(R.id.memorize_btn);
		memorizeBtn.setOnClickListener(this); 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.aerobics_btn) {
			Intent result = new Intent();
		    result.putExtra("back", FreeMode.aerobics_mode);
		    setResult(RESULT_OK, result);
		    finish();
	
		} else if (v.getId() == R.id.fast_btn) {
			Intent result = new Intent();
		    result.putExtra("back", FreeMode.fast_mode);
		    setResult(RESULT_OK, result);
		    finish();

		}  
		 else if (v.getId() == R.id.streng_btn) {
			 Intent result = new Intent();
			    result.putExtra("back", FreeMode.strength_mode);
			    setResult(RESULT_OK, result);
			    finish();
				
			}  
		 else if (v.getId() == R.id.memorize_btn) {
			 Intent result = new Intent();
			    result.putExtra("back", FreeMode.memorize_mode);
			    setResult(RESULT_OK, result);
			    finish();
				
			} 
		else if (v.getId() == R.id.header_leftbtn) {
		    Intent result = new Intent();
		    result.putExtra("back", 0);
		    setResult(RESULT_OK, result);
		    finish();
		}
	}
}



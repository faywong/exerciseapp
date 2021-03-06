package io.github.faywong.exerciseapp;

import sportsSDK.PinSDK;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import io.github.faywong.exerciseapp.R;
import io.github.faywong.exerciseapp.thirdparty.WifiManagerMain;

/**
 * @author faywong
 *
 */
public class Main extends Activity implements View.OnClickListener {
	private static final String TAG = "Main";
	ImageButton freeModeBtn;
	ImageButton intelliModeBtn;
	ImageButton immersionModeBtn;
	private ImageButton wifiSettingBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise_main);
		freeModeBtn = (ImageButton)findViewById(R.id.free_mode_btn);
		freeModeBtn.setOnClickListener(this);
		intelliModeBtn = (ImageButton)findViewById(R.id.intelli_mode_btn);
		intelliModeBtn.setOnClickListener(this);
		immersionModeBtn = (ImageButton)findViewById(R.id.immersion_mode_btn);
		immersionModeBtn.setOnClickListener(this);
		wifiSettingBtn = (ImageButton)findViewById(R.id.wifi_settings);
		wifiSettingBtn.setOnClickListener(this);
		
		PinSDK.getInstance();
		
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
			/*Intent intent = new Intent();
			intent.setClass(this, FreeMode.class);
			startActivity(intent);*/
			FreeMode.curMode=FreeMode.free_mode;
			finish();
		} else if (v.getId() == R.id.intelli_mode_btn) {
			
			Intent intent = new Intent().setClass(this, exerciseSmart.class);
			startActivityForResult(intent, 0);
		} else if (v.getId() == R.id.immersion_mode_btn) {
			Intent intent = new Intent().setClass(this, exerciseReal.class);
			startActivityForResult(intent, 0);
		} else if (v.getId() == R.id.wifi_settings) {
			//Intent intent = new Intent().setClass(this, WifiManagerMain.class);
			//Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
			
			Intent intent = new Intent().setClass(this, exercissetting.class);
			startActivityForResult(intent, 0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onKeyDown() keyCode:" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(TAG, "onKeyDown() BACK case");
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	 @Override 
	    public void onActivityResult(int requestCode, int resultCode, Intent data){
		 super.onActivityResult(requestCode, resultCode, data);

	            if (resultCode == Activity.RESULT_OK){
	            	Bundle bundle = data.getExtras();  
	                int ret = bundle.getInt("back");  
	                if(ret!=0)
	                {
	                	FreeMode.curMode=ret;
	                	finish();
	                }
	            }else if (resultCode == Activity.RESULT_CANCELED){
	            }
	       
	        }
}

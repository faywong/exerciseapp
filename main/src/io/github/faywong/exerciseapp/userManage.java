package io.github.faywong.exerciseapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class userManage extends Activity implements View.OnClickListener {

	Button backBtn;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercis_user);

		backBtn = (Button)findViewById(R.id.header_leftbtn);
		backBtn.setOnClickListener(this); 
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.user_btn) {
	
		} else if (v.getId() == R.id.header_leftbtn) {
			finish();
		}
	}

}

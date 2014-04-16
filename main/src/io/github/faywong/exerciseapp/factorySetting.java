package io.github.faywong.exerciseapp;

import sportsSDK.PinSDK;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class factorySetting extends Activity implements View.OnClickListener {

	Button backBtn;
	Button updateBtn;
	EditText resistanceEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_factory_setting);

		backBtn = (Button)findViewById(R.id.header_leftbtn);
		backBtn.setOnClickListener(this); 
		
		updateBtn = (Button)findViewById(R.id.update_btn);
		updateBtn.setOnClickListener(this); 
		
		resistanceEdit = (EditText)findViewById(R.id.resistance_edit);
		resistanceEdit.setOnClickListener(this); 
		resistanceEdit.setText(String.valueOf(PinSDK.getInstance().getResistance()));
		
		resistanceEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.update_btn) {
			
			PinSDK.getInstance().setResistance(Float.valueOf(resistanceEdit.getText().toString()));
			Log.d("factory setting ","set resistanceEdit to"+PinSDK.getInstance().getResistance());
	
		} else if (v.getId() == R.id.header_leftbtn) {
			finish();
		}
	}

}

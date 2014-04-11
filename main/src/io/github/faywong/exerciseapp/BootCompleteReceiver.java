package io.github.faywong.exerciseapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
	private static final String TAG = "BootCompleteReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent == null) {
			return;
		}
		final String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			context.startActivity(new Intent().setClass(context, Main.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			Log.d(TAG, "Boot complete!");
		}
	}

}

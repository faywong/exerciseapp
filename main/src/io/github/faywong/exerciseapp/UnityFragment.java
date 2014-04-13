package io.github.faywong.exerciseapp;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class UnityFragment extends Fragment {
	private boolean oldHasFocus = false;

	public static UnityPlayer mUnityPlayer;
	private static String TAG = "UnityFragment";

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	public void windowFocusChanged(boolean hasFocus) {
		if (mUnityPlayer != null) {
			mUnityPlayer.windowFocusChanged(hasFocus);
		}
		oldHasFocus = hasFocus;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (mUnityPlayer == null) {
			Log.e(TAG, "mUnityPlayer is created");
			mUnityPlayer = new UnityPlayer(activity);
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		//mUnityPlayer = null;
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mUnityPlayer.pause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (mUnityPlayer == null || inflater == null) {
			Log.e(TAG, "mUnityPlayer is null");
			return null;
		}
		FrameLayout parentLayout = (FrameLayout)inflater.inflate(R.layout.unity_fragment_layout, container, false);
		if (parentLayout == null) {
			Log.e(TAG, "parentLayout is null");
			return null;
		}
		
		int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
		try {
			mUnityPlayer.init(glesMode, false);
		} catch (Exception e) {
			Log.e(TAG, "FATAL ERROR, init unity player failed!");
			return null;
		}
		final View unityView = mUnityPlayer.getView();

		LayoutParams lp = new
		LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		Log.d(TAG, "mUnityPlayer view is " + unityView);
		ViewParent viewParent = (unityView.getParent());
		if (viewParent != null) {
			Log.d(TAG, "unityview is removed from previous parent view!");
			((ViewGroup)viewParent).removeView(unityView);
		}
		parentLayout.addView(unityView, 0, lp);
		mUnityPlayer.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mUnityPlayer.windowFocusChanged(oldHasFocus);	
			}
		});

		return parentLayout;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// mUnityPlayer.quit();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mUnityPlayer != null) {
			try {
				mUnityPlayer.resume();
			} catch (Exception e) {
				Log.e(TAG, "FATAL ERROR! Unity player failed in on resume!");
			}
		}
	}

}

package io.github.faywong.exerciseapp;

import java.util.Observable;

import android.util.Log;

public class SettingObservable extends Observable {
	private static final String TAG = "SettingObservable";
	private SettingModel mSettingModel;

	static private SettingObservable mSettingObservable=null;
	
	static public void destory()
	{
		mSettingObservable=null;
	}
	static public SettingObservable getInstance()
	{
		if(mSettingObservable==null)
			mSettingObservable = new SettingObservable();
		return mSettingObservable;
	}
	public SettingModel getSettingModel() {
		return mSettingModel;
	}
	
	public SettingObservable() {
		mSettingModel = new SettingModel(this);
	}
	
	public SettingObservable(SettingModel model) {
		mSettingModel = model;
	}
	
	public void onSettingChanged(int settingType, String newValue) {
		if (mSettingModel != null) {
			Log.d(TAG, "mSettingModel onSettingChanged");
			boolean needUpdate = mSettingModel.onSettingChanged(settingType, newValue);
			if (needUpdate) {
				setChanged();
				Log.d(TAG, "notifyObservers");
				notifyObservers(this);
				clearChanged();
			}
		}
	}
}

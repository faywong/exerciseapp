package io.github.faywong.exerciseapp;

import java.util.Observable;

public class SettingObservable extends Observable {
	private SettingModel mSettingModel;

	public SettingModel getSettingModel() {
		return mSettingModel;
	}
	
	public SettingObservable() {
		mSettingModel = new SettingModel();
	}
	
	public SettingObservable(SettingModel model) {
		mSettingModel = model;
	}
}

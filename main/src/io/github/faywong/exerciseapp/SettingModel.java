package io.github.faywong.exerciseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingModel {
	private final static String SETTING_FILE = "exercise_setting";
	private final static String SETTING_KEY_TIME = "time";
	private final static String SETTING_KEY_DISTANCE = "distance";
	private final static String SETTING_KEY_CALORIE = "calorie";
	private final static String SETTING_KEY_INCLINE = "incline";
	private final static String SETTING_KEY_SPEED = "speed";

	private int time = 0;
	private int distance = 0;
	private int calorie = 0;
	private int incline = 0;
	private int speed = 0;
	private boolean hasSpeedChanged = false;
	private boolean hasInclineChanged = false;

	public SettingModel(int time, int distance, int calorie, int incline,
			int speed) {
		super();
		this.time = time;
		this.distance = distance;
		this.calorie = calorie;
		this.incline = incline;
		this.speed = speed;
	}

	public boolean persistent(Context context) {
		if (context == null) {
			return false;
		}
		SharedPreferences preferences = context.getSharedPreferences(
				SETTING_FILE, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(SETTING_KEY_TIME, this.time);
		editor.putInt(SETTING_KEY_DISTANCE, this.distance);
		editor.putInt(SETTING_KEY_CALORIE, this.calorie);
		editor.putInt(SETTING_KEY_INCLINE, this.incline);
		editor.putInt(SETTING_KEY_SPEED, this.speed);
		editor.commit();
		return true;

	}

	static SettingModel fromPersistent(Context context) {
		if (context == null) {
			return null;
		} else {
			SharedPreferences preferences = context.getSharedPreferences(
					SETTING_FILE, Context.MODE_PRIVATE);
			if (preferences == null) {
				return null;
			}
			return new SettingModel(preferences.getInt(SETTING_KEY_TIME, 0),
					preferences.getInt(SETTING_KEY_DISTANCE, 0),
					preferences.getInt(SETTING_KEY_CALORIE, 0),
					preferences.getInt(SETTING_KEY_INCLINE, 0),
					preferences.getInt(SETTING_KEY_SPEED, 0));

		}
	}

	public SettingModel() {
		super();
	}

	public void clearSpeedChanged() {
		hasSpeedChanged = false;
	}

	public boolean isSpeedChanged() {
		return hasSpeedChanged;
	}

	public boolean isInclineChanged() {
		return hasInclineChanged;
	}

	public void clearInclineChanged() {
		hasInclineChanged = false;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

	public int getIncline() {
		return incline;
	}

	public void setIncline(int incline) {
		hasInclineChanged = (incline != this.incline);
		this.incline = incline;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		hasSpeedChanged = (speed != this.speed);
		this.speed = speed;
	}

}

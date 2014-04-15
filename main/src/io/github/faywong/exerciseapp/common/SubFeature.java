package io.github.faywong.exerciseapp.common;

import android.content.SharedPreferences;

public interface SubFeature {
	public SharedPreferences getPreferences();
	public String description();
	public void enable(boolean enable);
	public void isEnabled();
	public void onSwitchedIn();
	public void onSwitchedOut();
}

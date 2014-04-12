package io.github.faywong.exerciseapp.interfaces;

import io.github.faywong.exerciseapp.FreeMode;
import io.github.faywong.exerciseapp.SettingObservable;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class WidgetGroup<M extends Button, B extends TextView> {

	private String defaultValue;
	private List<WidgetGroup<Button, TextView>> conflictBuddys;
	private int settingType;
	private SettingObservable settingObservable;

	public WidgetGroup(ArrayList<B> associcatedBuddy, int[] members, int containerId, FreeMode context, String defaultValue, int settingType, SettingObservable settingObservable) {
		super();
		this.associcatedBuddy = associcatedBuddy;
		this.context = context;
		this.members = members;
		this.containerId = containerId;
		this.defaultValue = defaultValue;
		this.settingType = settingType;
		this.settingObservable = settingObservable;
		for (int viewId : members) {
			((M) context.findViewById(viewId)).setOnClickListener(context);
		}
	}

	public void setConflictBuddys(List<WidgetGroup<Button, TextView>> conflictBuddys) {
		this.conflictBuddys = conflictBuddys;
	}
	
	public int getContainerId() {
		return containerId;
	}

	private int[] members;
	private ArrayList<B> associcatedBuddy;
	private int containerId;
	private FreeMode context;
	private String TAG = "WidgetGroup";

	private boolean isMember(int id) {
		if (members != null) {
			for (int memberId : members) {
				if (id == memberId) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void associateAction(final int sourceId) {
		if (!isMember(sourceId)) {
			Log.d(TAG , "view with id:" + sourceId + " is not member!");
			return;
		}
		String newValue = ((M)context.findViewById(sourceId)).getText().toString();
		// sync values between ourself buddys in this group
		if (associcatedBuddy != null && !associcatedBuddy.isEmpty()) {
			for (B buddy: associcatedBuddy) {
				Log.d(TAG , "right case");
				buddy.setText(newValue);
			}
		}
		
		// reset conflict buddys' value
		if (conflictBuddys != null && !conflictBuddys.isEmpty()) {
			for (WidgetGroup<Button, TextView> conflictBuddy : conflictBuddys) {
				conflictBuddy.reset((WidgetGroup<Button, TextView>) this);
			}
		}
		
		// notify setting changed
		if (settingObservable != null) {
			Log.d(TAG, "settingObservable onSettingChanged");
			settingObservable.onSettingChanged(this.settingType, newValue);
		}
	}
	
	public void reset(final WidgetGroup<Button, TextView> conflictBuddy) {
		if (associcatedBuddy != null && !associcatedBuddy.isEmpty()) {
			for (B buddy: associcatedBuddy) {
				Log.d(TAG , "right case, set value to default:" + this.defaultValue);
				buddy.setText(this.defaultValue);
			}
		}
	}
}

package io.github.faywong.exerciseapp.interfaces;

import io.github.faywong.exerciseapp.FreeMode;

import java.util.ArrayList;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class WidgetGroup<M extends Button, B extends TextView> {

	public WidgetGroup(ArrayList<B> associcatedBuddy, int[] members, int containerId, FreeMode context) {
		super();
		this.associcatedBuddy = associcatedBuddy;
		this.context = context;
		this.members = members;
		this.containerId = containerId;
		for (int viewId : members) {
			((M) context.findViewById(viewId)).setOnClickListener(context);
		}
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
		if (associcatedBuddy != null && !associcatedBuddy.isEmpty()) {
			for (B buddy: associcatedBuddy) {
				Log.d(TAG , "right case");
				buddy.setText(((M)context.findViewById(sourceId)).getText().toString());
			}
		}
	}
}

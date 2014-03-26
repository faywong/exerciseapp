package io.github.faywong.exerciseapp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.util.SparseArray;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.github.faywong.exerciseapp.R;
import io.github.faywong.exerciseapp.interfaces.WidgetGroup;

public class FreeMode extends Activity implements View.OnClickListener {
	private static final String TAG = "FreeMode";
	private RelativeLayout timeControlLayout;
	private RelativeLayout timeControlPanel;
	private Button addTimeBtn;
	private Button subTimeBtn;
	private TextView timeValueText;
	private TextView distanceValueText;

	final static private String TIME_DISPLAY_FORMAT = "%s分钟";
	final static private String DISTANCE_DISPLAY_FORMAT = "%skm";
	final static private String CALORIE_DISPLAY_FORMAT = "%s千卡";
	
	final static private Pattern TIME_PATTERN = Pattern.compile("(\\d+)分钟");
	final static private Pattern DISTANCE_PATTERN = Pattern.compile("(\\d+)km");
	final static private Pattern CALORIE_PATTERN = Pattern.compile("(\\d+)千卡");

	final static private int TIME_ADJUST_STEP = 10;
	final static private int DISTANCE_ADJUST_STEP = 1;
	final static private int CALORIE_ADJUST_STEP = 100;
	
	private WidgetGroup timeGroup;
	// id of parent layout of header button control --> associated panel
	// WidgetGroup
	private SparseArray<WidgetGroup> headerControlMaps = new SparseArray<WidgetGroup>();
	private RelativeLayout distanceControlLayout;
	private RelativeLayout distanceControlPanel;
	private Button addDistanceBtn;
	private Button subDistanceBtn;
	private WidgetGroup<Button, TextView> distanceGroup;
	private Button subCalorieBtn;
	private RelativeLayout calorieControlPanel;
	private RelativeLayout calorieControlLayout;
	private Button addCalorieBtn;
	private TextView calorieValueText;
	private RelativeLayout speedControlLayout;
	private RelativeLayout speedControlPanel;
	private Button addSpeedBtn;
	private Button subSpeedBtn;
	private WidgetGroup<Button, TextView> calorieGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise_free_mode);

		// initialize time related views/handlers
		timeControlLayout = (RelativeLayout) findViewById(R.id.time_control);
		timeControlLayout.setOnClickListener(this);
		timeControlPanel = (RelativeLayout) findViewById(R.id.time_selection);
		addTimeBtn = (Button) findViewById(R.id.add_time_btn);
		addTimeBtn.setOnClickListener(this);
		subTimeBtn = (Button) findViewById(R.id.sub_time_btn);
		subTimeBtn.setOnClickListener(this);
		timeValueText = (TextView) findViewById(R.id.adjusted_time_val);
		ArrayList<TextView> timeAssocaiteBuddy = new ArrayList<TextView>();
		timeAssocaiteBuddy.add(timeValueText);
		timeGroup = new WidgetGroup<Button, TextView>(timeAssocaiteBuddy,
				new int[] { 
						R.id.time_selection_10min,
						R.id.time_selection_20min, 
						R.id.time_selection_30min,
						R.id.time_selection_40min, 
						R.id.time_selection_50min },
				R.id.time_selection, this);
		headerControlMaps.append(R.id.time_control, timeGroup);

		// initialize distance related views/handlers
		distanceControlLayout = (RelativeLayout) findViewById(R.id.distance_control);
		distanceControlLayout.setOnClickListener(this);
		distanceControlPanel = (RelativeLayout) findViewById(R.id.distance_selection);
		addDistanceBtn = (Button) findViewById(R.id.add_distance_btn);
		addDistanceBtn.setOnClickListener(this);
		subDistanceBtn = (Button) findViewById(R.id.sub_distance_btn);
		subDistanceBtn.setOnClickListener(this);
		distanceValueText = (TextView) findViewById(R.id.adjusted_distance_val);
		ArrayList<TextView> distanceAssocaiteBuddy = new ArrayList<TextView>();
		distanceAssocaiteBuddy.add(distanceValueText);
		distanceGroup = new WidgetGroup<Button, TextView>(
				distanceAssocaiteBuddy, new int[] {
						R.id.distance_selection_1km,
						R.id.distance_selection_2km,
						R.id.distance_selection_3km,
						R.id.distance_selection_4km,
						R.id.distance_selection_5km }, R.id.distance_selection,
				this);
		headerControlMaps.append(R.id.distance_control, distanceGroup);

		// initialize calorie related views/handlers
		calorieControlLayout = (RelativeLayout) findViewById(R.id.calorie_control);
		calorieControlLayout.setOnClickListener(this);
		calorieControlPanel = (RelativeLayout) findViewById(R.id.calorie_selection);
		addCalorieBtn = (Button) findViewById(R.id.add_calorie_btn);
		addCalorieBtn.setOnClickListener(this);
		subCalorieBtn = (Button) findViewById(R.id.sub_calorie_btn);
		subCalorieBtn.setOnClickListener(this);
		calorieValueText = (TextView) findViewById(R.id.adjusted_calorie_val);
		ArrayList<TextView> calorieAssocaiteBuddy = new ArrayList<TextView>();
		calorieAssocaiteBuddy.add(calorieValueText);
		calorieGroup = new WidgetGroup<Button, TextView>(
				distanceAssocaiteBuddy, new int[] {
						R.id.calorie_selection_100kcal,
						R.id.calorie_selection_100kcal,
						R.id.calorie_selection_100kcal,
						R.id.calorie_selection_100kcal,
						R.id.calorie_selection_100kcal },
				R.id.distance_selection, this);
		headerControlMaps.append(R.id.calorie_control, calorieGroup);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
	private static int parse(final Pattern pattern, final String targetString)
			throws IllegalArgumentException {
		Matcher m = pattern.matcher(targetString);
		while (m.find()) { // Find each match in turn; String can't do this.
			String time = m.group(1);
			return Integer.parseInt(time);
		}
		throw new IllegalArgumentException("The string(" + targetString
				+ ") format is invalid!");
	}
	
	private static int parseTime(final String targetString)
			throws IllegalArgumentException {
		return parse(TIME_PATTERN, targetString);
	}

	
	private static int parseDistance(final String targetString)
			throws IllegalArgumentException {
		return parse(DISTANCE_PATTERN, targetString);
	}
	
	private static int parseCalorie(final String targetString)
			throws IllegalArgumentException {
		return parse(CALORIE_PATTERN, targetString);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		}
		boolean containerBecomeVisible = false;
		int containerIdBecameVisible = 0;
		final int viewId = v.getId();
		Log.d(TAG, "onClick() viewId:" + viewId);
		if (viewId == R.id.time_control 
	         || viewId == R.id.distance_control
		     || viewId == R.id.calorie_control) {
			final int containerId = headerControlMaps.get(viewId)
					.getContainerId();
			final RelativeLayout controlLayout = (RelativeLayout) findViewById(containerId);
			if (controlLayout.getVisibility() == View.GONE) {
				controlLayout.setVisibility(View.VISIBLE);
				containerBecomeVisible = true;
				containerIdBecameVisible = containerId;
				Log.d(TAG, "controlIdBecameVisible:" + containerIdBecameVisible);
			} else if (controlLayout.getVisibility() == View.VISIBLE) {
				controlLayout.setVisibility(View.GONE);
			}
		} else if (viewId == R.id.add_time_btn) {
			final String oldString = timeValueText.getText().toString();
			final int oldTime = parseTime(oldString);
			timeValueText.setText(String.format(TIME_DISPLAY_FORMAT,
					String.valueOf(oldTime + TIME_ADJUST_STEP)));
		} else if (viewId == R.id.sub_time_btn) {
			final String oldString = timeValueText.getText().toString();
			final int oldTime = parseTime(oldString);
			timeValueText.setText(String.format(TIME_DISPLAY_FORMAT,
					String.valueOf(Math.max(oldTime - TIME_ADJUST_STEP, 0))));
		} else if (viewId == R.id.add_distance_btn) {
			final String oldString = distanceValueText.getText().toString();
			final int oldTime = parseDistance(oldString);
			timeValueText.setText(String.format(DISTANCE_DISPLAY_FORMAT,
					String.valueOf(oldTime + DISTANCE_ADJUST_STEP)));
		} else if (viewId == R.id.sub_distance_btn) {
			final String oldString = distanceValueText.getText().toString();
			final int oldTime = parseDistance(oldString);
			timeValueText.setText(String.format(DISTANCE_DISPLAY_FORMAT,
					String.valueOf(Math.max(oldTime - DISTANCE_ADJUST_STEP, 0))));
		}

		for (int i = 0; i < headerControlMaps.size(); i++) {
			WidgetGroup<Button, TextView> group = (WidgetGroup<Button, TextView>) headerControlMaps
					.get(headerControlMaps.keyAt(i));
			if (group == null) {
				continue;
			}
			final int containerId = group.getContainerId();
			final RelativeLayout containerLayout = (RelativeLayout) findViewById(containerId);

			// dismiss all the unwanted containers
			Log.d(TAG, "containerId:" + containerId);
			if (containerBecomeVisible
					&& containerId != containerIdBecameVisible) {
				Log.d(TAG, "containerLayout:" + containerLayout
						+ " containerId:" + containerId);
				containerLayout.setVisibility(View.GONE);
			}

			// dispatch event to the container members
			// if (containerLayout.getVisibility() == View.VISIBLE) {
			group.associateAction(viewId);
			// }
		}
	}
}

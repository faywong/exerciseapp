package io.github.faywong.exerciseapp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	private boolean exerciseSessionStarted = false;

	final static private String TIME_DISPLAY_FORMAT = "%s分钟";
	final static private String DISTANCE_DISPLAY_FORMAT = "%skm";
	final static private String CALORIE_DISPLAY_FORMAT = "%s千卡";
	final static private String SPEED_DISPLAY_FORMAT = "%skm/h";
	final static private String INCLINE_DISPLAY_FORMAT = "%s%%";

	final static private Pattern TIME_PATTERN = Pattern.compile("(\\d+)分钟");
	final static private Pattern DISTANCE_PATTERN = Pattern.compile("(\\d+)km");
	final static private Pattern CALORIE_PATTERN = Pattern.compile("(\\d+)千卡");
	final static private Pattern SPEED_PATTERN = Pattern.compile("(\\d+)km/h");
	final static private Pattern INCLINE_PATTERN = Pattern.compile("(\\d+)%");

	final static private int TIME_ADJUST_STEP = 10;
	final static private int DISTANCE_ADJUST_STEP = 1;
	final static private int CALORIE_ADJUST_STEP = 100;
	final static private int SPEED_ADJUST_STEP = 1;
	final static private int INCLINE_ADJUST_STEP = 1;

	private WidgetGroup timeGroup;
	// id of parent layout of header button control --> associated panel
	// WidgetGroup
	private SparseArray<WidgetGroup<Button, TextView>> headerControlMaps = new SparseArray<WidgetGroup<Button, TextView>>();
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
	private TextView speedValueText;
	private WidgetGroup<Button, TextView> speedGroup;
	private RelativeLayout inclineControlLayout;
	private RelativeLayout inclineControlPanel;
	private Button addInclineBtn;
	private Button subInclineBtn;
	private TextView inclineValueText;
	private WidgetGroup<Button, TextView> inclineGroup;
	private ImageButton startBtn;
	private TextView countDownText;

	private ArrayList<RelativeLayout> panelLayouts = new ArrayList<RelativeLayout>();
	private Handler hander = new Handler();
	private Runnable operationTimeOutRunable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dismissAllPanels();
		}
	};
	private ImageView bgImage;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_free_mode);

		// initialize time related views/handlers
		timeControlLayout = (RelativeLayout) findViewById(R.id.time_control);
		timeControlLayout.setOnClickListener(this);
		timeControlPanel = (RelativeLayout) findViewById(R.id.time_selection);
		panelLayouts.add(timeControlPanel);
		addTimeBtn = (Button) findViewById(R.id.add_time_btn);
		addTimeBtn.setOnClickListener(this);
		subTimeBtn = (Button) findViewById(R.id.sub_time_btn);
		subTimeBtn.setOnClickListener(this);
		timeValueText = (TextView) findViewById(R.id.adjusted_time_val);
		ArrayList<TextView> timeAssocaiteBuddy = new ArrayList<TextView>();
		timeAssocaiteBuddy.add(timeValueText);
		timeGroup = new WidgetGroup<Button, TextView>(timeAssocaiteBuddy,
				new int[] { R.id.time_selection_10min,
						R.id.time_selection_20min, R.id.time_selection_30min,
						R.id.time_selection_40min, R.id.time_selection_50min },
				R.id.time_selection, this);
		headerControlMaps.append(R.id.time_control, timeGroup);

		// initialize distance related views/handlers
		distanceControlLayout = (RelativeLayout) findViewById(R.id.distance_control);
		distanceControlLayout.setOnClickListener(this);
		distanceControlPanel = (RelativeLayout) findViewById(R.id.distance_selection);
		panelLayouts.add(distanceControlPanel);
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
		panelLayouts.add(calorieControlPanel);
		addCalorieBtn = (Button) findViewById(R.id.add_calorie_btn);
		addCalorieBtn.setOnClickListener(this);
		subCalorieBtn = (Button) findViewById(R.id.sub_calorie_btn);
		subCalorieBtn.setOnClickListener(this);
		calorieValueText = (TextView) findViewById(R.id.adjusted_calorie_val);
		ArrayList<TextView> calorieAssocaiteBuddy = new ArrayList<TextView>();
		calorieAssocaiteBuddy.add(calorieValueText);
		calorieGroup = new WidgetGroup<Button, TextView>(calorieAssocaiteBuddy,
				new int[] { R.id.calorie_selection_100kcal,
						R.id.calorie_selection_200kcal,
						R.id.calorie_selection_300kcal,
						R.id.calorie_selection_400kcal,
						R.id.calorie_selection_500kcal },
				R.id.calorie_selection, this);
		headerControlMaps.append(R.id.calorie_control, calorieGroup);

		// initialize speed related views/handlers
		speedControlLayout = (RelativeLayout) findViewById(R.id.speed_control);
		speedControlLayout.setOnClickListener(this);
		speedControlPanel = (RelativeLayout) findViewById(R.id.speed_selection);
		panelLayouts.add(speedControlPanel);
		addSpeedBtn = (Button) findViewById(R.id.add_speed_btn);
		addSpeedBtn.setOnClickListener(this);
		subSpeedBtn = (Button) findViewById(R.id.sub_speed_btn);
		subSpeedBtn.setOnClickListener(this);
		speedValueText = (TextView) findViewById(R.id.adjusted_speed_val);
		ArrayList<TextView> speedAssocaiteBuddy = new ArrayList<TextView>();
		speedAssocaiteBuddy.add(speedValueText);
		speedGroup = new WidgetGroup<Button, TextView>(speedAssocaiteBuddy,
				new int[] { R.id.speed_selection_4km_per_h,
						R.id.speed_selection_6km_per_h,
						R.id.speed_selection_8km_per_h,
						R.id.speed_selection_10km_per_h,
						R.id.speed_selection_12km_per_h },
				R.id.speed_selection, this);
		headerControlMaps.append(R.id.speed_control, speedGroup);

		// initialize incline related views/handlers
		inclineControlLayout = (RelativeLayout) findViewById(R.id.incline_control);
		inclineControlLayout.setOnClickListener(this);
		inclineControlPanel = (RelativeLayout) findViewById(R.id.incline_selection);
		panelLayouts.add(inclineControlPanel);
		addInclineBtn = (Button) findViewById(R.id.add_incline_btn);
		addInclineBtn.setOnClickListener(this);
		subInclineBtn = (Button) findViewById(R.id.sub_incline_btn);
		subInclineBtn.setOnClickListener(this);
		inclineValueText = (TextView) findViewById(R.id.adjusted_incline_val);
		ArrayList<TextView> inclineAssocaiteBuddy = new ArrayList<TextView>();
		inclineAssocaiteBuddy.add(inclineValueText);
		inclineGroup = new WidgetGroup<Button, TextView>(inclineAssocaiteBuddy,
				new int[] { R.id.incline_selection_0_percent,
						R.id.incline_selection_3_percent,
						R.id.incline_selection_6_percent,
						R.id.incline_selection_9_percent,
						R.id.incline_selection_12_percent },
				R.id.incline_selection, this);
		headerControlMaps.append(R.id.incline_control, inclineGroup);

		startBtn = (ImageButton) findViewById(R.id.start_btn);
		startBtn.setOnClickListener(this);

		countDownText = (TextView) findViewById(R.id.count_down_text);
		bgImage = (ImageView) findViewById(R.id.bg_image);

		final GestureDetector gdt = new GestureDetector(this,
				new OnGestureListener() {

					@Override
					public boolean onDown(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// TODO Auto-generated method stub
						if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
								&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
							Log.d(TAG, "Right to left fing!");
							return false; // Right to left
						} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
								&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
							Log.d(TAG, "Left to right fing!");
							return false; // Left to right
						}

						if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
								&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
							Log.d(TAG, "Bottom to top fing!");
							return false; // Bottom to top
						} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
								&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
							Log.d(TAG, "Top to bottom fing!");
							return false; // Top to bottom
						}
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}

				});

		bgImage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				return !gdt.onTouchEvent(event);
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		dismissAllPanels();
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

	private static int parseSpeed(final String targetString)
			throws IllegalArgumentException {
		return parse(SPEED_PATTERN, targetString);
	}

	private static int parseIncline(final String targetString)
			throws IllegalArgumentException {
		return parse(INCLINE_PATTERN, targetString);
	}

	private void startCountDown(long totalMillis, long deltaMillis) {
		countDownText.setVisibility(View.VISIBLE);
		new CountDownTimer(totalMillis, deltaMillis) {
			public void onTick(long millisUntilFinished) {
				countDownText.setText("" + millisUntilFinished / 1000);
			}

			public void onFinish() {
				countDownText.setText("GO!");
				countDownText.setVisibility(View.GONE);
			}
		}.start();
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
		if (viewId == R.id.start_btn) {
			dismissAllPanels();
			if (exerciseSessionStarted) {
				startBtn.setImageResource(R.drawable.start);
			} else {
				startBtn.setImageResource(R.drawable.stop);
			}
			exerciseSessionStarted = (!exerciseSessionStarted);
			if (exerciseSessionStarted) {
				startCountDown(6000, 1000);
			} else {
				// TODO: stop the current exercise session
			}
		}

		if (viewId == R.id.time_control || viewId == R.id.distance_control
				|| viewId == R.id.calorie_control
				|| viewId == R.id.speed_control
				|| viewId == R.id.incline_control) {
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
			timeValueText.setText(String.format(TIME_DISPLAY_FORMAT, String
					.valueOf(Math.max(oldTime - TIME_ADJUST_STEP,
							TIME_ADJUST_STEP))));
		} else if (viewId == R.id.add_distance_btn) {
			final String oldString = distanceValueText.getText().toString();
			final int oldTime = parseDistance(oldString);
			distanceValueText.setText(String.format(DISTANCE_DISPLAY_FORMAT,
					String.valueOf(oldTime + DISTANCE_ADJUST_STEP)));
		} else if (viewId == R.id.sub_distance_btn) {
			final String oldString = distanceValueText.getText().toString();
			final int oldTime = parseDistance(oldString);
			distanceValueText.setText(String.format(DISTANCE_DISPLAY_FORMAT,
					String.valueOf(Math.max(oldTime - DISTANCE_ADJUST_STEP,
							DISTANCE_ADJUST_STEP))));
		} else if (viewId == R.id.add_calorie_btn) {
			final String oldString = calorieValueText.getText().toString();
			final int oldTime = parseCalorie(oldString);
			calorieValueText.setText(String.format(CALORIE_DISPLAY_FORMAT,
					String.valueOf(oldTime + CALORIE_ADJUST_STEP)));
		} else if (viewId == R.id.sub_calorie_btn) {
			final String oldString = calorieValueText.getText().toString();
			final int oldTime = parseCalorie(oldString);
			calorieValueText.setText(String.format(CALORIE_DISPLAY_FORMAT,
					String.valueOf(Math.max(oldTime - CALORIE_ADJUST_STEP,
							CALORIE_ADJUST_STEP))));
		} else if (viewId == R.id.add_speed_btn) {
			final String oldString = speedValueText.getText().toString();
			final int oldTime = parseSpeed(oldString);
			speedValueText.setText(String.format(SPEED_DISPLAY_FORMAT,
					String.valueOf(oldTime + SPEED_ADJUST_STEP)));
		} else if (viewId == R.id.sub_speed_btn) {
			final String oldString = speedValueText.getText().toString();
			final int oldTime = parseSpeed(oldString);
			speedValueText.setText(String.format(SPEED_DISPLAY_FORMAT,
					String.valueOf(Math.max(oldTime - SPEED_ADJUST_STEP, 0))));
		} else if (viewId == R.id.add_incline_btn) {
			final String oldString = inclineValueText.getText().toString();
			final int oldTime = parseIncline(oldString);
			inclineValueText.setText(String.format(INCLINE_DISPLAY_FORMAT,
					String.valueOf(oldTime + INCLINE_ADJUST_STEP)));
		} else if (viewId == R.id.sub_incline_btn) {
			final String oldString = inclineValueText.getText().toString();
			final int oldTime = parseIncline(oldString);
			inclineValueText
					.setText(String.format(
							INCLINE_DISPLAY_FORMAT,
							String.valueOf(Math.max(oldTime
									- INCLINE_ADJUST_STEP, 0))));
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

		hander.removeCallbacks(operationTimeOutRunable);
		hander.postDelayed(operationTimeOutRunable, 3000);
	}

	private void dismissAllPanels() {
		// TODO Auto-generated method stub
		for (RelativeLayout panel : panelLayouts) {
			panel.setVisibility(View.GONE);
		}
	}
}


package io.github.faywong.exerciseapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sportsSDK.PinSDK;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.BoringLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.github.faywong.exerciseapp.R;
import io.github.faywong.exerciseapp.common.WidgetGroup;

/**
 * All rights reserved.
 * 
 * @author faywong(philip584521@gmail.com)
 */
public class FreeMode extends FragmentActivity implements View.OnClickListener, OnClickListener {
    private static final String TAG = "FreeMode";
    private RelativeLayout timeControlLayout;
    private RelativeLayout timeControlPanel;
    private Button addTimeBtn;
    private Button subTimeBtn;
    private TextView timeValueText;
    private TextView distanceValueText;
    private AtomicBoolean sessionStarted = new AtomicBoolean(false);

    final static private String TIME_DISPLAY_FORMAT = "%s分钟";
    final static private String DISTANCE_DISPLAY_FORMAT = "%skm";
    final static private String CALORIE_DISPLAY_FORMAT = "%s千卡";
    final static private String SPEED_DISPLAY_FORMAT = "%skm/h";
    final static private String INCLINE_DISPLAY_FORMAT = "%s%%";

    final static private Pattern TIME_PATTERN = Pattern.compile("(\\d+)分钟");
    final static private Pattern DISTANCE_PATTERN = Pattern.compile("([0-9]*\\.?[0-9]+)km");
    final static private Pattern CALORIE_PATTERN = Pattern.compile("(\\d+)千卡");
    final static private Pattern SPEED_PATTERN = Pattern.compile("([0-9]*\\.?[0-9]+)km/h");
    final static private Pattern INCLINE_PATTERN = Pattern.compile("(\\d+)%");

    final static private int TIME_ADJUST_STEP = 1;
    final static private int DISTANCE_ADJUST_STEP = 1;
    final static private int CALORIE_ADJUST_STEP = 100;
    final static private int SPEED_ADJUST_STEP = 1;
    final static private int INCLINE_ADJUST_STEP = 1;
    final static private int INCLINE_MAX = 12;
    final static private int SPEED_MAX = 15;

    final static public int free_mode = 1;
    final static public int aerobics_mode = 2;
    final static public int fast_mode = 3;
    final static public int strength_mode = 4;
    final static public int memorize_mode = 5;
    final static public int real_mode = 5;

    static public int curMode = free_mode;
    private DecimalFormat mDecimalFormator = new DecimalFormat("##0.00");

    static boolean firstStart = false;
    static boolean firstTimeStart = false;
    private WidgetGroup<Button, TextView> timeGroup;
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
    private ImageButton musicBtn;
    private ImageButton webBtn;
    private ImageButton tvBtn;
    private TextView countDownText;
    private Button backBtn;

    private ArrayList<RelativeLayout> panelLayouts = new ArrayList<RelativeLayout>();
    private Handler hander = new Handler();
    private int mLastErrorCode = -1;
    private Runnable operationTimeOutRunable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            dismissAllPanels();
        }
    };

    private Runnable countDownTextOperationTimeOutRunable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            countDownText.setTextColor(color.transparent);
        }
    };

    public static interface IFragmentControlHandler {
        public View getControlView();

        public void onSwitchedIn();

        public void onSwitchedOut();

        public void onToggleScreen();
    }

    private TextView timeHeadText;
    private TextView distanceHeadText;
    private TextView calorieHeadText;
    private TextView speedHeadText;
    private TextView inclineHeadText;

    private ColorStateList defaultColorStateList;

    final Animation countDownTextIn = new AlphaAnimation(0.0f, 1.0f);
    final Animation countDownTextOut = new AlphaAnimation(1.0f, 0.0f);
    private ImageButton videoPlayBtn;

    private static final int SWIPE_MIN_DISTANCE_HORIZONTAL = 80;
    private static final int SWIPE_THRESHOLD_VELOCITY_HORIZONTAL = 100;
    private static final int SWIPE_MIN_DISTANCE_VERTICAL = SWIPE_MIN_DISTANCE_HORIZONTAL / 2;
    private static final int SWIPE_THRESHOLD_VELOCITY_VERTICAL = SWIPE_MIN_DISTANCE_VERTICAL * 2 / 3;

    private SettingObservable mSettingObservable;
    private FragmentManager mFragmentManager;
    private UnityFragment mUnityFragment;
    private SurfFragment mSurfFragment;
    private MusicFragment mMusicFragment;
    private VideoPlayerFragment mVideoPlayerFragment;
    private ImageButton unityBtn;
    private UnityObserver mUnityObserver;
    private CountDownTimer mCountDownTimer;
    private AlertDialog mAlertDialog;
    public Handler mHandler;
    public static FreeMode sInstance = null;

    private IHWStatusListener mHWStatusListener = new IHWStatusListener() {

        @Override
        public void onSpeedPlus() {
            // TODO Auto-generated method stub
            FreeMode.this.onClick_(R.id.add_speed_btn);
        }

        @Override
        public void onSpeedReduce() {
            // TODO Auto-generated method stub
            FreeMode.this.onClick_(R.id.sub_speed_btn);
        }

        @Override
        public void onInclinePlus() {
            // TODO Auto-generated method stub
            FreeMode.this.onClick_(R.id.add_incline_btn);
        }

        @Override
        public void onInclineReduce() {
            // TODO Auto-generated method stub
            FreeMode.this.onClick_(R.id.sub_incline_btn);
        }

        @Override
        public void onHWStatusChanged(int errorCode, int calory, int pulse) {
            // TODO Auto-generated method stub
            /*
             * Log.d(TAG, "HW status changed[errorCode:" + errorCode +
             * " calorie:" + calory + " pulse:" + pulse + "]");
             */
            mLastErrorCode = errorCode;
            FreeMode.this.mSettingObservable.setLastErrorCode(mLastErrorCode);
            
            if (errorCode != 0) {
                Log.d(TAG, "Error occurred! errorCode: " + errorCode);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        setSessionState(false);
                    }
                });
                FreeMode.this.popUpErrorDialog(errorCode);
            } else if (FreeMode.this.sessionStarted.get()) {
                final int finalPulse = pulse;
                // Log.d(TAG, "onHWStatusChanged() pulse: " + finalPulse);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                       FreeMode.this.heartRateText.setText(finalPulse + "");
                    }
                    
                });
            }
        }

        @Override
        public void onStartOrStop() {
            // TODO Auto-generated method stub
            swapSessionState();
        }

    };
    private LinearLayout mFragmentControlParentLayout;
    private IntentFilter mIntentFilter;
    private int mExerciseTime = 0;

    private HandlerThread mHandlerThread;
    public Handler mBackgroundThreadHandler;
    private TextView timeElapsedText;
    private TextView heartRateText;

    private void popUpErrorDialog(final int errorCode) {
        Log.d(TAG, "popUpErrorDialog() errorCode:" + errorCode);

        String errorPrompt = "";
        switch (errorCode) {
            case 0:
                Log.d(TAG, "Flow should not come here!");
                break;
            case 2:
                errorPrompt = "电压过低";
                break;
            case 3:
                errorPrompt = "电机没有插或电机电压测量故障";
                break;
            case 4:
                errorPrompt = "电压过高";
                break;

            case 5:
                errorPrompt = "过流";
                break;

            case 6:
                errorPrompt = "输出短路";
                break;

            case 7:
                errorPrompt = "IBGT短路";
                break;

            case 10:
                errorPrompt = "系统故障";
                break;

            case 11:
                errorPrompt = "安全开关脱落";
                break;

        }

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .create();
            mAlertDialog.setTitle(R.string.error_dialog_title);
        }
        mAlertDialog.dismiss();
        mAlertDialog.setMessage(errorPrompt + "(错误码: " + errorCode + " )");
        mAlertDialog.setButton(
                getString(R.string.confirm_error_notification), this);
        mAlertDialog.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_free_mode);
        

        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("media_loader");
            mHandlerThread.start();
            mBackgroundThreadHandler = new Handler(mHandlerThread.getLooper());
        }
        
        sInstance = this;
        mHandler = new Handler();
        mFragmentControlParentLayout = (LinearLayout) findViewById(R.id.fregment_control);
        mFragmentManager = getSupportFragmentManager();
        LayoutInflater.from(this);
        // initialize animations
        countDownTextIn.setDuration(2000);
        countDownTextOut.setDuration(2000);
        countDownTextOut.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                // countDownText.startAnimation(countDownTextOut);
                countDownText.setTextColor(color.transparent);
                ;
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Au to-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }
        });
        mSettingObservable = SettingObservable.getInstance();

        final Resources resources = getResources();

        timeElapsedText = (TextView) findViewById(R.id.time_elapsed);
        
        // initialize time related views/handlers
        backBtn = (Button) findViewById(R.id.freeback_btn);
        backBtn.setOnClickListener(this);
        timeControlLayout = (RelativeLayout) findViewById(R.id.time_control);
        timeControlLayout.setOnClickListener(this);
        timeControlPanel = (RelativeLayout) findViewById(R.id.time_selection);
        panelLayouts.add(timeControlPanel);
        addTimeBtn = (Button) findViewById(R.id.add_time_btn);
        addTimeBtn.setOnClickListener(this);
        subTimeBtn = (Button) findViewById(R.id.sub_time_btn);
        subTimeBtn.setOnClickListener(this);
        timeValueText = (TextView) findViewById(R.id.adjusted_time_val);
        timeHeadText = (TextView) findViewById(R.id.time);
        ArrayList<TextView> timeAssocaiteBuddy = new ArrayList<TextView>();
        timeAssocaiteBuddy.add(timeValueText);
        timeAssocaiteBuddy.add(timeHeadText);
        timeGroup = new WidgetGroup<Button, TextView>(timeAssocaiteBuddy,
                new int[] {
                        R.id.time_selection_10min,
                        R.id.time_selection_20min, R.id.time_selection_30min,
                        R.id.time_selection_40min, R.id.time_selection_50min
                },
                R.id.time_selection, this,
                resources.getString(R.string.default_time_display),
                SettingModel.SETTING_TYPE_TIME, mSettingObservable);
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
        distanceHeadText = (TextView) findViewById(R.id.distance);
        ArrayList<TextView> distanceAssocaiteBuddy = new ArrayList<TextView>();
        distanceAssocaiteBuddy.add(distanceValueText);
        distanceAssocaiteBuddy.add(distanceHeadText);
        distanceGroup = new WidgetGroup<Button, TextView>(
                distanceAssocaiteBuddy, new int[] {
                        R.id.distance_selection_1km,
                        R.id.distance_selection_2km,
                        R.id.distance_selection_3km,
                        R.id.distance_selection_4km,
                        R.id.distance_selection_5km
                }, R.id.distance_selection,
                this, resources.getString(R.string.default_distance_display),
                SettingModel.SETTING_TYPE_DISTANCE, mSettingObservable);
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
        calorieHeadText = (TextView) findViewById(R.id.calorie);
        ArrayList<TextView> calorieAssocaiteBuddy = new ArrayList<TextView>();
        calorieAssocaiteBuddy.add(calorieValueText);
        calorieAssocaiteBuddy.add(calorieHeadText);
        calorieGroup = new WidgetGroup<Button, TextView>(calorieAssocaiteBuddy,
                new int[] {
                        R.id.calorie_selection_100kcal,
                        R.id.calorie_selection_200kcal,
                        R.id.calorie_selection_300kcal,
                        R.id.calorie_selection_400kcal,
                        R.id.calorie_selection_500kcal
                },
                R.id.calorie_selection, this,
                resources.getString(R.string.default_calorie_display),
                SettingModel.SETTING_TYPE_CALORIE, mSettingObservable);
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
        speedHeadText = (TextView) findViewById(R.id.speed);
        ArrayList<TextView> speedAssocaiteBuddy = new ArrayList<TextView>();
        speedAssocaiteBuddy.add(speedValueText);
        speedAssocaiteBuddy.add(speedHeadText);
        speedGroup = new WidgetGroup<Button, TextView>(speedAssocaiteBuddy,
                new int[] {
                        R.id.speed_selection_4km_per_h,
                        R.id.speed_selection_6km_per_h,
                        R.id.speed_selection_8km_per_h,
                        R.id.speed_selection_10km_per_h,
                        R.id.speed_selection_12km_per_h
                },
                R.id.speed_selection, this,
                resources.getString(R.string.default_speed_display),
                SettingModel.SETTING_TYPE_SPEED, mSettingObservable);
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
        inclineHeadText = (TextView) findViewById(R.id.incline);
        ArrayList<TextView> inclineAssocaiteBuddy = new ArrayList<TextView>();
        inclineAssocaiteBuddy.add(inclineValueText);
        inclineAssocaiteBuddy.add(inclineHeadText);
        inclineGroup = new WidgetGroup<Button, TextView>(inclineAssocaiteBuddy,
                new int[] {
                        R.id.incline_selection_0_percent,
                        R.id.incline_selection_3_percent,
                        R.id.incline_selection_6_percent,
                        R.id.incline_selection_9_percent,
                        R.id.incline_selection_12_percent
                },
                R.id.incline_selection, this,
                resources.getString(R.string.default_incline_display),
                SettingModel.SETTING_TYPE_INCLINE, mSettingObservable);
        headerControlMaps.append(R.id.incline_control, inclineGroup);

        // build the conflict relationships

        // time group
        List<WidgetGroup<Button, TextView>> conflicts = new ArrayList<WidgetGroup<Button, TextView>>();
        conflicts.add(distanceGroup);
        conflicts.add(calorieGroup);
        timeGroup.setConflictBuddys(conflicts);

        // distance group
        conflicts = new ArrayList<WidgetGroup<Button, TextView>>();
        conflicts.add(timeGroup);
        conflicts.add(calorieGroup);
        distanceGroup.setConflictBuddys(conflicts);

        // calorie group
        conflicts = new ArrayList<WidgetGroup<Button, TextView>>();
        conflicts.add(timeGroup);
        conflicts.add(distanceGroup);
        calorieGroup.setConflictBuddys(conflicts);

        startBtn = (ImageButton) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(this);

        musicBtn = (ImageButton) findViewById(R.id.music_btn);
        musicBtn.setOnClickListener(this);

        webBtn = (ImageButton) findViewById(R.id.surf_btn);
        webBtn.setOnClickListener(this);

        tvBtn = (ImageButton) findViewById(R.id.tv_btn);
        tvBtn.setOnClickListener(this);

        countDownText = (TextView) findViewById(R.id.count_down_text);
        
        heartRateText = (TextView) findViewById(R.id.heart_rate_label_val);

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
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE_HORIZONTAL
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY_HORIZONTAL) {
                            Log.d(TAG, "Right to left fing!");
                            handleHorizontalFling(false);
                            return false; // Right to left
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE_HORIZONTAL
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY_HORIZONTAL) {
                            Log.d(TAG, "Left to right fing!");
                            handleHorizontalFling(true);
                            return false; // Left to right
                        }

                        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE_VERTICAL
                                && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY_VERTICAL) {
                            Log.d(TAG, "Bottom to top fing!");
                            handleVerticalFling(true);
                            return false; // Bottom to top
                        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE_VERTICAL
                                && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY_VERTICAL) {
                            Log.d(TAG, "Top to bottom fing!");
                            handleVerticalFling(false);
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

        countDownText.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                return !gdt.onTouchEvent(event);
            }
        });

        unityBtn = (ImageButton) findViewById(R.id.unity_btn);
        unityBtn.setOnClickListener(this);
        initializeBottomTools();

        if (mUnityFragment == null) {
            mUnityFragment = new UnityFragment();
        }

        if (mMusicFragment == null) {
            mMusicFragment = new MusicFragment();
        }

        if (mSurfFragment == null) {
            mSurfFragment = new SurfFragment();
        }

        if (mVideoPlayerFragment == null) {
            mVideoPlayerFragment = new VideoPlayerFragment();
        }

        mUnityObserver = new UnityObserver();
        mSettingObservable.addObserver(mUnityObserver);

        PinSDK.getInstance().setHWStatusListener(mHWStatusListener);

        if (!firstStart)
        {
            firstTimeStart = true;
            Intent intent = new Intent();
            intent.setClass(this, Main.class);
            startActivityForResult(intent, 0);
            firstStart = true;
        }
        else
            initMode();
    }

    private void initMode()
    {
        if (curMode == free_mode)
        {

        } else if (curMode == real_mode)
        {
            this.switchToVideoPlayerFragment(VideoPlayerFragment.real_mode);
        }

    }

    private void enableTargetSettingControls(final boolean enable) {
        timeControlLayout.setClickable(enable);
        distanceControlLayout.setClickable(enable);
        calorieControlLayout.setClickable(enable);
    }

    private void initializeBottomTools() {
        // TODO Auto-generated method stub
        videoPlayBtn = (ImageButton) findViewById(R.id.video_btn);
        videoPlayBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mUnityFragment != null) {
            mUnityFragment.onParentActiviytDestroyed();
        }
        
        if (mVideoPlayerFragment != null) {
            mVideoPlayerFragment.finish();
            mVideoPlayerFragment = null;
        }
        
        if (mMusicFragment != null) {
            mMusicFragment.finish();
            mMusicFragment = null;
        }
        
        PinSDK.getInstance().setHWStatusListener(null);

        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
            mBackgroundThreadHandler = null;
        }
    }

    private void switchToUnityFragment() {
        if (mFragmentManager == null) {
            return;
        }
        mFragmentControlParentLayout.removeAllViews();

        android.support.v4.app.FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mUnityFragment);
        fragmentTransaction.commit();
    }

    private void switchToMusicFragment() {
        if (mFragmentManager == null) {
            return;
        }

        mFragmentControlParentLayout.removeAllViews();

        android.support.v4.app.FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mMusicFragment);

        fragmentTransaction.commit();
        mFragmentControlParentLayout.addView(mMusicFragment.getControlView());
    }

    private void switchToVideoPlayerFragment(int mode) {
        if (mFragmentManager == null) {
            return;
        }

        mFragmentControlParentLayout.removeAllViews();

        android.support.v4.app.FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                mVideoPlayerFragment);
        fragmentTransaction.commit();
        mVideoPlayerFragment.curMode = mode;
        mFragmentControlParentLayout.addView(mVideoPlayerFragment.getControlView());
    }

    private void switchToSurfFragment(int mode) {
        if (mFragmentManager == null) {
            return;
        }
        
        mFragmentControlParentLayout.removeAllViews();

        SurfFragment.curMode = mode;
        mSurfFragment.setMode();
        
        android.support.v4.app.FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mSurfFragment);
        fragmentTransaction.commit();
        mFragmentControlParentLayout.addView(mSurfFragment.getControlView());
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (mUnityFragment != null) {
            mUnityFragment.windowFocusChanged(hasFocus);
        }
    }

    private void handleHorizontalFling(final boolean right) {
        if (!defaultColorStateList.equals(countDownText.getTextColors())) {
            Log.d(TAG, "start to display case");
            countDownText.setText(speedHeadText.getText());
            countDownText.setTextColor(defaultColorStateList);
            /*
             * countDownText.clearAnimation(); countDownTextIn.setDuration(500);
             * countDownText.startAnimation(countDownTextIn);
             */
        } else {
            final String newValue = getNewSpeedText(speedHeadText.getText()
                    .toString(), right);
            countDownText.setText(newValue);
            speedHeadText.setText(newValue);
            speedValueText.setText(newValue);

            /*
             * countDownText.clearAnimation();
             * countDownTextOut.setDuration(2000);
             * countDownText.startAnimation(countDownTextOut);
             */
        }

        hander.removeCallbacks(countDownTextOperationTimeOutRunable);
        hander.postDelayed(countDownTextOperationTimeOutRunable, 3000);
    }

    private void handleVerticalFling(final boolean top) {
        if (!defaultColorStateList.equals(countDownText.getTextColors())) {
            Log.d(TAG, "start to display case");
            countDownText.setText(inclineHeadText.getText());
            countDownText.setTextColor(defaultColorStateList);
            /*
             * countDownText.clearAnimation(); countDownTextIn.setDuration(500);
             * countDownText.startAnimation(countDownTextIn);
             */
        } else {
            final String newValue = getNewInclineText(inclineHeadText.getText()
                    .toString(), top);
            countDownText.setText(newValue);
            inclineHeadText.setText(newValue);
            inclineValueText.setText(newValue);
            /*
             * countDownText.clearAnimation();
             * countDownTextOut.setDuration(2000);
             * countDownText.startAnimation(countDownTextOut);
             */
        }

        hander.removeCallbacks(countDownTextOperationTimeOutRunable);
        hander.postDelayed(countDownTextOperationTimeOutRunable, 3000);
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
        
        Log.d(TAG, "firstStart:" + firstStart);
        if (firstTimeStart) {
            firstTimeStart = false; 
            return;
        }
/*        
        if (mVideoPlayerFragment != null) {
            mVideoPlayerFragment.finish();
            mVideoPlayerFragment = null;
        }*/
        
        if (mMusicFragment != null) {
            mMusicFragment.stop();
            mMusicFragment = null;
        }
        
        setSessionState(false);
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

    private static float parseFloat(final Pattern pattern, final String targetString)
            throws IllegalArgumentException {
        Matcher m = pattern.matcher(targetString);
        while (m.find()) { // Find each match in turn; String can't do this.
            String value = m.group(1);
            return Float.parseFloat(value);
        }
        throw new IllegalArgumentException("The string(" + targetString
                + ") format is invalid!");
    }

    public static int parseTime(final String targetString)
            throws IllegalArgumentException {
        return parse(TIME_PATTERN, targetString);
    }

    public static float parseDistance(final String targetString)
            throws IllegalArgumentException {
        return parseFloat(DISTANCE_PATTERN, targetString);
    }

    public static int parseCalorie(final String targetString)
            throws IllegalArgumentException {
        return parse(CALORIE_PATTERN, targetString);
    }

    public static float parseSpeed(final String targetString)
            throws IllegalArgumentException {
        return parseFloat(SPEED_PATTERN, targetString);
    }

    public static int parseIncline(final String targetString)
            throws IllegalArgumentException {
        return parse(INCLINE_PATTERN, targetString);
    }

    private int mCountDownValue = 5;
    private float mTargetDistance;
    private int mTargetTime;
    private int mTargetCalorie;
    private int mConsumedEnergy;
    private float mFinishedDistance = 0;
    
    private void startCountDown(long totalMillis, long deltaMillis) {
        countDownText.setVisibility(View.VISIBLE);
        if (defaultColorStateList == null) {
            defaultColorStateList = countDownText.getTextColors();
        } else {
            countDownText.setTextColor(defaultColorStateList);
        }
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(totalMillis, deltaMillis) {


                public void onTick(long millisUntilFinished) {
                    if (mCountDownValue > 0) {
                        mSettingObservable.setCountDownValue(mCountDownValue);
                        mSettingObservable.setCountDownStarted(true);
                        countDownText.setText("" + mCountDownValue--);
                    } else if (mCountDownValue == 0) {
                        countDownText.setText("GO!");
                        countDownText.clearAnimation();
                        countDownTextOut.setDuration(2000);
                        countDownText.startAnimation(countDownTextOut);
                        // countdown finished
                        mSettingObservable.setCountDownStarted(false);
                        mCountDownValue = -1;
                    }
                    if (mCountDownValue == -1) {
                        mExerciseTime++;
                        
                        // update time value
                        if (mExerciseTime / 60 > 0 && mTargetTime != 0) {
                            final int newTime = mTargetTime - mExerciseTime / 60;
                            if (newTime <= 0) {
                                FreeMode.this.finishExercisePlan();
                            } else {
                                FreeMode.this.setCurrentTime(mTargetTime - mExerciseTime / 60);
                            }
                        }
                        
                        // update distance value
                        final float deltaDistance = FreeMode.this.getCurrentSpeed() / 3600;
                        mFinishedDistance += deltaDistance;
                        
                        if (!isValueZero(mTargetDistance)) {
                            float remainedDistance = mTargetDistance - mFinishedDistance;
                            Log.d(TAG, "remainedDistance:" + remainedDistance + " deltaDistance:" + deltaDistance);
                            if (Math.abs(remainedDistance - 0.0) < 0.001) {
                                FreeMode.this.finishExercisePlan();
                            } else {
                                // remaining distance
                                float tmp = Float.parseFloat(String.format("%.3f", remainedDistance));
                                //Log.d(TAG, "tmp:" + tmp);
                                FreeMode.this.setCurrentDistance(tmp);
                            }
                        }
                        
                        mConsumedEnergy = (int)(mExerciseTime / 3600.0 * mUserWeight * 30 * (FreeMode.this.getCurrentSpeed() * 1000 / 60) / 400);
                        final int remainedCalorie = mTargetCalorie - mConsumedEnergy;
                        if (mTargetCalorie != 0) {
                            if (remainedCalorie <= 0) {
                                FreeMode.this.finishExercisePlan();
                            } else {
                                FreeMode.this.setCurrentCalorie(remainedCalorie);
                            }
                        }
                        
                        // Log.d(TAG, "mConsumedEnergy: " + mConsumedEnergy);

                        // Log.d(TAG, "count down time:" + mExerciseTime);
                        FreeMode.this.updateElapsedTimeText(mExerciseTime);
                    }
                }

                public void onFinish() {

                }
            };
        }
        mCountDownTimer.cancel();
        mCountDownTimer.start();
    }
    
    // in the unit of kg
    private int mUserWeight = 65;
    
    private void finishExercisePlan() {
        Toast.makeText(FreeMode.this, "您的运动计划已完成，:)", Toast.LENGTH_SHORT).show();
        swapSessionState();
        FreeMode.this.setCurrentTime(0);
        mExerciseTime = 0;
        mCountDownValue = 5;
        updateElapsedTimeText(mExerciseTime);
        FreeMode.this.mCountDownTimer.cancel();
    }
    
    private float getCurrentDistance() {
        final String oldString = distanceHeadText.getText().toString();
        return parseDistance(oldString);
    }
    
    private void setCurrentDistance(float newDistance) {
        final String newValue = String.format("%.3fkm", Math.max(newDistance,
                        0.000f));
        distanceValueText.setText(newValue);
        distanceHeadText.setText(newValue);
    }
    
    private float getCurrentSpeed() {
        final String oldString = speedHeadText.getText().toString();
        return parseSpeed(oldString);
    }
    
    private int getCurrentTime() {
        final String oldString = timeHeadText.getText().toString();
        return parseTime(oldString);
    }
    
    private void setCurrentTime(int newTime) {
        final String newValue = String.format(TIME_DISPLAY_FORMAT, String
                .valueOf(Math.max(newTime,
                        0)));
        timeValueText.setText(newValue);
        timeHeadText.setText(newValue);
    }
    
    private int getCurrentCalorie() {
        final String oldString = calorieHeadText.getText().toString();
        return parseCalorie(oldString);
    }
    
    private void setCurrentCalorie(int newCalorie) {
        final String newValue = String.format(CALORIE_DISPLAY_FORMAT, String
                .valueOf(Math.max(newCalorie,
                        0)));
        calorieValueText.setText(newValue);
        calorieHeadText.setText(newValue);
    }
    
    private void updateElapsedTimeText(final int elapsedTimeSeconds) {
        timeElapsedText.setText(elapsedTimeSeconds / 3600 + "h " + 
                (elapsedTimeSeconds % 3600) / 60  + "m " + ((elapsedTimeSeconds % 3600)) % 60 + "s");
    }

    private static String getNewSpeedText(final String origin, boolean incr) {
        final float oldSpeed = parseSpeed(origin);
        final String newValue = String.format(SPEED_DISPLAY_FORMAT, String
                .valueOf(incr ? Math.min((oldSpeed + SPEED_ADJUST_STEP), SPEED_MAX) : (Math.max(
                        oldSpeed - SPEED_ADJUST_STEP, 0))));
        return newValue;
    }

    private static String getNewInclineText(final String origin, boolean incr) {
        final int oldIncline = parseIncline(origin);
        final String newValue = String.format(INCLINE_DISPLAY_FORMAT, String
                .valueOf(incr ? Math.min((oldIncline + INCLINE_ADJUST_STEP), INCLINE_MAX) : (Math
                        .max(
                                oldIncline - INCLINE_ADJUST_STEP, 0))));
        return newValue;
    }

    private void setSessionState(boolean newState) {
        if (sessionStarted.get() == newState) {
            return;
        } else {
            swapSessionState();
        }
    }
    
    private boolean isValueZero(float val) {
        return (Math.abs(val - 0.0) < 0.1);
    }

    private void swapSessionState() {
        dismissAllPanels();
        if (sessionStarted.get()) {
            setCurrentTime(0);
            startBtn.setBackgroundResource(R.drawable.start);
        } else {
            
            // check user settings
            mTargetDistance = getCurrentDistance();
            mTargetTime = getCurrentTime();
            mTargetCalorie = getCurrentCalorie();
            
            Log.d(TAG, "distance: " + mTargetDistance);
            Log.d(TAG, "time: " + mTargetTime);
            Log.d(TAG, "calorie: " + mTargetCalorie);
            
            if (mTargetCalorie == 0 && mTargetTime == 0 && Math.abs(mTargetDistance - 0.0) < 0.1) {
                Toast.makeText(this, "请设置时间、距离、卡路里中任意一项目标后开始运动", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isValueZero(getCurrentSpeed())) {
                Toast.makeText(this, "请设置合理的速度后开始运动", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Toast.makeText(this, "千万请确保设置了合理的速度及坡度值！", Toast.LENGTH_LONG).show();
            
            startBtn.setBackgroundResource(R.drawable.stop);
            mExerciseTime = 0;
            mCountDownValue = 5;
            mFinishedDistance = 0;
            mConsumedEnergy = 0;
            setCurrentDistance(0.0f);
            updateElapsedTimeText(mExerciseTime);
            heartRateText.setText("");;
        }
        sessionStarted.set((!sessionStarted.get()));
        final boolean started = sessionStarted.get();
        if (started) {
            enableTargetSettingControls(false);
            startCountDown(Integer.MAX_VALUE, 1000);
            mSettingObservable.setCountDownStarted(true);
        } else {
            countDownText.setVisibility(View.GONE);
            enableTargetSettingControls(true);
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                mSettingObservable.setCountDownStarted(false);
                ;
            }
            // TODO: stop the current exercise session
        }

        // notify setting changed
        if (mSettingObservable != null) {
            Log.d(TAG, "setSessionStarted() " + started);
            mSettingObservable.setSessionStarted(started);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == null) {
            return;
        }
        onClick_(v.getId());
    }
    

    private void onClick_(final int viewId) {
        Log.d(TAG, "onClick_() viewId:" + viewId);
        boolean containerBecomeVisible = false;
        int containerIdBecameVisible = 0;
        if (viewId == R.id.start_btn) {
            swapSessionState();
        }
        else if (viewId == R.id.freeback_btn) {
            Intent intent = new Intent();
            intent.setClass(this, Main.class);
            startActivityForResult(intent, 0);
        } else if (viewId == R.id.music_btn) {
            switchToMusicFragment();
        } else if (viewId == R.id.surf_btn) {
            switchToSurfFragment(SurfFragment.free_mode);
        } else if (viewId == R.id.tv_btn) {
            switchToSurfFragment(SurfFragment.tv_mode);
        } else if (viewId == R.id.video_btn) {
            switchToVideoPlayerFragment(VideoPlayerFragment.free_mode);
        } else if (viewId == R.id.time_control
                || viewId == R.id.distance_control
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
            final String newValue = String.format(TIME_DISPLAY_FORMAT,
                    String.valueOf(oldTime + TIME_ADJUST_STEP));
            timeValueText.setText(newValue);
            timeHeadText.setText(newValue);
        } else if (viewId == R.id.sub_time_btn) {
            final String oldString = timeValueText.getText().toString();
            final int oldTime = parseTime(oldString);
            final String newValue = String.format(TIME_DISPLAY_FORMAT, String
                    .valueOf(Math.max(oldTime - TIME_ADJUST_STEP,
                            TIME_ADJUST_STEP / TIME_ADJUST_STEP)));
            timeValueText.setText(newValue);
            timeHeadText.setText(newValue);
        } else if (viewId == R.id.add_distance_btn) {
            final String oldString = distanceValueText.getText().toString();
            final float oldTime = parseDistance(oldString);
            final String newValue = String.format(DISTANCE_DISPLAY_FORMAT,
                    String.valueOf(oldTime + DISTANCE_ADJUST_STEP));
            distanceValueText.setText(newValue);
            distanceHeadText.setText(newValue);
        } else if (viewId == R.id.sub_distance_btn) {
            final String oldString = distanceValueText.getText().toString();
            final float oldTime = parseDistance(oldString);
            final String newValue = String.format(DISTANCE_DISPLAY_FORMAT,
                    String.valueOf(Math.max(oldTime - DISTANCE_ADJUST_STEP,
                            DISTANCE_ADJUST_STEP)));
            distanceValueText.setText(newValue);
            distanceHeadText.setText(newValue);
        } else if (viewId == R.id.add_calorie_btn) {
            final String oldString = calorieValueText.getText().toString();
            final int oldTime = parseCalorie(oldString);
            final String newValue = String.format(CALORIE_DISPLAY_FORMAT,
                    String.valueOf(oldTime + CALORIE_ADJUST_STEP));
            calorieValueText.setText(newValue);
            calorieHeadText.setText(newValue);
        } else if (viewId == R.id.sub_calorie_btn) {
            final String oldString = calorieValueText.getText().toString();
            final int oldTime = parseCalorie(oldString);
            final String newValue = String.format(CALORIE_DISPLAY_FORMAT,
                    String.valueOf(Math.max(oldTime - CALORIE_ADJUST_STEP,
                            CALORIE_ADJUST_STEP)));
            calorieValueText.setText(newValue);
            calorieHeadText.setText(newValue);
        } else if (viewId == R.id.add_speed_btn) {
            final String oldString = speedValueText.getText().toString();
            final String newValue = getNewSpeedText(oldString, true);
            speedValueText.setText(newValue);
            speedHeadText.setText(newValue);
        } else if (viewId == R.id.sub_speed_btn) {
            final String oldString = speedValueText.getText().toString();
            final String newValue = getNewSpeedText(oldString, false);
            speedValueText.setText(newValue);
            speedHeadText.setText(newValue);
        } else if (viewId == R.id.add_incline_btn) {
            final String oldString = inclineValueText.getText().toString();
            final String newValue = getNewInclineText(oldString, true);
            inclineValueText.setText(newValue);
            inclineHeadText.setText(newValue);
        } else if (viewId == R.id.sub_incline_btn) {
            final String oldString = inclineValueText.getText().toString();
            final String newValue = getNewInclineText(oldString, false);
            inclineValueText.setText(newValue);
            inclineHeadText.setText(newValue);
        } else if (viewId == R.id.unity_btn) {
            switchToUnityFragment();
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
            if (containerBecomeVisible
                    && containerId != containerIdBecameVisible) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onKeyDown() keyCode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "onKeyDown() BACK case");
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        initMode();

    }
}


package io.github.faywong.exerciseapp;

import io.github.faywong.exerciseapp.thirdparty.SoundView;
import io.github.faywong.exerciseapp.thirdparty.VideoChooseActivity;
import io.github.faywong.exerciseapp.thirdparty.VideoView;
import io.github.faywong.exerciseapp.thirdparty.SoundView.OnVolumeChangedListener;
import io.github.faywong.exerciseapp.thirdparty.VideoView.MySizeChangeLinstener;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class VideoPlayerFragment extends Fragment implements OnClickListener,
        FreeMode.IFragmentControlHandler {

    private static final String TAG = "VideoPlayerFragment";
    private boolean isOnline = false;
    private boolean isChangedVideo = false;

    public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();

    public class MovieInfo {
        public String displayName;
        public String path;
    }

    private Uri videoListUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private static int position;
    private static boolean backFromAD = false;
    private int playedTime;

    private VideoView vv = null;
    // private SeekBar seekBar = null;
    // private TextView durationTextView = null;
    // private TextView playedTextView = null;
    private GestureDetector mGestureDetector = null;
    private AudioManager mAudioManager = null;

    private int maxVolume = 0;
    private int currentVolume = 0;

    private ImageButton bn1 = null;
    private ImageButton bn2 = null;
    private ImageButton bn3 = null;
    private ImageButton bn4 = null;
    private ImageButton bn5 = null;

    private View controlView = null;
    private PopupWindow controler = null;

    private SoundView mSoundView = null;
    private PopupWindow mSoundWindow = null;

    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static int controlHeight = 0;

    private final static int TIME = 6868;

    private boolean isControllerShow = true;
    private boolean isPaused = false;
    private boolean isFullScreen = false;
    private boolean isSilent = false;
    private boolean isSoundShow = false;

    private final static int PROGRESS_CHANGED = 0;
    private final static int HIDE_CONTROLER = 1;
    private final static int SCREEN_FULL = 0;
    private final static int SCREEN_DEFAULT = 1;

    public final static int free_mode = 1;
    public final static int real_mode = 2;

    public int curMode = free_mode;

    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {

                case PROGRESS_CHANGED:
                    /*
                     * int i = vv.getCurrentPosition(); seekBar.setProgress(i);
                     * if (isOnline) { int j = vv.getBufferPercentage();
                     * seekBar.setSecondaryProgress(j * seekBar.getMax() / 100);
                     * } else { seekBar.setSecondaryProgress(0); } i /= 1000;
                     * int minute = i / 60; int hour = minute / 60; int second =
                     * i % 60; minute %= 60;
                     * playedTextView.setText(String.format("%02d:%02d:%02d",
                     * hour, minute, second));
                     * sendEmptyMessageDelayed(PROGRESS_CHANGED, 100); break;
                     */

                case HIDE_CONTROLER:
                    hideController();
                    break;
            }

            super.handleMessage(msg);
        }
    };

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

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View parentLayout = inflater.inflate(R.layout.video_player_main,
                container, false);

        if (parentLayout == null) {
            Log.e(TAG, "parentLayout is null");
            return null;
        }

        Looper.myQueue().addIdleHandler(new IdleHandler() {

            @Override
            public boolean queueIdle() {

                // TODO Auto-generated method stub
                /*
                 * if (controler != null && vv.isShown()) {
                 * controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0); //
                 * controler.update(screenWidth, controlHeight);
                 * controler.update(0, 0, screenWidth, controlHeight); }
                 */

                // myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
                return false;
            }
        });
        getControlView();
        initVideoView(parentLayout);
        return parentLayout;
    }

    private void getScreenSize() {
        Display display = FreeMode.sInstance.getWindowManager().getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
        controlHeight = screenHeight / 4;
    }

    private void hideController() {
        /*
         * if (controler.isShowing()) { controler.update(0, 0, 0, 0);
         * isControllerShow = false; }
         */
        if (mSoundWindow.isShowing()) {
            mSoundWindow.dismiss();
            isSoundShow = false;
        }
    }

    private void hideControllerDelay() {
        myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
    }

    public void showController() {
        // controler.update(0, 0, screenWidth, controlHeight);
        // isControllerShow = true;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (!isChangedVideo) {
            vv.seekTo(playedTime);
            vv.start();
        } else {
            isChangedVideo = false;
        }

        // if(vv.getVideoHeight()!=0){
        if (vv.isPlaying()) {
            bn3.setImageResource(R.drawable.pause);
            hideControllerDelay();
        }

        if (FreeMode.sInstance.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            FreeMode.sInstance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        /*
         * if (controler.isShowing()) { controler.dismiss(); }
         */
        if (mSoundWindow.isShowing()) {
            mSoundWindow.dismiss();
        }

        myHandler.removeMessages(PROGRESS_CHANGED);
        myHandler.removeMessages(HIDE_CONTROLER);

        if (vv.isPlaying()) {
            vv.stopPlayback();
        }

        playList.clear();
        super.onStop();
    }

    private void cancelDelayHide() {
        myHandler.removeMessages(HIDE_CONTROLER);
    }

    private void getVideoFile(final LinkedList<MovieInfo> list, File file) {
        Log.d(TAG, "getVideoFile() file:" + file);
        if (file == null || !file.exists()) {
            return;
        }

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                // TODO Auto-generated method stub
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")) {

                        MovieInfo mi = new MovieInfo();
                        mi.displayName = file.getName();
                        mi.path = file.getAbsolutePath();
                        list.add(mi);
                        Log.d(TAG, "add an MovieInfo:" + mi.displayName);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onActivityResult() req:" + requestCode + " data:" + data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            vv.stopPlayback();

            int result = data.getIntExtra("CHOOSE", -1);
            Log.d("RESULT", "" + result);
            if (result != -1) {
                isOnline = false;
                isChangedVideo = true;
                vv.setVideoPath(playList.get(result).path);
                position = result;
            } else {
                String url = data.getStringExtra("CHOOSE_URL");
                if (url != null) {
                    vv.setVideoPath(url);
                    isOnline = true;
                    isChangedVideo = true;
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int findAlphaFromSound() {
        if (mAudioManager != null) {
            // int currentVolume =
            // mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int alpha = currentVolume * (0xCC - 0x55) / maxVolume + 0x55;
            return alpha;
        } else {
            return 0xCC;
        }
    }

    private void updateVolume(int index) {
        if (mAudioManager != null) {
            if (isSilent) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
                        0);
            }
            currentVolume = index;
            bn5.setAlpha(findAlphaFromSound());
        }
    }

    private void setVideoScale(int flag) {

        LayoutParams lp = vv.getLayoutParams();

        switch (flag) {
            case SCREEN_FULL:

                Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
                vv.setVideoScale(screenWidth, screenHeight);
                FreeMode.sInstance.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                break;

            case SCREEN_DEFAULT:

                int videoWidth = vv.getVideoWidth();
                int videoHeight = vv.getVideoHeight();
                int mWidth = screenWidth;
                int mHeight = screenHeight - 25;

                if (videoWidth > 0 && videoHeight > 0) {
                    if (videoWidth * mHeight > mWidth * videoHeight) {
                        // Log.i("@@@", "image too tall, correcting");
                        mHeight = mWidth * videoHeight / videoWidth;
                    } else if (videoWidth * mHeight < mWidth * videoHeight) {
                        // Log.i("@@@", "image too wide, correcting");
                        mWidth = mHeight * videoWidth / videoHeight;
                    } else {

                    }
                }

                vv.setVideoScale(mWidth, mHeight);

                FreeMode.sInstance.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                break;
        }
    }

    private void initMode()
    {
        if (curMode == real_mode)
        {
            String uri2 = "android.resource://" + getActivity().getPackageName() + "/"
                    + R.raw.road1;
            vv.setVideoURI(Uri.parse(uri2));
            vv.start();
        }
    }

    private void initVideoView(final View parentView) {

        vv = (VideoView) parentView.findViewById(R.id.vv);

        vv.clearAnimation();

        vv.setBackgroundDrawable(null);

        vv.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                vv.stopPlayback();
                isOnline = false;

                new AlertDialog.Builder(FreeMode.sInstance)
                        .setTitle("中止播放")
                        .setMessage("播放时遇到未知错误")
                        .setPositiveButton("确定",
                                new AlertDialog.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {

                                        vv.stopPlayback();

                                    }

                                }).setCancelable(false).show();

                return false;
            }

        });

        Uri uri = FreeMode.sInstance.getIntent().getData();
        if (uri != null) {
            vv.stopPlayback();
            vv.setVideoURI(uri);
            isOnline = true;
            bn3.setImageResource(R.drawable.pause);
        } else {
            bn3.setImageResource(R.drawable.play);
        }

        FreeMode.sInstance.mBackgroundThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                getVideoFile(playList, new File(Environment.getExternalStorageState()));
            }
        });

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {

            Cursor cursor = FreeMode.sInstance.getContentResolver()
                    .query(videoListUri,
                            new String[] {
                                    "_display_name", "_data"
                            }, null,
                            null, null);
            int n = cursor.getCount();
            cursor.moveToFirst();
            LinkedList<MovieInfo> playList2 = new LinkedList<MovieInfo>();
            for (int i = 0; i != n; ++i) {
                MovieInfo mInfo = new MovieInfo();
                mInfo.displayName = cursor.getString(cursor
                        .getColumnIndex("_display_name"));
                mInfo.path = cursor.getString(cursor.getColumnIndex("_data"));
                playList2.add(mInfo);
                cursor.moveToNext();
            }

            if (playList2.size() > playList.size()) {
                playList = playList2;
            }
        }

        vv.setMySizeChangeLinstener(new MySizeChangeLinstener() {

            @Override
            public void doMyThings() {
                // TODO Auto-generated method stub
                setVideoScale(SCREEN_FULL);
            }

        });

        vv.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                // TODO Auto-generated method stub

                setVideoScale(SCREEN_FULL);
                Log.d(TAG, "faywong OnPreparedListener()");

                /*
                 * FreeMode.sInstance.mHandler.postDelayed(new Runnable() {
                 * @Override public void run() { // TODO Auto-generated method
                 * stub if (vv != null) { if (vv.isPlaying()) {
                 * bn3.setImageResource(R.drawable.pause); Log.d(TAG,
                 * "faywong pause"); hideControllerDelay(); } else {
                 * bn3.setImageResource(R.drawable.play); Log.d(TAG,
                 * "faywong play"); } } } }, 200);
                 */

                /*
                 * isFullScreen = false; if (isControllerShow) {
                 * showController(); } int i = vv.getDuration();
                 * Log.d("onCompletion", "" + i); seekBar.setMax(i); i /= 1000;
                 * int minute = i / 60; int hour = minute / 60; int second = i %
                 * 60; minute %= 60;
                 * durationTextView.setText(String.format("%02d:%02d:%02d",
                 * hour, minute, second));
                 */

                vv.start();
                Drawable pauseDrawable = FreeMode.sInstance.getResources().getDrawable(
                        R.drawable.pause);
                bn3.setImageResource(R.drawable.pause);
                bn3.setImageDrawable(pauseDrawable);
                Log.d(TAG, "faywong OnPreparedListener() setImageResource() pause");

                /*
                 * hideControllerDelay();
                 * myHandler.sendEmptyMessage(PROGRESS_CHANGED);
                 */
            }
        });

        vv.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                if (curMode == real_mode)
                {
                    initMode();
                    return;
                }
                int n = playList.size();
                isOnline = false;
                if (++position < n) {
                    vv.setVideoPath(playList.get(position).path);
                } else {
                    vv.stopPlayback();
                    finish();
                }
            }
        });
    }

    public void finish() {
        // TODO Auto-generated method stub
        if (vv != null) {
            vv.stopPlayback();
        }
        FreeMode.sInstance.getSupportFragmentManager().beginTransaction()
                .remove(VideoPlayerFragment.this).commit();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
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
        initMode();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.GoBtn:
                break;
            case R.id.BackBtn:
                break;
            case R.id.forwardBtn:
                break;
            case R.id.reloadBtn:
                break;
        }
    }

    @Override
    public View getControlView() {
        // TODO Auto-generated method stub
        controlView = FreeMode.sInstance.getLayoutInflater().inflate(
                R.layout.video_controller, null);
        // controler = new PopupWindow(controlView);
        // durationTextView = (TextView)
        // controlView.findViewById(R.id.duration);
        // playedTextView = (TextView)
        // controlView.findViewById(R.id.has_played);

        mSoundView = new SoundView(FreeMode.sInstance);
        mSoundView.setOnVolumeChangeListener(new OnVolumeChangedListener() {

            @Override
            public void setYourVolume(int index) {

                cancelDelayHide();
                updateVolume(index);
                hideControllerDelay();
            }
        });

        mSoundWindow = new PopupWindow(mSoundView);

        position = -1;

        bn1 = (ImageButton) controlView.findViewById(R.id.button1);
        bn2 = (ImageButton) controlView.findViewById(R.id.button2);
        bn3 = (ImageButton) controlView.findViewById(R.id.button3);
        bn4 = (ImageButton) controlView.findViewById(R.id.button4);
        bn5 = (ImageButton) controlView.findViewById(R.id.button5);

        bn1.setAlpha(0xBB);
        bn2.setAlpha(0xBB);
        bn3.setAlpha(0xBB);
        bn4.setAlpha(0xBB);

        mAudioManager = (AudioManager) FreeMode.sInstance.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        bn5.setAlpha(findAlphaFromSound());

        bn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(FreeMode.sInstance,
                        VideoChooseActivity.class);
                VideoPlayerFragment.this.startActivityForResult(intent, 0);
                cancelDelayHide();
            }
        });

        bn4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int n = playList.size();
                isOnline = false;
                if (++position < n) {
                    vv.setVideoPath(playList.get(position).path);
                    cancelDelayHide();
                    hideControllerDelay();
                } else {
                    finish();
                }
            }

        });

        // start/pause button
        bn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancelDelayHide();
                if (isPaused) {
                    vv.start();
                    bn3.setImageResource(R.drawable.pause);
                    Log.d(TAG, "faywong pause case 1");
                    hideControllerDelay();
                } else {
                    vv.pause();
                    bn3.setImageResource(R.drawable.play);
                    Log.d(TAG, "faywong play case 1");
                }
                isPaused = !isPaused;
            }

        });

        bn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnline = false;
                if (--position >= 0) {
                    vv.setVideoPath(playList.get(position).path);
                    cancelDelayHide();
                    hideControllerDelay();
                } else {
                    finish();
                }
            }

        });

        bn5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancelDelayHide();
                if (isSoundShow) {
                    mSoundWindow.dismiss();
                } else {
                    if (mSoundWindow.isShowing()) {
                        mSoundWindow.update(15, 0, SoundView.MY_WIDTH,
                                SoundView.MY_HEIGHT);
                    } else {
                        mSoundWindow.showAtLocation(vv, Gravity.RIGHT
                                | Gravity.CENTER_VERTICAL, 15, 0);
                        mSoundWindow.update(15, 0, SoundView.MY_WIDTH,
                                SoundView.MY_HEIGHT);
                    }
                }
                isSoundShow = !isSoundShow;
                hideControllerDelay();
            }
        });

        bn5.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                if (isSilent) {
                    bn5.setImageResource(R.drawable.soundenable);
                } else {
                    bn5.setImageResource(R.drawable.sounddisable);
                }
                isSilent = !isSilent;
                updateVolume(currentVolume);
                cancelDelayHide();
                hideControllerDelay();
                return true;
            }

        });

        /*
         * seekBar = (SeekBar) controlView.findViewById(R.id.seekbar);
         * seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
         * @Override public void onProgressChanged(SeekBar seekbar, int
         * progress, boolean fromUser) { // TODO Auto-generated method stub if
         * (fromUser) { if (!isOnline) { vv.seekTo(progress); } } }
         * @Override public void onStartTrackingTouch(SeekBar arg0) { // TODO
         * Auto-generated method stub myHandler.removeMessages(HIDE_CONTROLER);
         * }
         * @Override public void onStopTrackingTouch(SeekBar seekBar) { // TODO
         * Auto-generated method stub
         * myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME); } });
         */

        getScreenSize();

        mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // TODO Auto-generated method stub
                if (isFullScreen) {
                    setVideoScale(SCREEN_FULL);
                } else {
                    setVideoScale(SCREEN_FULL);
                }
                isFullScreen = !isFullScreen;
                Log.d(TAG, "onDoubleTap");

                if (isControllerShow) {
                    showController();
                }
                // return super.onDoubleTap(e);
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // TODO Auto-generated method stub
                if (!isControllerShow) {
                    showController();
                    hideControllerDelay();
                } else {
                    cancelDelayHide();
                    hideController();
                }
                // return super.onSingleTapConfirmed(e);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub
                if (isPaused) {
                    vv.start();
                    bn3.setImageResource(R.drawable.pause);
                    cancelDelayHide();
                    hideControllerDelay();
                } else {
                    vv.pause();
                    bn3.setImageResource(R.drawable.play);
                    cancelDelayHide();
                    showController();
                }
                isPaused = !isPaused;
                // super.onLongPress(e);
            }
        });

        // vv.setVideoPath("http://202.108.16.171/cctv/video/A7/E8/69/27/A7E86927D2BF4D2FA63471D1C5F97D36/gphone/480_320/200/0.mp4");
        return controlView;
    }

    @Override
    public void onSwitchedIn() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSwitchedOut() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onToggleScreen() {
        // TODO Auto-generated method stub

    }

}

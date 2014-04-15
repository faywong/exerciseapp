
package io.github.faywong.exerciseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SettingModel {
    private final static String SETTING_FILE = "exercise_setting";
    private final static String SETTING_KEY_TIME = "time";
    private final static String SETTING_KEY_DISTANCE = "distance";
    private final static String SETTING_KEY_CALORIE = "calorie";
    private final static String SETTING_KEY_INCLINE = "incline";
    private final static String SETTING_KEY_SPEED = "speed";

    public final static int SETTING_TYPE_NONE = -1;
    public final static int SETTING_TYPE_TIME = 0;
    public final static int SETTING_TYPE_DISTANCE = 1;
    public final static int SETTING_TYPE_CALORIE = 2;
    public final static int SETTING_TYPE_INCLINE = 3;
    public final static int SETTING_TYPE_SPEED = 4;

    private int time = 0;
    private int distance = 0;
    private int calorie = 0;
    private int incline = 0;
    private int speed = 0;
    private boolean hasSpeedChanged = false;
    private boolean hasInclineChanged = false;
    private boolean sessionStarted = false;
    private SettingObservable owner;
    private boolean hasDistanceChanged;
    private boolean hasCalorieChanged;
    private boolean hasTimeChanged;

    public SettingModel(int time, int distance, int calorie, int incline,
            int speed, SettingObservable owner) {
        super();
        this.time = time;
        this.distance = distance;
        this.calorie = calorie;
        this.incline = incline;
        this.speed = speed;
        this.owner = owner;
    }

    /**
     * apply settings
     */
    public boolean setSessionStarted(boolean started) {
        if (sessionStarted != started) {
            sessionStarted = started;
            return true;
        }
        return false;
    }

    public boolean isSessionStarted() {
        return sessionStarted;
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

    static SettingModel fromPersistent(Context context, SettingObservable owner) {
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
                    preferences.getInt(SETTING_KEY_SPEED, 0), owner);

        }
    }

    public SettingModel(SettingObservable owner) {
        super();
        this.owner = owner;
    }

    public boolean onSettingChanged(int settingType, String newValue) {
        boolean result = false;
        if (newValue == null) {
            return result;
        }
        switch (settingType) {
            case SETTING_TYPE_INCLINE:
                result = setIncline(FreeMode.parseIncline(newValue));
                break;

            case SETTING_TYPE_SPEED:
                result = setSpeed(FreeMode.parseSpeed(newValue));
                break;

            case SETTING_TYPE_TIME:
                result = setTime(FreeMode.parseTime(newValue));
                break;

            case SETTING_TYPE_DISTANCE:
                result = setDistance(FreeMode.parseDistance(newValue));
                break;

            case SETTING_TYPE_CALORIE:
                result = setCalorie(FreeMode.parseCalorie(newValue));
                break;
        }

        if (owner != null && result) {
            owner.notifyObservers(this);
        }
        return result;
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

    private boolean setTime(int time) {
        hasTimeChanged = (time != this.time);
        this.time = time;
        return hasTimeChanged;
    }

    public boolean isTimeChanged() {
        return hasTimeChanged;
    }

    public void clearTimeChanged() {
        hasTimeChanged = false;
    }

    public int getDistance() {
        return distance;
    }

    private boolean setDistance(int distance) {
        hasDistanceChanged = (distance != this.distance);

        this.distance = distance;
        return hasDistanceChanged;
    }

    public boolean isDistanceChanged() {
        return hasDistanceChanged;
    }

    public void clearDistanceChanged() {
        hasDistanceChanged = false;
    }

    public int getCalorie() {
        return calorie;
    }

    private boolean setCalorie(int calorie) {
        hasCalorieChanged = (calorie != this.calorie);
        this.calorie = calorie;
        return hasCalorieChanged;
    }

    public boolean isCalorieChanged() {
        return hasCalorieChanged;
    }

    public void clearCalorieChanged() {
        hasCalorieChanged = false;
    }

    public int getIncline() {
        return incline;
    }

    private boolean setIncline(int incline) {
        hasInclineChanged = (incline != this.incline);
        this.incline = incline;
        return hasInclineChanged;
    }

    public int getSpeed() {
        return speed;
    }

    private boolean setSpeed(int speed) {
        hasSpeedChanged = (speed != this.speed);
        this.speed = speed;
        return hasSpeedChanged;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[time" + time
                + " distance:" + distance
                + " calorie:" + calorie
                + " speed:" + speed
                + " incline:" + incline
                + "]";
    }

}


package io.github.faywong.exerciseapp;

import android.app.Application;
import android.content.Intent;

public class ExerciseApp extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Intent intent = new Intent(this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

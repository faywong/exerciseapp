
package io.github.faywong.exerciseapp;

import java.util.Observable;
import java.util.Observer;

import com.unity3d.player.UnityPlayer;

public class UnityObserver implements Observer {

    public UnityObserver() {

    }

    @Override
    public void update(Observable observable, Object data) {
        // TODO Auto-generated method stub
        SettingObservable obj = (SettingObservable) observable;
        SettingModel dataCon = obj.getSettingModel();
        if (UnityFragment.mUnityPlayer != null)
        {
            if (dataCon.isSpeedChanged())
            {
                UnityPlayer.UnitySendMessage("MsgRecv", "SetSpeed",
                        Float.toString(dataCon.getSpeed()));
            }

            if (dataCon.isInclineChanged())
            {
                UnityPlayer.UnitySendMessage("MsgRecv", "SetIncline",
                        Integer.toString(dataCon.getIncline()));
            }
        }

    }

}

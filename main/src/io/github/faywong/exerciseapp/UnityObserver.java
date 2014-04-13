package io.github.faywong.exerciseapp;

import java.util.Observable;
import java.util.Observer;

import com.unity3d.player.UnityPlayer;
public class UnityObserver implements Observer{

	
	public UnityObserver() {
	
	}
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		SettingObservable obj= (SettingObservable)observable;
		SettingModel dataCon = obj.getSettingModel();
		if(dataCon.isSpeedChanged())
		{
			// TODO:
			// fix bug reported at: https://bitbucket.org/faywong/exerciseapp/issue/1/unity-native-method-not-found
			
			// UnityPlayer.UnitySendMessage("MsgRecv","SetSpeed",Integer.toString(dataCon.getSpeed())) ;
		}
		
		if(dataCon.isInclineChanged())
		{
			// UnityPlayer.UnitySendMessage("MsgRecv","SetIncline",Integer.toString(dataCon.getIncline())) ;
		}
	}

}

package io.github.faywong.exerciseapp;

public interface IHWStatusListener {
	
	
	// key state
	void onStartOrStop();
	void onSpeedPlus();
	void onSpeedReduce();	
	void onInclinePlus();
	void onInclineReduce();
	
	//data 
	void onHWStatusChanged(int errorCode,int calory,int pulse);
	
}



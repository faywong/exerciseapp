package sportsSDK;

import io.github.faywong.exerciseapp.IHWStatusListener;
import io.github.faywong.exerciseapp.SettingModel;
import io.github.faywong.exerciseapp.SettingObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;

import com.hardware.comm.TreadmillComm;

import sportsSDK.Pins;

public class PinSDK {
	
	static PinSDK msdk=null;
	
	
	IHWStatusListener mListener=null;
	
	final int keyDelay=20;
	final int sdkDelay=250;
	final List<Pins> pins = new ArrayList<Pins>();
	final List<Pins> pins_out = new ArrayList<Pins>();
	final List<Pins> pins_bak = new ArrayList<Pins>();
	
	boolean isInit=false;
	
	int speed = 0;
	int gradient = 0;
	int  gradient_state = 0;
	int run_state = 0;
	int resistance_flag = 0;
	int resistance = 0;
	int playvoice = 0;
	
	
	byte[] in_buffer = new byte[11];
	
	int error_code = 0;
	int motor_current = 0;
	int bus_voltage = 0;
	int motor_voltage = 0;
	float curent_resistance = 0f;
	int calories = 0;
	int pulse = 0;
	
	final boolean[] keyState = new boolean[7];
	
	public void setHWStatusListener(IHWStatusListener  listener)
	{
		mListener = listener;
	}
	
	public PinSDK()
	{
		
		new Thread(new Runnable() {  
	        public void run() {  
	        	//init();
	        	while(true)
	        	{
	        		try {
						Thread.sleep(sdkDelay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		write();
	        		read();
	        		if(mListener!=null)
	        			mListener.onHWStatusChanged(error_code, calories, pulse);
	        	}
	        }  
	    }).start();  
		
		// 开启线程侦测按键的状态
		
			
			new Thread(new Runnable()
			{
				@Override
				public void run() 
				{				
					while (true) 
					{	
						for (int i = 0; i < pins.size(); i++) 
						{
							final Pins temp_pin = pins.get(i);
							final Pins temp_pin_out = pins_out.get(i);

						if (temp_pin.getStatus() == 0)
						{
							keyState[i]=true;
						}
						else
						{
							checkKeyS(i);
						}
					}
				}						
			}
		}).start();	
	}
	
	private void checkKeyS(final int pos)
	{

		if(mListener!=null&keyState[pos])
		{
			switch(pos)
			{
			case 0:
				mListener.onStartOrStop();
				break;
			case 1:
				mListener.onSpeedPlus();
				break;
			case 2:
				mListener.onSpeedReduce();
				break;
			case 3:
				mListener.onInclinePlus();
				break;
			case 4:
				mListener.onInclineReduce();
				break;
			default:
				break;
			}
		}
		keyState[pos]=false;
	}
	
	private void read()
	{
		if(!isInit)
			return;
		
		// test
		if (TreadmillComm.read(in_buffer, in_buffer.length) < 0) {
			//Fail!
			//return;
			Log.e("PinSDK", "fatal error ,read TreadmillComm   failed  !");
		}
		else {
			error_code = (int)in_buffer[0];
			//motor_current = in_buffer[1] << 8 + (int)in_buffer[2];	//(暂时无用)
			//bus_voltage = in_buffer[3] << 8 + (int)in_buffer[4];		//(暂时无用)
			//motor_voltage = in_buffer[5] << 8 + (int)in_buffer[6];	//(暂时无用)
			curent_resistance = ((float)(in_buffer[7])) / 16f;
			calories = (in_buffer[8] << 8 + (int)in_buffer[9]);
			pulse = (int)in_buffer[10];		
		}
	}
	private void write()
	{
		
		if(!isInit)
			return;
		
		//-------------------------------------------------------------------------------------------------------------//				
		//1、设定速度，单位0.1km/h，1字节。范围是0.0-25.5
		//2、设定坡度，单位0.1度，1字节。范围是0.0-15.0
		//3、坡度升降控制，0停止，1上升，2下降，1字节。
		//4、运行控制，0停止，1运行，1字节。
		//5、设定电机电阻，每16表示1欧姆，2字节，第一字节为标志，第二字节为数据。  app显示设定范围为0.0--15.9，确认后乘以16发送到接口库
		//6、提示语音播放状态，0没有播放，1正在播放
		final byte[] out_buffer = new byte[7];
		
		
		
		out_buffer[0] = (byte)(speed * 10f);
		out_buffer[1] = (byte)(gradient * 10f);
		out_buffer[2] = (byte)(gradient_state);
		out_buffer[3] = (byte)(run_state);
		out_buffer[4] = (byte)(resistance_flag);
		out_buffer[5] = (byte)(resistance * 16f);
		out_buffer[6] = (byte)(playvoice);
		
		if(TreadmillComm.write(out_buffer, out_buffer.length)<0)
		{
			Log.e("PinSDK", "fatal error ,write TreadmillComm   failed  !");
		}
	}
	private void init()
	{
		isInit = true;
		
		
		try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }	

		// input-------key1,key2,key3,key4,key5,key6,key7
		pins.add(new Pins("GPIO-3:5", 167));
		pins.add(new Pins("GPIO-3:6", 27));
		pins.add(new Pins("GPIO-3:7", 169));
		pins.add(new Pins("GPIO-3:8", 28));
		pins.add(new Pins("GPIO-3:9", 174));
		pins.add(new Pins("GPIO-3:10", 29));
		pins.add(new Pins("GPIO-3:11", 176));
		
		// 初始化7个GPIO为输入按键
		for (int i = 0; i < pins.size(); i++) {
			final Pins temp_pin = pins.get(i);
			temp_pin.exportPin();
			String dir = temp_pin.getDirection();
			temp_pin.setDirection("in");
		}
		
		// output-------key1,key2,key3,key4,key5,key6,key7
		// 实际可能用不到
		pins_out.add(new Pins("GPIO-3:12", 30));
		pins_out.add(new Pins("GPIO-3:13", 177));
		pins_out.add(new Pins("GPIO-3:14", 31));
		pins_out.add(new Pins("GPIO-3:15", 178));
		pins_out.add(new Pins("GPIO-3:16", 32));
		pins_out.add(new Pins("GPIO-3:17", 179));
		pins_out.add(new Pins("GPIO-3:18", 34));

		// 初始化7个GPIO为输出
		for (int i = 0; i < pins_out.size(); i++) {
			final Pins temp_pin_out = pins_out.get(i);
			temp_pin_out.exportPin();
			String dir = temp_pin_out.getDirection();
			temp_pin_out.setDirection("out");
		}
		/*
		// 开启线程侦测按键的状态
		for (int i = 0; i < pins.size(); i++) {
			final Pins temp_pin = pins.get(i);
			final Pins temp_pin_out = pins_out.get(i);
			
			new Thread(new Runnable() {							
				@Override
				public void run() {				
					while (true) {	
						// 按键松开时默认高电平,temp_pin.getStatus() = 1
						// 按键按下时，变成低电平，temp_pin.getStatus() = 0
						if (temp_pin.getStatus() == 0) {							
							// 按键down(按下输入键时，对应的输出IO变 High，仅供测试使用，需要屏蔽此段代码)
							temp_pin_out.setLevel(1);								
						}
						else
						{
							// 按键up(松开输入键时，对应的输出IO变 Low，仅供测试使用，需要屏蔽此段代码)
							temp_pin_out.setLevel(0);						
						}
					}
				}						
			}).start();	
		}
		*/
		//---------------------------------------------------
		// 预留10个IO，实际可能用不到 PH14,PH15,PH17,PH20-PH26
		pins_bak.add(new Pins("GPIO-3:21", 181));		
		pins_bak.add(new Pins("GPIO-3:23", 182));	
		//pins_bak.add(new Pins("GPIO-3:25", 183));
		pins_bak.add(new Pins("GPIO-3:27", 184));
		//pins_bak.add(new Pins("GPIO-3:29", 185));
		//pins_bak.add(new Pins("GPIO-3:31", 186));
		pins_bak.add(new Pins("GPIO-3:33", 187));
		pins_bak.add(new Pins("GPIO-3:35", 188));
		pins_bak.add(new Pins("GPIO-3:37", 189));
		pins_bak.add(new Pins("GPIO-3:39", 190));
		pins_bak.add(new Pins("GPIO-3:34", 191));
		pins_bak.add(new Pins("GPIO-3:36", 192));
		pins_bak.add(new Pins("GPIO-3:38", 193));

		// 初始化10个GPIO为输出，置高位
		for (int i = 0; i < pins_bak.size(); i++) {
			final Pins temp_pin_bak = pins_bak.get(i);
			temp_pin_bak.exportPin();
			String dir = temp_pin_bak.getDirection();
			temp_pin_bak.setDirection("out");
			temp_pin_bak.setLevel(1);
		}		
		//---------------------------------------------------
		
		
		
		/*
		1、错误代码,1字节。
			0: 表示没有故障
	        2：电压低
	        3：电机没有插或电机电压测量故障
	        4：电压高
	        5：过流
	        6：输出短路
	        7：IBGT短路
	        10：系统故障
	        11：安全开关脱落
		2、电机电流，每32表示1A电流，2字节。 (暂时无用)
		3、母线电压，单位1V，2字节。	     (暂时无用)	
		4、电机电压，单位1V，2字节。         (暂时无用)
		5、当前电机电阻，每16表示1欧姆，1字节。 (在app的电机电阻设定界面显示)
		6、消耗卡路里，单位0.1Cal，2字节。
		7、脉搏数，单位次每分钟，1字节。		
		*/			
		
				
		// 初始化通讯
		try {
			TreadmillComm.getPermission();
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (TreadmillComm.init() < 0) {
			//Fail!
			//return;
			Log.e("PinSDK", "fatal error ,TreadmillComm init  failed  !");
		}
		
		
	}
	
	static public PinSDK getInstance()
	{
		if(msdk==null)
			msdk = new PinSDK();
		return msdk;
	}


}

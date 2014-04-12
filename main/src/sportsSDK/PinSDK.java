package sportsSDK;

import java.util.ArrayList;
import java.util.List;

import sportsSDK.TreadmillComm;

import sportsSDK.Pins;

public class PinSDK {
	
	static PinSDK msdk=null;
	public PinSDK()
	{
		
		
	}
	
	private void init()
	{
		final List<Pins> pins = new ArrayList<Pins>();
		final List<Pins> pins_out = new ArrayList<Pins>();
		final List<Pins> pins_bak = new ArrayList<Pins>();
		
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
		
		// ��ʼ��7��GPIOΪ���밴��
		for (int i = 0; i < pins.size(); i++) {
			final Pins temp_pin = pins.get(i);
			temp_pin.exportPin();
			String dir = temp_pin.getDirection();
			temp_pin.setDirection("in");
		}
		
		// output-------key1,key2,key3,key4,key5,key6,key7
		// ʵ�ʿ����ò���
		pins_out.add(new Pins("GPIO-3:12", 30));
		pins_out.add(new Pins("GPIO-3:13", 177));
		pins_out.add(new Pins("GPIO-3:14", 31));
		pins_out.add(new Pins("GPIO-3:15", 178));
		pins_out.add(new Pins("GPIO-3:16", 32));
		pins_out.add(new Pins("GPIO-3:17", 179));
		pins_out.add(new Pins("GPIO-3:18", 34));

		// ��ʼ��7��GPIOΪ���
		for (int i = 0; i < pins_out.size(); i++) {
			final Pins temp_pin_out = pins_out.get(i);
			temp_pin_out.exportPin();
			String dir = temp_pin_out.getDirection();
			temp_pin_out.setDirection("out");
		}
		/*
		// �����߳���ⰴ����״̬
		for (int i = 0; i < pins.size(); i++) {
			final Pins temp_pin = pins.get(i);
			final Pins temp_pin_out = pins_out.get(i);
			
			new Thread(new Runnable() {							
				@Override
				public void run() {				
					while (true) {	
						// �����ɿ�ʱĬ�ϸߵ�ƽ,temp_pin.getStatus() = 1
						// ��������ʱ����ɵ͵�ƽ��temp_pin.getStatus() = 0
						if (temp_pin.getStatus() == 0) {							
							// ����down(���������ʱ����Ӧ�����IO�� High����������ʹ�ã���Ҫ���δ˶δ���)
							temp_pin_out.setLevel(1);								
						}
						else
						{
							// ����up(�ɿ������ʱ����Ӧ�����IO�� Low����������ʹ�ã���Ҫ���δ˶δ���)
							temp_pin_out.setLevel(0);						
						}
					}
				}						
			}).start();	
		}
		*/
		//---------------------------------------------------
		// Ԥ��10��IO��ʵ�ʿ����ò��� PH14,PH15,PH17,PH20-PH26
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

		// ��ʼ��10��GPIOΪ������ø�λ
		for (int i = 0; i < pins_bak.size(); i++) {
			final Pins temp_pin_bak = pins_bak.get(i);
			temp_pin_bak.exportPin();
			String dir = temp_pin_bak.getDirection();
			temp_pin_bak.setDirection("out");
			temp_pin_bak.setLevel(1);
		}		
		//---------------------------------------------------
		
		
		//-------------------------------------------------------------------------------------------------------------//				
		//1���趨�ٶȣ���λ0.1km/h��1�ֽڡ���Χ��0.0-25.5
		//2���趨�¶ȣ���λ0.1�ȣ�1�ֽڡ���Χ��0.0-15.0
		//3���¶��������ƣ�0ֹͣ��1������2�½���1�ֽڡ�
		//4�����п��ƣ�0ֹͣ��1���У�1�ֽڡ�
		//5���趨������裬ÿ16��ʾ1ŷķ��2�ֽڣ���һ�ֽ�Ϊ��־���ڶ��ֽ�Ϊ���ݡ�  app��ʾ�趨��ΧΪ0.0--15.9��ȷ�Ϻ����16���͵��ӿڿ�
		//6����ʾ��������״̬��0û�в��ţ�1���ڲ���
		final byte[] out_buffer = new byte[7];
		
		float speed = 0f;
		float gradient = 0f;
		int  gradient_state = 0;
		int run_state = 0;
		int resistance_flag = 0;
		float resistance = 0f;
		int playvoice = 0;
		
		out_buffer[0] = (byte)(speed * 10f);
		out_buffer[1] = (byte)(gradient * 10f);
		out_buffer[2] = (byte)(gradient_state);
		out_buffer[3] = (byte)(run_state);
		out_buffer[4] = (byte)(resistance_flag);
		out_buffer[5] = (byte)(resistance * 16f);
		out_buffer[6] = (byte)(playvoice);
		
		/*
		1���������,1�ֽڡ�
			0: ��ʾû�й���
	        2����ѹ��
	        3�����û�в������ѹ��������
	        4����ѹ��
	        5������
	        6�������·
	        7��IBGT��·
	        10��ϵͳ����
	        11����ȫ��������
		2�����������ÿ32��ʾ1A������2�ֽڡ� (��ʱ����)
		3��ĸ�ߵ�ѹ����λ1V��2�ֽڡ�	     (��ʱ����)	
		4�������ѹ����λ1V��2�ֽڡ�         (��ʱ����)
		5����ǰ������裬ÿ16��ʾ1ŷķ��1�ֽڡ� (��app�ĵ�������趨������ʾ)
		6�����Ŀ�·���λ0.1Cal��2�ֽڡ�
		7������������λ��ÿ���ӣ�1�ֽڡ�		
		*/			
		byte[] in_buffer = new byte[11];
		
		int error_code = 0;
		int motor_current = 0;
		int bus_voltage = 0;
		int motor_voltage = 0;
		float curent_resistance = 0f;
		float calories = 0f;
		int pulse = 0;
				
		// ��ʼ��ͨѶ
		try {
			TreadmillComm.getPermission();
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (TreadmillComm.init() < 0) {
			//Fail!
			//return;
		}
		
		
		
		// test
		if (TreadmillComm.read(in_buffer, in_buffer.length) < 0) {
			//Fail!
			//return;
		}
		else {
			error_code = (int)in_buffer[0];
			//motor_current = in_buffer[1] << 8 + (int)in_buffer[2];	//(��ʱ����)
			//bus_voltage = in_buffer[3] << 8 + (int)in_buffer[4];		//(��ʱ����)
			//motor_voltage = in_buffer[5] << 8 + (int)in_buffer[6];	//(��ʱ����)
			curent_resistance = ((float)(in_buffer[7])) / 16f;
			calories = ((float)(in_buffer[8] << 8 + (int)in_buffer[9])) / 10f;
			pulse = (int)in_buffer[10];		
		}
		
		// test
		out_buffer[0] = 33;
		out_buffer[1] = 44;
		out_buffer[2] = 55;
		out_buffer[3] = 66;
		out_buffer[4] = 22;
		out_buffer[5] = 11;
		out_buffer[6] = 00;
		 
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
				TreadmillComm.write(out_buffer, out_buffer.length);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
	}
	
	static public PinSDK getInstance()
	{
		if(msdk==null)
			msdk = new PinSDK();
		return msdk;
	}
}

package sportsSDK;


import java.io.IOException;

public class TreadmillComm {

	public static native int init();

	public static native int close();

	public static native int write(byte[] buf, int len);

	public static native int read(byte[] buf, int len);
	
	// ���Ϻ�������ֵ<0ʱ���������ʧ��

	/* Load shared library */
	static {
		System.loadLibrary("TreadmillComm");
	}

	public static void getPermission() {
		try {
			Process p = Runtime.getRuntime().exec(
					new String[] { "su", "-c", "chmod 777 /dev/i2c-1" });
			p.waitFor();			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}

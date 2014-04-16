
package sportsSDK;

import io.github.faywong.exerciseapp.IHWStatusListener;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.hardware.comm.TreadmillComm;

import sportsSDK.Pins;

public class PinSDK {

    static PinSDK msdk = null;
    public float resistanceFactory =1f;

    IHWStatusListener mListener = null;

    final int keyDelay = 20;
    final int sdkDelay = 250;
    final List<Pins> pins = new ArrayList<Pins>();
    final List<Pins> pins_out = new ArrayList<Pins>();
    final List<Pins> pins_bak = new ArrayList<Pins>();

    boolean isInit = false;

    int speed = 0;
    int gradient = 0;
    int gradient_state = 0;
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

    public void setResistance(float value)
    {
    	resistanceFactory=value;
    }
    
    public float getResistance()
    {
    	return resistanceFactory;
    }
    public void setHWStatusListener(IHWStatusListener listener)
    {
        mListener = listener;
    }

    public PinSDK()
    {

        new Thread(new Runnable() {
            public void run() {
                // init();
                while (true)
                {
                    try {
                        Thread.sleep(sdkDelay);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    write();
                    read();
                    if (mListener != null)
                        mListener.onHWStatusChanged(error_code, calories, pulse);
                }
            }
        }).start();

        // 锟斤拷锟斤拷锟竭筹拷锟斤拷獍达拷锟斤拷状态

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
                            keyState[i] = true;
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

        if (mListener != null & keyState[pos])
        {
            switch (pos)
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
        keyState[pos] = false;
    }

    private void read()
    {
        if (!isInit)
            return;

        // test
        if (TreadmillComm.read(in_buffer, in_buffer.length) < 0) {
            // Fail!
            // return;
            Log.e("PinSDK", "fatal error ,read TreadmillComm   failed  !");
        }
        else {
            error_code = (int) in_buffer[0];
            // motor_current = in_buffer[1] << 8 + (int)in_buffer[2];
            // //(锟斤拷时锟斤拷锟斤拷)
            // bus_voltage = in_buffer[3] << 8 + (int)in_buffer[4]; //(锟斤拷时锟斤拷锟斤拷)
            // motor_voltage = in_buffer[5] << 8 + (int)in_buffer[6];
            // //(锟斤拷时锟斤拷锟斤拷)
            curent_resistance = ((float) (in_buffer[7])) / 16f;
            calories = (in_buffer[8] << 8 + (int) in_buffer[9]);
            pulse = (int) in_buffer[10];
        }
    }

    private void write()
    {

        if (!isInit)
            return;

        // -------------------------------------------------------------------------------------------------------------//
        // 1锟斤拷锟借定锟劫度ｏ拷锟斤拷位0.1km/h锟斤拷1锟街节★拷锟斤拷围锟斤拷0.0-25.5
        // 2锟斤拷锟借定锟铰度ｏ拷锟斤拷位0.1锟饺ｏ拷1锟街节★拷锟斤拷围锟斤拷0.0-15.0
        // 3锟斤拷锟铰讹拷锟斤拷锟斤拷疲锟�0停止锟斤拷1锟斤拷锟斤拷2锟铰斤拷锟斤拷1锟街节★拷
        // 4锟斤拷锟斤拷锟叫匡拷锟狡ｏ拷0停止锟斤拷1锟斤拷锟叫ｏ拷1锟街节★拷
        // 5锟斤拷锟借定锟斤拷锟斤拷锟借，每16锟斤拷示1欧姆锟斤拷2锟街节ｏ拷锟斤拷一锟街斤拷为锟斤拷志锟斤拷锟节讹拷锟街斤拷为锟斤拷荨锟�
        // app锟斤拷示锟借定锟斤拷围为0.0--15.9锟斤拷确锟较猴拷锟斤拷锟�16锟斤拷锟酵碉拷锟接口匡拷
        // 6锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷状态锟斤拷0没锟叫诧拷锟脚ｏ拷1锟斤拷锟节诧拷锟斤拷
        final byte[] out_buffer = new byte[7];

        out_buffer[0] = (byte) (speed * 10f);
        out_buffer[1] = (byte) (gradient * 10f);
        out_buffer[2] = (byte) (gradient_state);
        out_buffer[3] = (byte) (run_state);
        out_buffer[4] = (byte) (resistance_flag);
        out_buffer[5] = (byte) (resistance * 16f);
        out_buffer[6] = (byte) (playvoice);

        if (TreadmillComm.write(out_buffer, out_buffer.length) < 0)
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

        // 锟斤拷始锟斤拷7锟斤拷GPIO为锟斤拷锟诫按锟斤拷
        for (int i = 0; i < pins.size(); i++) {
            final Pins temp_pin = pins.get(i);
            temp_pin.exportPin();
            String dir = temp_pin.getDirection();
            temp_pin.setDirection("in");
        }

        // output-------key1,key2,key3,key4,key5,key6,key7
        // 实锟绞匡拷锟斤拷锟矫诧拷锟斤拷
        pins_out.add(new Pins("GPIO-3:12", 30));
        pins_out.add(new Pins("GPIO-3:13", 177));
        pins_out.add(new Pins("GPIO-3:14", 31));
        pins_out.add(new Pins("GPIO-3:15", 178));
        pins_out.add(new Pins("GPIO-3:16", 32));
        pins_out.add(new Pins("GPIO-3:17", 179));
        pins_out.add(new Pins("GPIO-3:18", 34));

        // 锟斤拷始锟斤拷7锟斤拷GPIO为锟斤拷锟�
        for (int i = 0; i < pins_out.size(); i++) {
            final Pins temp_pin_out = pins_out.get(i);
            temp_pin_out.exportPin();
            String dir = temp_pin_out.getDirection();
            temp_pin_out.setDirection("out");
        }
        /*
         * // 锟斤拷锟斤拷锟竭筹拷锟斤拷獍达拷锟斤拷状态 for (int i = 0; i < pins.size(); i++) { final Pins
         * temp_pin = pins.get(i); final Pins temp_pin_out = pins_out.get(i);
         * new Thread(new Runnable() {
         * @Override public void run() { while (true) { //
         * 锟斤拷锟斤拷锟缴匡拷时默锟较高碉拷平,temp_pin.getStatus() = 1 //
         * 锟斤拷锟斤拷锟斤拷时锟斤拷锟斤拷傻偷锟狡斤拷锟絫emp_pin.getStatus() = 0 if (temp_pin.getStatus() ==
         * 0) { // 锟斤拷锟斤拷down(锟斤拷锟斤拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟接︼拷锟斤拷锟斤拷IO锟斤拷
         * High锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷使锟矫ｏ拷锟斤拷要锟斤拷锟轿此段达拷锟斤拷) temp_pin_out.setLevel(1); } else { //
         * 锟斤拷锟斤拷up(锟缴匡拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟接︼拷锟斤拷锟斤拷IO锟斤拷 Low锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷使锟矫ｏ拷锟斤拷要锟斤拷锟轿此段达拷锟斤拷)
         * temp_pin_out.setLevel(0); } } } }).start(); }
         */
        // ---------------------------------------------------
        // 预锟斤拷10锟斤拷IO锟斤拷实锟绞匡拷锟斤拷锟矫诧拷锟斤拷 PH14,PH15,PH17,PH20-PH26
        pins_bak.add(new Pins("GPIO-3:21", 181));
        pins_bak.add(new Pins("GPIO-3:23", 182));
        // pins_bak.add(new Pins("GPIO-3:25", 183));
        pins_bak.add(new Pins("GPIO-3:27", 184));
        // pins_bak.add(new Pins("GPIO-3:29", 185));
        // pins_bak.add(new Pins("GPIO-3:31", 186));
        pins_bak.add(new Pins("GPIO-3:33", 187));
        pins_bak.add(new Pins("GPIO-3:35", 188));
        pins_bak.add(new Pins("GPIO-3:37", 189));
        pins_bak.add(new Pins("GPIO-3:39", 190));
        pins_bak.add(new Pins("GPIO-3:34", 191));
        pins_bak.add(new Pins("GPIO-3:36", 192));
        pins_bak.add(new Pins("GPIO-3:38", 193));

        // 锟斤拷始锟斤拷10锟斤拷GPIO为锟斤拷锟斤拷锟斤拷酶锟轿�
        for (int i = 0; i < pins_bak.size(); i++) {
            final Pins temp_pin_bak = pins_bak.get(i);
            temp_pin_bak.exportPin();
            String dir = temp_pin_bak.getDirection();
            temp_pin_bak.setDirection("out");
            temp_pin_bak.setLevel(1);
        }
        // ---------------------------------------------------

        /*
         * 1锟斤拷锟斤拷锟斤拷锟斤拷锟�,1锟街节★拷 0: 锟斤拷示没锟叫癸拷锟斤拷 2锟斤拷锟斤拷压锟斤拷 3锟斤拷锟斤拷锟矫伙拷胁锟斤拷锟斤拷锟窖癸拷锟斤拷锟斤拷锟斤拷锟�
         * 4锟斤拷锟斤拷压锟斤拷 5锟斤拷锟斤拷锟斤拷 6锟斤拷锟斤拷锟斤拷锟铰� 7锟斤拷IBGT锟斤拷路 10锟斤拷系统锟斤拷锟斤拷 11锟斤拷锟斤拷全锟斤拷锟斤拷锟斤拷锟斤拷
         * 2锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷每32锟斤拷示1A锟斤拷锟斤拷锟斤拷2锟街节★拷 (锟斤拷时锟斤拷锟斤拷) 3锟斤拷母锟竭碉拷压锟斤拷锟斤拷位1V锟斤拷2锟街节★拷
         * (锟斤拷时锟斤拷锟斤拷) 4锟斤拷锟斤拷锟斤拷压锟斤拷锟斤拷位1V锟斤拷2锟街节★拷 (锟斤拷时锟斤拷锟斤拷)
         * 5锟斤拷锟斤拷前锟斤拷锟斤拷锟借，每16锟斤拷示1欧姆锟斤拷1锟街节★拷 (锟斤拷app锟侥碉拷锟斤拷锟斤拷锟借定锟斤拷锟斤拷锟斤拷示)
         * 6锟斤拷锟斤拷目锟铰凤拷铮拷锟轿�0.1Cal锟斤拷2锟街节★拷 7锟斤拷锟斤拷锟斤拷锟斤拷位锟斤拷每锟斤拷锟接ｏ拷1锟街节★拷
         */

        // 锟斤拷始锟斤拷通讯
        try {
            TreadmillComm.getPermission();
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TreadmillComm.init() < 0) {
            // Fail!
            // return;
            Log.e("PinSDK", "fatal error ,TreadmillComm init  failed  !");
        }

    }

    static public PinSDK getInstance()
    {
        if (msdk == null)
            msdk = new PinSDK();
        return msdk;
    }

}

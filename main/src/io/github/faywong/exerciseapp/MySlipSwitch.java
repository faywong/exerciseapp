package io.github.faywong.exerciseapp;

import io.github.faywong.exerciseapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MySlipSwitch extends View implements OnTouchListener {

	//�?关开启时的背景，关闭时的背景，滑动按�?
	private Bitmap switch_on_Bkg, switch_off_Bkg, slip_Btn;
	private Rect on_Rect, off_Rect;
	
	//是否正在滑动
	private boolean isSlipping = false;
	//当前�?关状态，true为开启，false为关�?
	private boolean isSwitchOn = false;
	
	//手指按下时的水平坐标X，当前的水平坐标X
	private float previousX, currentX;
	
	//�?关监听器
	private OnSwitchListener onSwitchListener;
	//是否设置了开关监听器
	private boolean isSwitchListenerOn = false;
	
	
	public MySlipSwitch(Context context) {
		super(context);
		init();
	}
	
	
	public MySlipSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	
	private void init() {
		setOnTouchListener(this);
		setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
	}
	
	
	protected void setImageResource(int switchOnBkg, int switchOffBkg, int slipBtn) {
		switch_on_Bkg = BitmapFactory.decodeResource(getResources(), switchOnBkg);
		switch_off_Bkg = BitmapFactory.decodeResource(getResources(), switchOffBkg);
		slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtn);	
		
		//右半边Rect，即滑动按钮在右半边时表示开关开�?
		on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0, switch_off_Bkg.getWidth(), slip_Btn.getHeight());
		//左半边Rect，即滑动按钮在左半边时表示开关关�?
		off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());
	}
	
	
	protected void setSwitchState(boolean switchState) {
		isSwitchOn = switchState;
	}
	
	
	protected boolean getSwitchState() {
		return isSwitchOn;
	}
	
	
	protected void updateSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		invalidate();
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		//滑动按钮的左边坐�?
		float left_SlipBtn;
		
		//手指滑动到左半边的时候表示开关为关闭状�?�，滑动到右半边的时候表示开关为�?启状�?
		if(currentX < (switch_on_Bkg.getWidth() / 2)) {
			canvas.drawBitmap(switch_off_Bkg, matrix, paint);
		} else {
			canvas.drawBitmap(switch_on_Bkg, matrix, paint);
		}
		
		//判断当前是否正在滑动
		if(isSlipping) {
			if(currentX > switch_on_Bkg.getWidth()) {
				left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
			} else {
				left_SlipBtn = currentX - slip_Btn.getWidth() / 2;
			}
		} else {
			//根据当前的开关状态设置滑动按钮的位置
			if(isSwitchOn) {
				left_SlipBtn = on_Rect.left;
			} else {
				left_SlipBtn = off_Rect.left;
			}
		}
		
		//对滑动按钮的位置进行异常判断
		if(left_SlipBtn < 0) {
			left_SlipBtn = 0;
		} else if(left_SlipBtn > switch_on_Bkg.getWidth() - slip_Btn.getWidth()) {
			left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
		}
		
		canvas.drawBitmap(slip_Btn, left_SlipBtn, 0, paint);
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(switch_on_Bkg.getWidth(), switch_on_Bkg.getHeight());
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()) {
		//滑动
		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			break;
			
		//按下
		case MotionEvent.ACTION_DOWN:
			if(event.getX() > switch_on_Bkg.getWidth() || event.getY() > switch_on_Bkg.getHeight()) {
				return false;
			}
			
			isSlipping = true;
			previousX = event.getX();
			currentX = previousX;
			break;
		
		//松开
		case MotionEvent.ACTION_UP:
			isSlipping = false;
			//松开前开关的状�??
			boolean previousSwitchState  = isSwitchOn;
			
			if(event.getX() >= (switch_on_Bkg.getWidth() / 2)) {
				isSwitchOn = true;
			} else {
				isSwitchOn = false;
			}
			
			//如果设置了监听器，则调用此方�?
			if(isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
				onSwitchListener.onSwitched(isSwitchOn);
			}
			break;
		
		default:
			break;
		}
		
		//重新绘制控件
		invalidate();
		return true;
	}


	public void setOnSwitchListener(OnSwitchListener listener) {
		onSwitchListener = listener;
		isSwitchListenerOn = true;
	}
	
	
	public interface OnSwitchListener {
		abstract void onSwitched(boolean isSwitchOn);
	}
}

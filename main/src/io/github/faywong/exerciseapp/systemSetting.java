package io.github.faywong.exerciseapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import io.github.faywong.exerciseapp.TabAdapter;
import io.github.faywong.exerciseapp.R;
import io.github.faywong.exerciseapp.Tab1View;
import io.github.faywong.exerciseapp.Tab2View;
import io.github.faywong.exerciseapp.Tab3View;
import io.github.faywong.exerciseapp.Tab4View;




public class systemSetting extends Activity implements View.OnClickListener {

	Button backBtn;

	
	private RadioButton mTab1, mTab2, mTab3,mTab4,mCurTab;
	private CustomViewPager mViewPager;
	private List<View> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_setting);

		backBtn = (Button)findViewById(R.id.header_leftbtn);
		backBtn.setOnClickListener(this); 
		initView();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == null) {
			return;
		} else if (v.getId() == R.id.user_btn) {
		
		} else if (v.getId() == R.id.header_leftbtn) {
			finish();
		}
	}
private void initView() {
		
		mList = new ArrayList<View>();

		Tab1View tab1View = new Tab1View(this);
		Tab2View tab2View = new Tab2View(this);
		Tab3View tab3View = new Tab3View(this);
		Tab4View tab4View = new Tab4View(this);

		mList.add(tab1View.getView());
		mList.add(tab2View.getView());
		mList.add(tab3View.getView());
		mList.add(tab4View.getView());

		
		mViewPager = (CustomViewPager) this.findViewById(R.id.viewpager);

		mViewPager.setAdapter(new TabAdapter(mList));

		mTab1 = (RadioButton) this.findViewById(R.id.main_tab1);
		mTab2 = (RadioButton) this.findViewById(R.id.main_tab2);
		mTab3 = (RadioButton) this.findViewById(R.id.main_tab3);
		mTab4 = (RadioButton) this.findViewById(R.id.main_tab4);
		
		mCurTab = mTab1;
		mTab1.setOnClickListener(mOnTabClickListener);
		mTab2.setOnClickListener(mOnTabClickListener);
		mTab3.setOnClickListener(mOnTabClickListener);
		mTab4.setOnClickListener(mOnTabClickListener);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					mCurTab.setChecked(false);
					mCurTab = mTab1;
					mCurTab.setChecked(true);
					break;
				case 1:
					mCurTab.setChecked(false);
					mCurTab = mTab2;
					mCurTab.setChecked(true);
					break;
				case 2:
					mCurTab.setChecked(false);
					mCurTab = mTab3;
					mCurTab.setChecked(true);
					break;
				case 3:
					mCurTab.setChecked(false);
					mCurTab = mTab4;
					mCurTab.setChecked(true);
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	OnClickListener mOnTabClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.main_tab1:
				if (mCurTab != mTab1) {
					mViewPager.setCurrentItem(0);
					mCurTab = mTab1;
				}
				break;
			case R.id.main_tab2:
				if (mCurTab != mTab2) {
					mViewPager.setCurrentItem(1);
					mCurTab = mTab2;
				}
				break;
			case R.id.main_tab3:
				if (mCurTab != mTab3) {
					mViewPager.setCurrentItem(2);
					mCurTab = mTab3;
				}
				break;
			case R.id.main_tab4:
				if (mCurTab != mTab4) {
					mViewPager.setCurrentItem(3);
					mCurTab = mTab4;
				}
				break;
			}
		}

	};
}

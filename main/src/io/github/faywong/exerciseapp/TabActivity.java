package io.github.faywong.exerciseapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_container);
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab tabA = bar.newTab().setText("A Tab");
		ActionBar.Tab tabB = bar.newTab().setText("B Tab");
		//ActionBar.Tab tabC = bar.newTab().setText("C Tab");

		Fragment fragmentA = new AFragmentTab();
		Fragment fragmentB = new BFragmentTab();
		//Fragment fragmentC = new CFragmentTab();

		tabA.setTabListener(new MyTabsListener(fragmentA));
		tabB.setTabListener(new MyTabsListener(fragmentB));
		// tabC.setTabListener(new MyTabsListener(fragmentC));

		bar.addTab(tabA);
		bar.addTab(tabB);
		//bar.addTab(tabC);
	}

	protected class MyTabsListener implements ActionBar.TabListener {

		private Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.fragment_container, fragment, null);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// some people needed this line as well to make it work:
			ft.remove(fragment);
		}
	}
	
	public class AFragmentTab extends Fragment
	{
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	  {
	    return inflater.inflate(R.layout.activity_exercise_free_mode, container, false);
	  }
	}
	
	public class BFragmentTab extends Fragment
	{
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	  {
	    return inflater.inflate(R.layout.activity_exercise_main, container, false);
	  }
	}
}

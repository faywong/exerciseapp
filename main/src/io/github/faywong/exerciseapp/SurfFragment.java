package io.github.faywong.exerciseapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class SurfFragment extends Fragment implements OnClickListener {

	
	final static public int free_mode = 1;
    final static public int tv_mode = 2;
    final static public int online_music_mode = 3;
    final static public int online_video_mode = 4;
    static public int curMode = free_mode;
    
	private static String TAG = "SurfFragment";
	EditText url;
	// TextView mTitle;
	WebView mWebView;
	Button goButton;
	Button backButton;
	Button aboutButton;

	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url_) {
			view.loadUrl(url_);
			//url.setText(url_);
			// mTitle.setText("you are browsing web: "+url_);
			return true;
		}
	}
	
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	public void windowFocusChanged(boolean hasFocus) {

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View parentLayout = inflater.inflate(R.layout.activity_web_browser,
				container, false);
		if (parentLayout == null) {
			Log.e(TAG, "parentLayout is null");
			return null;
		}

		setControl(parentLayout);
		setWebStyle();

		return parentLayout;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void setControl(View parentView) {
		url = (EditText) parentView.findViewById(R.id.urltext);
		mWebView = (WebView) parentView.findViewById(R.id.webshow);
		goButton = (Button) parentView.findViewById(R.id.GoBtn);
		backButton = (Button) parentView.findViewById(R.id.BackBtn);
		aboutButton = (Button) parentView.findViewById(R.id.reloadBtn);
		// mTitle=(TextView)parentView.findViewById(R.id.WebTitle);
		goButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);
	}
	public void setMode()
	{
		if(mWebView==null)
			return;
		if(curMode==free_mode)
			mWebView.loadUrl("http://www.baidu.com");
		else if(curMode==online_music_mode)
		{
			mWebView.loadUrl("http://fm.baidu.com/");
		}
		else if(curMode==online_video_mode)
		{
			mWebView.loadUrl("http://mini123.com/psv/");
		}
		else if(curMode==tv_mode)
		{
			mWebView.loadUrl("http://mini123.com/psv/psvtv.html");
		}
	}
	private void setWebStyle() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		
		mWebView.setWebViewClient(new MyWebViewClient());
		setMode();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.GoBtn:
			String url_text;
			String url_head = "http://";
			url_text = url.getText().toString();
			if (!url_text.contains("http://")) {
				url_text = url_head.concat(url_text);

			}
			mWebView.loadUrl(url_text);
			// mTitle.setText("you are browsing web: "+url_text);
			break;
		case R.id.BackBtn:
			mWebView.goBack();
			break;
		case R.id.forwardBtn:
			mWebView.goForward();
			break;
		case R.id.reloadBtn:
			mWebView.reload();
			break;
		}
	}

}

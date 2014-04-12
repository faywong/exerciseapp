package io.github.faywong.exerciseapp;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SurfFragment extends Fragment implements OnClickListener{
	
	private static String TAG = "SurfFragment";
	EditText url;
    //TextView mTitle;
    WebView mWebView;
    Button goButton;
    Button backButton;
    Button aboutButton;
    
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

		View parentLayout = inflater.inflate(R.layout.activity_web_browser, container, false);
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
	
	private void setControl( View parentView) {
    	url=(EditText)parentView.findViewById(R.id.urltext);
    	mWebView=(WebView)parentView.findViewById(R.id.webshow);
    	goButton=(Button)parentView.findViewById(R.id.GoBtn);
    	backButton=(Button)parentView.findViewById(R.id.BackBtn);
    	aboutButton=(Button)parentView.findViewById(R.id.reloadBtn);
    	//mTitle=(TextView)parentView.findViewById(R.id.WebTitle);
		goButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);
	}
    private void setWebStyle() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.loadUrl("http://www.baidu.com");
		mWebView.setWebViewClient(new MyWebViewClient());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.GoBtn:
			String url_text;
			String url_head = "http://";
			url_text=url.getText().toString();
			if(!url_text.contains("http://")){
				url_text=url_head.concat(url_text);
				
			}
			mWebView.loadUrl(url_text);
			//mTitle.setText("you are browsing web: "+url_text);
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
	class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view,String url_){
			view.loadUrl(url_);
			url.setText(url_);
			//mTitle.setText("you are browsing web: "+url_);
			return true;
		}
	}
}

package io.github.faywong.exerciseapp.thirdparty;


import io.github.faywong.exerciseapp.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class webBrowser extends Activity implements OnClickListener{
    EditText url;
    TextView mTitle;
    WebView mWebView;
    Button goButton;
    Button backButton;
    Button aboutButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        setControl();
        setWebStyle();
    }
    private void setControl() {
    	url=(EditText)findViewById(R.id.urltext);
    	mWebView=(WebView)findViewById(R.id.webshow);
    	goButton=(Button)findViewById(R.id.GoBtn);
    	backButton=(Button)findViewById(R.id.BackBtn);
    	aboutButton=(Button)findViewById(R.id.reloadBtn);
    	mTitle=(TextView)findViewById(R.id.WebTitle);
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
			mTitle.setText("you are browsing web: "+url_text);
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
			mTitle.setText("you are browsing web: "+url_);
			return true;
		}
	}
}

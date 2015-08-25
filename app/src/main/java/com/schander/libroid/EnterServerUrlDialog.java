package com.schander.libroid;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.app.Activity;

public class EnterServerUrlDialog extends Activity {
	
	private final String httpPrefix = "http://"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView myWebView = (WebView) findViewById(R.id.webView);
		myWebView.setDownloadListener(null);
		setContentView(R.layout.activity_enter_server_url_dialog);
	}

	public void serverUrlCklicked(View view){
		EditText urlEditText = (EditText) findViewById(R.id.urlField);
		String url = urlEditText.getText().toString();
		if(!url.startsWith(httpPrefix)){
			url = httpPrefix + url;
			urlEditText.setText(url);
		}
		WebView myWebView = (WebView) findViewById(R.id.webView);
		myWebView.setWebViewClient(new MyWebViewClient());
		myWebView.loadUrl(url);
	}
	
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	view.loadUrl(url);
	        return true;
	    }
	}
	

}

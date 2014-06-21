package tv.luxs.rcassistant;

import tv.luxs.rcassistant.utils.NetUtils;
import tv.luxs.rcassistant.utils.ShareUtlis;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class WebActivity extends Activity {

	private WebView mWebView;
	private ProgressBar mProgressBar;
//	private String url = "http://10.1.0.1/";
//	private String url = "http://s.luxs.tv/stage/index.html";
	private String url = "http://s.luxs.tv:8070/stage/";
	private static final String TAG="WebActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		initView();
		setMenu();
		Log.i(TAG, "IP:"+NetUtils.getLocalIpAddress());
	}

	private void initView() {
		mWebView = (WebView) findViewById(R.id.my_web_view);
		mProgressBar = (ProgressBar) findViewById(R.id.web_progressBar);
		
		String ua = mWebView.getSettings().getUserAgentString();
		Log.i("bing",
				"标志:" + ua + "; Luxshare" + "; Mac="
						+ NetUtils.GetMACAddress(WebActivity.this));
		mWebView.getSettings().setUserAgentString(
				ua + "; Luxshare" + "; Mac="
						+ NetUtils.GetMACAddress(WebActivity.this));
		
		mWebView.loadUrl(url);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				mProgressBar.setProgress(newProgress);
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
				} else {
					mProgressBar.setVisibility(View.VISIBLE);
				}
			}

		});

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);

			}

		});

		mWebView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& mWebView.canGoBack()) {
						mWebView.goBack();
						return true;
					}
				}

				return false;
			}
		});

		findViewById(R.id._back_img_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

	}

	@SuppressLint("NewApi")
	private void setMenu() {
		ImageView menu = (ImageView) findViewById(R.id.menu_img);
		MainActivityt.popupMenus[3] = new PopupMenu(WebActivity.this, menu);
		MainActivityt.popupMenus[3].inflate(R.menu.more_menu);
		MainActivityt.popupMenus[3]
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.le_more:
							intent.setClass(WebActivity.this,
									MoreActivity.class);
							startActivity(intent);
							break;

						case R.id.isao:
							intent.setClass(WebActivity.this,
									SettingConnectActivity.class);
							startActivity(intent);
							break;

						case R.id.le_vip_m:

							intent.setClass(WebActivity.this,
									LeVipActivity.class);
							startActivity(intent);
							break;

						case R.id.leshare:

							ShareUtlis.share2Fre(WebActivity.this);

							break;

						default:
							break;
						}
						return false;
					}
				});

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivityt.popupMenus[3].show();
			}
		});

	}

}

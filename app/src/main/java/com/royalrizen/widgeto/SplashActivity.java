package com.royalrizen.widgeto;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.royalrizen.widgeto.databinding.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class SplashActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private SplashBinding binding;
	private boolean noInternetShown = false;
	
	private TimerTask checkNetworkTimer;
	private RequestNetwork Network;
	private RequestNetwork.RequestListener _Network_request_listener;
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = SplashBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		Network = new RequestNetwork(this);
		
		binding.webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
		
		_Network_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		binding.linear1.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFFE5E8FF,0xFFF8FAFC}));
		binding.progressbar1.setIndeterminate(true);
		checkNetworkTimer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (SketchwareUtil.isConnected(getApplicationContext())) {
							checkNetworkTimer.cancel();
							
							// Delay code
							new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
								@Override
								public void run() {
									// Set up WebView here
									binding.webview1.setVisibility(View.INVISIBLE);
									
									WebSettings settings = binding.webview1.getSettings();
									
									settings.setJavaScriptEnabled(true);
									settings.setDomStorageEnabled(true);
									settings.setDatabaseEnabled(true);
									
									settings.setUseWideViewPort(true);
									settings.setLoadWithOverviewMode(true);
									
									settings.setCacheMode(WebSettings.LOAD_DEFAULT);
									
									settings.setUserAgentString(
									"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36"
									);
									
									CookieManager cookieManager = CookieManager.getInstance();
									
									cookieManager.setAcceptCookie(true);
									
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
										cookieManager.setAcceptThirdPartyCookies(binding.webview1, true);
									}
									
									cookieManager.flush();
									
									// Set WebViewClient
									binding.webview1.setWebViewClient(new WebViewClient() {
										private boolean launched = false;
										
										private void openActivity(final Class<?> cls) {
											if (launched)
											return;
											
											launched = true;
											
											CookieManager.getInstance().flush();
											
											binding.progressbar1.setIndeterminate(false);
											
											ValueAnimator animator = ValueAnimator.ofInt(0, 100);
											animator.setDuration(1000);
											
											animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
												@Override
												public void onAnimationUpdate(ValueAnimator animation) {
													binding.progressbar1.setProgressCompat((int) animation.getAnimatedValue(), true);
												}
											});
											
											animator.addListener(new AnimatorListenerAdapter() {
												@Override
												public void onAnimationEnd(Animator animation) {
													binding.webview1.stopLoading();
													
													startActivity(new Intent(SplashActivity.this, cls));
													finish();
												}
											});
											
											animator.start();
										}
										
										private void checkDiscordToken() {
											binding.webview1.evaluateJavascript(
											"(function(){"
											+ "var i=document.createElement('iframe');"
											+ "document.body.appendChild(i);"
											+ "return i.contentWindow.localStorage.token;"
											+ "})()",
											new ValueCallback<String>() {
												@Override
												public void onReceiveValue(String result) {
													if (result == null || result.equals("null")) {
														openActivity(MainActivity.class);
														return;
													}
													
													try {
														String token = new org.json.JSONArray("[" + result + "]").getString(0);
														token = token.replace("\"", "");
														
														if (token.equals("") || token.equals("null")) {
															openActivity(MainActivity.class);
															return;
														}
														
														openActivity(WidgetoActivity.class);
													} catch (Exception e) {
														openActivity(MainActivity.class);
													}
												}
											});
										}
										
										@Override
										public void onPageFinished(WebView view, String url) {
											super.onPageFinished(view, url);
											if (launched)
											return;
											checkDiscordToken();
										}
										
										@Override
										public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
											super.onReceivedError(view, request, error);
											openActivity(MainActivity.class);
										}
									});
									
									binding.webview1.loadUrl("https://discord.com/login?redirect_to=%2Fdevelopers%2Fapplications");
								}
							}, 1600);
						} else {
							if (!noInternetShown) {
								noInternetShown = true;
								com.google.android.material.snackbar.Snackbar.make(binding.linear1, "You're not connected to the Internet.", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).setAction("", new View.OnClickListener(){
									@Override
									public void onClick(View _view) {
										
									}
								}).show();
							}
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(checkNetworkTimer, (int)(500), (int)(1000));
	}
	
}
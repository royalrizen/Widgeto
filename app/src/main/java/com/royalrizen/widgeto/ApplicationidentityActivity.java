package com.royalrizen.widgeto;

import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.royalrizen.widgeto.databinding.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class ApplicationidentityActivity extends AppCompatActivity {
	
	private ApplicationidentityBinding binding;
	private String authToken = "";
	private boolean tokenLoaded = false;
	private HashMap<String, Object> userDataMap = new HashMap<>();
	private HashMap<String, Object> Authorization = new HashMap<>();
	private boolean fetchedApps = false;
	private boolean hasSocialSDK = false;
	private String flagString = "";
	private String AppID = "";
	private HashMap<String, Object> body = new HashMap<>();
	private String copiedText = "";
	private HashMap<String, Object> AuthorizationBot = new HashMap<>();
	private HashMap<String, Object> botDataMap = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> userApplications = new ArrayList<>();
	private ArrayList<String> Applications = new ArrayList<>();
	private ArrayList<String> ApplicationsID = new ArrayList<>();
	private ArrayList<String> ApplicationsIcon = new ArrayList<>();
	private ArrayList<String> ApplicationsFlags = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> targetPlatforms = new ArrayList<>();
	
	private RequestNetwork r;
	private RequestNetwork.RequestListener _r_request_listener;
	private RequestNetwork s;
	private RequestNetwork.RequestListener _s_request_listener;
	private RequestNetwork t;
	private RequestNetwork.RequestListener _t_request_listener;
	private AlertDialog.Builder dialog;
	private ProgressDialog progressDialog;
	private RequestNetwork u;
	private RequestNetwork.RequestListener _u_request_listener;
	private AlertDialog.Builder bot;
	private RequestNetwork v;
	private RequestNetwork.RequestListener _v_request_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = ApplicationidentityBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		setSupportActionBar(binding.Toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.Toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		r = new RequestNetwork(this);
		s = new RequestNetwork(this);
		t = new RequestNetwork(this);
		dialog = new AlertDialog.Builder(this);
		u = new RequestNetwork(this);
		bot = new AlertDialog.Builder(this);
		v = new RequestNetwork(this);
		
		binding.webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				if (!tokenLoaded) {
					_fetchTokenUser();
				}
				super.onPageFinished(_param1, _param2);
			}
		});
		
		binding.appsspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (ApplicationsIcon.get((int)(_position)).equals("null")) {
					binding.imageview1.setBackgroundColor(Color.TRANSPARENT);
					binding.imageview1.setImageResource(R.drawable.icon_error_round);
				} else {
					binding.imageview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFD5D9FE));
					Glide.with(getApplicationContext()).load(Uri.parse("https://cdn.discordapp.com/app-icons/".concat(ApplicationsID.get((int)(_position)).trim().concat("/".concat(ApplicationsIcon.get((int)(_position)).trim().concat(".png")))))).into(binding.imageview1);
				}
				if (!ApplicationsFlags.get((int)(_position)).equals("null")) {
					flagString = ApplicationsFlags.get((int)(_position));
				}
				if (!ApplicationsID.get((int)(_position)).equals("null")) {
					AppID = ApplicationsID.get((int)(_position));
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		binding.next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (AppID.equals("null")) {
					SketchwareUtil.showMessage(getApplicationContext(), "We don't see any applications created by you.");
				} else {
					binding.app.animate()
					.alpha(0f)
					.scaleX(0.9f)
					.scaleY(0.9f)
					.setDuration(200)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							binding.app.setVisibility(View.GONE);
							
							// Reset app for next time
							binding.app.setAlpha(1f);
							binding.app.setScaleX(1f);
							binding.app.setScaleY(1f);
							
							// Show sdk
							binding.sdk.setAlpha(0f);
							binding.sdk.setScaleX(0.9f);
							binding.sdk.setScaleY(0.9f);
							binding.sdk.setVisibility(View.VISIBLE);
							
							binding.sdk.animate()
							.alpha(1f)
							.scaleX(1f)
							.scaleY(1f)
							.setDuration(250)
							.start();
						}
					})
					.start();
					try {
						long flags = (long) Double.parseDouble(flagString);
						
						if ((flags & 1024L) != 0) {
							hasSocialSDK = true;
						} else {
							hasSocialSDK = false;
						}
						if (hasSocialSDK) {
							binding.gamessdk.setEnabled(false);
							binding.gamessdk.setText("Enabled");
							binding.gamessdk.setBackgroundColor(0xFF4CAF50);
						} else {
							binding.gamessdk.setEnabled(true);
							binding.gamessdk.setText("Enable");
							binding.gamessdk.setBackgroundColor(0xFFD32F2F);
						}
					} catch (Exception e) {
						SketchwareUtil.showMessage(getApplicationContext(), e.toString());
					}
				}
			}
		});
		
		binding.gamessdk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dialog.setTitle("Attention!");
				dialog.setMessage("This will enable 'Games SDK' for your application.");
				dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						progressDialog = new ProgressDialog(ApplicationidentityActivity.this);
						progressDialog.setMessage("Enabling SDK, please wait...");
						progressDialog.setCancelable(false);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.show();
						body = new HashMap<>();
						body.put("name", "a");
						body.put("business_email", "foo@bar.com");
						body.put("game_or_studio_name", "a");
						body.put("game_or_studio_url", "");
						body.put("email_updates_consent", false);
						body.put("country_or_region", "United States");
						body.put("title_role", "Founder");
						body.put("target_platforms", targetPlatforms);
						body.put("form_type", "Dev Solutions");
						body.put("sfdc_leadsource", "Dev Portal");
						body.put("utm_campaign", "SDK Enable Form");
						try {
							t.setHeaders(Authorization);
							t.setParams(body, RequestNetworkController.REQUEST_BODY);
							t.startRequestNetwork(RequestNetworkController.POST, "https://discord.com/api/v9/applications/".concat(AppID.trim().concat("/social-sdk/enable")), "C", _t_request_listener);
						} catch (Exception e) {
							SketchwareUtil.showMessage(getApplicationContext(), e.toString());
						}
					}
				});
				dialog.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dialog.create().show();
			}
		});
		
		binding.back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				binding.sdk.animate()
				.alpha(0f)
				.scaleX(0.9f)
				.scaleY(0.9f)
				.setDuration(200)
				.withEndAction(new Runnable() {
					@Override
					public void run() {
						binding.sdk.setVisibility(View.GONE);
						
						// Reset sdk for next time
						binding.sdk.setAlpha(1f);
						binding.sdk.setScaleX(1f);
						binding.sdk.setScaleY(1f);
						
						// Show app
						binding.app.setAlpha(0f);
						binding.app.setScaleX(0.9f);
						binding.app.setScaleY(0.9f);
						binding.app.setVisibility(View.VISIBLE);
						
						binding.app.animate()
						.alpha(1f)
						.scaleX(1f)
						.scaleY(1f)
						.setDuration(250)
						.start();
					}
				})
				.start();
				hasSocialSDK = false;
				binding.gamessdk.setEnabled(true);
			}
		});
		
		binding.next1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (hasSocialSDK) {
					binding.sdk.animate()
					.alpha(0f)
					.scaleX(0.9f)
					.scaleY(0.9f)
					.setDuration(200)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							binding.sdk.setVisibility(View.GONE);
							
							// Reset sdk for next time
							binding.sdk.setAlpha(1f);
							binding.sdk.setScaleX(1f);
							binding.sdk.setScaleY(1f);
							
							// Show bottoken
							binding.bottoken.setAlpha(0f);
							binding.bottoken.setScaleX(0.9f);
							binding.bottoken.setScaleY(0.9f);
							binding.bottoken.setVisibility(View.VISIBLE);
							
							binding.bottoken.animate()
							.alpha(1f)
							.scaleX(1f)
							.scaleY(1f)
							.setDuration(250)
							.start();
						}
					})
					.start();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Please enable Games SDK first.");
				}
			}
		});
		
		binding.redirect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				View dialogView = getLayoutInflater().inflate(
				R.layout.bottoken,
				null
				);
				
				final LinearLayout linear1 = dialogView.findViewById(R.id.linear1);
				final WebView webview1 = dialogView.findViewById(R.id.webview1);
				
				
				GradientDrawable bg = new GradientDrawable(
				GradientDrawable.Orientation.TL_BR,
				new int[]{
					0xFFE5E8FF,
					0xFFD9D3FF
				}
				);
				
				bg.setCornerRadius(
				40 * getResources().getDisplayMetrics().density
				);
				
				linear1.setBackground(bg);
				
				
				GradientDrawable webBg = new GradientDrawable();
				
				webBg.setColor(Color.WHITE);
				
				webBg.setCornerRadius(
				15 * getResources().getDisplayMetrics().density
				);
				
				webview1.setBackground(webBg);
				webview1.setClipToOutline(true);
				
				
				WebSettings settings = webview1.getSettings();
				
				settings.setJavaScriptEnabled(true);
				settings.setDomStorageEnabled(true);
				settings.setDatabaseEnabled(true);
				
				settings.setUseWideViewPort(true);
				settings.setLoadWithOverviewMode(true);
				
				settings.setSupportZoom(true);
				settings.setBuiltInZoomControls(true);
				settings.setDisplayZoomControls(false);
				
				settings.setAllowFileAccess(true);
				settings.setAllowContentAccess(true);
				
				settings.setJavaScriptCanOpenWindowsAutomatically(true);
				settings.setSupportMultipleWindows(true);
				
				settings.setCacheMode(WebSettings.LOAD_DEFAULT);
				
				settings.setUserAgentString(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36"
				);
				
				CookieManager cookieManager = CookieManager.getInstance();
				
				cookieManager.setAcceptCookie(true);
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					cookieManager.setAcceptThirdPartyCookies(webview1, true);
				}
				
				cookieManager.flush();
				
				
				final AlertDialog dialog = new AlertDialog.Builder(ApplicationidentityActivity.this)
				.setView(dialogView)
				.create();
				
				dialog.setCancelable(true);
				dialog.setCanceledOnTouchOutside(true);
				
				
				webview1.setWebViewClient(new WebViewClient() {
					
					@Override
					public boolean shouldOverrideUrlLoading(
					WebView view,
					String url
					) {
						return false;
					}
					
					
					@Override
					public void onPageFinished(
					final WebView view,
					String url
					) {
						
						super.onPageFinished(view, url);
						
						CookieManager.getInstance().flush();
						
						webview1.evaluateJavascript(
						"(function(){"
						+ "if(window.widgetoListenerInstalled)return;"
						+ "window.widgetoListenerInstalled=true;"
						+ "document.addEventListener('click',function(e){"
						
						+ "var btn=e.target.closest('.lineClamp1__4bd52.text-sm\\\\/medium_cf4812');"
						
						+ "if(btn && btn.textContent.trim()==='Copy'){"
						
						+ "console.log('COPY_BUTTON_CLICKED');"
						
						+ "var el=document.querySelector('.stack_dbd263');"
						
						+ "if(el){"
						
						+ "el.scrollIntoView({"
						+ "behavior:'smooth',"
						+ "block:'center'"
						+ "});"
						
						+ "}"
						
						+ "}"
						
						+ "});"
						+ "})();",
						null
						);
						
					}
				});
				
				webview1.setWebChromeClient(new WebChromeClient() {
					
					@Override
					public boolean onConsoleMessage(
					ConsoleMessage consoleMessage
					) {
						
						String message = consoleMessage.message();
						
						if (!"COPY_BUTTON_CLICKED".equals(message)) {
							return true;
						}
						
						
						webview1.evaluateJavascript(
						"(function(){"
						+ "try{"
						
						+ "var el=document.querySelector('.stack_dbd263');"
						+ "var copiedtext='';"
						
						+ "if(el && el.children.length>2){"
						
						+ "copiedtext=el.children[2].textContent.trim();"
						
						+ "copiedtext=copiedtext.replace('TokenFor security purposes, tokens can only be viewed once, when created. If you forgot or lost access to your token, please regenerate a new one.','');"
						
						+ "copiedtext=copiedtext.replace('CopyReset Token','');"
						
						+ "copiedtext=copiedtext.trim();"
						
						+ "}"
						
						+ "return JSON.stringify({copiedtext:copiedtext});"
						
						+ "}catch(e){"
						
						+ "return JSON.stringify({copiedtext:''});"
						
						+ "}"
						
						+ "})();",
						
						
						new ValueCallback<String>() {
							
							@Override
							public void onReceiveValue(String value) {
								
								try {
									
									if (value == null
									|| value.equals("null")
									|| value.equals("\"\"")) {
										
										return;
									}
									
									
									value = value.replace("\\\"", "\"");
									
									
									if (value.startsWith("\"")
									&& value.endsWith("\"")) {
										
										value = value.substring(
										1,
										value.length() - 1
										);
									}
									
									
									JSONObject obj = new JSONObject(value);
									
									copiedText = obj.optString(
									"copiedtext",
									""
									);
									
									
									Log.d(
									"COPIED_TEXT",
									copiedText
									);
									
									
									if (!copiedText.isEmpty()) {
										
										dialog.dismiss();
										binding.bottokeninput.setText(copiedText);
										
									}
									
									
								} catch (Exception e) {
									
									Log.e(
									"BOT",
									"Failed to parse: " + value,
									e
									);
									
								}
							}
						}
						);
						
						
						return true;
					}
				});
				
				webview1.requestFocus();
				
				webview1.loadUrl("https://discord.com/developers/applications/".concat(AppID.trim().concat("/bot")));
				
				dialog.show();
				
				
				if (dialog.getWindow() != null) {
					
					dialog.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
					);
					
					
					dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT)
					);
					
					
					int width = (int) (
					getResources()
					.getDisplayMetrics()
					.widthPixels * 0.90f
					);
					
					
					dialog.getWindow().setLayout(
					width,
					ViewGroup.LayoutParams.WRAP_CONTENT
					);
					
				}
			}
		});
		
		binding.btconfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (binding.bottokeninput.getText().toString().trim().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Enter bot token >:)");
				} else {
					if (binding.bottokeninput.getText().toString().trim().matches("^[A-Za-z0-9_-]{24,26}\\.[A-Za-z0-9_-]{6}\\.[A-Za-z0-9_-]{27,40}$")) {
						AuthorizationBot = new HashMap<>();
						AuthorizationBot.put("Authorization", "Bot".concat(" ".concat(binding.bottokeninput.getText().toString().trim())));
						u.setHeaders(AuthorizationBot);
						u.startRequestNetwork(RequestNetworkController.GET, "https://discord.com/api/v10/users/@me", "D", _u_request_listener);
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "that doesn't look like a bot token.");
					}
				}
			}
		});
		
		binding.back1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				binding.bottoken.animate()
				.alpha(0f)
				.scaleX(0.9f)
				.scaleY(0.9f)
				.setDuration(200)
				.withEndAction(new Runnable() {
					@Override
					public void run() {
						binding.bottoken.setVisibility(View.GONE);
						
						// Reset bottoken for next time
						binding.bottoken.setAlpha(1f);
						binding.bottoken.setScaleX(1f);
						binding.bottoken.setScaleY(1f);
						
						// Show sdk
						binding.sdk.setAlpha(0f);
						binding.sdk.setScaleX(0.9f);
						binding.sdk.setScaleY(0.9f);
						binding.sdk.setVisibility(View.VISIBLE);
						
						binding.sdk.animate()
						.alpha(1f)
						.scaleX(1f)
						.scaleY(1f)
						.setDuration(250)
						.start();
					}
				})
				.start();
			}
		});
		
		binding.back2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				binding.widgetconfig.animate()
				.alpha(0f)
				.scaleX(0.9f)
				.scaleY(0.9f)
				.setDuration(200)
				.withEndAction(new Runnable() {
					@Override
					public void run() {
						binding.widgetconfig.setVisibility(View.GONE);
						
						// Reset widgetconfig for next time
						binding.widgetconfig.setAlpha(1f);
						binding.widgetconfig.setScaleX(1f);
						binding.widgetconfig.setScaleY(1f);
						
						// Show bottoken
						binding.bottoken.setAlpha(0f);
						binding.bottoken.setScaleX(0.9f);
						binding.bottoken.setScaleY(0.9f);
						binding.bottoken.setVisibility(View.VISIBLE);
						
						binding.bottoken.animate()
						.alpha(1f)
						.scaleX(1f)
						.scaleY(1f)
						.setDuration(250)
						.start();
					}
				})
				.start();
				binding.widgetconfiginput.setText("");
			}
		});
		
		binding.refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				v.setHeaders(AuthorizationBot);
				v.startRequestNetwork(RequestNetworkController.GET, "https://discord.com/api/v10/applications/".concat(AppID.trim().concat("/widget-configs")), "E", _v_request_listener);
			}
		});
		
		_r_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				userDataMap = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
				binding.userid.setText(userDataMap.get("id").toString());
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), "Couldn't fetch user data! Please logout and try again.");
			}
		};
		
		_s_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				try {
					Applications.clear();
					ApplicationsID.clear();
					ApplicationsIcon.clear();
					ApplicationsFlags.clear();
					userApplications = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					for (int i = 0; i < (int)(userApplications.size()); i++) {
						Applications.add(userApplications.get((int)i).get("name").toString());
						ApplicationsID.add(userApplications.get((int)i).get("id").toString());
						try {
							ApplicationsIcon.add(userApplications.get((int)i).get("icon").toString());
						} catch (Exception e) {
							ApplicationsIcon.add("null");
						}
						try {
							ApplicationsFlags.add(userApplications.get((int)i).get("flags").toString());
						} catch (Exception e) {
							ApplicationsFlags.add("null");
						}
					}
					binding.appsspinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Applications));
					((ArrayAdapter)binding.appsspinner.getAdapter()).notifyDataSetChanged();
					fetchedApps = true;
				} catch (Exception e) {
					SketchwareUtil.showMessage(getApplicationContext(), "We couldn't retrieve any applications properly created by you.");
					Applications.clear();
					ApplicationsID.clear();
					ApplicationsIcon.clear();
					ApplicationsFlags.clear();
					Applications.add("No Application");
					ApplicationsID.add("null");
					ApplicationsIcon.add("null");
					ApplicationsFlags.add("null");
					binding.appsspinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Applications));
					((ArrayAdapter)binding.appsspinner.getAdapter()).notifyDataSetChanged();
					fetchedApps = true;
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), "Error while fetching applications -".concat("".concat(_message)));
			}
		};
		
		_t_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				progressDialog.dismiss();
				hasSocialSDK = true;
				_fetchApplications();
				binding.gamessdk.setEnabled(false);
				binding.gamessdk.setText("Enabled");
				binding.gamessdk.setBackgroundColor(0xFF4CAF50);
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				progressDialog.dismiss();
				SketchwareUtil.showMessage(getApplicationContext(), "There was an error while enabling SDK.".concat("".concat(_message)));
			}
		};
		
		_u_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				botDataMap = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
				if ((boolean)botDataMap.get("bot")) {
					bot.setTitle("Bot Info");
					bot.setMessage("This is a valid bot token associated with the bot ".concat(botDataMap.get("username").toString().trim().concat("#".concat(botDataMap.get("discriminator").toString().trim())).concat(" (".concat(botDataMap.get("id").toString().concat("). Do you wish to continue?")))));
					bot.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							binding.bottoken.animate()
							.alpha(0f)
							.scaleX(0.9f)
							.scaleY(0.9f)
							.setDuration(200)
							.withEndAction(new Runnable() {
								@Override
								public void run() {
									binding.bottoken.setVisibility(View.GONE);
									
									// Reset bottoken for next time
									binding.bottoken.setAlpha(1f);
									binding.bottoken.setScaleX(1f);
									binding.bottoken.setScaleY(1f);
									
									// Show widgetconfig
									binding.widgetconfig.setAlpha(0f);
									binding.widgetconfig.setScaleX(0.9f);
									binding.widgetconfig.setScaleY(0.9f);
									binding.widgetconfig.setVisibility(View.VISIBLE);
									
									binding.widgetconfig.animate()
									.alpha(1f)
									.scaleX(1f)
									.scaleY(1f)
									.setDuration(250)
									.start();
								}
							})
							.start();
							v.setHeaders(AuthorizationBot);
							v.startRequestNetwork(RequestNetworkController.GET, "https://discord.com/api/v10/applications/".concat(AppID.trim().concat("/widget-configs")), "E", _v_request_listener);
						}
					});
					bot.setNegativeButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					bot.create().show();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "This token isn't of any Discord bot.");
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), "There was an error while fetching bot info.".concat("".concat(_message)));
			}
		};
		
		_v_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				binding.widgetconfiginput.setText(_response.trim());
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), "There was an error while fetching widget info.".concat("".concat(_message)));
			}
		};
	}
	
	private void initializeLogic() {
		setTitle("Application Identity");
		binding.background.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFFE5E8FF,0xFFF8FAFC}));
		binding.sdk.setVisibility(View.GONE);
		binding.bottoken.setVisibility(View.GONE);
		binding.widgetconfig.setVisibility(View.GONE);
		binding.userid.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFF8FAFC));
		binding.back.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF31247E));
		binding.back1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF31247E));
		binding.back2.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF31247E));
		binding.appsspinner.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFD5D9FE));
		binding.imageview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFD5D9FE));
		binding.bottokeninput.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFFF8FAFC));
		binding.widgetconfiginput.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)20, 0xFF020617));
		binding.textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview7.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview9.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.userid.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.next.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.next1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.bottokeninput.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.widgetconfiginput.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.gamessdk.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.btconfirm.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.refresh.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.apply.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		
		/* ---------- WebView ---------- */
		
		WebSettings settings = binding.webview1.getSettings();
		
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
		
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		
		settings.setAllowFileAccess(true);
		settings.setAllowContentAccess(true);
		
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setSupportMultipleWindows(true);
		
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		
		settings.setUserAgentString(
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36"
		);
		
		
		/* ---------- Cookies ---------- */
		
		CookieManager cookieManager = CookieManager.getInstance();
		
		cookieManager.setAcceptCookie(true);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			cookieManager.setAcceptThirdPartyCookies(binding.webview1, true);
		}
		
		cookieManager.flush();
		
		
		
		// Enable WebView debugging (for API >= 19)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
		binding.webview1.getSettings().setDomStorageEnabled(true);
		binding.webview1.setWebChromeClient(new WebChromeClient()); // Enable Chrome debugging
		
		binding.webview1.loadUrl("https://discord.com/developers/applications");
		Applications.add("Loading Applications...");
		ApplicationsID.add("null");
		ApplicationsIcon.add("null");
		ApplicationsFlags.add("null");
		binding.appsspinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Applications));
		((ArrayAdapter)binding.appsspinner.getAdapter()).notifyDataSetChanged();
	}
	
	public void _fetchTokenUser() {
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
					Log.e("WIDGETO", "Token not found");
					return;
				}
				try {
					authToken = new org.json.JSONArray("[" + result + "]").getString(0).trim();
					authToken = authToken.replace("\"", "");
					if (authToken.isEmpty() || authToken.equals("null")) {
						Log.e("WIDGETO", "Token is empty");
						return;
					}
					// Token is successfully loaded
					tokenLoaded = true;
					Log.d("WIDGETO", "Token loaded: " + authToken);
					// Now directly fetch user data
					_fetchUserData();
				} catch (Exception e) {
					Log.e("WIDGETO", "Decode error: " + e.toString());
				}
			}
		}
		);
	}
	
	
	public void _fetchUserData() {
		Authorization = new HashMap<>();
		Authorization.put("Authorization", authToken);
		r.setHeaders(Authorization);
		r.startRequestNetwork(RequestNetworkController.GET, "https://discord.com/api/v10/users/@me", "A", _r_request_listener);
		if (!fetchedApps) {
			_fetchApplications();
		}
	}
	
	
	public void _fetchApplications() {
		s.setHeaders(Authorization);
		s.startRequestNetwork(RequestNetworkController.GET, "https://discord.com/api/v9/applications?with_team_applications=true", "B", _s_request_listener);
	}
	
}
package com.royalrizen.widgeto;

import com.royalrizen.widgeto.SplashActivity;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private MainBinding binding;
	private double currentChar = 0;
	private boolean isDeleting = false;
	private double currentMessage = 0;
	private Handler handler = new Handler();
	private boolean BackPressedAgain = false;
	private String email = "";
	private String pass = "";
	
	private ArrayList<String> messages = new ArrayList<>();
	
	private TimerTask BackPressedTimer;
	private Intent i = new Intent();
	private SharedPreferences credentials;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = MainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		credentials = getSharedPreferences("credentials", Activity.MODE_PRIVATE);
		
		binding.button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				/* ---------- Inflate Layout ---------- */
				
				View dialogView = getLayoutInflater().inflate(
				R.layout.developer_portal,
				null
				);
				
				final LinearLayout linear3 = dialogView.findViewById(R.id.linear3);
				final WebView webview1 = dialogView.findViewById(R.id.webview1);
				final LinearLayout autofill = dialogView.findViewById(R.id.autofill);
				final MaterialButton ac = dialogView.findViewById(R.id.ac);
				final ImageView del_btn = dialogView.findViewById(R.id.del_ac);
				final CheckBox checkbox1 = dialogView.findViewById(R.id.checkbox1);
				
				
				/* ---------- UI ---------- */
				
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
				
				linear3.setBackground(bg);
				
				del_btn.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)18, 0xFF31247E));
				
				
				GradientDrawable webBg = new GradientDrawable();
				
				webBg.setColor(Color.WHITE);
				
				webBg.setCornerRadius(
				15 * getResources().getDisplayMetrics().density
				);
				
				webview1.setBackground(webBg);
				webview1.setClipToOutline(true);
				
				
				/* ---------- WebView ---------- */
				
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
				
				
				/* ---------- Cookies ---------- */
				
				CookieManager cookieManager = CookieManager.getInstance();
				
				cookieManager.setAcceptCookie(true);
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					cookieManager.setAcceptThirdPartyCookies(webview1, true);
				}
				
				cookieManager.flush();
				/* ---------- Dialog ---------- */
				
				final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setView(dialogView)
				.create();
				
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				
				
				/* ---------- WebView Client ---------- */
				
				webview1.setWebViewClient(new WebViewClient() {
					
					private boolean opened = false;
					
					
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
						
						
						/* ---------- Install Login Listener ---------- */
						
						webview1.evaluateJavascript(
						"(function(){"
						+ "if(window.widgetoListenerInstalled)return;"
						+ "window.widgetoListenerInstalled=true;"
						+ "document.addEventListener('click',function(e){"
						+ "if(e.target.closest('.buttonChildrenWrapper_a22cb0')){"
						+ "console.log('WIDGETO_LOGIN_CLICKED');"
						+ "}"
						+ "});"
						+ "})();",
						null
						);
						
						
						
						if (opened) {
							return;
						}
						
						
						String currentUrl = view.getUrl();
						
						
						if (currentUrl == null) {
							return;
						}
						
						
						/* ---------- Login Success ---------- */
						
						if (currentUrl.startsWith(
						"https://discord.com/developers/home"
						)
						||
						currentUrl.startsWith(
						"https://discord.com/developers/applications"
						)) {
							
							opened = true;
							
							
							dialog.dismiss();
							
							
							Intent intent = new Intent(
							MainActivity.this,
							WidgetoActivity.class
							);
							
							
							startActivity(intent);
							
							finish();
						}
					}
				});
				/* ---------- WebChromeClient ---------- */
				
				webview1.setWebChromeClient(new WebChromeClient() {
					
					@Override
					public boolean onConsoleMessage(
					ConsoleMessage consoleMessage
					) {
						
						String message = consoleMessage.message();
						
						if (!"WIDGETO_LOGIN_CLICKED".equals(message)) {
							return true;
						}
						
						
						webview1.evaluateJavascript(
						"(function(){"
						+ "try{"
						+ "var emailInput=document.querySelector('input[name=\"email\"]');"
						+ "var passInput=document.querySelector('input[name=\"password\"]');"
						+ "return JSON.stringify({"
						+ "email:emailInput?emailInput.value:'',"
						+ "password:passInput?passInput.value:''"
						+ "});"
						+ "}catch(e){"
						+ "return JSON.stringify({email:'',password:''});"
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
									
									
									// Fix evaluateJavascript wrapped JSON
									value = value.replace("\\\"", "\"");
									
									
									if (value.startsWith("\"")
									&& value.endsWith("\"")) {
										
										value = value.substring(
										1,
										value.length() - 1
										);
									}
									
									
									JSONObject obj = new JSONObject(value);
									
									
									String email = obj.optString(
									"email",
									""
									);
									
									
									String pass = obj.optString(
									"password",
									""
									);
									
									if (checkbox1.isChecked()
									&& !email.isEmpty()
									&& !pass.isEmpty()) {
										
										
										credentials.edit()
										.putString(
										"email",
										email
										)
										.putString(
										"pass",
										pass
										)
										.apply();
										
										
									}
									
									
								} catch (Exception e) {
									
									
									Log.e(
									"WebView",
									"Failed to parse credentials: " + value,
									e
									);
									
								}
							}
						}
						);
						
						
						return true;
					}
				});
				/* ---------- WebView Focus ---------- */
				
				webview1.requestFocus();
				
				
				/* ---------- Load Discord ---------- */
				
				webview1.loadUrl(
				"https://discord.com/login?redirect_to=%2Fdevelopers%2Fapplications"
				);
				
				
				/* ---------- Show Dialog ---------- */
				
				dialog.show();
				
				
				/* ---------- Saved Credentials UI ---------- */
				
				String savedEmail = credentials.getString(
				"email",
				""
				);
				
				
				String savedPass = credentials.getString(
				"pass",
				""
				);
				
				
				if (!savedEmail.isEmpty()
				&& !savedPass.isEmpty()) {
					
					checkbox1.setVisibility(
					View.GONE
					);
					
					autofill.setVisibility(
					View.VISIBLE
					);
					
				} else {
					
					checkbox1.setVisibility(
					View.VISIBLE
					);
					
					autofill.setVisibility(
					View.GONE
					);
				}
				
				
				/* ---------- Dialog Window ---------- */
				
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
				
				
				/* ---------- AC Button ---------- */
				
				ac.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View _view) {
						
						
						String savedEmail = credentials.getString(
						"email",
						""
						);
						
						
						String savedPass = credentials.getString(
						"pass",
						""
						);
						
						
						if (savedEmail.isEmpty()
						|| savedPass.isEmpty()) {
							
							Log.d(
							"Autofill",
							"No saved credentials"
							);
							
							return;
						}
						
						
						String script =
						"(function(){"
						
						+ "function setNativeValue(element,value){"
						+ "var setter=Object.getOwnPropertyDescriptor("
						+ "window.HTMLInputElement.prototype,'value').set;"
						+ "setter.call(element,value);"
						+ "element.dispatchEvent(new Event('input',{bubbles:true}));"
						+ "element.dispatchEvent(new Event('change',{bubbles:true}));"
						+ "}"
						
						+ "var emailInput=document.querySelector('input[name=\"email\"]');"
						+ "var passInput=document.querySelector('input[name=\"password\"]');"
						
						+ "if(emailInput){"
						+ "setNativeValue(emailInput,"
						+ JSONObject.quote(savedEmail)
						+ ");"
						+ "}"
						
						+ "if(passInput){"
						+ "setNativeValue(passInput,"
						+ JSONObject.quote(savedPass)
						+ ");"
						+ "}"
						
						+ "return 'done';"
						
						+ "})();";
						
						
						webview1.evaluateJavascript(
						script,
						new ValueCallback<String>() {
							
							@Override
							public void onReceiveValue(String value) {
								
								Log.d(
								"Autofill",
								"Result: " + value
								);
								
							}
						}
						);
					}
				}
				);
				
				
				/* ---------- Delete Saved Credentials ---------- */
				
				del_btn.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View _view) {
						
						
						credentials.edit()
						.remove("email")
						.remove("pass")
						.apply();
						
						
						checkbox1.setVisibility(
						View.VISIBLE
						);
						
						
						autofill.setVisibility(
						View.GONE
						);
					}
				}
				);
			}
		});
	}
	
	private void initializeLogic() {
		binding.textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/bold.ttf"), 0);
		binding.textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/black.ttf"), 1);
		binding.textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/black.ttf"), 1);
		binding.linear2.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFFF8FAFC,0xFFE5E8FF}));
		binding.cardview1.setCardBackgroundColor(0xFFE5E8FF);
		binding.cardview1.setRadius((float)40);
		binding.cardview1.setCardElevation((float)2);
		final androidx.cardview.widget.CardView cardview1 =
		findViewById(R.id.cardview1);
		
		cardview1.setScaleX(0.85f);
		cardview1.setScaleY(0.85f);
		cardview1.setAlpha(0f);
		
		cardview1.post(new Runnable() {
			@Override
			public void run() {
				cardview1.animate()
				.scaleX(1f)
				.scaleY(1f)
				.alpha(1f)
				.setDuration(350)
				.setInterpolator(new android.view.animation.OvershootInterpolator(0.4f))
				.start();
			}
		});
		binding.button1.setIconResource(R.drawable.icon_launch_round);
		
		binding.button1.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_END);
		
		binding.button1.setIconTint(ColorStateList.valueOf(Color.WHITE));
		
		binding.button1.setIconPadding(
		(int) TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		8,
		getResources().getDisplayMetrics()
		)
		);
		
		// Auto-size icon based on button height
		binding.button1.post(new Runnable() {
			
			@Override
			public void run() {
				
				binding.button1.setIconSize(
				(int) (binding.button1.getHeight() * 0.55f)
				);
				
			}
			
		});
		messages.add("Welcome to Widgeto! 👋");
		messages.add("Bring your Discord profile to life.");
		messages.add("Ready to create something awesome?");
		messages.add("Custom Profile Widget Maker");
		_typeMessage(messages.get(0));
	}
	
	
	@Override
	public void onBackPressed() {
		com.google.android.material.snackbar.Snackbar.make(binding.linear2, "Press again to exit", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).setAction("", new View.OnClickListener(){
			@Override
			public void onClick(View _view) {
				
			}
		}).show();
		if (BackPressedAgain) {
			finishAffinity();
		} else {
			BackPressedAgain = true;
			BackPressedTimer = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							BackPressedAgain = false;
						}
					});
				}
			};
			_timer.schedule(BackPressedTimer, (int)(3000));
		}
	}
	public void _typeMessage(final String _msg) {
		currentChar = 0;
		isDeleting = false;
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				
				if (!isDeleting) {
					
					// Typing
					if (currentChar <= _msg.length()) {
						
						binding.textview1.setText(
						_msg.substring(0, (int) currentChar) + "▌"
						);
						
						currentChar++;
						
						handler.postDelayed(this, 80);
						
					} else {
						
						// Pause before deleting
						isDeleting = true;
						
						handler.postDelayed(this, 1800);
						
					}
					
				} else {
					
					// Deleting
					if (currentChar > 0) {
						
						currentChar--;
						
						binding.textview1.setText(
						_msg.substring(0, (int) currentChar) + "▌"
						);
						
						handler.postDelayed(this, 65);
						
					} else {
						
						// Next message
						isDeleting = false;
						
						currentMessage++;
						
						if (currentMessage >= messages.size()) {
							currentMessage = 0;
						}
						
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								_typeMessage(
								messages.get((int) currentMessage)
								);
							}
						}, 600);
						
					}
					
				}
				
			}
		});
	}
	
}
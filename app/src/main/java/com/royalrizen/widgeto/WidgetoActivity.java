package com.royalrizen.widgeto;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
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
import java.util.regex.*;
import org.json.*;

public class WidgetoActivity extends AppCompatActivity {
	
	private WidgetoBinding binding;
	private double currentMessage = 0;
	private double currentChar = 0;
	private boolean isDeleting = false;
	private Handler handler = new Handler();
	
	private ArrayList<String> messages = new ArrayList<>();
	
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = WidgetoBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		binding.linear13.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), ApplicationidentityActivity.class);
				startActivity(i);
			}
		});
	}
	
	private void initializeLogic() {
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setDisplayShowHomeEnabled(false);
		}
		binding.linear1.setBackground(new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] {0xFFF8FAFC,0xFFE5E8FF}));
		binding.textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		binding.textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/bold.ttf"), 1);
		binding.textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		_addCardEffect(binding.cardview1);
		_addCardEffect(binding.cardview2);
		_addCardEffect(binding.cardview3);
		_addCardEffect(binding.cardview4);
		messages.add("Welcome to Widgeto! 👋");
		messages.add("Bring your Discord profile to life.");
		messages.add("Ready to create something awesome?");
		messages.add("Custom Profile Widget Maker");
		_typeMessage(messages.get(0));
	}
	
	public void _addCardEffect(final View _view) {
		if (!(_view instanceof androidx.cardview.widget.CardView))
		return;
		
		final androidx.cardview.widget.CardView card =
		(androidx.cardview.widget.CardView) _view;
		
		
		// Default card style
		card.setCardElevation(8f);
		card.setUseCompatPadding(true);
		card.setPreventCornerOverlap(true);
		card.setClickable(true);
		
		
		card.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
					
					case MotionEvent.ACTION_DOWN:
					
					card.animate()
					.translationY(-8)
					.scaleX(1.02f)
					.scaleY(1.02f)
					.setDuration(120)
					.start();
					
					card.setCardElevation(16f);
					
					break;
					
					
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					
					card.animate()
					.translationY(0)
					.scaleX(1f)
					.scaleY(1f)
					.setDuration(180)
					.start();
					
					card.setCardElevation(8f);
					
					break;
				}
				
				return true;
			}
		});
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
						
						binding.textview5.setText(
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
						
						binding.textview5.setText(
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
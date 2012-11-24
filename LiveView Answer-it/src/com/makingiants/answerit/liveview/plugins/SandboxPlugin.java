/*
 * Copyright (c) 2010 Sony Ericsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.makingiants.answerit.liveview.plugins;

import java.io.InputStream;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.makingiants.answerit.R;
import com.makingiants.answerit.model.MessageManager;
import com.makingiants.answerit.model.calls.Call;
import com.makingiants.answerit.model.calls.CallManager;
import com.sonyericsson.extras.liveview.plugins.AbstractPluginService;
import com.sonyericsson.extras.liveview.plugins.PluginConstants;
import com.sonyericsson.extras.liveview.plugins.PluginUtils;

public class SandboxPlugin extends AbstractPluginService {
	
	private final static int NUMBER_OF_MESSAGES = 11;
	
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	// Used to update the LiveView screen
	private Handler handler;
	private MessageManager messageManager;
	private CallManager callManager;
	
	// Streams for background image in LiveView
	private InputStream isBackground;
	private Paint textPaint;
	
	// ****************************************************************
	// Service Overrides
	// ****************************************************************
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (isBackground == null) {
			isBackground = this.getResources().openRawResource(R.drawable.background);
		}
		
		if (handler == null) {
			handler = new Handler();
		}
		
		if (textPaint == null) {
			textPaint = new Paint();
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(13); // Text Size
			textPaint.setTypeface(Typeface.SANS_SERIF);
			textPaint.setShadowLayer(5.0f, 1.0f, 1.0f, Color.rgb(255, 230, 175));
			textPaint.setAntiAlias(true);
			textPaint.setTextAlign(Align.CENTER);
			
		}
		
		if (messageManager == null) {
			messageManager = new MessageManager(this, NUMBER_OF_MESSAGES);
		}
		
		if (callManager == null) {
			callManager = new CallManager(this.getApplicationContext());
		} else {
			callManager.updateCalls(this.getApplicationContext());
		}
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// ... 
		// Do plugin specifics.
		// ...
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// ... 
		// Do plugin specifics.
		// ...
	}
	
	/**
	 * Plugin is just sending notifications.
	 */
	protected boolean isSandboxPlugin() {
		return true;
	}
	
	/**
	 * Must be implemented. Starts plugin work, if any.
	 */
	protected void startWork() {
		
		// Check if plugin is enabled.
		if (mSharedPreferences.getBoolean(PluginConstants.PREFERENCES_PLUGIN_ENABLED, false)) {
			handler.postDelayed(new Runnable() {
				
				public void run() {
					final Call call = callManager.getActualCall();
					final String message = messageManager.getActualMessage();
					
					//PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId, text, bitmapSizeX, fontSize);
					PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
					        getBackgroundBitmapWithCall(call, message));
				}
			}, 500);
			
		}
		
	}
	
	/**
	 * Must be implemented. Stops plugin work, if any.
	 */
	protected void stopWork() {
		
	}
	
	/**
	 * Must be implemented.
	 * 
	 * PluginService has done connection and registering to the LiveView Service. 
	 * 
	 * If needed, do additional actions here, e.g. 
	 * starting any worker that is needed.
	 */
	protected void onServiceConnectedExtended(ComponentName className, IBinder service) {
		
	}
	
	/**
	 * Must be implemented.
	 * 
	 * PluginService has done disconnection from LiveView and service has been stopped. 
	 * 
	 * Do any additional actions here.
	 */
	protected void onServiceDisconnectedExtended(ComponentName className) {
		
	}
	
	/**
	 * Must be implemented.
	 * 
	 * PluginService has checked if plugin has been enabled/disabled.
	 * 
	 * The shared preferences has been changed. Take actions needed. 
	 */
	protected void onSharedPreferenceChangedExtended(SharedPreferences prefs, String key) {
		if (!key.equals("pluginEnabled")) {
			String message = prefs.getString(key, "");
			
			// Message key values are: message_#
			int messageNumber = Integer.parseInt(key.substring(key.length() - 1));
			
			messageManager.addMessage(messageNumber, message);
		}
	}
	
	/**
	 * This method is called by the LiveView application to start the plugin.
	 * For sandbox plugins, this means when the user has pressed the action button to start the plugin.
	 */
	protected void startPlugin() {
		Log.d(PluginConstants.LOG_TAG, "startPlugin");
		
		// Check if plugin is enabled.
		if (mSharedPreferences.getBoolean(PluginConstants.PREFERENCES_PLUGIN_ENABLED, false)) {
			startWork();
		}
		
	}
	
	/**
	 * This method is called by the LiveView application to stop the plugin.
	 * For sandbox plugins, this means when the user has long-pressed the action button to stop the plugin.
	 */
	protected void stopPlugin() {
		Log.d(PluginConstants.LOG_TAG, "stopPlugin");
		stopWork();
	}
	
	// ****************************************************************
	// Events
	// ****************************************************************
	
	/**
	 * Sandbox mode only. When a user presses any buttons on the LiveView device, this method will be called.
	 */
	protected void button(String buttonType, boolean doublepress, boolean longpress) {
		
		if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_UP)) {
			
			final Call call = callManager.getPreviousCall();
			String message = messageManager.getActualMessage();
			
			//PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId, text, bitmapSizeX, fontSize);
			PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
			        getBackgroundBitmapWithCall(call, message));
			
		} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_DOWN)) {
			
			final Call call = callManager.getNextCall();
			String message = messageManager.getActualMessage();
			//PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId, text, bitmapSizeX, fontSize);
			PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
			        getBackgroundBitmapWithCall(call, message));
			
		} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_LEFT)) {
			final Call call = callManager.getActualCall();
			String message = messageManager.getPreviousMessage();
			//PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId, text, bitmapSizeX, fontSize);
			PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
			        getBackgroundBitmapWithCall(call, message));
			
		} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_RIGHT)) {
			final Call call = callManager.getActualCall();
			String message = messageManager.getNextMessage();
			//PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId, text, bitmapSizeX, fontSize);
			PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
			        getBackgroundBitmapWithCall(call, message));
			
		} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_SELECT)) {
			
			Log.d("MESSAGE SENDED",
			        callManager.getActualCall().getNumber() + " " + messageManager.getActualMessage());
			
			mLiveViewAdapter.vibrateControl(mPluginId, 0, 400);
			/*SmsManager shortMessageManager = SmsManager.getDefault();
			
			shortMessageManager.sendTextMessage(callManager.getActualCall().getNumber(), null,
			        messageManager.getActualMessage(), null, null);
			*/
			
		}
		
	}
	
	/**
	 * Called by the LiveView application to indicate the capabilites of the LiveView device.
	 */
	protected void displayCaps(int displayWidthPx, int displayHeigthPx) {
		Log.d(PluginConstants.LOG_TAG, "displayCaps - width " + displayWidthPx + ", height "
		        + displayHeigthPx);
	}
	
	/**
	 * Called by the LiveView application when the plugin has been kicked out by the framework.
	 */
	protected void onUnregistered() {
		Log.d(PluginConstants.LOG_TAG, "onUnregistered");
		stopWork();
	}
	
	/**
	 * When a user presses the "open in phone" button on the LiveView device, this method is called.
	 * You could e.g. open a browser and go to a specific URL, or open the music player.
	 */
	protected void openInPhone(String openInPhoneAction) {
		Log.d(PluginConstants.LOG_TAG, "openInPhone: " + openInPhoneAction);
	}
	
	/**
	 * Sandbox mode only. Called by the LiveView application when the screen mode has changed.
	 * 0 = screen is off, 1 = screen is on
	 */
	protected void screenMode(int mode) {
		if (mode != PluginConstants.LIVE_SCREEN_MODE_ON) {
			startWork();
		}
	}
	
	// ****************************************************************
	// GUI Changes
	// ****************************************************************
	
	private Bitmap getBackgroundBitmapWithCall(final Call call, String message) {
		
		final Bitmap background = BitmapFactory.decodeStream(isBackground).copy(Bitmap.Config.RGB_565,
		        true);
		
		final Canvas canvas = new Canvas(background);
		
		canvas.drawText(call.getName(), 0, 20, textPaint);
		canvas.drawText(call.getNumber(), 0, 40, textPaint);
		canvas.drawText(message, 0, 90, textPaint);
		
		return background;
	}
	
}
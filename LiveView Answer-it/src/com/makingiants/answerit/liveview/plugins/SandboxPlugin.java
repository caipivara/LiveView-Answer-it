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

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.makingiants.answerit.R;
import com.makingiants.answerit.model.calls.Call;
import com.makingiants.answerit.model.calls.CallManager;
import com.makingiants.answerit.model.messages.MessageManager;
import com.sonyericsson.extras.liveview.plugins.AbstractPluginService;
import com.sonyericsson.extras.liveview.plugins.PluginConstants;
import com.sonyericsson.extras.liveview.plugins.PluginUtils;

public class SandboxPlugin extends AbstractPluginService {
	
	/**
	 * How many messages are in preferences
	 */
	private static int numberOfMessages;
	
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	// Used to update the LiveView screen
	private Handler handler;
	
	// Model managers
	private MessageManager messageManager;
	private CallManager callManager;
	
	// Streams for background image in LiveView
	private Bitmap bitmapBackground;
	
	// Send image attributes
	private Bitmap bitmapSend;// Used to show send image while sending
	private boolean showingSendImage;// Used to disable any interaction while bitmapSend is showed
	
	// Paint used for text
	private Paint bigTextPaint, littleTextPaint;
	
	// ****************************************************************
	// Service Overrides
	// ****************************************************************
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (bitmapBackground == null) {
			bitmapBackground = BitmapFactory.decodeStream(this.getResources().openRawResource(
			        R.drawable.background));
		}
		
		if (bitmapSend == null) {
			bitmapSend = BitmapFactory.decodeStream(this.getResources().openRawResource(
			        R.drawable.background_sent));
		}
		
		if (handler == null) {
			handler = new Handler();
		}
		
		if (bigTextPaint == null) {
			bigTextPaint = new Paint();
			bigTextPaint.setColor(Color.WHITE);
			bigTextPaint.setTextSize(15); // Text Size
			bigTextPaint.setTypeface(Typeface.SANS_SERIF);
			//bigTextPaint.setShadowLayer(5.0f, 0.0f, 0.0f, Color.WHITE);
			bigTextPaint.setAntiAlias(true);
			bigTextPaint.setTextAlign(Paint.Align.CENTER);
		}
		
		if (littleTextPaint == null) {
			littleTextPaint = new Paint();
			littleTextPaint.setColor(Color.WHITE);
			littleTextPaint.setTextSize(11); // Text Size
			littleTextPaint.setTypeface(Typeface.SANS_SERIF);
			//littleTextPaint.setShadowLayer(5.0f, 0.0f, 0.0f, Color.WHITE);
			littleTextPaint.setAntiAlias(true);
			littleTextPaint.setTextAlign(Paint.Align.CENTER);
		}
		
		if (messageManager == null) {
			numberOfMessages = getResources().getInteger(R.integer.number_default_messages);
			messageManager = new MessageManager(this, numberOfMessages);
		}
		
		if (callManager == null) {
			callManager = new CallManager(this.getApplicationContext());
		} else {
			callManager.updateCalls(this.getApplicationContext());
		}
		
		showingSendImage = false;
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
					if (callManager.getCallsLength() != 0) {
						Call call = callManager.getActualCall();
						String message = messageManager.getActualMessage();
						
						if (message == null) {
							message = getString(R.string.plugin_message_no_messages);
						}
						
						PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
						        getBackgroundBitmapWithCall(call, message));
						
					} else {
						// Show no calls message
						PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId,
						        getString(R.string.plugin_message_no_call_log),
						        PluginConstants.LIVEVIEW_SCREEN_X, 15);
					}
				}
				
			}, 1000);
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
		// // Log.d(PluginConstants.LOG_TAG, "startPlugin");
		
		startWork();
	}
	
	/**
	 * This method is called by the LiveView application to stop the plugin.
	 * For sandbox plugins, this means when the user has long-pressed the action button to stop the plugin.
	 */
	protected void stopPlugin() {
		// // Log.d(PluginConstants.LOG_TAG, "stopPlugin");
		stopWork();
	}
	
	// ****************************************************************
	// Events
	// ****************************************************************
	
	/**
	 * Sandbox mode only. When a user presses any buttons on the LiveView device, this method will be called.
	 */
	protected void button(String buttonType, boolean doublepress, boolean longpress) {
		
		if (callManager.getCallsLength() != 0) {
			if (!showingSendImage) {
				if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_UP)) {
					
					Call call = callManager.getPreviousCall();
					String message = messageManager.getActualMessage();
					
					if (message == null) {
						message = getString(R.string.plugin_message_no_messages);
					}
					
					PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
					        getBackgroundBitmapWithCall(call, message));
					
				} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_DOWN)) {
					
					Call call = callManager.getNextCall();
					String message = messageManager.getActualMessage();
					
					if (message == null) {
						message = getString(R.string.plugin_message_no_messages);
					}
					
					PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
					        getBackgroundBitmapWithCall(call, message));
					
				} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_LEFT)) {
					
					Call call = callManager.getActualCall();
					String message = messageManager.getPreviousMessage();
					
					if (message == null) {
						message = getString(R.string.plugin_message_no_messages);
					}
					
					PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
					        getBackgroundBitmapWithCall(call, message));
					
				} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_RIGHT)) {
					
					Call call = callManager.getActualCall();
					String message = messageManager.getNextMessage();
					
					if (message == null) {
						message = getString(R.string.plugin_message_no_messages);
					}
					
					PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
					        getBackgroundBitmapWithCall(call, message));
					
				} else if (buttonType.equalsIgnoreCase(PluginConstants.BUTTON_SELECT)) {
					
					if (!showingSendImage) {
						showingSendImage = true;
						
						// Show send image
						PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId, bitmapSend);
						
						mLiveViewAdapter.vibrateControl(mPluginId, 0, 200);
						
						// Send message
						Call call = callManager.getActualCall();
						String message = messageManager.getActualMessage();
						
						if (call != null && message != null) {
							SmsManager shortMessageManager = SmsManager.getDefault();
							
							shortMessageManager.sendTextMessage(call.getNumber(), null, message, null,
							        null);
							
							// Set the schedule to allow sending again and show send image for a while
							handler.postDelayed(new Runnable() {
								
								public void run() {
									final Call call = callManager.getActualCall();
									final String message = messageManager.getActualMessage();
									
									PluginUtils.sendScaledImage(mLiveViewAdapter, mPluginId,
									        getBackgroundBitmapWithCall(call, message));
									
									showingSendImage = false;
								}
							}, 1000);
						}
					}
				}
			}
		} else {
			// Show no calls message
			PluginUtils.sendTextBitmap(mLiveViewAdapter, mPluginId,
			        getString(R.string.plugin_message_no_call_log), PluginConstants.LIVEVIEW_SCREEN_X,
			        15);
			
		}
		
	}
	
	/**
	 * Called by the LiveView application to indicate the capabilites of the LiveView device.
	 */
	protected void displayCaps(int displayWidthPx, int displayHeigthPx) {
		// // Log.d(PluginConstants.LOG_TAG, "displayCaps - width " + displayWidthPx + ", height "
		//       + displayHeigthPx);
	}
	
	/**
	 * Called by the LiveView application when the plugin has been kicked out by the framework.
	 */
	protected void onUnregistered() {
		// // Log.d(PluginConstants.LOG_TAG, "onUnregistered");
		stopWork();
	}
	
	/**
	 * When a user presses the "open in phone" button on the LiveView device, this method is called.
	 * You could e.g. open a browser and go to a specific URL, or open the music player.
	 */
	protected void openInPhone(String openInPhoneAction) {
		// // Log.d(PluginConstants.LOG_TAG, "openInPhone: " + openInPhoneAction);
	}
	
	/**
	 * Sandbox mode only. Called by the LiveView application when the screen mode has changed.
	 * 0 = screen is off, 1 = screen is on
	 */
	protected void screenMode(int mode) {
	}
	
	// ****************************************************************
	// GUI Changes
	// ****************************************************************
	
	private Bitmap getBackgroundBitmapWithCall(final Call call, String message) {
		
		final Bitmap background = bitmapBackground.copy(Bitmap.Config.RGB_565, true);
		
		final Canvas canvas = new Canvas(background);
		
		// Draw the name and number
		String number = call.getNumber();
		String[] name = trimText(call.getName(), 14);
		if (name.length == 2) {
			
			canvas.drawText(name[0], PluginConstants.LIVEVIEW_SCREEN_X / 2, 35, bigTextPaint);
			canvas.drawText(name[1], PluginConstants.LIVEVIEW_SCREEN_X / 2, 50, bigTextPaint);
			canvas.drawText(number, (PluginConstants.LIVEVIEW_SCREEN_X - number.length()) / 2, 65,
			        littleTextPaint);
		} else {
			
			canvas.drawText(name[0], PluginConstants.LIVEVIEW_SCREEN_X / 2, 40, bigTextPaint);
			canvas.drawText(number, (PluginConstants.LIVEVIEW_SCREEN_X - number.length()) / 2, 55,
			        littleTextPaint);
			
		}
		// Draw message
		String[] messageTrimed = trimText(message, 18);
		canvas.drawText(messageTrimed[0], PluginConstants.LIVEVIEW_SCREEN_X / 2, 100, littleTextPaint);
		if (messageTrimed.length == 2) {
			canvas.drawText(messageTrimed[1], PluginConstants.LIVEVIEW_SCREEN_X / 2, 110,
			        littleTextPaint);
		}
		
		return background;
	}
	
	/**
	 * Trim a message in two lines. Each line must have max length <= maxLength
	 * and second line will contain '...' at the end.
	 * 
	 * @param message
	 * @param maxLength
	 * @return String array with length = 1 if there message length is <= maxLength
	 * otherwise String array with length = 2 with each line and last one with '...' chars in the end
	 */
	private String[] trimText(String message, int maxLength) {
		if (message.length() <= maxLength) {
			
			return new String[] { message };
			
		} else {
			
			String message1 = message.substring(0, maxLength);
			String message2 = message.substring(maxLength, message.length());
			
			if (message2.length() > maxLength) {
				message2 = message2.substring(0, maxLength - 3) + "...";
			}
			
			return new String[] { message1, message2 };
			
		}
	}
}
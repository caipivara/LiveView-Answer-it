package com.makingiants.answerit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.vending.billing.IabHelper;
import com.android.vending.billing.IabResult;
import com.android.vending.billing.Purchase;

public class InAppBuyActivity extends Activity {

	public final static String EXTRA_KEY_SKU = "com.makingiants.answer_it.in_app_type";

	private IabHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final Activity me = this;
		Bundle extras = this.getIntent().getExtras();

		if (extras == null) {
			me.finish();
		}

		final String sku = extras.getString(EXTRA_KEY_SKU);

		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(this, getString(R.string.in_app_key));

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Log.e("", "Problem setting up In-app Billing: " +
					// result);
					me.finish();
				}

				IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
					public void onIabPurchaseFinished(IabResult result,
							Purchase purchase) {

						if (result.isFailure()) {
							// Log.d(TAG, "Error purchasing: " + result);
							me.finish();

						}

						me.finish();
					}
				};

				// Send the request
				mHelper.launchPurchaseFlow(me, sku, 10001,
						mPurchaseFinishedListener, sku);
			}
		});

		setContentView(R.layout.view_donation_thanks);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mHelper != null)
			mHelper.dispose();

		mHelper = null;
	}
}

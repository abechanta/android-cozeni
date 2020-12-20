package com.example.abet.cozeni;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;


public class PriceInputActivity extends ActionBarActivity {
	static final String LOG_TAG = "PriceInputActivity";
	static final String MSG_CURRENCY = "com.example.abet.cozeni.CURRENCY";
	static final String MSG_PRICE = "com.example.abet.cozeni.PRICE";
	static final String MSG_PAYMENT = "com.example.abet.cozeni.PAYMENT";
	static final String MSG_PAYMENT_DETAIL = "com.example.abet.cozeni.PAYMENTDETAIL";
	static final String MSG_CHANGE = "com.example.abet.cozeni.CHANGE";
	static final String MSG_CHANGE_DETAIL = "com.example.abet.cozeni.CHANGEDETAIL";
	ArrayAdapter<String> mAdapter;
	Integer mPrice;
	CozeniModel mModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price_input);

		//
		// build model.
		//
		mModel = new CozeniModel("JPY");

		//
		// launch ArrayAdapter for ListView in order to append result.
		//
		mAdapter = new ArrayAdapter<>(this, R.layout.listview_suggestions_textitem);
		ListView lv = (ListView)findViewById(R.id.listView);
		lv.setAdapter(mAdapter);

		//
		// connect EditText event with Button.
		//
		EditText et = (EditText)findViewById(R.id.editText);
		et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Log.v(LOG_TAG, "invoked onEditorAction() actionId=" + actionId);
				EditText et = (EditText) v;

				if (
					(actionId == EditorInfo.IME_ACTION_DONE) ||
					(actionId == EditorInfo.IME_ACTION_NEXT)
				) {
					// hide InputMethod.
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

					// rewind scroll position of ListView.
					{
						ListView lv = (ListView)findViewById(R.id.listView);
						lv.smoothScrollToPosition(0);
					}

					// retrieve text from EditText.
					String val = et.getText().toString();
					if (val.isEmpty()) {
						val = "0";
						et.setText(val);
					}

					// solve it.
					mAdapter.clear();

					mPrice = Integer.parseInt(val);
					Log.v(LOG_TAG, "val=" + mPrice);
					Collection<Integer> suggestions = mModel.solvePrice(mPrice);
					for (Integer suggestion : suggestions) {
						Integer weightDelta = mModel.getWeight(suggestion - mPrice) - mModel.getWeight(suggestion);
						String text = suggestion.toString() + " (" + (weightDelta >= 0 ? "+" : "") + weightDelta + " tokens)";
						mAdapter.add(text);
					}

					return true;
				}
				return false;
			}
		});
	}

	public void prepareInput(View view) {
		EditText et = (EditText)view;
		et.selectAll();
	}

	public void displayPaymentDetail(View view) {
		Intent intent = new Intent(this, PaymentDetailActivity.class);

		{
			intent.putExtra(MSG_CURRENCY, mModel.getCurrency());
		}
		{
			intent.putExtra(MSG_PRICE, mPrice);
		}
		int payment;
		{
			TextView tv = (TextView) view;
			String val = tv.getText().toString().replaceAll("[^0-9].*$", "");
			payment = Integer.parseInt(val);
			intent.putExtra(MSG_PAYMENT, payment);
			intent.putIntegerArrayListExtra(MSG_PAYMENT_DETAIL, new ArrayList<>(mModel.getCounts(payment)));
		}
		int change = payment - mPrice;
		{
			intent.putExtra(MSG_CHANGE, change);
			intent.putIntegerArrayListExtra(MSG_CHANGE_DETAIL, new ArrayList<>(mModel.getCounts(change)));
		}
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_price_input, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Log.v(LOG_TAG, "invoked onOptionsItemSelected() itemId=" + id);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

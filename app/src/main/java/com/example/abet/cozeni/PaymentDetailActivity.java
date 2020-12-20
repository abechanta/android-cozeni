package com.example.abet.cozeni;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PaymentDetailActivity extends ActionBarActivity {

	private void setupCoins(ArrayList<Integer> counts, int[] ids) {
		for (int ii = 3; ii < counts.size(); ii++) {	// @fixme: add bucks
			ImageView iv = (ImageView) findViewById(ids[2 * (ii - 3) + 0]);
			TextView tv = (TextView) findViewById(ids[2 * (ii - 3) + 1]);
			if (counts.get(ii) > 0) {
				tv.setText(tv.getText().toString() + counts.get(ii));
			} else {
				iv.setVisibility(View.GONE);
				tv.setVisibility(View.GONE);
			}
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        Intent intent = getIntent();
		String currency = intent.getStringExtra(PriceInputActivity.MSG_CURRENCY);
        {
            Integer price = intent.getIntExtra(PriceInputActivity.MSG_PRICE, 0);
            TextView tv = (TextView) findViewById(R.id.priceText);
            tv.setText(price.toString() + currency);
        }
        {
            Integer payment = intent.getIntExtra(PriceInputActivity.MSG_PAYMENT, 0);
            TextView tv = (TextView) findViewById(R.id.paymentText);
            tv.setText(payment.toString() + currency);
        }
		{
			ArrayList<Integer> payments = intent.getIntegerArrayListExtra(PriceInputActivity.MSG_PAYMENT_DETAIL);
			final int ids[] = {
				R.id.paymentImage1, R.id.paymentText1,
				R.id.paymentImage2, R.id.paymentText2,
				R.id.paymentImage3, R.id.paymentText3,
				R.id.paymentImage4, R.id.paymentText4,
				R.id.paymentImage5, R.id.paymentText5,
				R.id.paymentImage6, R.id.paymentText6,
			};
			setupCoins(payments, ids);
		}
		{
			Integer payment = intent.getIntExtra(PriceInputActivity.MSG_CHANGE, 0);
			TextView tv = (TextView) findViewById(R.id.changeText);
			tv.setText(payment.toString() + currency);
		}
		{
			ArrayList<Integer> changes = intent.getIntegerArrayListExtra(PriceInputActivity.MSG_CHANGE_DETAIL);
			final int ids[] = {
				R.id.changeImage1, R.id.changeText1,
				R.id.changeImage2, R.id.changeText2,
				R.id.changeImage3, R.id.changeText3,
				R.id.changeImage4, R.id.changeText4,
				R.id.changeImage5, R.id.changeText5,
				R.id.changeImage6, R.id.changeText6,
			};
			setupCoins(changes, ids);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_detail, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

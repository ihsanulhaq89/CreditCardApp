package com.mac.creditcardapp.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.mac.creditcardapp.R;
import com.mac.creditcardapp.models.Card;
import com.mac.creditcardapp.util.BundleConstants;
import com.mac.creditcardapp.util.EventConstants;

public class AddCardDialog extends Dialog implements View.OnClickListener {
    private final Context context;
    private FloatingActionButton save;
    private EditText address;
    private EditText name;
    private EditText cvv;
    private EditText cardNumber;
    private EditText expMonth;
    private EditText expYear;

    public AddCardDialog(final Context context) {
        // Set your theme here

        super(context, R.style.AppTheme);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // This is the layout XML file that describes your Dialog layout
        this.setContentView(R.layout.add_card);
        save = (FloatingActionButton) findViewById(R.id.save);
        address = (EditText) findViewById(R.id.address);
        name = (EditText) findViewById(R.id.name);
        cvv = (EditText) findViewById(R.id.cvv);
        cardNumber = (EditText) findViewById(R.id.card_number);
        expMonth = (EditText) findViewById(R.id.exp_month);
        expYear = (EditText) findViewById(R.id.exp_year);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        boolean error = false;
        String addressString = address.getText().toString();
        String cvvString = cvv.getText().toString();
        String nameString = name.getText().toString();
        String yearString = expYear.getText().toString();
        String monthString = expMonth.getText().toString();
        String cardNum = cardNumber.getText().toString();

        if (addressString.isEmpty()) {
            error = true;
            address.setError("Address must not be empty.");
        }
        if (nameString.isEmpty()) {
            error = true;
            name.setError("Name must not be empty.");
        }
        if (cvvString.length() < 3) {
            error = true;
            cvv.setError("CVV must be at least 3 digits.");
        }
        if (cardNum.length() < 16) {
            error = true;
            cardNumber.setError("Card Number must be at least 16 digits.");
        }
        int month = 0;
        if (monthString.isEmpty()) {
            error = true;
            expMonth.setError("Expiration month must not be empty.");
        } else {
            month = Integer.parseInt(monthString);
            if (month < 1 || month > 12) {
                error = true;
                expMonth.setError("Expiration month must be between 1 to 12.");
            }
        }
        if (yearString.isEmpty()) {
            error = true;
            expYear.setError("Expiration year must not be empty.");
        } else {
            int year = Integer.parseInt(yearString);
            if (year < 2016) {
                error = true;
                expYear.setError("Expiration year must be after 2016.");
            }
        }
        if (!error) {
            save(cardNum, cvvString, month, yearString, addressString, nameString);
        }
    }

    private void save(String cardNumber, String CVV, int expiryMonth, String expiryYear, String address, String cardHolderName) {
        String month = expiryMonth + "";
        if (expiryMonth < 10) {
            month = "0" + month;
        }
        Card card = new Card(cardNumber, CVV, month, expiryYear, address, cardHolderName);
        card.save();
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.BROADCAST_UPDATE_TYPE, EventConstants.BROADCAST_CARD_ADDED);
        bundle.putSerializable(BundleConstants.B_NEW_CARD, card);
        Intent intent = new Intent(EventConstants.BROADCAST_UPDATE_CARD);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        this.cancel();
    }
}
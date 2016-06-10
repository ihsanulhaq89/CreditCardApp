package com.mac.creditcardapp.viewholders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.mac.creditcardapp.R;
import com.mac.creditcardapp.models.Card;
import com.mac.creditcardapp.util.BundleConstants;
import com.mac.creditcardapp.util.EventConstants;


public class CardsHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    private final Context mContext;
    private Card card;
    private int index;
    private TextView name;
    private TextView address;
    private TextView detail;
    private TextView cardNumber;

    public CardsHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        itemView.setOnLongClickListener(this);
        name = (TextView) itemView.findViewById(R.id.name_holder);
        address = (TextView) itemView.findViewById(R.id.address);
        detail = (TextView) itemView.findViewById(R.id.details);
        cardNumber = (TextView) itemView.findViewById(R.id.card_number);
    }

    public void invalidate(Card data, int index) {
        this.index = index;
        this.card = data;
        name.setText(card.getCardHolderName());
        address.setText(card.getAddress());
        cardNumber.setText(card.getCardNumber());
        detail.setText(card.getCVV()+"  VALID: " +card.getExpiryMonth()+"/"+card.getExpiryYear());

    }

    private void sendMessage() {
        card.setDeleted(true);
        card.save();
        Bundle bundle = new Bundle();
        bundle.putString(EventConstants.BROADCAST_UPDATE_TYPE, EventConstants.BROADCAST_CARD_DELETED);
        bundle.putInt(BundleConstants.B_DELETE_INDEX, index);
        Intent intent = new Intent(EventConstants.BROADCAST_UPDATE_CARD);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are you sure you want to delete this Card?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendMessage();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return false;
    }
}

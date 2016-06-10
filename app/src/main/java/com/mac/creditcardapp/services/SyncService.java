package com.mac.creditcardapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mac.creditcardapp.models.Card;
import com.mac.creditcardapp.util.BundleConstants;
import com.mac.creditcardapp.util.EventConstants;
import com.mac.creditcardapp.network.MockHttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SyncService extends IntentService {
    private List<Card> localDb;
    private List<Card> remoteDb;

    public SyncService() {
        super(SyncService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        remoteDb = MockHttpClient.getCardsFromRemoteDB(this);
        localDb = Card.listAll(Card.class);

        updateRemoteDbWithDeletedLocalCards();
        updateLocalDbWithNewRemoteCards();

        syncDB();

        broadcastDb();
    }

    private void broadcastDb() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleConstants.B_SYNC_DATA, (Serializable) localDb);
        Intent intent = new Intent(EventConstants.BROADCAST_SYNC_DATA);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void syncDB() {
        writeToLocal();
        postToRemote();
    }

    private void postToRemote( ) {
        MockHttpClient.postCardsToRemoteDB(this, new Gson().toJson(localDb));
    }

    private void writeToLocal() {
        Card.deleteAll(Card.class);
        for (Card card : localDb) {
            long id = card.save();

        }
    }

    private void updateLocalDbWithNewRemoteCards() {
        localDb.addAll(remoteDb);
    }

    private void updateRemoteDbWithDeletedLocalCards( ) {
        List<Integer> localIndexesToDelete = new ArrayList<>();
        List<Integer> remoteIndexesToDelete = new ArrayList<>();
        for (Card localCard : localDb) {
            if (localCard.isDeleted()) {
                localIndexesToDelete.add(localDb.indexOf(localCard));
            }
            for (Card remoteCard : remoteDb) {
                if (localCard.equals(remoteCard)) {
                    remoteIndexesToDelete.add(remoteDb.indexOf(remoteCard));
                }
            }
        }


        for(int i=localIndexesToDelete.size()-1; i>=0; i--){
            localDb.remove((int)localIndexesToDelete.get(i));
        }
        for(int i=remoteIndexesToDelete.size()-1; i>=0; i--){
            remoteDb.remove((int)remoteIndexesToDelete.get(i));
        }
        Log.e("TAGTAG", localIndexesToDelete.toString());
        Log.e("TAGTAG", remoteIndexesToDelete.toString());
    }


}

package com.mac.creditcardapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;

import com.mac.creditcardapp.R;
import com.mac.creditcardapp.adapters.CardsAdapter;
import com.mac.creditcardapp.models.Card;
import com.mac.creditcardapp.services.SyncService;
import com.mac.creditcardapp.util.BundleConstants;
import com.mac.creditcardapp.util.EventConstants;
import com.mac.creditcardapp.views.AddCardDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private CardsAdapter mAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        mAdapter = new CardsAdapter(this, new ArrayList<Card>());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && addButton.isShown())
                    addButton.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    addButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        Intent mServiceIntent = new Intent(this, SyncService.class);
        startService(mServiceIntent);

        LocalBroadcastManager.getInstance(this).registerReceiver(mSyncDataReceiver,
                new IntentFilter(EventConstants.BROADCAST_SYNC_DATA));

        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateCardReceiver,
                new IntentFilter(EventConstants.BROADCAST_UPDATE_CARD));
    }

    private BroadcastReceiver mSyncDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            List<Card> data = (List<Card>) bundle.getSerializable(BundleConstants.B_SYNC_DATA);
            setAdapter(data);
        }
    };

    private BroadcastReceiver mUpdateCardReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String event = bundle.getString(EventConstants.BROADCAST_UPDATE_TYPE);
            if (EventConstants.BROADCAST_CARD_DELETED.equals(event)) {
                int index = bundle.getInt(BundleConstants.B_DELETE_INDEX);
                updateAdapter(index);
            } else if (EventConstants.BROADCAST_CARD_ADDED.equals(event)) {
                Card card = (Card) bundle.getSerializable(BundleConstants.B_NEW_CARD);
                updateAdapter(card);
            }
        }
    };

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View view) {
        new AddCardDialog(this).show();
    }

    public void setAdapter(List<Card> data) {
        mAdapter.addMore(data);
        mAdapter.notifyDataSetChanged();
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    private void updateAdapter(int index) {
        mAdapter.remove(index);
        mAdapter.notifyDataSetChanged();
    }

    private void updateAdapter(Card card) {
        mAdapter.add(card);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSyncDataReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateCardReceiver);
        super.onDestroy();
    }
}

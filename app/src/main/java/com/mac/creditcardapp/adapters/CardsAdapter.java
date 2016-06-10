package com.mac.creditcardapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mac.creditcardapp.R;
import com.mac.creditcardapp.models.Card;
import com.mac.creditcardapp.viewholders.CardsHolder;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final List<Card> mItems;
    public CardsAdapter(Context context, List<Card> cards) {

        mContext = context;
        mItems = cards;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.card_view, viewGroup, false);
        return new CardsHolder(mContext, v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((CardsHolder) viewHolder).invalidate(mItems.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addMore( List<Card> moreData){
        mItems.addAll(moreData);
    }

    public void remove(int index) {
        mItems.remove(index);
    }

    public void add(Card card) {
        mItems.add(card);
    }
}

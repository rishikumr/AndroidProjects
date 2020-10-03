package com.dynasty.myapplication.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.dynasty.myapplication.R;

import java.util.ArrayList;


public class GiftListAdaptor extends RecyclerView.Adapter<GiftListAdaptor.GiftViewListHolder>
{
    private ArrayList<String> itemList;
    private Context context;
    boolean isMyGift;
    private OnEventClickListener listener;

    public GiftListAdaptor(Context context,    ArrayList<String> itemList , boolean isMyGift)
    {
        this.itemList = itemList;
        this.context = context;
        this.isMyGift = isMyGift;
    }

    @Override
    public GiftViewListHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate( isMyGift ? R.layout.my_gift_list_item :R.layout.gift_list_item , parent  , false);
        GiftViewListHolder rcv = new GiftViewListHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(GiftViewListHolder holder, int position)
    {
        holder.giftItem.setText(itemList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }


    public interface OnEventClickListener {
        void onItemClick(int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;

    }


    class GiftViewListHolder extends RecyclerView.ViewHolder
    {
        public TextView giftItem;

        public GiftViewListHolder(View itemView)
        {
            super(itemView);
            if(isMyGift){
                giftItem = itemView.findViewById(R.id.my_gift_list_item);
                itemView.findViewById(R.id.remove_Gift).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                });
            }else{
                giftItem = itemView.findViewById(R.id.gift_list_item);
            }


        }



    }


}



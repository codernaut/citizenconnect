package org.cfp.citizenconnect.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.facebook.drawee.view.SimpleDraweeView;

import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by shahzaibshahid on 13/12/2017.
 */

public class NotificationLayoutAdapter extends RecyclerView.Adapter<NotificationLayoutAdapter.MyViewHolder> {

    List<Notifications> snapList;
    Context mContext;
    private LayoutInflater inflater;
    OnItemInteractionListener mListener;

    public NotificationLayoutAdapter(Context mContext, List<Notifications> snapList, OnItemInteractionListener mListener) {
        this.snapList = snapList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.snapHolder.setImageURI(Uri.parse(snapList.get(position).getFilePath()));
    }

    @Override
    public int getItemCount() {
        return snapList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView snapHolder;
        ImageButton BtnShare;

        public MyViewHolder(final View itemView) {
            super(itemView);
            snapHolder = itemView.findViewById(R.id.notificationLayoutHolder);
            BtnShare = itemView.findViewById(R.id.BtnShare);

            BtnShare.setOnClickListener(view -> mListener.ShareImageClickListener(getAdapterPosition(),snapHolder.getDrawable()));
        }
    }
    public  interface  OnItemInteractionListener{
         void ShareImageClickListener(int position, Drawable image);
    }
}

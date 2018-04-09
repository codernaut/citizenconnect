package org.cfp.citizenconnect.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.R;

import java.util.List;

/**
 * Created by shahzaibshahid on 13/12/2017.
 */

public class NotificationLayoutAdapter extends RecyclerView.Adapter<NotificationLayoutAdapter.MyViewHolder> {
    List<Notifications> notificationList;
    Context mContext;
    private LayoutInflater inflater;
    OnItemInteractionListener mListener;

    public NotificationLayoutAdapter(Context mContext, List<Notifications> snapList, OnItemInteractionListener mListener) {
        this.notificationList = snapList;
        this.mContext = mContext;
//        inflater = LayoutInflater.from(mContext);
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.notification_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.snapHolder.setImageURI(Uri.parse(notificationList.get(position).getFilePath()));
        holder.description.setText(notificationList.get(position).getDescription());
        holder.DateTime.setText(notificationList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView snapHolder;
        TextView BtnShare;
        TextView description;
        TextView DateTime;
        LinearLayout descriptionLayout;
        TextView view;

        public MyViewHolder(final View itemView) {
            super(itemView);
            snapHolder = itemView.findViewById(R.id.notificationLayoutHolder);
            BtnShare = itemView.findViewById(R.id.BtnShare);
            description = itemView.findViewById(R.id.description);
            DateTime = itemView.findViewById(R.id.DateTime);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
            view = itemView.findViewById(R.id.view);

            BtnShare.setOnClickListener(view -> mListener.ShareImageClickListener(getAdapterPosition(), snapHolder.getDrawable()));
            view.setOnClickListener(view -> mListener.FullSizeImageClickListener(notificationList.get(getAdapterPosition()).getFilePath(), description.getText().toString()));
            descriptionLayout.setOnClickListener(view -> mListener.FullSizeImageClickListener(notificationList.get(getAdapterPosition()).getFilePath(), description.getText().toString()));
            snapHolder.setOnClickListener(view -> mListener.FullSizeImageClickListener(notificationList.get(getAdapterPosition()).getFilePath(), description.getText().toString()));
        }
    }

    public interface OnItemInteractionListener {
        void ShareImageClickListener(int position, Drawable image);

        void FullSizeImageClickListener(String imagePath, String description);

    }
}

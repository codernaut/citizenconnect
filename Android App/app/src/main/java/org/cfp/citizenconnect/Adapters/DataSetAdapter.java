package org.cfp.citizenconnect.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.cfp.citizenconnect.Model.DataSet;
import org.cfp.citizenconnect.Popups.DataSetComplainDialog;
import org.cfp.citizenconnect.Popups.EmergencyContactFragment;
import org.cfp.citizenconnect.R;

import java.util.List;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

public class DataSetAdapter extends RecyclerView.Adapter<DataSetAdapter.ViewHolder>  {
    List<DataSet> list;
    Context mContext;
    OnComplainClickListener mOnComplainListener;

    public DataSetAdapter(Context mContext, List<DataSet> mList, OnComplainClickListener onComplainListener) {
        this.list = mList;
        this.mOnComplainListener = onComplainListener;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dataset_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, address;
        ImageButton complainBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            complainBtn = itemView.findViewById(R.id.complainBtn);
            complainBtn.setOnClickListener(view -> {
                mOnComplainListener.setOnComplainInteraction(name.getText().toString(),address.getText().toString());
            });
        }
    }

    public  interface  OnComplainClickListener {
        void setOnComplainInteraction(String dataSetName,String dataSetAddress);
    }
}

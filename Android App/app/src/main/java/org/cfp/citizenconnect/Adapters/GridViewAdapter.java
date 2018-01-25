package org.cfp.citizenconnect.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.cfp.citizenconnect.Model.Layout;
import org.cfp.citizenconnect.R;

import java.util.ArrayList;

/**
 * Created by shahzaibshahid on 22/01/2018.
 */

public class GridViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Layout> gridViewList;
    OnItemClickListener mListener;

    public GridViewAdapter(Context mContext, ArrayList<Layout> gridViewList, OnItemClickListener mListener) {
        this.mContext = mContext;
        this.gridViewList = gridViewList;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return gridViewList.size();
    }

    @Override
    public Object getItem(int i) {
        return gridViewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tiles_layout, viewGroup, false);
        }
        SimpleDraweeView icons = view.findViewById(R.id.iconHolder);
        TextView title = view.findViewById(R.id.titleGV);
        CardView mCardView = view.findViewById(R.id.mainCV);
        LinearLayout background = view.findViewById(R.id.background);
        final Layout layout = (Layout) this.getItem(i);
        icons.setImageURI(Uri.parse(layout.getIcon()));
        background.setBackgroundColor(Color.parseColor(layout.getColor()));
        title.setText(layout.getName());
        mCardView.setOnClickListener(view1 -> mListener.viewDataList(title.getText().toString()));
        return view;
    }
    public  interface OnItemClickListener{
        void viewDataList(String type);
    }
}

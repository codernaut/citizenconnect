package org.cfp.citizenconnect.Data;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cfp.citizenconnect.R;

/**
 * Created by shahzaibshahid on 18/01/2018.
 */

public class FragmentDataSet extends Fragment {

    public static FragmentDataSet newInstance() {
        FragmentDataSet fragmentDataSet = new FragmentDataSet();
        return fragmentDataSet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_fragment, container, false);
        return rootView;
    }
}

package org.cfp.citizenconnect.Feedback;

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

public class FragmentFeedback extends Fragment {

    public static FragmentFeedback newInstance() {
        FragmentFeedback fragmentFeedback = new FragmentFeedback();
        return fragmentFeedback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_fragment, container, false);
        return rootView;
    }
}

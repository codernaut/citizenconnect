package org.cfp.citizenconnect.Popups;

import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by AhmedAbbas on 3/1/2018.
 */

public class BaseFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(true);
            int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
            int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
            Window window = getDialog().getWindow();
            window.setLayout(dialogWidth, dialogHeight);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.5f);
            window.setGravity(Gravity.CENTER);
            window.addFlags(Window.FEATURE_NO_TITLE);
            View decorView = window.getDecorView();

        }
    }
}

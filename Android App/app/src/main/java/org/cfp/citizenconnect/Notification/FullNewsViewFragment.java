package org.cfp.citizenconnect.Notification;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.cfp.citizenconnect.Model.Notifications;
import org.cfp.citizenconnect.R;

import java.util.ArrayList;
import java.util.List;

public class FullNewsViewFragment extends DialogFragment {

    private String path;

    private ImageView imageView;

    private ViewPager viewPager;

    private List<Notifications> notifications = new ArrayList<>();
    private int position = 0;


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(true);
            int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
            int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;
            Window window = getDialog().getWindow();
            window.setLayout(dialogWidth, dialogHeight);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.5f);
            window.setGravity(Gravity.CENTER);
            window.addFlags(Window.FEATURE_NO_TITLE);
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_news_view, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        view.findViewById(R.id.imgArrowUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if (!TextUtils.isEmpty(path)) {
            try {
                Glide.with(getActivity()).load(path).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }*/
        viewPager.setAdapter(new FullScreenPagerAdapter(getChildFragmentManager(), notifications));
        viewPager.setCurrentItem(position);
    }
}
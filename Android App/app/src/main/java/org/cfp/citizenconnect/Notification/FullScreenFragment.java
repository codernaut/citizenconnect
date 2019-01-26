package org.cfp.citizenconnect.Notification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.cfp.citizenconnect.R;

import static org.cfp.citizenconnect.MyUtils.frescoImageRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreenFragment extends Fragment {

    private PhotoView imageView;
    private TextView description;
    private TextView noImageText;

    public static FullScreenFragment newInstance(String path, String text) {
        FullScreenFragment fragment = new FullScreenFragment();
        Bundle b = new Bundle();
        b.putString("image", path);
        b.putString("text", text);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen, container, false);

        imageView = (PhotoView) view.findViewById(R.id.imageView3);
        noImageText = (TextView) view.findViewById(R.id.no_image_text);
        description = (TextView) view.findViewById(R.id.description);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            description.setText(bundle.getString("text"));
            String path = bundle.getString("image");
            if (!TextUtils.isEmpty(path)) {

//                Picasso.with(getContext()).load(path).into(imageView);
                frescoImageRequest(path, getContext(), response -> {
                    Picasso.with(getContext()).load(path).into(imageView);
                    noImageText.setVisibility(View.GONE);
                }, error -> {
                    noImageText.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), "Failed to load Image", Toast.LENGTH_LONG).show();
                });
            }
        }

    }

}

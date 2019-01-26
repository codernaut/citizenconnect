package org.cfp.citizenconnect.Popups;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cfp.citizenconnect.MainActivity;
import org.cfp.citizenconnect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyContactFragment extends BaseFragment implements View.OnClickListener {

    static final int CALL_PERMISSION_REQUEST = 1;
    private LinearLayout linearLayoutPolice, linearLayoutAmbulance, linearLayoutFireBrigade;
    private TextView police, ambulance, fireBrigade;

    public EmergencyContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        linearLayoutPolice = view.findViewById(R.id.ll_police);
        linearLayoutAmbulance = view.findViewById(R.id.ll_ambulance);
        linearLayoutFireBrigade = view.findViewById(R.id.ll_fire_brigade);

        police = view.findViewById(R.id.police_number);
        ambulance = view.findViewById(R.id.ambulance_number);
        fireBrigade = view.findViewById(R.id.fire_brigade_number);

        linearLayoutPolice.setOnClickListener(this);
        linearLayoutAmbulance.setOnClickListener(this);
        linearLayoutFireBrigade.setOnClickListener(this);
        view.findViewById(R.id.closeButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.ll_police:
                ((MainActivity) getActivity()).setPhoneNo(police.getText().toString());
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + police.getText()));
                    startActivity(intent);
                }
                break;
            case R.id.ll_ambulance:
                ((MainActivity) getActivity()).setPhoneNo(ambulance.getText().toString());
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ambulance.getText()));
                    startActivity(intent);
                }
                break;
            case R.id.ll_fire_brigade:
                ((MainActivity) getActivity()).setPhoneNo(fireBrigade.getText().toString());
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + fireBrigade.getText()));
                    startActivity(intent);
                }
                break;
            case R.id.closeButton:
                dismissAllowingStateLoss();
                break;
        }

    }
}

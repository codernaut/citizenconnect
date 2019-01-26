package org.cfp.citizenconnect;

import android.Manifest;

/**
 * Created by AhmedAbbas on 12/12/2017.
 */

public class PermissionsRequest {
    public static final int LOCATION_REQUEST_CODE = 0;
    public static final int LOACTION_ENABLE_REQUEST = 1;
    public static final String LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String[] LOCATION_PERMISSIONS = new String[]{LOCATION_FINE,LOCATION_COARSE};

}

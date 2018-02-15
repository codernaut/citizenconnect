package org.cfp.citizenconnect;

import com.google.api.services.gmail.GmailScopes;

/**
 * Created by shahzaibshahid on 15/01/2018.
 */

public class Constants {
    public static final int CALL_PERMISSION_REQUEST = 1;
    public static final String DATASET_REFFERENCE = "project/data_sets";
    public static  final String SERVICES_REFFERENCE = "project/services";
    public static final  String DATA_MEDICAL_STORE = "dataset";

    public  static  final String DATA_TYPE = "dataType";
    public static  final String FILE_URL = "filePath";
    public static  final int ICT_NOTIFICATION_ID=0;
    public static final String[] SCOPES = { GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_READONLY};
}

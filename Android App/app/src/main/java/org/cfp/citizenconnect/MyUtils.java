package org.cfp.citizenconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.cfp.citizenconnect.CitizenConnectApplication.FILE_PROVIDER_AUTHORITY;

/**
 * Created by shahzaibshahid on 18/12/2017.
 */

public class MyUtils {

    public static Uri getBitmapUri(Uri uri, Context mContext) throws IOException {
        final Bitmap[] bmp = {null};

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, mContext);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(@Nullable Bitmap bitmap) {
                bmp[0] = bitmap;
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
        Uri bmpUri = null;
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        if (file == null) {
            return null;
        }
        FileOutputStream out = new FileOutputStream(file);
        if (out != null && bmp[0]!=null) {
            bmp[0].compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                bmpUri = Uri.fromFile(file);
            } else {
                bmpUri = FileProvider.getUriForFile(mContext, FILE_PROVIDER_AUTHORITY, file);
            }
        }

        return bmpUri;
    }

    public static Bitmap getBitmap(int drawable, Context mContext) {
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                drawable);
        return icon;
    }

    @WorkerThread
    public static void getAFireBaseData(DatabaseReference databaseReference, CustomCallBack.Listener<DataSnapshot> mListener, CustomCallBack.ErrorListener<DatabaseError> mErrorListener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListener.onResponse(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                mErrorListener.onErrorResponse(error);
            }
        });
    }

    public static void mSnakbar(String message, String actionText, int duration, int visibility, View view, View.OnClickListener listener) {
        Snackbar mySnackbar = Snackbar.make(view,
                message, Snackbar.LENGTH_SHORT);
        mySnackbar.setAction(actionText, listener);
        mySnackbar.setDuration(duration);
        switch (visibility) {
            case 1:
                mySnackbar.show();
                break;
            case 0:
                mySnackbar.dismiss();
                break;
        }
    }

    public static void frescoImageRequest(String imagePath, Context mContext, CustomCallBack.Listener<Bitmap> onBitmapReceived, CustomCallBack.ErrorListener<DataSource> onFailure) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imagePath))
                .setAutoRotateEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, mContext);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            protected void onNewResultImpl(@javax.annotation.Nullable Bitmap bitmap) {
                if (dataSource.isFinished() && bitmap != null) {
                    onBitmapReceived.onResponse(bitmap);
                    dataSource.close();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (dataSource != null) {
                    onFailure.onErrorResponse(dataSource);
                    dataSource.close();
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

    public static boolean isDeviceOnline(Context mContext) {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean validatePhoneNumber(EditText mPhoneNumberField) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }

        return true;
    }

    public static boolean canGetLocation(Context context) {
        boolean result = true;
        LocationManager lm = null;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        if (lm == null)

            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            networkEnabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gpsEnabled == false || networkEnabled == false) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    public static Drawable tintDrawable(Context mContext, int Color, int Drawable) {

        @ColorInt int color = ContextCompat.getColor(mContext, Color);
        Drawable drawable = ContextCompat.getDrawable(mContext, Drawable);
        drawable.setTint(color);
        return drawable;
    }

    public static Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}

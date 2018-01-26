package org.cfp.citizenconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
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
        Uri bmpUri;
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = new FileOutputStream(file);
        bmp[0].compress(Bitmap.CompressFormat.PNG, 90, out);
        out.close();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            bmpUri = Uri.fromFile(file);
        } else {
            bmpUri = FileProvider.getUriForFile(mContext, FILE_PROVIDER_AUTHORITY, file);
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
        databaseReference.addValueEventListener(new ValueEventListener() {
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

    public static void frescoImageRequest(String imagePath,Context mContext,CustomCallBack.Listener<Bitmap> onBitmapReceived,CustomCallBack.ErrorListener<DataSource> onFailure) {
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
}

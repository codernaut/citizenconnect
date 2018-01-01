package org.cfp.citizenconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.cfp.citizenconnect.CitizenConnectApplciation.FILE_PROVIDER_AUTHORITY;

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
}

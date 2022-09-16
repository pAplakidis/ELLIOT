package com.iprism.elliot.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ModelUtils {
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open("model/" + assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }

            return file.getAbsolutePath();
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        float scaleW = ((float) newWidth) / w;
        float scaleH = ((float) newHeight) / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);
        Bitmap ret = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, false);
        bm.recycle();
        return ret;
    }
}

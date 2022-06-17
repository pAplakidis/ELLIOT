package com.iprism.elliot.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ModelUtils {
    public static String getJsonFromAssets(Context context, String filename) {
        String jsonString;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

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

    public static Bitmap toBGR(Bitmap img) {
        int bytes = img.getByteCount(); // calc image's bytes
        ByteBuffer buffer = ByteBuffer.allocate(bytes); // create new buffer
        img.copyPixelsToBuffer(buffer); // move the bytes to the buffer

        byte[] temp = buffer.array();   // get the array of the data
        byte[] pixels = new byte[(temp.length / 4) * 3];    // allocate for 3 byte BGR

        // Copy pixels into place
        for (int i = 0; i < (temp.length / 4); i++) {
            pixels[i * 3] = temp[i * 4 + 3];    // B
            pixels[i * 3 + 1] = temp[i * 4 + 2];    // G
            pixels[i * 3 + 2] = temp[i * 4 + 1];    // R

            // NOTE: alpha is discarded
        }
        return BitmapFactory.decodeByteArray(pixels, 0, pixels.length);
    }

//    public static Rect center_crop(Mat img) {
//        int y1 = Math.round((img.rows() - 128) / 2);
//        int y2 = Math.round(y1 + 128);
//        int x1 = Math.round((img.cols() - 128) / 2);
//        int x2 = Math.round(x1 + 128);
//
//        return new Rect(x1, y1, (x2 - x1), (y2 - y1));
//    }
}

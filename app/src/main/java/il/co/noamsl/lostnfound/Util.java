package il.co.noamsl.lostnfound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;


public class Util {
    public static <T> LiveData<T> createLiveData(T object) {
        MutableLiveData<T> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(object);
        return mutableLiveData;
    }

    public static String trace(int skip) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        stackTrace = Arrays.copyOfRange(stackTrace, skip + 1, stackTrace.length);
        String[] stackTraceSimplifiedStrings = new String[stackTrace.length];
        for (int i = 0; i < stackTraceSimplifiedStrings.length; i++) {
            try {
                stackTraceSimplifiedStrings[i] = Class.forName(stackTrace[i].getClassName()).getSimpleName()
                        + "." + stackTrace[i].getMethodName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return " \nTRACE = " + Arrays.toString(stackTraceSimplifiedStrings);
    }

    public static InputStream inputStreamFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        return new ByteArrayInputStream(bitmapdata);

    }

    public static class MyToast {
        private static Toast lastToast;
        public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

        public static void show(Context context, String s, int duration) {
            if (lastToast != null) {
                lastToast.cancel();
            }
            lastToast = Toast.makeText(context, s, duration);
            lastToast.show();
        }
    }

    public static Drawable base64ToDrawable(Resources resources, String base64Image) {
        return base64ToDrawable(resources, base64Image, 100);
    }

    public static Drawable base64ToDrawable(Resources resources, String base64Image, int compressionRation) {
        if (base64Image == null) {
            return null;
        }
        byte[] decodedBytes = Base64.decodeBase64(base64Image.getBytes());
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        bitmap1 = compressBitmap(bitmap1,compressionRation);
        return new BitmapDrawable(resources, bitmap1);


    }

    public static Bitmap compressBitmap(Bitmap bitmap,int compressionRation) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRation, outputStream);
        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(outputStream.toByteArray()));
        return bitmap;
    }

}

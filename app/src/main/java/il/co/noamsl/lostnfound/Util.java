package il.co.noamsl.lostnfound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by noams on 16/11/2017.
 */

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
}

package il.co.noamsl.lostnfound.repository.item.fake;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.repository.item.NoamImage;

/**
 * Created by noams on 05/11/2017.
 */

public class FakeImage implements NoamImage {
    Drawable image;
    public FakeImage() {
        this.context = MainActivity.getContextRemoveThisMethod();
        Drawable image1 = ContextCompat.getDrawable(MainActivity.getContextRemoveThisMethod(), R.drawable.ic_home_black_24dp);
        Drawable image2 = ContextCompat.getDrawable(MainActivity.getContextRemoveThisMethod(), R.drawable.ic_dashboard_black_24dp);

        if(Math.random()>0.5){
            image = image1;
        }else{
            image = image2;
        }
    }

    private Context context;
    @Override
    public Drawable getDrawable() {
        return image;

    }
}

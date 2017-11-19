package il.co.noamsl.lostnfound.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

import static android.widget.Toast.LENGTH_SHORT;


public class EditItemActivity extends AppCompatActivity implements ItemReceiver<Boolean> {
    private static final String TAG = "EditItemActivity";
    public static final String ARG_ITEM_ID = "itemId";
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private final int COMPRESSION_RATIO = 50;


    public enum Mode {EDIT, ADD}

    public static final String ARG_MODE = "MODE";
    private static final boolean FOUND_TOGGLE_VALUE = false;
    private Mode MODE;

    private TextView etTitle;
    private TextView etDescription;
    private TextView etLocation;
    private CheckBox cbRelevant;
    private ToggleButton tgbLostOrFound;
    private Integer itemId;
    private ImageButton imageButton;
    private volatile String base64Image = null;
    private LfItem prevItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        int modeInt = getIntent().getExtras().getInt(ARG_MODE, -1);
        MODE = Mode.values()[modeInt];
        etTitle = (EditText) findViewById(R.id.edit_item_et_title);
        etDescription = (TextView) findViewById(R.id.edit_item_et_description);
        etLocation = (TextView) findViewById(R.id.edit_item_et_adress);
        cbRelevant = (CheckBox) findViewById(R.id.edit_item_cb_relevant);
        tgbLostOrFound = (ToggleButton) findViewById(R.id.editItem_toggleButton_lostOrFound);
        imageButton = (ImageButton) findViewById(R.id.edit_item_ib_picture);
        //restore
        itemId = getIntent().getExtras().getInt(ARG_ITEM_ID, -1);

        prevItem = ServiceLocator.getRepository().getItemById(itemId);

        switch (MODE) {
            case EDIT:
                restoreFields();
                break;
            case ADD:
                break;
            default:
                throw new IllegalStateException("mode must be edit or add");
        }
    }

    private void restoreFields() {
        restorePicture(prevItem);
        etTitle.setText(prevItem.getName());
        etDescription.setText(prevItem.getDescription());
        etLocation.setText(prevItem.getLocation());
        cbRelevant.setChecked(prevItem.getRelevant());

        tgbLostOrFound.setChecked(prevItem.isAFound() ? FOUND_TOGGLE_VALUE : !FOUND_TOGGLE_VALUE);
    }

    private void restorePicture(LfItem item) {
        base64Image = item.getPicture();
        Log.d(TAG, "restorePicture: base64Image = " + base64Image);

        if(base64Image!=null) {
            imageButton.setImageDrawable(Util.base64ToDrawable(getResources(), base64Image));
        }
    }


    public void itemSubmitted(View v) {
        Util.MyToast.show(getApplicationContext(), "Submitting", LENGTH_SHORT);

        String name = etTitle.getText() + "";
        boolean isAFound = isToggleButtonAFound();
        boolean relevant = cbRelevant.isChecked();
        Integer owner = getOwner(prevItem)/*null*/;
        String location = etLocation.getText() + "";
        String description = etDescription.getText() + "";
        String picture = base64Image;
        LfItem newItem = new LfItem(itemId, name, description, location, owner, picture, relevant, isAFound);
        switch (MODE) {
            case EDIT:
                ServiceLocator.getExternalRepository().updateItem(this, newItem);
                break;
            case ADD:
                ServiceLocator.getExternalRepository().addItem(this, newItem);
                break;
        }
    }

    private Integer getOwner(LfItem lfItem) {
        switch (MODE) {
            case EDIT:
                return lfItem.getOwner();
            case ADD:
                return ServiceLocator.getRepository().getLoggedInUserId();
        }
        throw new IllegalStateException("mode should be edit or add");
    }

    private boolean isToggleButtonAFound() {
        return tgbLostOrFound.isChecked() == FOUND_TOGGLE_VALUE;
    }

    @Override
    public void onItemArrived(Boolean item) {
        Util.MyToast.show(getApplicationContext(), "Item Submit Successful!", LENGTH_SHORT);
        try {
            this.onBackPressed();
        } catch (Exception e) {
            Log.w(TAG, "back presseed after saved instance", e);
        }
    }

    @Override
    public void onRequestFailure() {
        Util.MyToast.show(getApplicationContext(), "Unable to submit!", LENGTH_SHORT);
    }

    public void pickImage(View v) {
        pickImage();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Util.MyToast.show(getApplicationContext(), "Unable to pick image", LENGTH_SHORT);
                return;
            }
            try {

                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = Util.compressBitmap(bitmap, COMPRESSION_RATIO);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                imageButton.setImageDrawable(bitmapDrawable);

                inputStream = Util.inputStreamFromBitmap(bitmap);

                base64Image = inputStreamToBase64(inputStream);
//                imageButton.setImageDrawable(Util.base64ToDrawable(getResources(),base64Image));


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }


    @NonNull
    private static String inputStreamToBase64(InputStream inputStream) throws IOException {
        return new String(Base64.encodeBase64(IOUtils.toByteArray(inputStream)));
    }

}

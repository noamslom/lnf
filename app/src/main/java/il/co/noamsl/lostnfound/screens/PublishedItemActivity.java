package il.co.noamsl.lostnfound.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.User.User;
import il.co.noamsl.lostnfound.repository.external.RepositoryExternal;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

public class PublishedItemActivity extends AppCompatActivity implements ItemReceiver<User> {

    public static final String ARG_ITEM_ID = "itemID";
    private static final String LOST_TEXT = "Lost Item:";
    private static final String FOUND_TEXT = "Found Item";

    private int itemId;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvLostOrFound;
    private ImageView ivMainImage;
    private User owner;
    private Button btnContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_item);

        Intent myIntent = getIntent(); // gets the previously created intent
        itemId = myIntent.getIntExtra(ARG_ITEM_ID, -1); // will return "FirstKeyValue"

        tvTitle = (TextView) findViewById(R.id.published_item_tv_name);
        tvDescription = (TextView) findViewById(R.id.published_item_tv_desc);
        tvEmail = (TextView) findViewById(R.id.published_item_tv_email);
        tvPhone = (TextView) findViewById(R.id.published_item_tv_phone);
        tvLocation = (TextView) findViewById(R.id.published_item_tv_location);
        ivMainImage = (ImageView) findViewById(R.id.published_item_iv_main_image);
        btnContact = (Button) findViewById(R.id.published_item_btn_contact);
        tvLostOrFound = (TextView) findViewById(R.id.published_item_tv_lost_or_found);
        updateFields();
    }

    private void updateFields() {
        RepositoryExternal re = ServiceLocator.getExternalRepository();
        LfItem item = re.getItemById(itemId);
        restoreUserContact(re, item);
        restorePicture(item);
        tvTitle.setText(item.getName());
        tvDescription.setText(item.getDescription());
        tvLocation.setText(item.getLocation());
        tvLostOrFound.setText(item.isAFound() ? FOUND_TEXT : LOST_TEXT);

    }

    private void restoreUserContact(RepositoryExternal re, LfItem item) {
        if (item.getOwner() != null) {
            re.getUserById(this, item.getOwner());
        } else {
            Util.MyToast.show(getApplicationContext(), "Unable to load owner", Toast.LENGTH_SHORT);
        }
    }

    public void showContect(View view) {
        btnContact.setEnabled(false);
        btnContact.setVisibility(View.GONE);
        tvEmail.setVisibility(View.VISIBLE);
        tvPhone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemArrived(User owner) {
        this.owner = owner;
        tvEmail.setText(owner.getEmail());
        tvPhone.setText(owner.getPhoneNumber());
    }

    @Override
    public void onRequestFailure() {
        Util.MyToast.show(getApplicationContext(), "Unable to get published item details", Toast.LENGTH_SHORT);
    }

    private void restorePicture(LfItem item) {
        String base64Image = item.getPicture();
        if (base64Image != null) {
            ivMainImage.setImageDrawable(Util.base64ToDrawable(getResources(), base64Image));
        }
    }

}

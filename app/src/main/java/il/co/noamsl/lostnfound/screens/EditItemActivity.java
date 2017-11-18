package il.co.noamsl.lostnfound.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.Util;
import il.co.noamsl.lostnfound.repository.item.LfItem;
import il.co.noamsl.lostnfound.webService.dataTransfer.ItemReceiver;

public class EditItemActivity extends AppCompatActivity implements ItemReceiver<Boolean> {
    public static final String ARG_ITEM_ID = "itemId";

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
        itemId = getIntent().getExtras().getInt(ARG_ITEM_ID, -1);
        LfItem item = ServiceLocator.getRepository().getItemById(itemId);
        etTitle.setText(item.getName());
        etDescription.setText(item.getDescription());
        etLocation.setText(item.getLocation());
        cbRelevant.setChecked(item.getRelevant());

        tgbLostOrFound.setChecked(item.isAFound() ? FOUND_TOGGLE_VALUE : !FOUND_TOGGLE_VALUE);
    }


    public void itemSubmitted(View v) {
        String name = etTitle.getText() + "";
        String picture = null;
        boolean isAFound = isToggleButtonAFound();
        boolean relevant = cbRelevant.isChecked();
        Integer owner = getOwner()/*null*/;
        String location = etLocation.getText() + "";
        String description = etDescription.getText() + "";
        LfItem newItem = new LfItem(itemId, name, description, location, owner, picture, relevant, isAFound);
        switch (MODE) {
            case EDIT:
                ServiceLocator.getExternalRepository().updateItem(this, newItem);
                break;
            case ADD:
                ServiceLocator.getExternalRepository().addItem(this, newItem);
                break;
        }
        Util.MyToast.makeText(getApplicationContext(), "Submitting", Toast.LENGTH_SHORT);
    }

    private Integer getOwner() {
        switch (MODE) {
            case EDIT:
                return null;
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
        Util.MyToast.makeText(getApplicationContext(), "Item Submit Successful!", Toast.LENGTH_SHORT);
        this.onBackPressed();
    }

    @Override
    public void onRequestFailure() {
        Util.MyToast.makeText(getApplicationContext(), "Unable to submit!", Toast.LENGTH_SHORT);
    }


}

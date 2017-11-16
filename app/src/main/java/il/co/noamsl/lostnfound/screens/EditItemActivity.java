package il.co.noamsl.lostnfound.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.ServiceLocator;
import il.co.noamsl.lostnfound.repository.Repository;
import il.co.noamsl.lostnfound.repository.item.LfItem;

public class EditItemActivity extends AppCompatActivity {
    public enum Mode {EDIT, ADD}
    public static final String ARG_MODE ="MODE";
    private static final boolean FOUND_TOGGLE_VALUE = false;
    private Mode MODE;

    private TextView etTitle;
    private TextView etDescription;
    private TextView etLocation;
    private CheckBox cbRelevant;
    private ToggleButton tgbLostOrFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        int modeInt = getIntent().getExtras().getInt(ARG_MODE,-1);
        MODE = Mode.values()[modeInt];

        etTitle = (EditText) findViewById(R.id.edit_item_et_title);
        etDescription = (TextView) findViewById(R.id.edit_item_et_description);
        etLocation = (TextView) findViewById(R.id.edit_item_et_adress);
        cbRelevant = (CheckBox) findViewById(R.id.edit_item_cb_relevant);
        tgbLostOrFound = (ToggleButton) findViewById(R.id.editItem_toggleButton_lostOrFound);
    }


    public void itemSubmitted(View v) {
        String name = etTitle.getText() + "";
        String picture = "pic not imp";
        boolean isAFound = isToggleButtonAFound();
        boolean relevant = cbRelevant.isChecked();
        Integer owner = getOwner()/*null*/;
        String location = etLocation.getText() + "";
        String description = etDescription.getText() + "";
        LfItem newItem = new LfItem(null, name, description, location, owner, picture, relevant, isAFound);
        switch (MODE) {
            case EDIT: ServiceLocator.getExternalRepository().addItem(newItem);
                break;
            case ADD:
                ServiceLocator.getExternalRepository().updateItem(newItem);
            break;
        }
        this.onBackPressed();
        Toast.makeText(getApplicationContext(), "Submit Successful!", Toast.LENGTH_SHORT).show();
    }

    private Integer getOwner() {
        switch (MODE) {
            case EDIT: return null;
            case ADD: return Repository.getGlobal().getLoggedInUserId();
        }
        throw new IllegalStateException("mode should be edit or add");
    }

    private boolean isToggleButtonAFound() {
        return tgbLostOrFound.isChecked() == FOUND_TOGGLE_VALUE;
    }
}

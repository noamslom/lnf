package il.co.noamsl.lostnfound.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.repository.item.LfItem;

public class EditItemActivity extends AppCompatActivity {
    private TextView etTitle;
    private ToggleButton tgbLostOrFound;
    private static final boolean FOUND_TOGGLE_VALUE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etTitle = (EditText) findViewById(R.id.edit_item_et_title);
        tgbLostOrFound = (ToggleButton) findViewById(R.id.editItem_toggleButton_lostOrFound);
    }


    public void itemSubmitted(View v) {
        String name = etTitle.getText() + "";
        String picture = "pic";

        boolean isAFound = isToggleButtonAFound();
        boolean relevant = true;
        Integer owner = 3/*null*/;

        String location = "My House"/*null*/;
        String description = "Cool thing i swear" /*null*/;
        MainActivity.getExternalRepository().addItem(new LfItem(3, name, description, location, owner, picture, relevant, isAFound));
        this.onBackPressed();
    }

    private boolean isToggleButtonAFound() {
        return tgbLostOrFound.isChecked() == FOUND_TOGGLE_VALUE;
    }
}

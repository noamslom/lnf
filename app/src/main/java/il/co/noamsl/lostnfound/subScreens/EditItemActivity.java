package il.co.noamsl.lostnfound.subScreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.item.LfItemImpl;
import il.co.noamsl.lostnfound.serverInterface.fake.FakeImage;

public class EditItemActivity extends AppCompatActivity {
    TextView etTitle;
    ToggleButton tgbLostOrFound;
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

        boolean checked = tgbLostOrFound.isChecked();
        boolean relevant = true;
        Integer owner = 3/*null*/;

        String location = "My House"/*null*/;
        String description = "Cool thing i swear" /*null*/;
        MainActivity.getExternalRepository().addItem(new LfItemImpl(3, name, description, location, owner, picture, relevant, checked));
        this.onBackPressed();
    }
}

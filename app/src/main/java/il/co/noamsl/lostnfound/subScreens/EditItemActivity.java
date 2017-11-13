package il.co.noamsl.lostnfound.subScreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import il.co.noamsl.lostnfound.MainActivity;
import il.co.noamsl.lostnfound.R;
import il.co.noamsl.lostnfound.item.Item;

public class EditItemActivity extends AppCompatActivity {
    TextView etTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etTitle = (EditText) findViewById(R.id.edit_item_et_title);

    }


    public void itemSubmitted(View v) {
        MainActivity.getServer().addItem(etTitle.getText()+"");
        this.onBackPressed();
    }
}

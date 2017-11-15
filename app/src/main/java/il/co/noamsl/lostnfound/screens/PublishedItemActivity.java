package il.co.noamsl.lostnfound.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import il.co.noamsl.lostnfound.R;

public class PublishedItemActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "itemID";
    private long  itemId;
    private TextView tvTitle;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_item);

        Intent myIntent = getIntent(); // gets the previously created intent
        itemId = myIntent.getLongExtra(ARG_ITEM_ID,-1); // will return "FirstKeyValue"

        tvTitle = (TextView) findViewById(R.id.published_item_tv_name);
        updateFields();
    }

    private void updateFields() {
//        LfItem displayedItem = MainActivity.getExternalRepository().getItemById(itemId);
//        tvTitle.setText(displayedItem.getName());
        //// TODO: 15/11/2017
    }
}

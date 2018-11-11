package com.python.cat.potato.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseActivity;

public class EventEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.empty);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_events_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_event) {
            LogUtils.d("add event ... ### ");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

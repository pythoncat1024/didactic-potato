package com.python.cat.potato.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.python.cat.potato.R;
import com.python.cat.potato.fragment.EventDetailFragment;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.viewmodel.BaseVM;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        long eventID = getIntent().getLongExtra(GlobalInfo.IntentKey.KEY_EVENT_ID, -1);
        EventDetailFragment fragment = EventDetailFragment.newInstance(eventID);
        BaseVM.jump2Target(
                getSupportFragmentManager(),
                fragment,
                R.id.activity_event_info_container,
                false,
                true
        );
    }
}

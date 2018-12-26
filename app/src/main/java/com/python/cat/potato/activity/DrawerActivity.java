package com.python.cat.potato.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.python.cat.potato.R;
import com.python.cat.potato.base.BaseActivity;
import com.python.cat.potato.base.BaseApplication;
import com.python.cat.potato.base.LoginCallback;
import com.python.cat.potato.base.NeedLogin;
import com.python.cat.potato.base.TitleHook;
import com.python.cat.potato.fragment.CalendarFragment;
import com.python.cat.potato.fragment.LoginFragment;
import com.python.cat.potato.fragment.TODOFragment;
import com.python.cat.potato.fragment.ViewFragment;
import com.python.cat.potato.global.GlobalInfo;
import com.python.cat.potato.utils.SpUtils;
import com.python.cat.potato.utils.ToastHelper;
import com.python.cat.potato.viewmodel.BaseVM;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TitleHook {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initNavHeader();
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                initNavHeader();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        showDefaultFragment();
    }

    private void initNavHeader() {
        // app:headerLayout="@layout/nav_header_drawer"
        LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.textView_username);
        String username = SpUtils.get(this, GlobalInfo.SP_KEY_USERNAME);
        if (!TextUtils.isEmpty(username)) {
            tvUsername.setText(username);
        }
        headerView.setOnClickListener(v -> {
            showLoginFragment(success -> showDefaultFragment());
            closeDrawer();
        });
    }

    private void showDefaultFragment() {
        // default show calendar
        navigationView.setCheckedItem(R.id.nav_view_custom); // 并不会调用点击的逻辑，只是UI 显示
        navFragment(R.id.nav_view_custom);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        LogUtils.d("onOptionsItemSelected " + item);
        if (id == android.R.id.home) {
            LogUtils.w("onOptionsItemSelected home");
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView, true);
            } else {
                drawerLayout.openDrawer(navigationView, true);
            }
            drawerToggle.syncState();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle bottom_navigation view item clicks here.
        int id = item.getItemId();
        navFragment(id);
        return true;
    }

    private void navFragment(int id) {
        if (id == R.id.nav_view_custom) {
            showViewFragment();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_calendar) {
            // calendar
            showCalendarFragment();
        } else if (id == R.id.nav_todo) {
            showTODOFragment();
        }

        closeDrawer();
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showViewFragment() {
        com.apkfuns.logutils.LogUtils.v("show default fragment...");
        ViewFragment fragment = ViewFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseVM.jump2Target(fragmentManager, fragment,
                R.id.drawer_content_frame_layout, false, true);
//        toolbar.setTitle(fragment.getClass().getSimpleName());
    }

    private void showCalendarFragment() {
        com.apkfuns.logutils.LogUtils.v("click nav calendar...");
        CalendarFragment fragment = CalendarFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseVM.jump2Target(fragmentManager, fragment,
                R.id.drawer_content_frame_layout, false, true);
//        toolbar.setTitle(fragment.getClass().getSimpleName());
    }

    private void showTODOFragment() {
        com.apkfuns.logutils.LogUtils.v("click nav todo...");
        TODOFragment fragment = TODOFragment.newInstance();
        fragment.setNeedLogin(() -> {
            ToastHelper.show(BaseApplication.get(), "need login first!");
            showLoginFragment(success -> {

                LogUtils.w("login result...");
                if (success) {
                    navigationView.setCheckedItem(R.id.nav_todo);
                    showTODOFragment();
                } else {
                    showDefaultFragment();
                }
            });
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseVM.jump2Target(fragmentManager, fragment,
                R.id.drawer_content_frame_layout, false, true);
        toolbar.setTitle(fragment.getClass().getSimpleName());
    }

    private void showLoginFragment(LoginCallback callback) {
        MenuItem checkedItem = navigationView.getCheckedItem();
        if (checkedItem != null) {
            checkedItem.setChecked(false);
        }
        LoginFragment fragment = LoginFragment.newInstance();
        fragment.setLoginResult(callback);
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseVM.jump2Target(fragmentManager, fragment,
                R.id.drawer_content_frame_layout, false, true);
        toolbar.setTitle(fragment.getClass().getSimpleName());
    }


    @Override
    public void setFragmentTitle(String simpleName) {
        toolbar.setTitle(simpleName);
    }
}

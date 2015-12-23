package com.fedor.pavel.goodssearcher;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.fedor.pavel.goodssearcher.adapters.NavigationViewPagerAdapter;
import com.fedor.pavel.goodssearcher.data.SQLManager;
import com.fedor.pavel.goodssearcher.fragments.SavedGoodsFragment;
import com.fedor.pavel.goodssearcher.fragments.SearchGoodsFragment;

public class NavigationActivity extends AppCompatActivity {

    private TabLayout tblNavigation;

    private SQLManager sqlManager;

    private ViewPager vpNavigation;

    private NavigationViewPagerAdapter vpAdapter;

    private String LOG_TAG = "NavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);


        sqlManager = SQLManager.getInstance();

        sqlManager.open();

        vpAdapter = new NavigationViewPagerAdapter(getSupportFragmentManager(), this);

        prepareView();


    }

    private void prepareView() {

        vpNavigation = (ViewPager) findViewById(R.id.navigation_activity_vp);

        vpNavigation.setAdapter(vpAdapter);

        prepareToolBar();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("1", true);

        super.onSaveInstanceState(outState);
    }

    private void prepareToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.navigation_activity_toolbar);
        setSupportActionBar(toolbar);

        tblNavigation = (TabLayout) findViewById(R.id.navigation_activity_tbl);

        /*tblNavigation.addTab(tblNavigation.newTab().setText(getResources().getText(R.string.tab_searchGoods_title).toString()), true);

        tblNavigation.addTab(tblNavigation.newTab().setText(getResources().getText(R.string.tab_savedGoods_title).toString()));*/

        tblNavigation.setupWithViewPager(vpNavigation);

       /* tblNavigation.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                vpNavigation.setCurrentItem(tab.getPosition(),false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqlManager.close();
    }


}

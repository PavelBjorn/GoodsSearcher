package com.fedor.pavel.goodssearcher.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fedor.pavel.goodssearcher.R;
import com.fedor.pavel.goodssearcher.fragments.SavedGoodsFragment;
import com.fedor.pavel.goodssearcher.fragments.SearchGoodsFragment;


public class NavigationViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;

    private Fragment[] fragments = {new SearchGoodsFragment(), new SavedGoodsFragment()};

    private String[] pageTitles;


    public NavigationViewPagerAdapter(FragmentManager fm, Context context) {

        super(fm);

        pageTitles = new String[]{context.getResources().getText(R.string.tab_searchGoods_title).toString()
                , context.getResources().getText(R.string.tab_savedGoods_title).toString()};
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {

            return new SearchGoodsFragment();

        }

        return new SavedGoodsFragment();
    }

    @Override
    public int getCount() {

        return PAGE_COUNT;

    }

    @Override
    public CharSequence getPageTitle(int position) {

        return pageTitles[position];
    }
}

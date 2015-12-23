package com.fedor.pavel.goodssearcher.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.fedor.pavel.goodssearcher.fragments.GoodsPhotoVPFragment;
import com.fedor.pavel.goodssearcher.models.GoodsModel;


public class ViewPagerPhotoAdapter extends FragmentStatePagerAdapter {

    private GoodsModel goodsModel;

    public ViewPagerPhotoAdapter(FragmentManager fm, GoodsModel goodsModel) {
        super(fm);

        this.goodsModel = goodsModel;


    }

    @Override
    public Fragment getItem(int position) {

        GoodsPhotoVPFragment fragment = new GoodsPhotoVPFragment();

        Bundle photoUri = new Bundle();

        photoUri.putString(APIQueryConstants.API_GOODS_PHOTO_500xN_KEY, goodsModel.getPhotoUri(position).getPhotoUrl500xN());

        fragment.setArguments(photoUri);

        return fragment;
    }

    @Override
    public int getCount() {

        return goodsModel.getNumPhoto();

    }


}

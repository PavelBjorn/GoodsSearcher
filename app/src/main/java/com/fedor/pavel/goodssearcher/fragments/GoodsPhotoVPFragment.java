package com.fedor.pavel.goodssearcher.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fedor.pavel.goodssearcher.R;
import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class GoodsPhotoVPFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_vp_photos, container, false);

        ImageView imvPhoto = (ImageView) view.findViewById(R.id.view_pager_item_imv_photo);

        ImageLoader.getInstance().displayImage(getArguments().getString(APIQueryConstants.API_GOODS_PHOTO_500xN_KEY)
                , imvPhoto, displayImageOptions());

        return view;
    }

    public static DisplayImageOptions displayImageOptions() {

        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.background_ic)
                .showImageOnFail(R.drawable.background_ic)
                .showImageOnLoading(R.drawable.background_ic)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }
}

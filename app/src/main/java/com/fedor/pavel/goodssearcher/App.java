package com.fedor.pavel.goodssearcher;

import android.app.Application;

import com.fedor.pavel.goodssearcher.data.SQLManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class App extends Application {


    @Override
    public void onCreate() {

        super.onCreate();

        configureImageLoader();

        SQLManager.initialized(this);

    }

    public void configureImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheFileCount(100)
                .diskCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(8)
                .build();

        ImageLoader.getInstance().init(config);

    }
}

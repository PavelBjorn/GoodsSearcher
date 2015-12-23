package com.fedor.pavel.goodssearcher;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.fedor.pavel.goodssearcher.adapters.ViewPagerPhotoAdapter;
import com.fedor.pavel.goodssearcher.constants.SaveStateConstants;
import com.fedor.pavel.goodssearcher.data.SQLHelper;
import com.fedor.pavel.goodssearcher.data.SQLManager;
import com.fedor.pavel.goodssearcher.models.GoodsModel;
import com.google.gson.GsonBuilder;
import com.viewpagerindicator.UnderlinePageIndicator;


public class FullItemInfoActivity extends AppCompatActivity {

    private ViewPager vpPhotos;
    private ViewPagerPhotoAdapter vpPhotoAdapter;
    private TextView tvPrice, tvTitle, tvDescription;
    private GoodsModel goodsModel;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout layout;
    private FloatingActionButton flabAddItem;
    private boolean isInDb;


    private static final String LOG_TAG = "FullItemInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_full_info);

        goodsModel = new GsonBuilder().create()
                .fromJson(getIntent().getStringExtra(SaveStateConstants.SAVE_STATE_GOODS_MODEL), GoodsModel.class);

        isInDb = getIntent().getBooleanExtra(SaveStateConstants.SAVE_STATE_GOODS_IS_ITEM_IN_DB, false);

        if (!isInDb) {
            isInDb = SQLManager.getInstance().checkDbForObject(goodsModel);
        }

        prepareView();

    }

    private void prepareView() {

        findViews();

        vpPhotoAdapter = new ViewPagerPhotoAdapter(getSupportFragmentManager(), goodsModel);

        vpPhotos.setAdapter(vpPhotoAdapter);

        UnderlinePageIndicator circlePageIndicator = (UnderlinePageIndicator) findViewById(R.id.full_info_cpi_photo);


        circlePageIndicator.setViewPager(vpPhotos);

        tvPrice.setText(goodsModel.getPrice() + " " + goodsModel.getCurrenciesCode());

        String title = goodsModel.getTitle();

        if (title == null || title.isEmpty()) {
            title = "Unnamed";
        }

        tvTitle.setText(title);


        String description = goodsModel.getDescription();

        if (description == null || title.isEmpty()) {
            description = "";
        }

        tvDescription.setText(description);

        prepareFAButton();
    }

    private void prepareToolbar() {

        appBarLayout = (AppBarLayout) findViewById(R.id.full_info_appBarLayout);

        layout = (CollapsingToolbarLayout) findViewById(R.id.full_info_main_collapsing);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.full_info_activity_toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitle("");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (getSupportActionBar() != null) {

                    if (verticalOffset != 0 && Math.abs(verticalOffset) >= appBarLayout.getHeight() * 0.5) {


                        layout.setTitleEnabled(true);

                        layout.setTitle(getResources().getText(R.string.app_name));

                    } else {

                        getSupportActionBar().setTitle("");

                        layout.setTitleEnabled(false);

                    }
                }


            }
        });


        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    private void prepareFAButton() {

        if (isInDb) {

            flabAddItem.setImageResource(R.drawable.ic_remove);

        } else {

            flabAddItem.setImageResource(R.drawable.ic_add);
        }

        flabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isInDb) {

                    remove();

                    flabAddItem.setImageResource(R.drawable.ic_add);

                    isInDb = false;

                } else {

                    saveGoodsModel();

                    flabAddItem.setImageResource(R.drawable.ic_remove);

                    Snackbar.make(v, "Item saved", Snackbar.LENGTH_SHORT).show();

                    isInDb = true;

                }


            }
        });
    }

    private void findViews() {

        prepareToolbar();

        vpPhotos = (ViewPager) findViewById(R.id.full_info_vp_photo);

        tvPrice = (TextView) findViewById(R.id.full_info_tv_price);

        tvTitle = (TextView) findViewById(R.id.full_info_tv_title);

        tvDescription = (TextView) findViewById(R.id.full_info_tv_description);

        flabAddItem = (FloatingActionButton) findViewById(R.id.full_info_tv_description_flab_addItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                this.finish();

                break;
        }

        return true;

    }

    private void saveGoodsModel() {

        long goodsSavedId = SQLManager.getInstance().insertModel(SQLHelper.TABLE_NAME_GOODS, goodsModel.getContentValues());

        Log.d(LOG_TAG, "savedId" + goodsSavedId);

        for (int i = 0; i < goodsModel.getNumPhoto(); i++) {

            long photoSavedId = SQLManager.getInstance().insertModel(SQLHelper.TABLE_NAME_GOODS_PHOTO
                    , goodsModel.getPhotoUri(i).getContentValues(goodsModel.getServerId()));

            Log.d(LOG_TAG, "photoSavedId " + photoSavedId);

        }
    }

    private void remove() {

        SQLManager.getInstance().deleteGoodsModel(goodsModel);

        if (getIntent().getBooleanExtra(SaveStateConstants.SAVE_STATE_GOODS_IS_ITEM_IN_DB, false)) {

            setResult(RESULT_OK);

            finish();

        }

    }


}

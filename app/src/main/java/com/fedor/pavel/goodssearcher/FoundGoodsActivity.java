package com.fedor.pavel.goodssearcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.fedor.pavel.goodssearcher.adapters.GoodsRecyclerViewAdapter;
import com.fedor.pavel.goodssearcher.api.API;
import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.fedor.pavel.goodssearcher.constants.SaveStateConstants;
import com.fedor.pavel.goodssearcher.dialogs.AlertDialogManager;
import com.fedor.pavel.goodssearcher.models.GoodsResponse;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

// TODO: 20.12.2015 AppBar dimen for land
// TODO: 22.12.2015 Data base
// TODO: 22.12.2015 Page change indicator

public class FoundGoodsActivity extends AppCompatActivity {

    private RecyclerView rvGoodsList;

    private TextView tvMessage;

    private GoodsRecyclerViewAdapter goodsAdapter;

    private ProgressBar progressBar;

    private ProgressDialog progressDialog;

    private SwipeRefreshLayout refreshLayout;

    private static final String LOG_TAG = "FoundGoodsActivity";

    private int goodsQueryLimit;

    private int goodsQueryOffset = 0;

    private int numOfPages = 1;

    private long goodsCount;

    private boolean isLoadingData = false;

    private boolean isLimitChanged = false;

    private String keyWords;

    private String category;

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_found_goods);


        goodsAdapter = new GoodsRecyclerViewAdapter(this);

        createProgressDialog();

        prepareView();


        if (savedInstanceState != null) {

            loadState(savedInstanceState.getBundle(SaveStateConstants.SAVE_FOUND_GOODS_ACTIVITY_STATE));

        } else {

            keyWords = getIntent().getStringExtra(APIQueryConstants.API_KEYWORD_KEY);

            category = getIntent().getStringExtra(APIQueryConstants.API_CATEGORY_SEARCH_NAME_KEY);

            if (isHaveInternet()) {

                progressDialog.show();

                apiQueryFindGoods();

            } else {

                AlertDialogManager.createSettingsDialog(this);

            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBundle(SaveStateConstants.SAVE_FOUND_GOODS_ACTIVITY_STATE, saveState());

        super.onSaveInstanceState(outState);

    }

    private void prepareView() {

        findViews();

        prepareToolBar();

        rvGoodsList.setAdapter(goodsAdapter);

        int columnNum = getResources().getInteger(R.integer.recyclerView_num_Of_columns);

        goodsQueryLimit = columnNum == 2 ? 20 : 21;

        rvGoodsList.setLayoutManager(new GridLayoutManager(this, columnNum));

        rvGoodsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    pagination((GridLayoutManager) recyclerView.getLayoutManager());
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isHaveInternet()) {

                    goodsAdapter.clear();

                    apiQueryFindGoods();

                    if (snackbar != null && snackbar.isShown()) {
                        snackbar.dismiss();
                    }

                } else {

                    showSettingsSnackBar();
                    refreshLayout.setRefreshing(false);

                }

            }
        });

    }

    private void findViews() {

        rvGoodsList = (RecyclerView) findViewById(R.id.found_goods_activity_rv_goodsList);

        progressBar = (ProgressBar) findViewById(R.id.found_goods_activity_progressBar);

        tvMessage = (TextView) findViewById(R.id.found_goods_activity_tv_message);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.found_goods_activity_refreshLayOut);
    }

    private void prepareToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.found_goods_activity_toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    private void apiQueryFindGoods() {

        if (!isHaveInternet()) {

            showSettingsSnackBar();
            return;

        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(LOG_TAG))
                .setEndpoint(APIQueryConstants.API_BASE_URL)
                .build();

        API service = restAdapter.create(API.class);

        Map<String, String> queryParams = new HashMap<>();

        queryParams.put(APIQueryConstants.API_LIMIT_KEY, "" + goodsQueryLimit);

        queryParams.put(APIQueryConstants.API_OFFSET_KEY, "" + goodsQueryOffset);

        queryParams.put(APIQueryConstants.API_APP_ID_KEY, getResources().getString(R.string.applicationId));

        if (category != null && !category.isEmpty()) {
            queryParams.put(APIQueryConstants.API_CATEGORY_KEY, category);
        }

        if (keyWords != null && !keyWords.isEmpty()) {

            queryParams.put(APIQueryConstants.API_KEYWORD_KEY, keyWords);

        }

        queryParams.put(APIQueryConstants.API_INCLUDE_KEY, APIQueryConstants.API_GOODS_PHOTO_KEY);


        service.getGoods(queryParams, new Callback<GoodsResponse>() {

                    @Override
                    public void success(GoodsResponse goodsResponse, Response response) {

                        goodsCount = goodsResponse.getOverallGoodsCount();

                        goodsAdapter.addAllGoods(goodsResponse.getGoods());


                        isGoodsFound(goodsResponse.getGoods().size());

                        Log.d(LOG_TAG, "Loaded goods = " + goodsResponse.getGoods().size() + ", sum = " + goodsAdapter.getItemCount());

                        isLoadingData = false;

                        progressBar.setVisibility(View.GONE);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (refreshLayout.isRefreshing()) {

                            refreshLayout.setRefreshing(false);

                        }

                        setToolBarSubtitle();

                        if (isLimitChanged) {

                            goodsQueryLimit--;

                            isLimitChanged = false;

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {


                        Log.d("MyTag", "" + error);

                        isLoadingData = false;

                        progressBar.setVisibility(View.GONE);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (refreshLayout.isRefreshing()) {

                            refreshLayout.setRefreshing(false);

                        }

                    }
                }

        );

    }

    private void pagination(GridLayoutManager manager) {

        int itemsCount = manager.getItemCount();

        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();

        int childItemsCount = manager.getChildCount();


        if ((!isLoadingData)
                && (lastVisibleItem == itemsCount - 1)
                && (itemsCount < goodsCount)
                ) {

            numOfPages++;

            Log.d(LOG_TAG, "itemsCount = " + itemsCount
                    + ", lastVisibleItem = " + lastVisibleItem
                    + ", childItemsCount =" + childItemsCount);

            goodsQueryOffset = itemsCount;


            progressBar.setVisibility(View.VISIBLE);


            if ((goodsAdapter.getItemCount() % getResources().getInteger(R.integer.recyclerView_num_Of_columns)) != 0) {
                goodsQueryLimit++;
                isLimitChanged = true;
            }

            if (numOfPages == 5) {
                ImageLoader.getInstance().clearMemoryCache();
                numOfPages = 1;
            }

            apiQueryFindGoods();

        }

    }

    private void createProgressDialog() {

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getResources().getText(R.string.load_dialog_message));

        progressDialog.setCancelable(false);


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

    public boolean isHaveInternet() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null;

    }

    public boolean isGoodsFound(int count) {

        if (count == 0) {

            tvMessage.setVisibility(View.VISIBLE);

            rvGoodsList.setVisibility(View.GONE);

            return false;
        }

        tvMessage.setVisibility(View.GONE);

        rvGoodsList.setVisibility(View.VISIBLE);

        return true;
    }

    private Bundle saveState() {

        Bundle bundle = new Bundle();

        GoodsResponse response = new GoodsResponse();

        response.addAll(goodsAdapter.getAllGoods());

        response.setOverallGoodsCount(goodsCount);

        bundle.putString(SaveStateConstants.SAVE_STATE_GOODS_RESPONSE
                , new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(response));

        bundle.putInt(SaveStateConstants.SAVE_STATE_GOODS_QUERY_OFFSET, goodsQueryOffset);

        bundle.putString(SaveStateConstants.SAVE_STATE_KEY_WORDS, keyWords);

        bundle.putString(SaveStateConstants.SAVE_STATE_CATEGORY, category);

        bundle.putBoolean(SaveStateConstants.SAVE_STATE_IS_LOADING_DATA, isLoadingData);

        return bundle;

    }

    private void loadState(Bundle savedState) {

        GoodsResponse response = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                .fromJson(savedState.getString(SaveStateConstants.SAVE_STATE_GOODS_RESPONSE), GoodsResponse.class);

        if (isGoodsFound(response.getGoods().size())) {

            goodsAdapter.addAllGoods(response.getGoods());

            goodsCount = response.getOverallGoodsCount();

            setToolBarSubtitle();

            goodsQueryOffset = savedState.getInt(SaveStateConstants.SAVE_STATE_GOODS_QUERY_OFFSET);

            keyWords = savedState.getString(SaveStateConstants.SAVE_STATE_KEY_WORDS);

            category = savedState.getString(SaveStateConstants.SAVE_STATE_CATEGORY);

            isLoadingData = savedState.getBoolean(SaveStateConstants.SAVE_STATE_IS_LOADING_DATA);

        }

    }

    public void setToolBarSubtitle() {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setSubtitle(getResources().getText(R.string.activity_found_goods_subTitle)
                    + " " + goodsCount);
        }


    }

    private void showSettingsSnackBar() {
        snackbar = Snackbar.make(rvGoodsList, "No internet!", Snackbar.LENGTH_INDEFINITE).setAction("Settings "
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                FoundGoodsActivity.this.startActivity(intent);
                FoundGoodsActivity.this.finish();

            }

        });

        snackbar.show();
    }

}

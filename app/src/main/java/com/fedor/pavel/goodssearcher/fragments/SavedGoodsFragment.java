package com.fedor.pavel.goodssearcher.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fedor.pavel.goodssearcher.R;
import com.fedor.pavel.goodssearcher.adapters.GoodsRecyclerViewAdapter;
import com.fedor.pavel.goodssearcher.constants.SaveStateConstants;
import com.fedor.pavel.goodssearcher.listeners.GoodsLoadListener;
import com.fedor.pavel.goodssearcher.listeners.ItemNotifyChangedListener;
import com.fedor.pavel.goodssearcher.models.GoodsModel;
import com.fedor.pavel.goodssearcher.models.GoodsResponse;
import com.fedor.pavel.goodssearcher.task.GoodsLoadTask;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SavedGoodsFragment extends Fragment implements GoodsLoadListener, ItemNotifyChangedListener {

    private RecyclerView rvSavedGoods;
    private TextView tvMessage;
    private GoodsRecyclerViewAdapter goodsAdapter;
    private boolean isStateChanged = false;
    private static final String LOG_TAG = "SavedGoodsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_saved_goods, container, false);

        goodsAdapter = new GoodsRecyclerViewAdapter(this);
        goodsAdapter.isSavedGoods(true);
        goodsAdapter.setItemNotifyChangedListener(this);

        prepareViews(view);

        if (!isStateChanged) {

            searchInBd();

        } else {

            loadDataFromBundle(savedInstanceState.getBundle(SaveStateConstants.SAVE_SAVED_GOODS_FRAGMENT_STATE));

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isStateChanged) {
            isStateChanged = false;
        } else {
            searchInBd();
        }

        Log.d(LOG_TAG, "onResume");
    }

    private void prepareViews(View view) {

        rvSavedGoods = (RecyclerView) view.findViewById(R.id.saved_goods_activity_rv_goodsList);

        rvSavedGoods.setAdapter(goodsAdapter);

        rvSavedGoods.setLayoutManager(new GridLayoutManager(getActivity(), getActivity().getResources().getInteger(R.integer.recyclerView_num_Of_columns)));

        tvMessage = (TextView) view.findViewById(R.id.saved_goods_activity_tv_message);

    }

    @Override
    public void onGoodsLoadFinish(ArrayList<GoodsModel> goodsModels) {


        goodsAdapter.clear();

        if (isHaveGoods(goodsModels.size())) {

            goodsAdapter.addAllGoods(goodsModels);

        }
    }

    private boolean isHaveGoods(int count) {

        if (count == 0) {

            tvMessage.setVisibility(View.VISIBLE);

            rvSavedGoods.setVisibility(View.GONE);

            return false;

        }

        tvMessage.setVisibility(View.GONE);

        rvSavedGoods.setVisibility(View.VISIBLE);

        return true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(SaveStateConstants.SAVE_SAVED_GOODS_FRAGMENT_STATE, saveStateToBundle());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            goodsAdapter.remove(requestCode);


        }

    }

    @Override
    public void onRemove(int itemsCount) {

        isHaveGoods(itemsCount);

    }

    @Override
    public void onStartFullItemInfo(boolean start) {

        isStateChanged = start;

    }

    private void searchInBd() {

        if (goodsAdapter != null) {

            goodsAdapter.clear();

            GoodsLoadTask loadTask = new GoodsLoadTask(this);

            loadTask.execute();
        }
    }

    public Bundle saveStateToBundle() {


        GoodsResponse response = new GoodsResponse();

        response.addAll(goodsAdapter.getAllGoods());

        Bundle bundle = new Bundle();

        bundle.putString(SaveStateConstants.SAVE_STATE_GOODS_RESPONSE, new Gson().toJson(response));

        return bundle;
    }

    public void loadDataFromBundle(Bundle bundle) {

        GoodsResponse response = new Gson().fromJson(bundle.getString(SaveStateConstants.SAVE_STATE_GOODS_RESPONSE)
                , GoodsResponse.class);

        goodsAdapter.addAllGoods(response.getGoods());

    }


}

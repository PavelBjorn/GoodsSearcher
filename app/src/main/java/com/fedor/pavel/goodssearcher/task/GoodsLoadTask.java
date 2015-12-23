package com.fedor.pavel.goodssearcher.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.fedor.pavel.goodssearcher.data.SQLManager;
import com.fedor.pavel.goodssearcher.listeners.GoodsLoadListener;
import com.fedor.pavel.goodssearcher.models.GoodsModel;

import java.util.ArrayList;


public class GoodsLoadTask extends AsyncTask<Void, Void, ArrayList<GoodsModel>> {


    private GoodsLoadListener listener;


    public GoodsLoadTask(GoodsLoadListener listener) {

        this.listener = listener;

    }


    @Override
    protected ArrayList<GoodsModel> doInBackground(Void... params) {

        return SQLManager.getInstance().getGoodsModel();

    }


    @Override
    protected void onPostExecute(ArrayList<GoodsModel> goodsModels) {

        listener.onGoodsLoadFinish(goodsModels);

        super.onPostExecute(goodsModels);
    }


}

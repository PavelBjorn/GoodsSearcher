package com.fedor.pavel.goodssearcher.models;

import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GoodsResponse {

    @Expose
    @SerializedName(APIQueryConstants.API_RESULTS_KEY)
    private ArrayList<GoodsModel> goods = new ArrayList<>();

    @Expose
    @SerializedName(APIQueryConstants.API_OVERALL_GOODS_COUNT)
    private long overallGoodsCount;

    public ArrayList<GoodsModel> getGoods() {

        return goods;

    }

    public long getOverallGoodsCount() {

        return overallGoodsCount;

    }

    public void addAll(ArrayList<GoodsModel> goods) {


        this.goods.addAll(goods);

    }

    public void setOverallGoodsCount(long overallGoodsCount) {

        this.overallGoodsCount = overallGoodsCount;

    }

}

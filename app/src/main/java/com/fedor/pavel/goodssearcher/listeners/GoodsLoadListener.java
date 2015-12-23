package com.fedor.pavel.goodssearcher.listeners;


import com.fedor.pavel.goodssearcher.models.GoodsModel;

import java.util.ArrayList;

public interface  GoodsLoadListener  {


    void onGoodsLoadFinish(ArrayList<GoodsModel> goodsModel);


}

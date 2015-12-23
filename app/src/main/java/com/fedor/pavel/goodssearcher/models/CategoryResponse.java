package com.fedor.pavel.goodssearcher.models;


import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse {

    @Expose
    @SerializedName(APIQueryConstants.API_RESULTS_KEY)
    private ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    public CategoryResponse(){

    }

    public CategoryResponse(ArrayList<CategoryModel> categoryModels) {

        this.categoryModels.addAll(categoryModels);

    }

    public ArrayList<CategoryModel> getCategoryModels() {
        return categoryModels;
    }

    public void addCaregories(ArrayList<CategoryModel> categoryModels) {

        this.categoryModels.addAll(categoryModels);

    }

}

package com.fedor.pavel.goodssearcher.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fedor.pavel.goodssearcher.FoundGoodsActivity;
import com.fedor.pavel.goodssearcher.NavigationActivity;
import com.fedor.pavel.goodssearcher.R;
import com.fedor.pavel.goodssearcher.adapters.CategorySpAdapter;
import com.fedor.pavel.goodssearcher.api.API;
import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.fedor.pavel.goodssearcher.constants.SaveStateConstants;
import com.fedor.pavel.goodssearcher.models.CategoryModel;
import com.fedor.pavel.goodssearcher.models.CategoryResponse;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchGoodsFragment extends Fragment {

    private NavigationActivity activity;

    private EditText edtItemName;

    private Spinner spCategory;

    private Button btnSearch;

    private ProgressDialog progressDialog;

    private CategorySpAdapter categoryAdapter;

    private static final String LOG_TAG = "SearchGoodsFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.activity = (NavigationActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_search_goods, container, false);

        categoryAdapter = new CategorySpAdapter(getActivity());

        categoryAdapter.add(new CategoryModel(0, "all", "All categories"));

        prepareViews(view);

        if (savedInstanceState != null) {

            CategoryResponse response = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                    .fromJson(savedInstanceState.getString(SaveStateConstants.SAVE_SEARCH_FRAGMENT_STATE), CategoryResponse.class);

            categoryAdapter.addAll(response.getCategoryModels());

            Log.d(LOG_TAG, "loadData");

        } else {

            getCategoriesFromApi();

        }

        return view;
    }

    private void prepareViews(View view) {

        findViews(view);

        createProgressDialog();

        spCategory.setAdapter(categoryAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FoundGoodsActivity.class);

                intent.putExtra(APIQueryConstants.API_KEYWORD_KEY, validateQuery(edtItemName.getText().toString()));

                intent.putExtra(APIQueryConstants.API_CATEGORY_SEARCH_NAME_KEY
                        , categoryAdapter.getItem(spCategory.getSelectedItemPosition()).getRequestName());

                getActivity().startActivity(intent);

            }
        });

    }

    private void findViews(View view) {

        edtItemName = (EditText) view.findViewById(R.id.fragment_search_edt_itemName);

        spCategory = (Spinner) view.findViewById(R.id.fragment_search_sp_category);

        btnSearch = (Button) view.findViewById(R.id.fragment_search_bt_search);

    }

    private void getCategoriesFromApi() {


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(APIQueryConstants.API_BASE_URL).build();

        API service = restAdapter.create(API.class);

        progressDialog.show();

        service.getCategories(activity.getResources().getString(R.string.applicationId), new Callback<CategoryResponse>() {


            @Override
            public void success(CategoryResponse categoryResponse, Response response) {

                Log.d("MyTag", categoryResponse.getCategoryModels().size() + "");

                categoryAdapter.addAll(categoryResponse.getCategoryModels());

                categoryAdapter.notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {

                Log.d("MyTag", error + "");

                progressDialog.dismiss();
            }
        });

    }

    private void createProgressDialog() {

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage(getResources().getText(R.string.load_dialog_message));

        progressDialog.setCancelable(false);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        outState.putString(SaveStateConstants.SAVE_SEARCH_FRAGMENT_STATE
                , new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                .toJson(new CategoryResponse(categoryAdapter.getObjects())));

        Log.d(LOG_TAG, "onSaveInstanceState");

        super.onSaveInstanceState(outState);
    }

    private String validateQuery(String string) {

        return string.toLowerCase().trim();

    }


}

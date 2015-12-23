package com.fedor.pavel.goodssearcher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fedor.pavel.goodssearcher.R;
import com.fedor.pavel.goodssearcher.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;


public class CategorySpAdapter extends ArrayAdapter<CategoryModel> {

    private int itemId = R.layout.item_category_adapter;
    private int dropItemId = R.layout.item_drop_down_category_adapter;

    public CategorySpAdapter(Context context) {
        super(context, 0);
    }

    public CategorySpAdapter(Context context, List<CategoryModel> objects) {

        super(context, 0, objects);

    }

    public ArrayList<CategoryModel> getObjects() {

        ArrayList<CategoryModel> objects = new ArrayList<>();

        for (int i = 1; i < getCount(); i++) {

            objects.add(getItem(i));

        }

        return objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView != null) {

            view = convertView;

        } else {

            view = LayoutInflater.from(getContext()).inflate(itemId, parent, false);

        }

        TextView tvTitle = (TextView) view.findViewById(android.R.id.text1);

        tvTitle.setText(getItem(position).getLongName().trim());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {

            view = convertView;

        } else {

            view = LayoutInflater.from(getContext()).inflate(dropItemId, parent, false);

        }

        TextView tvTitle = (TextView) view.findViewById(android.R.id.text1);

        tvTitle.setText(getItem(position).getLongName().trim());
        return view;
    }
}

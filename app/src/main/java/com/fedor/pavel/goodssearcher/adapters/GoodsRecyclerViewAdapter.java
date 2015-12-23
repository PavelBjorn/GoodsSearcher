package com.fedor.pavel.goodssearcher.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedor.pavel.goodssearcher.FullItemInfoActivity;
import com.fedor.pavel.goodssearcher.R;
import com.fedor.pavel.goodssearcher.constants.SaveStateConstants;
import com.fedor.pavel.goodssearcher.data.SQLManager;
import com.fedor.pavel.goodssearcher.listeners.ItemNotifyChangedListener;
import com.fedor.pavel.goodssearcher.models.GoodsModel;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;


public class GoodsRecyclerViewAdapter extends RecyclerView.Adapter<GoodsRecyclerViewAdapter.GoodsViewHolder> {


    private ArrayList<GoodsModel> goods = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Fragment fragment;
    private boolean isSavedGods = false;
    private ItemNotifyChangedListener listener;
    private static final String LOG_TAG = "GoodsRViewAdapter";


    public GoodsRecyclerViewAdapter(Context context) {

        this.context = context;

        inflater = LayoutInflater.from(context);

    }

    public GoodsRecyclerViewAdapter(Fragment fragment) {

        this.fragment = fragment;

        this.context = fragment.getActivity();

        inflater = LayoutInflater.from(fragment.getActivity());

    }

    public GoodsRecyclerViewAdapter(Context context, ArrayList<GoodsModel> goods) {

        this.context = context;

        inflater = LayoutInflater.from(context);

        this.goods.addAll(goods);

    }

    public GoodsModel getGoodsModel(int position) {

        if (position > goods.size() - 1 || position < 0) {
            return goods.get(0);
        }

        return goods.get(position);

    }

    public void addGoodsModel(GoodsModel goodsModel) {

        goods.add(goodsModel);

    }

    public void addAllGoods(ArrayList<GoodsModel> goods) {

        int start = this.goods.size();

        this.goods.addAll(goods);

        int end = goods.size();

        notifyItemRangeInserted(start, end);

    }

    public ArrayList<GoodsModel> getAllGoods() {
        return goods;
    }

    public void clear() {

        int end = goods.size();

        goods.clear();

        notifyItemRangeRemoved(0, end);
    }

    public void remove(int position) {

        goods.remove(position);

        notifyItemRemoved(position);

        if (listener != null) {
            listener.onRemove(goods.size());
        }

    }

    public void isSavedGoods(boolean isSavedGods) {
        this.isSavedGods = isSavedGods;

    }

    public void setItemNotifyChangedListener(ItemNotifyChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GoodsViewHolder(inflater.inflate(R.layout.item_recycler_view_goods, parent, false));

    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {

        String title = goods.get(position).getTitle();

        if (title == null || title.isEmpty()) {
            title = "Unnamed";
        }

        holder.tvTitle.setText(title);

        String imgUrl = null;

        try {

            imgUrl = goods.get(position).getPhotoUri(0).getPhotoUrl170x135();

        } catch (Exception e) {

            Log.d(LOG_TAG,""+e);

        }

        if (imgUrl != null) {
            ImageLoader.getInstance().displayImage(imgUrl, holder.imvPhoto, displayImageOptions());
        }

    }

    @Override
    public int getItemCount() {

        return goods.size();

    }

    public static DisplayImageOptions displayImageOptions() {

        return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.background_ic)
                .showImageForEmptyUri(R.drawable.background_ic)
                .showImageOnFail(R.drawable.background_ic)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        ImageView imvPhoto;

        ImageButton ibtnDel;

        FrameLayout flDel;

        FrameLayout flClick;

        public GoodsViewHolder(View itemView) {

            super(itemView);

            findViews();

            flClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, FullItemInfoActivity.class);

                    intent.putExtra(SaveStateConstants.SAVE_STATE_GOODS_MODEL, new GsonBuilder().create()
                            .toJson(goods.get(getAdapterPosition())));

                    intent.putExtra(SaveStateConstants.SAVE_STATE_GOODS_IS_ITEM_IN_DB, isSavedGods);

                    if (fragment == null) {
                        context.startActivity(intent);
                    } else {
                        fragment.startActivityForResult(intent, getAdapterPosition());
                    }

                }
            });

        }

        private void findViews() {

            tvTitle = (TextView) itemView.findViewById(R.id.recyclerView_goods_item_tv_title);

            imvPhoto = (ImageView) itemView.findViewById(R.id.recyclerView_goods_item_imv_photo);

            flClick = (FrameLayout) itemView.findViewById(R.id.recyclerView_goods_item_fl_click);

            if (isSavedGods) {

                ibtnDel = (ImageButton) itemView.findViewById(R.id.recyclerView_goods_item_ibtn_del);

                flDel = (FrameLayout) itemView.findViewById(R.id.recyclerView_goods_item_fl_del);

                flDel.setVisibility(View.VISIBLE);

                ibtnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        SQLManager.getInstance().deleteGoodsModel(goods.get(getAdapterPosition()));

                        remove(getAdapterPosition());

                    }
                });


            }
        }


    }


}

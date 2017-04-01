package com.d.dao.a.ui.fragment.meizi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.dao.a.R;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by dao on 2017/3/30.
 */

public class MeiZiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private SimpleDateFormat sdf;
    private Context mContext;
    private List<DataEntities> mList = new ArrayList<>();
    private LifecycleTransformer lifecycleTransformer;


    public MeiZiTypeAdapter(Context context, List<DataEntities> list, LifecycleTransformer<Object> objectLifecycleTransformer) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.lifecycleTransformer = objectLifecycleTransformer;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.
                inflate(R.layout.item_fragment_meizi_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //图片类型
        final DataEntities entities = mList.get(position);
        final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        GlideUtils.display(mContext, imageViewHolder.image, entities.getUrl());
        //设置点击事件
//        RxView.clicks(imageViewHolder.ll_continear)
//                .compose(lifecycleTransformer)
//                .throttleFirst(1L, TimeUnit.SECONDS).subscribe(new Consumer() {
//            @Override
//            public void accept(Object aVoid) throws Exception {
//                if (mClickListener != null) {
//                    mClickListener.imageItemClick(entities, imageViewHolder.image);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        View itemView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private ItemClickListener mClickListener;

    public void setOnItemClickListener(ItemClickListener listener) {
        mClickListener = listener;
    }

    public interface ItemClickListener {
        void imageItemClick(DataEntities dataEntities, ImageView imageView);
    }

    public void addAll(Collection<DataEntities> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public void setNewData(Collection<DataEntities> collection) {
        mList.clear();
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public List getData() {
        return mList;
    }
}

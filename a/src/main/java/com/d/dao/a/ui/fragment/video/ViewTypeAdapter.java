package com.d.dao.a.ui.fragment.video;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.dao.a.R;
import com.d.dao.a.bean.kaiyan.DataBean;
import com.d.dao.a.bean.kaiyan.ItemListBean;
import com.d.dao.a.utils.DateUtil;
import com.d.dao.zlibrary.baseutils.AutoUtils;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dao on 2017/3/23.
 */

public class ViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<ItemListBean> mList = new ArrayList<>();

    public ViewTypeAdapter(Context context, List<ItemListBean> list) {
        this.mList = list;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == R.layout.video_item) {
            return new MyHolder(mLayoutInflater.inflate(viewType, parent, false));
        }
        return new TopHolder(mLayoutInflater.inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemListBean itemListBean = mList.get(position);
        if (holder instanceof MyHolder) {
            final MyHolder myHolder = (MyHolder) holder;
            ViewCompat.setTransitionName(myHolder.imageView, String.valueOf(position) + "_image");
            DataBean data = itemListBean.getData();

            GlideUtils.display(context, myHolder.imageView, data.getCover().getDetail(),null);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(position, v, myHolder);
                    }
                }
            });

            myHolder.textView.setText(data.getTitle());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#").append(data.getCategory())
                    .append(" ")
                    .append(" / ")
                    .append(" ")
                    .append(DateUtil.formatTime2(data.getDuration()));
            myHolder.description.setText(stringBuilder.toString());
        }
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getType().equals("video")) {
            if (position == 0) {
                return R.layout.top_item;
            }
            return R.layout.video_item;
        } else {
            return R.layout.null_item;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView description;
        public ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            textView = ((TextView) itemView.findViewById(R.id.title));
            description = ((TextView) itemView.findViewById(R.id.description));
            imageView = ((ImageView) itemView.findViewById(R.id.img));
        }
    }


    public static class TopHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TopHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            textView = ((TextView) itemView.findViewById(R.id.name));
        }
    }

    protected OnItemClickListener mClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view, RecyclerView.ViewHolder vh);
    }

    public void addAll(Collection<ItemListBean> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public void setNewData(Collection<ItemListBean> collection) {
        mList.clear();
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public List getData() {
        return mList;
    }

}
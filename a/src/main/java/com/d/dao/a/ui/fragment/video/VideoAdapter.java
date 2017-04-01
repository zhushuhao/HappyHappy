package com.d.dao.a.ui.fragment.video;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.dao.a.R;
import com.d.dao.a.bean.kaiyan.DataBean;
import com.d.dao.a.bean.kaiyan.ItemListBean;
import com.d.dao.a.ui.base.BaseMultiTypeRecyclerAdapter;
import com.d.dao.a.utils.DateUtil;
import com.d.dao.zlibrary.baseutils.AutoUtils;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;

import java.util.List;


/**
 * Created by dao on 2017/3/23.
 */

public class VideoAdapter extends BaseMultiTypeRecyclerAdapter<ItemListBean,
        RecyclerView.ViewHolder> {


    public VideoAdapter(Context context, List<ItemListBean> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType().equals("video")) {
            if (position == 0) {
                return R.layout.top_item;
            }
            return R.layout.video_item;
        } else {
            return R.layout.null_item;
        }
    }

    @Override
    public RecyclerView.ViewHolder getCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.video_item) {
            return new MyHolder(inflater.inflate(viewType, parent, false));
        }
        return new TopHolder(inflater.inflate(viewType, parent, false));
    }

    @Override
    public void getBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemListBean itemListBean = list.get(position);
        if (holder instanceof MyHolder) {
            final MyHolder myHolder = (MyHolder) holder;
            ViewCompat.setTransitionName(myHolder.imageView, String.valueOf(position) + "_image");
            DataBean data = itemListBean.getData();

            GlideUtils.display(context, myHolder.imageView, data.getCover().getDetail());

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
}

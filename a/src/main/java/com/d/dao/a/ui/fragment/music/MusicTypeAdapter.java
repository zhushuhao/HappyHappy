package com.d.dao.a.ui.fragment.music;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.dao.a.R;
import com.d.dao.a.bean.MusicListBean;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<MusicListBean> mList = new ArrayList<>();

    public MusicTypeAdapter(Context context, List<MusicListBean> list) {
        this.mList = list;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        KLog.e("size ->" + list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.music_item) {
            return new MusicTypeAdapter.MyViewHolder(mLayoutInflater.inflate(viewType, parent, false));
        } else {
            return new MusicTypeAdapter.TopHolder(mLayoutInflater.inflate(viewType, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            MusicListBean musicListBean = mList.get(position);
            ViewCompat.setTransitionName(viewHolder.imageView, String.valueOf(position) + "_image");
            GlideUtils.display(context, viewHolder.imageView, musicListBean.getOphoto());
            viewHolder.title.setText(musicListBean.getMname());
            viewHolder.description.setText(musicListBean.getMdesc());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(position, v, holder);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return R.layout.top_item;
        }
        return R.layout.music_item;

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView imageView;
        public ExpandableTextView expandedDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = ((TextView) itemView.findViewById(R.id.title));
            description = ((TextView) itemView.findViewById(R.id.description));
            imageView = ((ImageView) itemView.findViewById(R.id.img));
        }
    }

    public static class TopHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TopHolder(View itemView) {
            super(itemView);
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

    public void addAll(Collection<MusicListBean> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public void setNewData(Collection<MusicListBean> collection) {
        mList.clear();
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public List getData() {
        return mList;
    }
}

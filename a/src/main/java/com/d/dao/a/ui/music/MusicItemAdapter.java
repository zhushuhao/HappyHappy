package com.d.dao.a.ui.music;

import com.d.dao.a.R;
import com.d.dao.a.bean.wy.TracksBean;
import com.d.dao.zlibrary.baserecyclerview.BaseRecyclerAdapter;
import com.d.dao.zlibrary.baserecyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by dao on 2017/3/29.
 */

public class MusicItemAdapter extends BaseRecyclerAdapter<TracksBean> {

    public MusicItemAdapter(int layoutResId, List<TracksBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TracksBean item) {
        helper.setText(R.id.title_num, (helper.getLayoutPosition() + 1) + "");
        helper.setText(R.id.title, item.getSongname());
        helper.setText(R.id.title_dis, item.getSonger());
    }
}

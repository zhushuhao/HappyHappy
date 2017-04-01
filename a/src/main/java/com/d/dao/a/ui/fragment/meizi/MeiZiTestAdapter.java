package com.d.dao.a.ui.fragment.meizi;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.d.dao.a.R;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.zlibrary.baserecyclerview.BaseRecyclerAdapter;
import com.d.dao.zlibrary.baserecyclerview.BaseViewHolder;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;

import java.util.List;

/**
 * Created by dao on 2017/3/30.
 */

public class MeiZiTestAdapter extends BaseRecyclerAdapter<DataEntities> {

    public MeiZiTestAdapter(int layoutResId, List<DataEntities> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataEntities item) {
        if (helper.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)
                    helper.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
        ImageView iv = helper.getView(R.id.image);
        iv.getLayoutParams().height = 200 + helper.getLayoutPosition() % 10 * 30;
        GlideUtils.display(mContext, iv, item.getUrl(), null);
    }
}

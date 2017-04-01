package com.d.dao.a.ui.fragment.meizi;

import android.widget.ImageView;

import com.d.dao.a.R;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.zlibrary.baserecyclerview.BaseRecyclerAdapter;
import com.d.dao.zlibrary.baserecyclerview.BaseViewHolder;
import com.d.dao.zlibrary.baseutils.UIUtils;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;

import java.util.List;

/**
 * Created by dao on 2017/3/30.
 */

public class MeiZiAdapter extends BaseRecyclerAdapter<DataEntities> {

    public MeiZiAdapter(int layoutResId, List<DataEntities> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataEntities item) {
        ImageView iv = helper.getView(R.id.image);
        iv.getLayoutParams().height = 200 + helper.getLayoutPosition() % 10 * 30;
        iv.getLayoutParams().width = UIUtils.getScreenWidth() / 2;
        GlideUtils.display(mContext, iv, item.getUrl(), null);

    }
}

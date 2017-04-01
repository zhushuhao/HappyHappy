package com.d.dao.a.ui.base;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.base.fragment.ZBaseFragment;

/**
 * Created by dao on 2017/3/23.
 */

public abstract class BaseFragment<T extends ZBasePresenter, E extends ZBaseModel> extends
        ZBaseFragment<T, E> {
    /**
     * 视图加载完毕，交给子类处理
     */
    @Override
    protected void dispatchLogicToChild() {

    }
}

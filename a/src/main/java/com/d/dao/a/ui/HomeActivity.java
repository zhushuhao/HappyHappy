package com.d.dao.a.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.d.dao.a.R;
import com.d.dao.a.ui.base.BaseActivity;
import com.d.dao.a.ui.fragment.meizi.MeiZiFragment;
import com.d.dao.a.ui.fragment.music.MusicFragment;
import com.d.dao.a.ui.fragment.video.VideoFragment;
import com.d.dao.zlibrary.basefragmenthelper.FragmentController;

import java.util.ArrayList;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private FragmentController mFragmentController;

//    @BindView(R.id.floating_search_view)
//    FloatingSearchView mSearchView;

    @BindView(R.id.navigation)
    BottomNavigationView mBNV;


    /**
     * 布局Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        initRxListener();
        initFragments();
        mBNV.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void initRxListener() {
//        mRxManager.on(Constants.VIDEO_SCROLL_FLAG, new Action1<Integer>() {
//            @Override
//            public void call(Integer i) {
//                KLog.e("接收到标志");
//                if (Constants.VIDEO_SCROLL_FLAG_START == i) {//滑动开始
//                    KLog.e("接收到开始标志");
//                    if (mSearchView.getVisibility() == View.VISIBLE) {
//                        mSearchView.setVisibility(View.INVISIBLE);
//                    }
//
//                } else if (Constants.VIDEO_SCROLL_FLAG_END == i) {//滑动结束
//                    KLog.e("接收到结束标志");
//                    if (mSearchView.getVisibility() == View.INVISIBLE) {
//                        mSearchView.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });
    }

    private void initFragments() {

        mFragmentList.add(new VideoFragment());
        mFragmentList.add(new MusicFragment());
        mFragmentList.add(new MeiZiFragment());

        mFragmentController = FragmentController.getInstance(HomeActivity.this, R.id.content, mFragmentList);
        mFragmentController.showFragment(0);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToFragment(0);
                    return true;
                case R.id.navigation_dashboard:
                    switchToFragment(1);
                    return true;
                case R.id.navigation_notifications:
                    switchToFragment(2);
                    return true;
            }
            return false;
        }

    };

    private void switchToFragment(int index) {
        if (index < 0) {
            index = 0;
        } else if (index > mFragmentList.size() - 1) {
            index = mFragmentList.size() - 1;
        }
//        mBNV.setBackgroundColor(Color.parseColor("#000000"));
//        StatusBarUtil.setColor(HomeActivity.this, Color.parseColor("#000000"), 0);
        mFragmentController.showFragment(index);

    }
}

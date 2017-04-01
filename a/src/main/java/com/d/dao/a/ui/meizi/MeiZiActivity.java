package com.d.dao.a.ui.meizi;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d.dao.a.R;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.a.ui.base.BaseActivity;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/31.
 */

public class MeiZiActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv)
    SubsamplingScaleImageView iv;
    private DataEntities mItem;

    /**
     * 布局Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_meizi;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        initOutData();

        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbar.getVisibility() == View.VISIBLE) {
                    mToolbar.setVisibility(View.GONE);
                } else {
                    mToolbar.setVisibility(View.VISIBLE);
                }
            }
        });
//        iv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                if (mToolbar.getVisibility() == View.VISIBLE) {
//                    mToolbar.setVisibility(View.GONE);
//                } else {
//                    mToolbar.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    private void initOutData() {
        mItem = (DataEntities) getIntent().getSerializableExtra("item");
        setImage();


    }

    private void setImage() {
        final File downDir = Environment.getExternalStorageDirectory();
        //使用Glide下载图片,保存到本地
        Glide.with(this)
                .load(mItem.getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        File file1 = new File(downDir, "/Glide/");
                        if (!file1.exists()) {
                            file1.mkdir();
                        }
                        File file2 = new File(file1, "m_1385635534691.jpg");
                        if (!file2.exists()) {
                            try {
                                file2.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        FileOutputStream fout = null;
                        try {
                            //保存图片
                            fout = new FileOutputStream(file2);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                            // 将保存的地址给SubsamplingScaleImageView,这里注意设置ImageViewState
                            //, new ImageViewState(1.0F, new PointF(0, 0), 0)
                            iv.setImage(ImageSource.uri(file2.getAbsolutePath()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fout != null) fout.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iv.recycle();
    }
}

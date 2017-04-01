package com.d.dao.a.ui.musicplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d.dao.a.R;
import com.d.dao.a.bean.wy.TracksBean;
import com.d.dao.zlibrary.baseutils.AutoUtils;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;
import com.socks.library.KLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by cuieney on 16/6/13.
 */
public class CoverFlowAdapter extends PagerAdapter {
    private List<TracksBean> list;
    private LayoutInflater inflater;
    private Context context;
    private List<WeakReference<View>> viewList = new ArrayList<>();
    private Map<Integer, Integer> colorMap = new HashMap<>();

    public CoverFlowAdapter(List<TracksBean> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        // 从废弃的里去取 取到则使用 取不到则创建
        if (viewList.size() > 0 && viewList.get(0) != null) {
            view = initView(viewList.get(0).get(), position);
        } else {
            view = initView(null, position);
        }
        container.addView(view);
        return view;
    }

    private View initView(View view, int position) {
        MyHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.play_music_item, null);
            holder = new MyHolder(view);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        /**
         * 初始化数据
         */
        if (list != null && position < list.size()) {
            TracksBean musicPlayerItem = list.get(position);
            initColor(holder, musicPlayerItem, position);
            GlideUtils.display(context, holder.bgImage, musicPlayerItem.getSongphoto());
        }

        return view;
    }


    int color = 0xff000000;

    private void initColor(final MyHolder holder, TracksBean tracksBean, final int position) {

        KLog.e(position);
        if (colorMap.containsKey(position)) {
            holder.container.setBackgroundColor(colorMap.get(position));
            if (mOnChangeColor != null) {
                mOnChangeColor.onChange(colorMap.get(position), position);
            }
            KLog.e("adapter 颜色->" + colorMap.get(position));
        } else {
            Glide.with(context).load(tracksBean.getSongphoto())
                    .asBitmap()
                    .override(300, 300)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    try {
                                        color = palette.getLightMutedSwatch().getRgb();
                                        holder.container.setBackgroundColor(color);
                                        colorMap.put(position, color);
                                        if (mOnChangeColor != null) {
                                            mOnChangeColor.onChange(color, position);
                                        }
                                        KLog.e("adapter 颜色->" + color);

                                        if (position == list.size() - 1) {
                                            KLog.e("adpter");
                                            for (Integer i : colorMap.values()) {
                                                KLog.e(i + "\n");
                                            }
                                        }
                                    } catch (Exception e) {
                                        color = 0xff000000;
                                        holder.container.setBackgroundColor(color);
                                        colorMap.put(position, color);
                                        if (mOnChangeColor != null) {
                                            mOnChangeColor.onChange(color, position);
                                        }
                                        e.printStackTrace();
                                        KLog.e(e.toString());
                                    }

                                }
                            });
                        }
                    });
        }


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        if (object instanceof View) {
//            View view = (View) object;
//            container.removeView(view);
//            viewList.add(new WeakReference<>(view));
//
//        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


    private static final class MyHolder {
        private final ImageView bgImage;
        private final ImageView collection;
        private final ImageView download;
        private final ImageView share;
        private final LinearLayout container;
        private View view;

        private MyHolder(View itemView) {
            view = itemView;
            AutoUtils.auto(view);
            bgImage = ((ImageView) view.findViewById(R.id.cover_img));
            collection = ((ImageView) view.findViewById(R.id.collect_icon));
            download = ((ImageView) view.findViewById(R.id.download_icon));
            share = ((ImageView) view.findViewById(R.id.share_icon));
            container = ((LinearLayout) view.findViewById(R.id.container));
        }

    }

    public void onDestroy() {
        viewList.clear();
        viewList = null;
        colorMap.clear();
        colorMap = null;
    }

    private OnChangeColor mOnChangeColor;

    public void setOnColorChanged(OnChangeColor onColorChanged) {
        this.mOnChangeColor = onColorChanged;
    }

    interface OnChangeColor {
        void onChange(int color, int position);
    }

    public void onScrolled(int position) {
        if (position == list.size() - 2 || position == list.size() - 1) {
            if (list != null && position < list.size()) {
                if (viewList.size() > 0 && viewList.get(0) != null) {
                    initView(viewList.get(0).get(), position);
                } else {
                    initView(null, position);
                }
            }
        }
    }
}

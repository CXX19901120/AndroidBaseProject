package com.mls.scm.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mls.scm.R;
import com.mls.scm.application.ActivityManager;
import com.mls.scm.application.AppContext;
import com.mls.scm.constant.Common;
import com.mls.scm.util.UIUtils;
import com.mls.scm.widget.PtrDefaultHeader;
import com.mls.scm.widget.PullToLoadMoreListView;
import com.orhanobut.logger.Logger;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**不需要滑动返回的Activity的基类
 * ===========================
 * 本类主要做了以下工作：
 * 1.开启一个新的Activity将Activity放入一个新的栈中，退出时从栈中出栈
 * 2.添加startActivity的统一调用方法
 * 3.添加统一的Toast方法showToast()
 * 4.添加统一的加载中布局需要时调用addLodingView()接口请求成功后会自动移除
 * 5.添加统一的错误布局：网络连接错误，服务端返回错误可自定义
 *   【因为统一处理的原因如果想统一交给父类处理子类的布局文件需要使用FrameLayout命名为fl_main
 *   如果使用linerlayout添加的布局会挤出屏幕】
 *   【如果为网络错误的时候想实现点击重新加载可以直接实现onNetErrorClick（）方法即可】
 * 6.添加统一的刷新和上拉加载布局子类子需要调用addRefresh()即可
 * ============================
 *
 * Created by CXX on 2015/9/24.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    protected Context mContext;
    public boolean canRefresh = false;

    @Override
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
        mContext = this;
        initView();
        initData(bundle);
        ActivityManager.getActivityManager().pushActivity(this);

    }

    /**
     * 当子类需要刷新时 直接调用此方法会生成对应的refresh如果需要上拉加载会生成loadMore方法
     * refresh和loadMore为两个空实现
     * @param ptrMain
     * @param lvContent
     */
    public void addRefresh(PtrFrameLayout ptrMain, final ListView lvContent) {
        PtrDefaultHeader header = new PtrDefaultHeader(this);
        ptrMain.setHeaderView(header);
        ptrMain.addPtrUIHandler(header);
        ptrMain.disableWhenHorizontalMove(true);
        ptrMain.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (canRefresh) {
                    return true;
                }
                int firstVisiblePosition = lvContent.getFirstVisiblePosition();
                View childAt = lvContent.getChildAt(0);
                if (childAt == null) {
                    return false;
                }
                int top = lvContent.getChildAt(0).getTop();
                return (top == 0 && firstVisiblePosition == 0) ? true : false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh(frame);
            }
        });

        if (lvContent instanceof PullToLoadMoreListView) {
            ((PullToLoadMoreListView)lvContent).setOnLoadMoreListener(() -> loadMore());
        }

    }

    /**
     * 下拉刷新
     *
     * @param frame
     */
    public void refresh(PtrFrameLayout frame) {

    }


    /**
     * 上拉加载
     */
    public void loadMore() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getActivityManager().popActivity(this);
        removeEmptyView();
        removeLodingView();
        removeNetErrorView();

    }

    @Override
    protected void onPause() {
        super.onPause();
        UIUtils.closeInputServer(this);
    }

    protected abstract void initView();

    protected abstract void initData(Bundle bundle);

    protected ListView initTitle(String title) {
        TextView txtTitle = (TextView) findViewById(R.id.txt_action_title);
        txtTitle.setText(title);
        findViewById(R.id.img_action_left).setOnClickListener(this);
        return null;
    }

    protected void initTitle(String title, String content) {
        TextView txtTitle = (TextView) findViewById(R.id.txt_action_title);
        txtTitle.setOnClickListener(this);
        TextView txtRight = (TextView) findViewById(R.id.txt_action_right);
        txtTitle.setText(title);
        txtRight.setText(content);
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setOnClickListener(this);
        txtTitle.setOnClickListener(this);
        findViewById(R.id.img_action_left).setOnClickListener(this);
    }

    protected void initTitle(String title, int drawright) {
        TextView txtTitle = (TextView) findViewById(R.id.txt_action_title);
        TextView txtRight = (TextView) findViewById(R.id.txt_action_right);
        txtTitle.setText(title);
        txtRight.setVisibility(View.GONE);
        ImageView imgRight = (ImageView) findViewById(R.id.img_right);
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setOnClickListener(this);
        imgRight.setImageResource(drawright);
        findViewById(R.id.img_action_left).setOnClickListener(this);
    }

    protected void initTitle(String title, String content, boolean isShow) {
        TextView txtTitle = (TextView) findViewById(R.id.txt_action_title);
        txtTitle.setOnClickListener(this);
        TextView txtRight = (TextView) findViewById(R.id.txt_action_right);
        txtTitle.setText(title);
        txtRight.setText(content);
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setOnClickListener(this);
        txtTitle.setOnClickListener(this);
        if (!isShow) {
            findViewById(R.id.img_action_left).setVisibility(View.GONE);
        } else {
            findViewById(R.id.img_action_left).setOnClickListener(this);
        }
    }


    protected void initTitle(String title, boolean isShow) {
        TextView txtTitle = (TextView) findViewById(R.id.txt_action_title);
        txtTitle.setOnClickListener(this);
        txtTitle.setText(title);
        txtTitle.setOnClickListener(this);
        if (!isShow) {
            findViewById(R.id.img_action_left).setVisibility(View.GONE);
        } else {
            findViewById(R.id.img_action_left).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_action_left) finish();
    }

    /**
     * @param fromClass
     * @param toClass
     */
    public void startActivity(Context fromClass, Class<?> toClass) {
        startActivity(fromClass, toClass, null);
    }

    /**
     * @param fromClass
     * @param toClass
     * @param bundle
     */
    public void startActivity(Context fromClass, Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(fromClass, toClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        fromClass.startActivity(intent);
    }


    protected void instantiateFrament(int containerId, Fragment fgm,
                                      String fragmentName) {
        try {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(containerId, fgm, fragmentName);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void instantiateFrament(int containerId, Fragment fgm) {
        try {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(containerId, fgm);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivityForResult(Activity fromClass, Class<?> toClass, int requestCode) {
        startActivityForResult(fromClass, toClass, requestCode, null);
    }


    public void startActivityForResult(Activity fromClass, Class<?> toClass, int requestCode, Bundle data) {
        Intent intent = new Intent();
        intent.setClass(fromClass, toClass);
        if (null != data) {
            intent.putExtras(data);
        }
        fromClass.startActivityForResult(intent, requestCode);
    }

    public void showToast(String msg) {
        AppContext.getInstance().showToast(msg);
    }

    public void showToast(int resId) {
        AppContext.getInstance().showToast(resId);
    }


    View emptyView;

    /**
     * 添加界面为空的布局 根布局要为FrameLayout
     */
    public void addEmptyView() {
        canRefresh = true;
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        emptyView = UIUtils.inflate(R.layout.view_empty);
        emptyView.findViewById(R.id.lin_main).setOnClickListener(v -> {

        });
        if (flMain!=null)
            flMain.addView(emptyView);
    }

    /**
     * 移除界面为空的布局
     */
    public void removeEmptyView() {
        canRefresh = false;
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        if (emptyView != null&&flMain!=null)
            flMain.removeView(emptyView);
    }

    /**
     * 添加界面为空的布局 根布局要为FrameLayout
     */
    public void addEmptyView(int imgSrc, String content) {
        canRefresh = true;
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        if (emptyView == null&&flMain!=null) {
            emptyView = UIUtils.inflate(R.layout.view_empty);
            ImageView ivContent = (ImageView) emptyView.findViewById(R.id.img_content);
            TextView tvContent = (TextView) emptyView.findViewById(R.id.tv_content);
            ivContent.setImageResource(imgSrc);
            tvContent.setText(content);
            emptyView.findViewById(R.id.lin_main).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            flMain.addView(emptyView);
        }

    }

    private View netErrorView;

    /**
     * 添加网络请求错误布局
     */
    public void addNetErrorView() {
        if (netErrorView != null) {
            return;
        }
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        netErrorView = UIUtils.inflate(R.layout.view_net_error);
        LinearLayout linMain = (LinearLayout) netErrorView.findViewById(R.id.lin_net_main);
        if (flMain != null) {
            flMain.addView(netErrorView);
            linMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNetErrorClick();
                }
            });
        }

    }

    /**
     * 点击网络错误的按钮 空实现交给子类实现
     */
    public void onNetErrorClick() {
        Logger.e("点击网络错误");
    }

    /**
     * 移除网络错误布局
     */
    public void removeNetErrorView() {
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        if (netErrorView != null&&flMain!=null) {
            flMain.removeView(netErrorView);
        }
        netErrorView = null;
    }

    /**
     * 添加错误布局界面
     */
    public void addErrorView(int errorCode) {
        if (errorCode == Common.ERROR_NET) {
            addNetErrorView();
        } else if (errorCode == Common.NO_DATA_ERRO) {
            addEmptyView();
        } else {
            addEmptyView();
        }
    }

    /**
     * 添加错误布局界面
     */
    public void addErrorView(int errorCode, int imgSrc, String content) {
        if (errorCode == Common.ERROR_NET) {
            addNetErrorView();
        } else if (errorCode == Common.NO_DATA_ERRO) {
            addEmptyView(imgSrc, content);
        } else {
            addEmptyView();
        }
    }

    /**
     * 添加错误布局
     *
     * @param errorCode 错误码
     * @param pageSize  当前listview中有多少个数据
     * @param lvContent 需要上拉加载的listview
     */
    public void addErrorView(int errorCode, int pageSize, PullToLoadMoreListView lvContent) {
        if (errorCode == Common.ERROR_NET) {
            addNetErrorView();
        } else if (errorCode == Common.NO_DATA_ERRO) {
            if (pageSize > 0) {
                lvContent.loadMoreComplete();
                lvContent.setHasMore(false);
            } else {
                addEmptyView();
            }

        } else {
            removeLodingView();
            addEmptyView();
        }
    }
    /**
     * 特殊情况 如当获取到数据为第二页返回no_date进行的特殊处理
     * @param errorCode 错误码
     * @param imgSrc 错误提示界面显示的图片
     * @param content 错误提示界面显示错误提醒的文字
     * @param pageSize 每页获取的数据个数
     * @param lvContent 当前的listview
     */
    public void addErrorView(int errorCode, int imgSrc, String content,int pageSize, PullToLoadMoreListView lvContent) {
        if (errorCode == Common.ERROR_NET) {
            addNetErrorView();
        } else if (errorCode == Common.NO_DATA_ERRO) {
            if (pageSize > 0) {
                lvContent.loadMoreComplete();
                lvContent.setHasMore(false);

            } else {
                addEmptyView(imgSrc, content);
            }

        } else {
            addEmptyView();
        }
    }
    public View lodingView;

    /**
     * 添加加载中的布局
     */
    public void addLodingView() {
        if (lodingView != null) {
            return;
        }
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        lodingView = UIUtils.inflate(R.layout.view_loding);
        RelativeLayout linMain = (RelativeLayout) lodingView.findViewById(R.id.rel_main);
        if (flMain!=null)
            flMain.addView(lodingView);
    }


    /**
     * 移除加载中的布局
     */
    public void removeLodingView() {
        View viewById = getWindow().getDecorView();
        FrameLayout flMain = (FrameLayout) viewById.findViewById(R.id.fl_main);
        if (lodingView != null&&flMain!=null) {
            flMain.removeView(lodingView);
        }
        lodingView = null;
    }

}

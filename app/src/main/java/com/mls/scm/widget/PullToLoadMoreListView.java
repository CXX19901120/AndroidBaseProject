package com.mls.scm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mls.scm.R;


/**
 * @author CXX
 * @since 2015/4/2
 */
public class PullToLoadMoreListView extends ListView implements OnScrollListener {

    private LayoutInflater inflater;
    // 加载更多视图（底部视图）
    private View footView;
    // 加载更多文字
    private TextView tvFootTitle;
    // 加载更多忙碌框
    private ProgressBar pbFootRefreshing;
    // 是否已经添加了footer
    private boolean addFooterFlag;
    // 是否还有数据标志
    private boolean hasMoreDataFlag = true;

    private RelativeLayout relLoadMore;
    /**
     * Scroll时到达最后一个Item的次数，只有第一次能触发自动刷新
     */
    private int reachLastPositionCount = 0;
    private OnLoadMoreListener loadMoreListener;
    private boolean isGetMoreing = false;

    public PullToLoadMoreListView(Context context) {
        this(context, null);
    }

    public PullToLoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {

        inflater = LayoutInflater.from(context);

        /**
         * 底部
         */
        footView = inflater.inflate(R.layout.view_list_load_more_foot, this, false);
        relLoadMore = (RelativeLayout) footView.findViewById(R.id.rel_loadmore_footer);
        tvFootTitle = (TextView) footView.findViewById(R.id.tv_foot_title);
        pbFootRefreshing = (ProgressBar) footView.findViewById(R.id.pb_foot_refreshing);

        // 滑动监听
        setOnScrollListener(this);
    }

    /**
     * 判断是否可以自动加载更多<br/>
     *
     * @return
     */
    private boolean checkCanAutoGetMore() {
        if (footView == null) {
            return false;
        }
        if (loadMoreListener == null) {
            return false;
        }
        if (isGetMoreing) {
            return false;
        }
        if (!hasMoreDataFlag) {
            return false;
        }
        if (getAdapter() == null) {
            return false;
        }
        if (!canScroll(1) && !canScroll(-1)) {
            return false;
        }
//        LogUtil.e("getLastVisiblePosition"+getLastVisiblePosition()+"..getCount"+getCount()+".."+reachLastPositionCount);
//        if (getLastVisiblePosition() != getCount() - 1) {
//            return false;
//        }
//        if (reachLastPositionCount != 1) {
//            return false;
//        }
        return true;
    }

    /**
     * 判断ListView是否可以滑动
     *
     * @param direction
     * @return
     */
    private boolean canScroll(int direction) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return false;
        }

        final int firstPosition = getFirstVisiblePosition();
        final int listPaddingTop = getPaddingTop();
        final int listPaddingBottom = getPaddingTop();
        final int itemCount = getAdapter().getCount();

        if (direction > 0) {
            final int lastBottom = getChildAt(childCount - 1).getBottom();
            final int lastPosition = firstPosition + childCount;
            return lastPosition < itemCount || lastBottom > getHeight() - listPaddingBottom;
        } else {
            final int firstTop = getChildAt(0).getTop();
            return firstPosition > 0 || firstTop < listPaddingTop;
        }
    }

    /**
     * 设置加载更多监听器
     *
     * @param getMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener getMoreListener) {
        this.loadMoreListener = getMoreListener;

    }

    // 加载更多
    private void getMore() {
        if (!addFooterFlag) {
            addFooterFlag = true;
            this.addFooterView(footView, null, false);
        }
        if (loadMoreListener != null) {
            isGetMoreing = true;
            showFoot("正在加载...");
            loadMoreListener.onLoadMore();
        }
    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        isGetMoreing = false;
        showFoot("正在加载...");
    }

    /**
     * 设置没有更多的数据了<br/>
     * 不再显示加载更多按钮
     */
    public void setNoMore() {
        hasMoreDataFlag = false;
        showFoot("已加载全部");
    }

    private void showFoot(String infos) {
        if (footView != null) {
            tvFootTitle.setText(infos);
            if (isGetMoreing) {
                pbFootRefreshing.setVisibility(View.VISIBLE);
            } else {
                footView.setVisibility(View.GONE);
                pbFootRefreshing.setVisibility(View.GONE);
            }

            if (reachLastPositionCount > 0) {
                footView.setVisibility(View.VISIBLE);
            } else footView.setVisibility(GONE);
        }
    }


    /**
     * 显示加载更多按钮
     */
    public void setHasMore() {
        hasMoreDataFlag = true;
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param hasMore
     */
    public void setHasMore(boolean hasMore) {
        if (hasMore) setHasMore();
        else setNoMore();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {

            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                break;
            case OnScrollListener.SCROLL_STATE_IDLE:
                if (getLastVisiblePosition() == getCount() - 1) {
                    if (checkCanAutoGetMore()) {
                        reachLastPositionCount++;
                        getMore();
                    }
                } else reachLastPositionCount = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getAdapter() == null) {
            return;
        }

        if (visibleItemCount == totalItemCount) reachLastPositionCount = 0;
    }

    // 加载更多接口
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

}

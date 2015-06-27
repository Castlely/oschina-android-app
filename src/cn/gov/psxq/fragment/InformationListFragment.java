package cn.gov.psxq.fragment;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.gov.psxq.AppContext;
import cn.gov.psxq.AppException;
import cn.gov.psxq.R;
import cn.gov.psxq.adapter.ListViewInformationAdapter;
import cn.gov.psxq.adapter.ListViewInformationAdapter.ListItemView;
import cn.gov.psxq.bean.Information;
import cn.gov.psxq.bean.InformationList;
import cn.gov.psxq.bean.Notice;
import cn.gov.psxq.common.UIHelper;
import cn.gov.psxq.widget.NewDataToast;
import cn.gov.psxq.widget.PullToRefreshListView;

import com.google.common.collect.Lists;

public class InformationListFragment extends Fragment {
    private List<Information>          lvInformationData = Lists.newArrayList();
    private Handler                    lvInformationHandler;
    private PullToRefreshListView      lvInformation;

    private ListViewInformationAdapter lvInformationAdapter;
    private int                        lvInformationSumData;
    private View                       lvInformation_footer;

    private TextView                   lvInformation_foot_more;

    private ProgressBar                lvInformation_foot_progress;
    private AppContext                 appContext;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化Handler
        appContext = (AppContext) this.getActivity().getApplication();

        String catalogName = this.getArguments().getString("catalogName");
        final String title = this.getArguments().getString("title");
        initInformationListView(catalogName, inflater, container);
        lvInformationHandler = this.getLvHandler(lvInformation, lvInformationAdapter,
            lvInformation_foot_more, lvInformation_foot_progress, AppContext.PAGE_SIZE);
        // 加载资讯数据
        if (lvInformationData.isEmpty()) {
            loadLvInformationData(catalogName, 0, lvInformationHandler,
                UIHelper.LISTVIEW_ACTION_INIT);
        }
        return lvInformation;
    }

    private void initInformationListView(final String catalog, LayoutInflater inflater,
                                         ViewGroup container) {
        lvInformationAdapter = new ListViewInformationAdapter(
            InformationListFragment.this.getActivity(), lvInformationData,
            R.layout.information_listitem);
        lvInformation_footer = inflater.inflate(R.layout.listview_footer, null);
        lvInformation_foot_more = (TextView) lvInformation_footer
            .findViewById(R.id.listview_foot_more);
        lvInformation_foot_progress = (ProgressBar) lvInformation_footer
            .findViewById(R.id.listview_foot_progress);
        View view = inflater.inflate(R.layout.frame_information, container);
        lvInformation = new PullToRefreshListView(appContext, null);
        //lvInformation.set
        lvInformation.addFooterView(lvInformation_footer);// 添加底部视图 必须在setAdapter前
        lvInformation.setAdapter(lvInformationAdapter);

        lvInformation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击头部、底部栏无效
                if (position == 0 || view == lvInformation_footer)
                    return;
                ListItemView listItemView = (ListItemView) view.getTag();
                Information information = (Information) listItemView.title.getTag();
                UIHelper.showWebDetail(view.getContext(), "http://" + information.getLink(),
                    "详细信息", listItemView.title.getText().toString());
                /*// 跳转到留言详情
                UIHelper.showInformationDetail(view.getContext(), AppData.gsonBuilder.create()
                    .toJson(information));*/
            }
        });
        lvInformation.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                lvInformation.onScrollStateChanged(view, scrollState);

                // 数据为空--不用继续下面代码了
                if (lvInformationData.isEmpty())
                    return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(lvInformation_footer) == view
                        .getLastVisiblePosition())
                        scrollEnd = true;
                } catch (Exception e) {
                    scrollEnd = false;
                }

                //int lvDataState = StringUtils.toInt(lvInformation.getTag());
                int lvDataState = 1;
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    lvInformation.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    lvInformation_foot_more.setText(R.string.load_ing);
                    lvInformation_foot_progress.setVisibility(View.VISIBLE);
                    // 当前pageIndex
                    int pageIndex = lvInformationSumData / AppContext.PAGE_SIZE;
                    loadLvInformationData(catalog, pageIndex, lvInformationHandler,
                        UIHelper.LISTVIEW_ACTION_SCROLL);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                lvInformation.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        lvInformation.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                // 刷新数据
                loadLvInformationData(catalog, 0, lvInformationHandler,
                    UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });
    }

    /**
     * 线程加载Information
     * 
     * @param pageIndex
     *            当前页数
     * @param handler
     * @param action
     */
    private void loadLvInformationData(final String catalog, final int pageIndex,
                                       final Handler handler, final int action) {
        new Thread() {
            public void run() {
                Message msg = new Message();
                boolean isRefresh = false;
                if (action == UIHelper.LISTVIEW_ACTION_REFRESH
                    || action == UIHelper.LISTVIEW_ACTION_SCROLL)
                    isRefresh = true;
                try {
                    InformationList list = appContext.getInformationList(catalog, pageIndex,
                        isRefresh);
                    if (list == null)
                        list = new InformationList();
                    msg.what = list.getPageSize();
                    msg.obj = list;
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.arg1 = action;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private Handler getLvHandler(final PullToRefreshListView lv, final BaseAdapter adapter,
                                 final TextView more, final ProgressBar progress, final int pageSize) {
        return new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what >= 0) {
                    // listview数据处理
                    Notice notice = handleLvData(msg.what, msg.obj, msg.arg2, msg.arg1);

                    if (msg.what < pageSize) {
                        lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_full);
                    } else if (msg.what == pageSize) {
                        lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_more);

                    }
                    // 发送通知广播
                    if (notice != null) {
                        UIHelper.sendBroadCast(lv.getContext(), notice);
                    }
                } else if (msg.what == -1) {
                    // 有异常--显示加载出错 & 弹出错误消息
                    lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                    more.setText(R.string.load_error);
                    ((AppException) msg.obj).makeToast(InformationListFragment.this.getActivity());
                }
                if (adapter.getCount() == 0) {
                    lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
                    more.setText(R.string.load_empty);
                }
                progress.setVisibility(ProgressBar.GONE);
                if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) {
                    lv.onRefreshComplete(getString(R.string.pull_to_refresh_update)
                                         + new Date().toLocaleString());
                    lv.setSelection(0);
                } else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }
            }
        };
    }

    /**
     * listview数据处理
     * 
     * @param what
     *            数量
     * @param obj
     *            数据
     * @param objtype
     *            数据类型
     * @param actiontype
     *            操作类型
     * @return notice 通知信息
     */
    private Notice handleLvData(int what, Object obj, int objtype, int actiontype) {
        Notice notice = null;
        switch (actiontype) {
            case UIHelper.LISTVIEW_ACTION_INIT:
            case UIHelper.LISTVIEW_ACTION_REFRESH:
            case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
                int newdata = 0;// 新加载数据-只有刷新动作才会使用到
                InformationList informationListNew = (InformationList) obj;
                notice = informationListNew.getNotice();
                lvInformationSumData = what;
                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                    if (lvInformationData.size() > 0) {
                        for (Information information1 : informationListNew.getDataList()) {
                            boolean b = false;
                            for (Information information2 : lvInformationData) {
                                if (information1.getId() == information2.getId()) {
                                    b = true;
                                    break;
                                }
                            }
                            if (!b)
                                newdata++;
                        }
                    } else {
                        newdata = what;
                    }
                }
                lvInformationData.clear();// 先清除原有数据
                lvInformationData.addAll(informationListNew.getDataList());
                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                    // 提示新加载数据
                    if (newdata > 0) {
                        NewDataToast.makeText(InformationListFragment.this.getActivity(),
                            getString(R.string.new_data_toast_message, newdata),
                            appContext.isAppSound()).show();
                    } else {
                        NewDataToast.makeText(InformationListFragment.this.getActivity(),
                            getString(R.string.new_data_toast_none), false).show();
                    }
                }
                break;
            case UIHelper.LISTVIEW_ACTION_SCROLL:
                InformationList informationListScroll = (InformationList) obj;
                notice = informationListScroll.getNotice();
                lvInformationSumData += what;
                if (lvInformationData.size() > 0) {
                    for (Information information1 : informationListScroll.getDataList()) {
                        boolean b = false;
                        for (Information information2 : lvInformationData) {
                            if (information1.getId() == information2.getId()) {
                                b = true;
                                break;
                            }
                        }
                        if (!b)
                            lvInformationData.add(information1);
                    }
                } else {
                    lvInformationData.addAll(informationListScroll.getDataList());
                }
                break;
        }
        return notice;
    }

}

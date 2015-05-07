package cn.gov.psxq.fragment;

import cn.gov.psxq.inteface.ActionBarProgressBarVisibility;
import cn.gov.psxq.inteface.FooterViewVisibility;
import cn.gov.psxq.widget.PullToRefreshListView;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;

public abstract class ListFragment extends Fragment {

    protected ActionBarProgressBarVisibility abProgress;
    protected FooterViewVisibility           footerView;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            abProgress = (ActionBarProgressBarVisibility) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                         + "must implenemt ActionBarProgressVisibility");
        }
        try {
            footerView = (FooterViewVisibility) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                         + "must implenemt FooterViewVisibility");
        }
    }

    protected abstract void initListView(final int catalog, final PullToRefreshListView listView,
                                         int action);

    protected abstract void loadLvData(final int catalog, final int pageIndex,
                                       final Handler handler, final int action);
}

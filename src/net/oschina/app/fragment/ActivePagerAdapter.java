package net.oschina.app.fragment;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class ActivePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment>     fragments;
    private List<CharSequence> titles;
    private String             primaryKey;
    private Context            context;

    public ActivePagerAdapter(FragmentManager fm, List<Fragment> fragments,
                              List<CharSequence> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public ActivePagerAdapter(View view, FragmentManager fm, List<Fragment> fragments,
                              List<CharSequence> titles, String primaryKey) {

        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).toString().equals(primaryKey)) {

                this.instantiateItem(view, i);
            }
        }
    }

    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    public int getCount() {
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        return position < titles.size() ? titles.get(position) : "";
    }

}

package io.soglomania.feelshare.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Locale;

import io.soglomania.feelshare.R;
/**
 * Created by sogloarcadius on 12/03/17.
 */


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages. This provides the data for the {@link ViewPager}.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context context;

    private final List<Fragment> listfragments;

    public SectionsPagerAdapter(Context nContext, FragmentManager fm, List<Fragment> lfragments) {
        super(fm);
        context = nContext;
        listfragments = lfragments;

    }

    /**
     * Get fragment corresponding to a specific position. This will be used to populate the
     * contents of the {@link ViewPager}.
     *
     * @param position Position to fetch fragment for.
     * @return Fragment for specified position.
     */
    @Override
    public Fragment getItem(int position) {

        return listfragments.get(position);
    }

    @Override
    public int getCount() {
        return listfragments.size();
    }


    /**
     * Get title for each of the pages. This will be displayed on each of the tabs.
     *
     * @param position Page to fetch title for.
     * @return Title for specified page.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();

        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return context.getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return context.getString(R.string.title_section4).toUpperCase(l);
        }
        return null;
    }


}

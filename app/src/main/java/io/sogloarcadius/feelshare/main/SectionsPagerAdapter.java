package io.sogloarcadius.feelshare.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import java.util.List;
import java.util.Locale;

import io.sogloarcadius.feelshare.R;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages. This provides the data for the {@link ViewPager}.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    Drawable myDrawable;
    String title;
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
                title = " ";
                myDrawable = context.getResources().getDrawable(R.drawable.ic_account_circle_black_24dp);
                break;
            case 1:
                title = " ";
                myDrawable = context.getResources().getDrawable(R.drawable.ic_mood_black_24dp);
                break;

            case 2:
                title = " ";
                myDrawable = context.getResources().getDrawable(R.drawable.people
                );
                break;

            case 3:
                title = " ";
                myDrawable = context.getResources().getDrawable(R.drawable.globe);
                break;

            default:
                break;

        }
        SpannableStringBuilder sb = new SpannableStringBuilder("" + title); // space added before text for convenience
        try {
            myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sb;
    }


}

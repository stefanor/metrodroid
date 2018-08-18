/*
 * TabPagerAdapter.java
 *
 * Copyright (C) 2011 Eric Butler <eric@codebutler.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.id.micolous.metrodroid.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import au.id.micolous.farebot.R;

public class TabPagerAdapter extends PagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final AppCompatActivity mActivity;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<>();
    private FragmentTransaction mCurTransaction = null;

    public TabPagerAdapter(AppCompatActivity activity, ViewPager pager) {
        mActivity = activity;
        mActionBar = activity.getSupportActionBar();
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.add(info);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void startUpdate(View view) {
    }

    @NonNull
    @SuppressLint("CommitTransaction")
    @Override
    @SuppressWarnings("deprecation")
    public Object instantiateItem(@NonNull View view, int position) {
        TabInfo info = mTabs.get(position);

        if (mCurTransaction == null) {
            mCurTransaction = mActivity.getFragmentManager().beginTransaction();
        }

        Fragment fragment = Fragment.instantiate(mActivity, info.mClass.getName(), info.mArgs);
        mCurTransaction.add(R.id.pager, fragment);
        return fragment;
    }

    @SuppressLint("CommitTransaction")
    @Override
    @SuppressWarnings("deprecation")
    public void destroyItem(@NonNull View view, int i, @NonNull Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mActivity.getFragmentManager().beginTransaction();
        }
        mCurTransaction.hide((Fragment) object);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void finishUpdate(@NonNull View view) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mActivity.getFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    private static final class TabInfo {
        private final Class<?> mClass;
        private final Bundle mArgs;

        public TabInfo(Class<?> klass, Bundle args) {
            mClass = klass;
            mArgs = args;
        }
    }
}

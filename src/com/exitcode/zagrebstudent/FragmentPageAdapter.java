package com.exitcode.zagrebstudent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class FragmentPageAdapter extends FragmentPagerAdapter {
	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	public FragmentPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container,
				position);
		registeredFragments.put(position, fragment);
		return fragment;

	}

	public Fragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = new FragmentFirst();
		switch (arg0) {
		case 0:
			fragment = new FragmentFirst();
			break;
		case 1:
			fragment = new FragmentSecond();
			break;
		default:
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position) {
		case 0:
			title = "Info";
			break;
		case 1:
			title = "Komentari";
			break;
		}
		return title;
	}

}

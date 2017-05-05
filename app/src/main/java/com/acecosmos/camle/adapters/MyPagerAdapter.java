package com.acecosmos.camle.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.acecosmos.camle.fragments.BlankFragment;
import com.acecosmos.camle.fragments.UsersFragment;
import com.acecosmos.camle.fragments.MyAccountFragment;
import com.acecosmos.camle.fragments.ProductsFragment;

/**
 * Created by sd on 28-04-2017.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

  SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

  public MyPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int pos) {
    switch (pos) {

      case 0:
        return ProductsFragment.newInstance("ProductsFragment");
      case 1:
        return UsersFragment.newInstance("UsersFragment");
      case 2:
        return MyAccountFragment.newInstance("MyAccountFragment");
      default:
        return BlankFragment.newInstance("BlankFragment");
    }
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    registeredFragments.put(position, fragment);
    return fragment;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    registeredFragments.remove(position);
    super.destroyItem(container, position, object);
  }

  public Fragment getRegisteredFragment(int position) {
    return registeredFragments.get(position);
  }

  @Override
  public int getCount() {
    return 3;
  }
}

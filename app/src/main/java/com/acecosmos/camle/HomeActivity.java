package com.acecosmos.camle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.acecosmos.camle.adapters.MyPagerAdapter;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

public class HomeActivity extends AppCompatActivity {

  private static final String TAG = "HomeActivity";
  private ViewPager mViewPager;

  private NavigationTabStrip mTopNavigationTabStrip;

  public MyPagerAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle("Camle");
    initUI();
    setUI();

//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//          .setAction("Action", null).show();
//      }
//    });
  }

  private void setUI() {

    mAdapter = new MyPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mAdapter);
    mTopNavigationTabStrip.setTitles("Products","Users","My Account");
    mTopNavigationTabStrip.setAnimationDuration(150);
    mTopNavigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
    mTopNavigationTabStrip.setViewPager(mViewPager,0);
  }

  private void initUI() {
    mViewPager = (ViewPager) findViewById(R.id.vp);
    mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_top);
  }

}

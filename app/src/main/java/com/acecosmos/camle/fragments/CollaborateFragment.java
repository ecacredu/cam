package com.acecosmos.camle.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acecosmos.camle.R;

/**
 * Created by sd on 28-04-2017.
 */

public class CollaborateFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_collaborate, container, false);

    return v;
  }

  public static CollaborateFragment newInstance(String text) {

    CollaborateFragment f = new CollaborateFragment();
    Bundle b = new Bundle();
    b.putString("msg", text);

    f.setArguments(b);

    return f;
  }
}

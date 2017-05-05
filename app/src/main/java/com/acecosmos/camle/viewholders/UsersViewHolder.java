package com.acecosmos.camle.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acecosmos.camle.R;

/**
 * Created by sd on 02-05-2017.
 */

public class UsersViewHolder extends RecyclerView.ViewHolder {

  View mView;

  public UsersViewHolder(View itemView) {
    super(itemView);
    mView = itemView;
  }

  public void setName(String name){
    TextView user_name = (TextView) mView.findViewById(R.id.user_list_name);
    user_name.setText(name);
  }
}

package com.acecosmos.camle.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acecosmos.camle.R;

/**
 * Created by sd on 02-05-2017.
 */

public class ProductsViewHolder extends RecyclerView.ViewHolder {

  View mView;

  public ProductsViewHolder(View itemView) {
    super(itemView);
    mView = itemView;
  }

  public void setTitle(String title){
    TextView product_title = (TextView) mView.findViewById(R.id.product_list_title);
    product_title.setText(title);
  }

  public void setRent(String rent){
    TextView product_rent = (TextView) mView.findViewById(R.id.product_list_rent);
    product_rent.setText("Rs. "+rent+"/3 days");
  }

  public void setUser(String username){
    TextView product_user_name = (TextView) mView.findViewById(R.id.product_user_name);
    product_user_name.setText("By "+username);
  }

  public void setCity(String productCity){
    TextView product_city = (TextView) mView.findViewById(R.id.product_city);
    product_city.setText(productCity);
  }

//  public void setImage(Context context, String image){
//    ImageView issue_image = (ImageView) mView.findViewById(R.id.issue_thumb);
////    issue_image.setImageURI(Uri.parse(image));
//    Glide.clear(issue_image);
//    Glide.with(context)
//      .load(image)
//      .into(issue_image);
//  }
}

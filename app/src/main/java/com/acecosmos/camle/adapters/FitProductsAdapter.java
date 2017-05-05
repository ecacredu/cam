package com.acecosmos.camle.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.acecosmos.camle.ProductSelectionActivity;
import com.acecosmos.camle.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import co.ceryle.fitgridview.FitGridAdapter;

/**
 * Created by sd on 01-05-2017.
 */

public class FitProductsAdapter extends FitGridAdapter {

  private ArrayList<Integer> selectedPositions = new ArrayList<>();
//  private ArrayList<Integer> newlyAddedPositions = new ArrayList<>();
//  private ArrayList<String> removedKeys = new ArrayList<>();

  public String[] productNames = {"Camera", "Lens", "Audio/Sound", "Flash", "Editing System",
    "Storage", "Filters", "Studio", "Shooting Props", "Lights",
    "Equipments", "Cases & Bags", "Monitor/Screen"};

  public Integer[] mThumbIds = {R.drawable.camera, R.drawable.lens, R.drawable.audio_sound, R.drawable.flash, R.drawable.editing_system,
    R.drawable.storage, R.drawable.filters, R.drawable.studio, R.drawable.shooting_props, R.drawable.lights,
    R.drawable.equipments, R.drawable.cases_and_bagss, R.drawable.monitor};

  private Context context;

  public FitProductsAdapter(Context context) {
    super(context, R.layout.service_grid_item, 13);
    this.context = context;
//    products = null;
  }

  public void addSelectedPosition(int position) {
    selectedPositions.add(position);
  }
  public ArrayList<Integer> getSelectedPositions(){
    return selectedPositions;
  }

  public String getProduct(int position){
    return productNames[position];
  }

  public int getProductPos(String product) {
    return Arrays.asList(productNames).indexOf(product);
  }

  @Override
  public void onBindView(final int position, View itemView) {


    final ImageView mImage = (ImageView) itemView.findViewById(R.id.servicegrid_image);
    mImage.setImageResource(mThumbIds[position]);
    final TextView textView = (TextView) itemView
      .findViewById(R.id.servicegrid_text);
    textView.setText(productNames[position]);
    textView.setSelected(true);
    final ColorStateList oldColors =  textView.getTextColors();

    for(Integer p: selectedPositions){
      if(getProduct(p)==productNames[position]){
        mImage.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        break;
      }
    }


    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if(selectedPositions.contains(position)){
          for(int i=0; i< selectedPositions.size(); i++){
            if(selectedPositions.get(i)==position){
              selectedPositions.remove(i);


              mImage.getDrawable().mutate().clearColorFilter();
              textView.setTextColor(oldColors);
            }
          }



        }else{
          selectedPositions.add(position);
//          newlyAddedPositions.add(position);
          mImage.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
          textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
      }
    });
  }

}

//  public FitProductsAdapter(Context context, HashMap<Object, Object> products) {
//    super(context, R.layout.service_grid_item, 13);
//    this.context = context;
//    setProducts(products);
//    if(products != null){
//      if(products.size() > 0){
//        Iterator it = products.entrySet().iterator();
//        while (it.hasNext()) {
//          HashMap.Entry pair = (HashMap.Entry)it.next();
//          int pos = getProductPos(pair.getKey().toString().replace("_","/"));
//          addSelectedPosition(pos);
////          it.remove(); // avoids a ConcurrentModificationException
//        }
//      }
//    }
//  }

//for(int i=0; i< newlyAddedPositions.size(); i++) {
//  if (newlyAddedPositions.get(i) == position) {
//  newlyAddedPositions.remove(i);
//  }
//  }

//  String prodToRemove = getProduct(position);
//
//              if(products != null){
//                Iterator it = products.entrySet().iterator();
//                while (it.hasNext()) {
//                HashMap.Entry pair = (HashMap.Entry)it.next();
//                if(pair.getKey().equals(prodToRemove)){
//                removedKeys.add(pair.getValue().toString());
////                    products.remove(pair.getKey());
//                break;
//                }
////                  it.remove(); // avoids a ConcurrentModificationException
//                }
//                }

//  public ArrayList<Integer> getNewlyAddedPositions() {
//    return newlyAddedPositions;
//  }
//
//  public void setNewlyAddedPositions(ArrayList<Integer> newlyAddedPositions) {
//    this.newlyAddedPositions = newlyAddedPositions;
//  }
//
//  HashMap<Object,Object> products;
//
//  public HashMap<Object, Object> getProducts() {
//    return products;
//  }
//
//  public void setProducts(HashMap<Object, Object> products) {
//    this.products = products;
//  }
//
//  public ArrayList<String> getRemovedKeys() {
//    return removedKeys;
//  }
//
//  public void setRemovedKeys(ArrayList<String> removedKeys) {
//    this.removedKeys = removedKeys;
//  }

package com.acecosmos.camle.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acecosmos.camle.R;
import com.acecosmos.camle.ServicesActivity;

import java.util.ArrayList;

import co.ceryle.fitgridview.FitGridAdapter;

/**
 * Created by sd on 28-04-2017.
 */

public class FitServicesAdapter extends FitGridAdapter {

  private ArrayList<Integer> selectedPositions= new ArrayList<>();

  //  Commercial, Makeup, Retouch manipu

  public String[] servicesNames = { "Cinematography", "Maternity", "Traditional", "Birthday", "Video Editing",
    "Pre Wedding", "Kids", "Events", "Commercial", "Makeup",
    "Conception and Artistic Direction", "Sound Editing", "Products", "Candid", "Scripting",
    "Architecture/Ambience", "Fashion/Model", "Corporate", "Wedding", "VFX Artist",
    "Retouching And Manipulation"};

  public Integer[] mThumbIds = {R.drawable.cinematography,R.drawable.maternity,R.drawable.traditional,R.drawable.birthday,R.drawable.video_editing,
    R.drawable.pre_wedding,R.drawable.kids,R.drawable.events,R.drawable.products,R.drawable.products,
    R.drawable.artstic_director,R.drawable.audio_editing,R.drawable.products,R.drawable.candid,R.drawable.scripting,
    R.drawable.arch_ambi,R.drawable.fashion,R.drawable.corportae,R.drawable.wedding,R.drawable.vfx,
    R.drawable.products};

  private Context context;

  public FitServicesAdapter(Context context) {
    super(context, R.layout.service_grid_item);
    this.context = context;
  }

  public ArrayList<Integer> getSelectedPositions(){
    return selectedPositions;
  }

  public String getService(int position){
    return servicesNames[position];
  }

  public void addSelectedPosition(int position) {
    selectedPositions.add(position);
  }

  public void removeSelectedPosition(int position) {
    selectedPositions.remove(position);
  }

  @Override
  public void onBindView(final int position, View itemView) {


    final ImageView mImage = (ImageView) itemView.findViewById(R.id.servicegrid_image);
    mImage.setImageResource(mThumbIds[position]);
    final TextView textView = (TextView) itemView
      .findViewById(R.id.servicegrid_text);
    textView.setText(servicesNames[position]);
    textView.setSelected(true);
    final ColorStateList oldColors =  textView.getTextColors();


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
          mImage.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
          textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
      }
    });
  }
}

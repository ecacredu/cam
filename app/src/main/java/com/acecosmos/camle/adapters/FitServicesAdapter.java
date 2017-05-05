package com.acecosmos.camle.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acecosmos.camle.R;
import com.acecosmos.camle.ServicesActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import co.ceryle.fitgridview.FitGridAdapter;

/**
 * Created by sd on 28-04-2017.
 */

public class FitServicesAdapter extends FitGridAdapter {

  private ArrayList<Integer> selectedPositions = new ArrayList<>();
  private View mView;
  private HashMap<String,Object> preFilledMap;

  //  Commercial, Makeup, Retouch manipu

  public String[] servicesNames = {"Cinematography", "Maternity", "Traditional", "Birthday", "Video Editing",
    "Pre Wedding", "Kids", "Events", "Conception and Artistic Direction", "Sound Editing",
    "Products", "Candid", "Scripting", "Architecture/Ambience", "Fashion/Model",
    "Corporate", "Wedding", "VFX Artist"};

  public Integer[] mThumbIds = {R.drawable.cinematography, R.drawable.maternity, R.drawable.traditional, R.drawable.birthday, R.drawable.video_editing,
    R.drawable.pre_wedding, R.drawable.kids, R.drawable.events, R.drawable.artstic_director, R.drawable.audio_editing,
    R.drawable.products, R.drawable.candid, R.drawable.scripting, R.drawable.arch_ambi, R.drawable.fashion,
    R.drawable.corportae, R.drawable.wedding, R.drawable.vfx};

  private Context context;

  public FitServicesAdapter(Context context) {
    super(context, R.layout.service_grid_item, 18);
    this.context = context;
    this.preFilledMap = null;
  }

  public FitServicesAdapter(Context context, HashMap<String, Object> services) {
    super(context, R.layout.service_grid_item, 18);
    this.context = context;
    if(services != null){
      if(services.size() > 0){
        Iterator it = services.entrySet().iterator();
        while (it.hasNext()) {
          HashMap.Entry pair = (HashMap.Entry)it.next();
          int pos = getServicePos(pair.getKey().toString().replace("_","/"));
          addSelectedPosition(pos);
          it.remove(); // avoids a ConcurrentModificationException
        }
      }
    }
  }

  public ArrayList<Integer> getSelectedPositions() {
    return selectedPositions;
  }

  public String getService(int position) {
    return servicesNames[position];
  }

  public int getServicePos(String service) {
    return Arrays.asList(servicesNames).indexOf(service);
  }

  public void addSelectedPosition(int position) {
    selectedPositions.add(position);
  }

  public void removeSelectedPosition(int position) {
    selectedPositions.remove(position);
  }

  @Override
  public void onBindView(final int position, final View itemView) {


    final ImageView mImage = (ImageView) itemView.findViewById(R.id.servicegrid_image);
    mImage.setImageResource(mThumbIds[position]);
    final TextView textView = (TextView) itemView
      .findViewById(R.id.servicegrid_text);
    textView.setText(servicesNames[position]);
    textView.setSelected(true);
    final ColorStateList oldColors = textView.getTextColors();

    for(Integer p: selectedPositions){
      if(getService(p)==servicesNames[position]){
        mImage.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        break;
      }
    }

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (selectedPositions.contains(position)) {
          for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.get(i) == position) {
              selectedPositions.remove(i);
              mImage.getDrawable().mutate().clearColorFilter();
              textView.setTextColor(oldColors);
            }
          }
        } else {
          selectedPositions.add(position);
          mImage.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
          textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
      }
    });

  }


}

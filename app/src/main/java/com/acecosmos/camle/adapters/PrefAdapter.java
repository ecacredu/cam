package com.acecosmos.camle.adapters;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acecosmos.camle.R;
import com.acecosmos.camle.models.Pref;

import java.util.List;

/**
 * Created by sd on 02-05-2017.
 */

public class PrefAdapter extends RecyclerView.Adapter<PrefAdapter.MyViewHolder> {

  private List<Pref> prefList;
  private View mView;

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, summary;

    public MyViewHolder(View view) {
      super(view);

      mView = view;
      title = (TextView) view.findViewById(R.id.pref_title);
      summary = (TextView) view.findViewById(R.id.pref_summary);
//      year = (TextView) view.findViewById(R.id.year);
    }
  }


  public PrefAdapter(List<Pref> prefList) {
    this.prefList = prefList;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.pref_list_row, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    Pref p = prefList.get(position);
    holder.title.setText(p.getTitle());
    holder.summary.setText(p.getSummary());
  }

  @Override
  public int getItemCount() {
    return prefList.size();
  }
}

package br.lhardt.bov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class CowAdapter extends RecyclerView.Adapter<CowAdapter.ViewHolder>{

    private List<Cow> items;
    private Context c;
    private int itemLayout;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v, c);
    }

    public CowAdapter( List<Cow> items, int itemLayout, Context c) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.c = c;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.idView.setText(Format.intToString(items.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filterList(ArrayList<Cow> filteredCows) {
        this.items = filteredCows;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        public TextView idView;

        public ViewHolder(final View itemView, Context _context ){
            super(itemView);

            idView = itemView.findViewById(R.id.cow_id_text);

            this.context = _context;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    Intent it = new Intent(context, ViewCowActivity.class);
                    it.putExtra("cowId", idView.getText().toString());
                    ((Activity)context).startActivityForResult(it, 11);
                }
            });
        }
    }
}

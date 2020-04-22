package com.example.project3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GrapesAdapter extends RecyclerView.Adapter<GrapesAdapter.GrapesAdapterVh> implements Filterable {

    private List<Grape> grapeModelList;
    private List<Grape> getgrapeModelListFiltered;
    private Context context;
    private Selectedgrape selectedgrape;

    public GrapesAdapter(List<Grape> grapeModelList, Selectedgrape selectedgrape)
    {
        this.grapeModelList = grapeModelList;
        this.getgrapeModelListFiltered = grapeModelList;
        this.selectedgrape = selectedgrape;
    }

    @NonNull
    @Override
    public GrapesAdapter.GrapesAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new GrapesAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_grapes,null));
    }

    @Override
    public void onBindViewHolder(@NonNull GrapesAdapter.GrapesAdapterVh holder, int position) {

        Grape grapeModel = grapeModelList.get(position);

        String grapename = grapeModel.getName();
        String prefix = grapeModel.getName().substring(0,1);

        holder.tvUsername.setText(grapename);
        holder.tvPrefix.setText(prefix);

    }

    @Override
    public int getItemCount() {
        return grapeModelList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getgrapeModelListFiltered.size();
                    filterResults.values = getgrapeModelListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<Grape> resultData = new ArrayList<>();

                    for(Grape grapeModel: getgrapeModelListFiltered){
                        if(grapeModel.getName().toLowerCase().contains(searchChr)){
                            resultData.add(grapeModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                grapeModelList = (List<Grape>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }


    public interface Selectedgrape{

        void selectedgrape(Grape GrapesModel, int position);

    }

    public class GrapesAdapterVh extends RecyclerView.ViewHolder {

        TextView tvPrefix;
        TextView tvUsername;
        ImageView imIcon;
        public GrapesAdapterVh(@NonNull View itemView)
        {
            super(itemView);
            tvPrefix = itemView.findViewById(R.id.prefix);
            tvUsername = itemView.findViewById(R.id.username);
            imIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedgrape.selectedgrape(grapeModelList.get(getAdapterPosition()), getAdapterPosition());
                }
            });


        }
    }

    public List<Grape> getData()
    {
        return grapeModelList;
    }

    public void removeItem(int position)
    {
        grapeModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Grape GrapesModel, int position)
    {
        grapeModelList.add(position, GrapesModel);
        notifyItemInserted(position);
    }


}

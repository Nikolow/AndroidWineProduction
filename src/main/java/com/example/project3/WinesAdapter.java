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

public class WinesAdapter extends RecyclerView.Adapter<WinesAdapter.WinesAdapterVh> implements Filterable {

    private List<Wine> WineModelList;
    private List<Wine> getWineModelListFiltered;
    private Context context;
    private SelectedWine selectedWine;

    public WinesAdapter(List<Wine> WineModelList, SelectedWine selectedWine)
    {
        this.WineModelList = WineModelList;
        this.getWineModelListFiltered = WineModelList;
        this.selectedWine = selectedWine;
    }

    @NonNull
    @Override
    public WinesAdapter.WinesAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new WinesAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_wines,null));
    }

    @Override
    public void onBindViewHolder(@NonNull WinesAdapter.WinesAdapterVh holder, int position) {

        Wine WineModel = WineModelList.get(position);

        String Winename = WineModel.getName();
        String prefix = WineModel.getName().substring(0,1);

        holder.tvUsername.setText(Winename);
        holder.tvPrefix.setText(prefix);

    }

    @Override
    public int getItemCount() {
        return WineModelList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getWineModelListFiltered.size();
                    filterResults.values = getWineModelListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<Wine> resultData = new ArrayList<>();

                    for(Wine WineModel: getWineModelListFiltered){
                        if(WineModel.getName().toLowerCase().contains(searchChr)){
                            resultData.add(WineModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                WineModelList = (List<Wine>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }


    public interface SelectedWine{

        void selectedWine(Wine WinesModel, int position);

    }

    public class WinesAdapterVh extends RecyclerView.ViewHolder {

        TextView tvPrefix;
        TextView tvUsername;
        ImageView imIcon;
        public WinesAdapterVh(@NonNull View itemView)
        {
            super(itemView);
            tvPrefix = itemView.findViewById(R.id.prefix);
            tvUsername = itemView.findViewById(R.id.username);
            imIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedWine.selectedWine(WineModelList.get(getAdapterPosition()), getAdapterPosition());
                }
            });


        }
    }

    public List<Wine> getData()
    {
        return WineModelList;
    }

    public void removeItem(int position)
    {
        WineModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Wine WinesModel, int position)
    {
        WineModelList.add(position, WinesModel);
        notifyItemInserted(position);
    }


}

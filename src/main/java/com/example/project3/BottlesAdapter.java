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

public class BottlesAdapter extends RecyclerView.Adapter<BottlesAdapter.BottlesAdapterVh> implements Filterable
{

    private List<Bottles> bottleModelList;
    private List<Bottles> getbottleModelListFiltered;
    private Context context;
    private SelectedBottle selectedBottle;

    // конструктор
    public BottlesAdapter(List<Bottles> bottleModelList, SelectedBottle selectedBottle)
    {
        this.bottleModelList = bottleModelList;
        this.getbottleModelListFiltered = bottleModelList;
        this.selectedBottle = selectedBottle;
    }

    @NonNull
    @Override
    public BottlesAdapter.BottlesAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();

        return new BottlesAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_bottles,null));
    }

    @Override
    public void onBindViewHolder(@NonNull BottlesAdapter.BottlesAdapterVh holder, int position)
    {
        // съставянето

        Bottles bottleModel = bottleModelList.get(position);

        String bottlename = bottleModel.getName();
        String prefix = bottleModel.getName().substring(0,1);

        holder.tvUsername.setText(bottlename);
        holder.tvPrefix.setText(prefix);
    }

    @Override
    public int getItemCount() {
        return bottleModelList.size();
    }

    @Override
    public Filter getFilter() // търсачката с филтър
    {

        Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0)
                {
                    filterResults.count = getbottleModelListFiltered.size();
                    filterResults.values = getbottleModelListFiltered;

                }
                else
                    {
                    String searchChr = charSequence.toString().toLowerCase();

                    List<Bottles> resultData = new ArrayList<>();

                    for(Bottles bottleModel: getbottleModelListFiltered)
                    {
                        if(bottleModel.getName().toLowerCase().contains(searchChr))
                        {
                            resultData.add(bottleModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                bottleModelList = (List<Bottles>) filterResults.values;
                notifyDataSetChanged();
                // показване на резултатите
            }
        };
        return filter;
    }


    public interface SelectedBottle // интерфейс !
    {
        void selectedBottle(Bottles bottlesModel, int position); // избраната бутилка - имплементира се в активитито
    }

    public class BottlesAdapterVh extends RecyclerView.ViewHolder
    {
        TextView tvPrefix;
        TextView tvUsername;
        ImageView imIcon;
        public BottlesAdapterVh(@NonNull View itemView)
        {
            super(itemView);
            tvPrefix = itemView.findViewById(R.id.prefix);
            tvUsername = itemView.findViewById(R.id.username);
            imIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedBottle.selectedBottle(bottleModelList.get(getAdapterPosition()), getAdapterPosition());
                }
            });


        }
    }

    public List<Bottles> getData()
    {
        return bottleModelList;
    }

    public void removeItem(int position) // махане
    {
        bottleModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Bottles bottlesModel, int position) // връщане
    {
        bottleModelList.add(position, bottlesModel);
        notifyItemInserted(position);
    }


}

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

public class BottlingAdapter extends RecyclerView.Adapter<BottlingAdapter.BottlingModelAdapterVh> implements Filterable
{

    private List<BottlingModel> BottlingModelList;
    private List<BottlingModel> getBottlingModelListFiltered;
    private Context context;
    private SelectedBottling selectedBottling;

    public BottlingAdapter(List<BottlingModel> BottlingModelList, SelectedBottling selectedBottling)
    {
        this.BottlingModelList = BottlingModelList;
        this.getBottlingModelListFiltered = BottlingModelList;
        this.selectedBottling = selectedBottling;
    }

    @NonNull
    @Override
    public BottlingAdapter.BottlingModelAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();

        return new BottlingModelAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_bottling,null));
    }

    @Override
    public void onBindViewHolder(@NonNull BottlingAdapter.BottlingModelAdapterVh holder, int position) {

        BottlingModel BottlingModel = BottlingModelList.get(position);

        String Bottlingname = BottlingModel.getName();
        String prefix = BottlingModel.getName().substring(0,1);

        holder.tvUsername.setText(Bottlingname);
        holder.tvPrefix.setText(prefix);

    }

    @Override
    public int getItemCount() {
        return BottlingModelList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getBottlingModelListFiltered.size();
                    filterResults.values = getBottlingModelListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<BottlingModel> resultData = new ArrayList<>();

                    for(BottlingModel BottlingModel: getBottlingModelListFiltered){
                        if(BottlingModel.getName().toLowerCase().contains(searchChr)){
                            resultData.add(BottlingModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                BottlingModelList = (List<BottlingModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }


    public interface SelectedBottling{

        void selectedBottling(BottlingModel BottlingModelModel, int position);

    }

    public class BottlingModelAdapterVh extends RecyclerView.ViewHolder {

        TextView tvPrefix;
        TextView tvUsername;
        ImageView imIcon;
        public BottlingModelAdapterVh(@NonNull View itemView)
        {
            super(itemView);
            tvPrefix = itemView.findViewById(R.id.prefix);
            tvUsername = itemView.findViewById(R.id.username);
            imIcon = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedBottling.selectedBottling(BottlingModelList.get(getAdapterPosition()), getAdapterPosition());
                }
            });


        }
    }

    public List<BottlingModel> getData()
    {
        return BottlingModelList;
    }

    public void removeItem(int position)
    {
        BottlingModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(BottlingModel BottlingModelModel, int position)
    {
        BottlingModelList.add(position, BottlingModelModel);
        notifyItemInserted(position);
    }


}

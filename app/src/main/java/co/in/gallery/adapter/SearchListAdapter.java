package co.in.gallery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import co.in.gallery.R;
import co.in.gallery.model.ImageModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>{

    private List<ImageModel> titleList;
    private Context context;

    public SearchListAdapter(List<ImageModel> titleList, Context context) {
        this.titleList = titleList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        if (position%2 == 0){
            holder.layout.setBackgroundColor(Color.parseColor("#FCFCFC"));
        }else{
            holder.layout.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }

        holder.title.setText(titleList.get(position).getTitle());
        Glide.with(context).load(titleList.get(position).getUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CircleImageView image;
        LinearLayout layout;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_search_list_title);
            image = itemView.findViewById(R.id.item_search_list_image);
            layout = itemView.findViewById(R.id.search_item_layout);
        }
    }

}

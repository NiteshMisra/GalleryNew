package co.in.gallery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.Objects;
import co.in.gallery.R;
import co.in.gallery.model.ImageModel;

public class ImageAdapter extends PagedListAdapter<ImageModel,ImageAdapter.ImageHolder> {

    private Context context;

    public ImageAdapter(Context context) {
        super(diffCallback);
        this.context = context;
    }

    private static DiffUtil.ItemCallback<ImageModel> diffCallback =
            new DiffUtil.ItemCallback<ImageModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull ImageModel oldItem, @NonNull ImageModel newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull ImageModel oldItem, @NonNull ImageModel newItem) {
                    return oldItem.equals(newItem);
                }
            };


    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images,parent,false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        String imageUrl = Objects.requireNonNull(getItem(position)).getUrl();
        if (imageUrl != null){
            Glide.with(context).load(imageUrl).into(holder.imageView);
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_gallery_image);
        }
    }

}

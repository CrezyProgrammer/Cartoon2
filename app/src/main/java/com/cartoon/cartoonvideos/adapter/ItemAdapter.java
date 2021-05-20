package com.cartoon.cartoonvideos.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.bumptech.glide.Glide;
import com.cartoon.cartoonvideos.PlayActivity;
import com.cartoon.cartoonvideos.R;
import com.cartoon.cartoonvideos.databinding.LayoutBinding;
import com.cartoon.cartoonvideos.entity.Video;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;


public final class ItemAdapter extends Adapter {
    @NotNull
    private final ArrayList<Video> items;

    @NotNull
    public ItemAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

           return new ItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false));
    }


    public void onBindViewHolder(@NotNull ItemAdapter.ViewHolder holder, int position) {

        final Video item = items.get(position);
        LayoutBinding binding =  LayoutBinding.bind(holder.itemView);

        Glide.with(holder.itemView.getContext()).load("https://img.youtube.com/vi/" + item.getId() + "/hqdefault.jpg").into(binding.imageView);
       binding.textView.setText((CharSequence)item.getTitle());
     binding.duration.setText((CharSequence)item.getDuration());
        binding.views.setText((CharSequence)(item.getViews() + " views"));
      binding.date.setText((CharSequence)(item.getDate() + " ago"));
        binding.linearLayout.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {

                it.getContext().startActivity((new Intent(it.getContext(), PlayActivity.class)).putExtra("title", item.getTitle()).putExtra("id", item.getId()));
            }
        }));
    }


    public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
        this.onBindViewHolder((ItemAdapter.ViewHolder)var1, var2);
    }

    public int getItemCount() {
        return this.items.size();
    }

    @NotNull
    public final ArrayList getItems() {
        return this.items;
    }

    public ItemAdapter(@NotNull ArrayList items) {
        Intrinsics.checkNotNullParameter(items, "items");

        this.items = items;
    }

    @Metadata(
            mv = {1, 4, 3},
            bv = {1, 0, 3},
            k = 1,
            d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"},
            d2 = {"Lcom/cartoon/cartoonvideos/adapter/ItemAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "Cartoon_Zone.app"}
    )
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public ViewHolder(@NotNull View itemView) {
            super(itemView);

        }
    }
}

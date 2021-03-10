package com.cartoon.funnyvideos.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cartoon.funnyvideos.PlayActivity
import com.cartoon.funnyvideos.R
import com.cartoon.funnyvideos.databinding.LayoutBinding
import com.cartoon.funnyvideos.model.Item

class ItemAdapter(val items: ArrayList<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=items[position]
        val binding=LayoutBinding.bind(holder.itemView)
    Glide.with(holder.itemView.context).load("https://img.youtube.com/vi/${item.url}/default.jpg").into(binding.imageView)
        binding.textView.text=item.name
        binding.linearLayout.setOnClickListener {
                    it.context.startActivity(Intent(it.context, PlayActivity::class.java).putExtra("title",item.name).putExtra("id",item.url))

        }
    }

    override fun getItemCount(): Int {
      return items.size
    }

}

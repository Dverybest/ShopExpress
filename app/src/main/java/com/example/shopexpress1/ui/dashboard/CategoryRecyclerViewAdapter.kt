package com.example.shopexpress1.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress1.ProductActivity
import com.example.shopexpress1.R
import com.example.shopexpress1.model.Category
import java.util.ArrayList

class CategoryRecyclerViewAdapter(
    var context: Context,
    val list: ArrayList<Category>
) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view :View  = LayoutInflater.from(context).inflate(R.layout.home_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.imageView.setImageResource(data.imageUrl)
        holder.name.setText(data.name)
        holder.view.setOnClickListener{
            val intent = Intent(context,ProductActivity::class.java)
            intent.putExtra("category",data.name)
            context.startActivity(intent)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.productImage)
        val name = itemView.findViewById<TextView>(R.id.productName)
         val view = itemView
    }
}
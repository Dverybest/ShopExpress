package com.example.shopexpress1.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopexpress1.DetailActivity
import com.example.shopexpress1.R
import com.example.shopexpress1.model.Product

class ProductRecyclerAdapter(var context: Context,var productList : ArrayList<Product>) : RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product:Product = productList[position]
        Glide.with(context as AppCompatActivity)
            .load(product.imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.ic_shopping_cart_black_24dp))
            .into(holder.image)
//        holder.image.setImageResource(product.imageUrl)
        holder.category.setText(product.category)
        holder.price.setText("N${product.price}")
        holder.name.setText(product.name)
        holder.view.setOnClickListener {
          val intent =  Intent(context,DetailActivity::class.java)
          intent.putExtra("product",product)
          context.startActivity(intent)
        }
    }

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val image:ImageView = itemView.findViewById(R.id.productImage)
        val name:TextView = itemView.findViewById(R.id.productName)
        val category:TextView = itemView.findViewById(R.id.productCategory)
        val price:TextView = itemView.findViewById(R.id.productPrice)
        val view = itemView
    }
}
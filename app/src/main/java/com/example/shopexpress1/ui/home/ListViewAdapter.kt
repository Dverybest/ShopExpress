package com.example.shopexpress1;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter

import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopexpress1.model.Product

import java.util.ArrayList;

/**
 * Created by BEN on 01/10/2018.
 */

class ListViewAdapter(var mContext:Context, var resource:Int, var userList:ArrayList<Product>) : ArrayAdapter<Product>(mContext, resource, userList) {

    override fun getCount(): Int {
         return userList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater:LayoutInflater =  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val viewHolder:ViewHolder;
        var rowView:View? = null
        if(rowView == null){
            rowView = inflater.inflate(resource, parent, false);
            viewHolder = ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }else {
            viewHolder = rowView.getTag() as ViewHolder
        }
        val user = userList.get(position);
        if((mContext as AppCompatActivity) is  CartActivity){

        }else{ viewHolder.delete.visibility =View.GONE}
        viewHolder.delete.setOnClickListener {
            Db.dataset.remove(user)
            userList.remove(user)
            notifyDataSetChanged()
        }
        Glide.with(mContext as AppCompatActivity)
            .load(user.imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.ic_shopping_cart_black_24dp))
            .into(viewHolder.imageView)
//        viewHolder.imageView.setImageResource(user.imageUrl);
        viewHolder.foodnameTextView.setText(user.name);
        viewHolder.descriptionTextView.setText(user.category);
        viewHolder.price.setText("N${user.price}");

        return rowView!!;

    }

    override fun getItem(position:Int):Product {
        return userList.get(position);
    }


     class ViewHolder(view: View){
         val imageView :ImageView = view.findViewById(R.id.userImage);
         val foodnameTextView :TextView = view.findViewById(R.id.foodName);
         val descriptionTextView:TextView = view.findViewById(R.id.foodDescription);
         val price:TextView = view.findViewById(R.id.price);
         val delete:ImageView = view.findViewById(R.id.delete)
    }
}


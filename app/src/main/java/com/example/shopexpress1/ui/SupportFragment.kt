package com.example.shopexpress1.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.example.shopexpress1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SupportFragment : Fragment() {


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_support, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        (activity as AppCompatActivity).supportActionBar!!.title ="About"
        fab.setOnClickListener{

                val phoneIntent =  Intent(Intent.ACTION_DIAL);
                val uri = Uri.parse("tel:+2348060365304");
                phoneIntent.setData(uri);
                startActivity(phoneIntent);
        }


        return view
    }


}

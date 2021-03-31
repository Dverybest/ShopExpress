package com.example.shopexpress1.ui.dashboard

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopexpress1.CartActivity
import com.example.shopexpress1.Db
import com.example.shopexpress1.R
import com.example.shopexpress1.model.Category
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private var list2: ArrayList<Category> = ArrayList()
    private var list: ArrayList<Category> = ArrayList()
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var recyclerViewAdapter : CategoryRecyclerViewAdapter
    private  lateinit var toolbar: Toolbar
    private  lateinit var shopingCartCounter:TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView : RecyclerView  = root.findViewById(R.id.recyclerView)
        toolbar = root.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)

        //toolbar.setTitle("ShowExpress")
        (activity as AppCompatActivity).supportActionBar!!.title ="Categories"
        setHasOptionsMenu(true)
        recyclerViewAdapter = CategoryRecyclerViewAdapter(this.context!!,list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerViewAdapter
        dashboardViewModel.categoryList.observe(this, Observer {
            it.forEach{
                category->
                list.add(category)
                list2.add(category)
            }

            recyclerViewAdapter.notifyDataSetChanged()
        })


        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // menu.clear()
        inflater.inflate(R.menu.menu_main,menu)

        val itemMessages = menu!!.findItem(R.id.cart)
        itemMessages.setActionView(R.layout.cart)
        val badgeLayout = itemMessages.actionView as RelativeLayout
        badgeLayout.setOnClickListener {
            startActivity(Intent(context, CartActivity::class.java))
        }
        shopingCartCounter = badgeLayout.findViewById(R.id.badge_textView) as TextView
        shopingCartCounter.setVisibility(View.GONE) // initially hidden


        val searchManager =  context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager;
        val searchMenu = menu.findItem(R.id.search);
        val searchView =  searchMenu.getActionView() as SearchView;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                search(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                search(s)
                return false
            }
        })
        render();
        return super.onCreateOptionsMenu(menu, inflater)
    }
    fun search(query: String) {
        list.clear()
        Log.e("TAG",query)

        list2.forEach {
            it->
                list.add(it)
        }
        if(query==""|| TextUtils.isEmpty(query)){
            Log.e("TAG","reset")
            recyclerViewAdapter.notifyDataSetChanged();
            return;
        }
        list.clear()
        Log.e("TAG","${list2.size}list cleared ${list.size}")

        list2.forEach {
            i->
            Log.e("TAG",i.name)
            if(i.name.toLowerCase().contains(query.toLowerCase())){
                Log.e("TAG",i.name.toLowerCase())
                list.add(i)
            }
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return true
    }

    private fun render() {
        if(::shopingCartCounter.isInitialized){
            if(Db.dataset.size!=0){
                shopingCartCounter.visibility = View.VISIBLE
                shopingCartCounter.setText("${Db.dataset.size}")
            }else{
                shopingCartCounter.visibility = View.GONE
                shopingCartCounter.setText("${Db.dataset.size}")
            }
        }
    }

}
package com.example.shopexpress1.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.shopexpress1.model.Product
import com.google.android.material.tabs.TabLayout
import kotlin.collections.ArrayList
import android.content.Intent
import android.util.Log
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.example.shopexpress1.*
import com.google.firebase.firestore.FirebaseFirestore
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private  lateinit var viewPager: ViewPager
    private  lateinit var tabLayout: TabLayout
    private  lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var listView: ListView
    private var list: ArrayList<Product> = ArrayList()
    private  lateinit var productRecyclerAdapter: ProductRecyclerAdapter
    private  lateinit var shopingCartCounter:TextView
    private lateinit var listAdapter: ListViewAdapter
    private  lateinit var carouselView: CarouselView


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = root.findViewById(R.id.viewPager)
        tabLayout = root.findViewById(R.id.tabLayout)
        toolbar = root.findViewById(R.id.toolbar)
        listView = root.findViewById(R.id.listView)
        carouselView = root.findViewById(R.id.carouselView)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        (activity as AppCompatActivity).supportActionBar!!.title ="ShopExpress"
        setHasOptionsMenu(true)
       // recyclerView = root.findViewById(R.id.productRecyclerView)


        listAdapter = ListViewAdapter(context!!,R.layout.list_view,list)

        listView.adapter= listAdapter
        listView.onItemClickListener = OnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            val product:Product = list[position]
            val intent =  Intent(context, DetailActivity::class.java)
            intent.putExtra("product",product)
            startActivity(intent)
        }

        carouselView.pageCount = fetchData().size

        val imageListener:ImageListener = ImageListener{ postion:Int ,image:ImageView ->
            image.setImageResource(fetchData()[postion])
        }
        carouselView.setImageListener(imageListener)

        tabLayout.setupWithViewPager(viewPager)

       fetchData2("Featured Products")
        render()
        return root
    }

    private fun fetchData2(category: String) {
        val db = FirebaseFirestore.getInstance()

        list.clear()
        db.collection("product")
            .document(category).collection("products")
            .get()
            .addOnCompleteListener {
                    task->
                if(task.isSuccessful ){
                    task.result?.forEach {result->
                        val product :Product = result.toObject(Product::class.java)
                        list.add(product)
                    }
                }else {
                    Log.e("error", "Error getting documents.", task.getException());
                }

                listAdapter.notifyDataSetChanged();
            }
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun fetchData(): ArrayList<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.image7)
        list.add(R.drawable.image9)
        list.add(R.drawable.image10)
        list.add(R.drawable.image6)
        return list
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main,menu)

        val itemMessages = menu!!.findItem(R.id.cart)
        val item = menu.findItem(R.id.search)
        item.setVisible(false)
        itemMessages.setActionView(R.layout.cart)
        val badgeLayout = itemMessages.actionView as RelativeLayout
        badgeLayout.setOnClickListener {
            startActivity(Intent(context,CartActivity::class.java))
        }
        shopingCartCounter = badgeLayout.findViewById(R.id.badge_textView) as TextView
        shopingCartCounter.setVisibility(View.GONE) // initially hidden
        render();
        return super.onCreateOptionsMenu(menu, inflater)
    }

}
package com.example.shopexpress1.ui.account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.shopexpress1.*


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.account_fragment.*


class AccountFragment : Fragment() {

    private  val TAG = "Account"
    private  var currentUser: FirebaseUser? =null
    private lateinit var login:Button
    private  lateinit var toolbar: Toolbar
    private  lateinit var shopingCartCounter:TextView
    private lateinit var viewModel: AccountViewModel
    private lateinit var firestore : FirebaseFirestore
    private  var mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.account_fragment, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val admin = view.findViewById<Button>(R.id.admin)
        toolbar.setTitleTextColor(Color.WHITE)
        //toolbar.setTitle("ShowExpress")
        (activity as AppCompatActivity).supportActionBar!!.title ="ShopExpress"
        setHasOptionsMenu(true)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        admin.setOnClickListener {
            startActivity(Intent(context, PostActivity::class.java))
        }
        admin.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("users").document(mAuth.currentUser!!.email!!)
            .get()
            .addOnCompleteListener {
               task->
                if(task.isSuccessful){
                    progressBar.visibility = View.GONE
                   val documentSnapshot = task.result?.data as Map<String,Any>
                    if(documentSnapshot.isNotEmpty()){
                        edName?.setText(documentSnapshot.get("name").toString())
                        edPhone?.setText(documentSnapshot.get("phoneNumber").toString())
                        edAddress?.setText(documentSnapshot.get("homeAddress").toString())
                        email?.setText(mAuth.currentUser!!.email!!)
                        if(mAuth.currentUser!!.email!!.contentEquals("c@admin.com")){
                            admin.visibility = View.VISIBLE
                        }
                    }
                }
            }

        return view
    }

    override fun onResume() {
        super.onResume()
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(::login.isInitialized){
          if(currentUser==null){
              login.visibility = View.VISIBLE
          }else{
              login.visibility = View.GONE
          }
        }
        render()
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
        inflater!!.inflate(R.menu.menu_acc,menu)

        val itemMessages = menu!!.findItem(R.id.cart)
        itemMessages.setActionView(R.layout.cart)
        val badgeLayout = itemMessages.actionView as RelativeLayout
        badgeLayout.setOnClickListener {
            startActivity(Intent(context, CartActivity::class.java))
        }
        shopingCartCounter = badgeLayout.findViewById(R.id.badge_textView) as TextView
        shopingCartCounter.setVisibility(View.GONE) // initially hidden
        render()
        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.logOut ->{
                mAuth.signOut()
                startActivity(Intent(context,LoginActivity::class.java))
                (context as AppCompatActivity).finish()
            }
        }
        return true
    }

}

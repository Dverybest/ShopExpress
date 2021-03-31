package com.example.shopexpress1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private  val TAG = "Account"
    private  var currentUser: FirebaseUser? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        login.setOnClickListener{
            val email = username.text.toString()
            val pass = password.text.toString()
            if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass)){
                Toast.makeText(this@LoginActivity, "Please fill all fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pass.length<6){
                Toast.makeText(this@LoginActivity, "password should not be less than 6 characters",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {

                    Log.e(TAG, "signInWithEmail:success")
                    Toast.makeText(this, "Authentication success.", Toast.LENGTH_SHORT).show()
                    val user = mAuth.currentUser
                    if(user!=null){
                        startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))
                        finish()
                    }
                } else {

                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
            }

        }

        create.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))
            finish();
        }
    }
}

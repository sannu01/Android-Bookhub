package com.internshala.bookhub.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import com.internshala.bookhub.R
import com.internshala.bookhub.database.BookDatabase
import com.internshala.bookhub.database.BookEntity
import com.internshala.bookhub.util.CheckNetwork
import com.internshala.bookhub.util.GlobalVars
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.*
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var bookImage:ImageView
    lateinit var bookName:TextView
    lateinit var bookAuthor:TextView
    lateinit var bookPrice:TextView
    lateinit var bookRating:TextView
    lateinit var bookDesc:TextView
    lateinit var btnfav:Button
    lateinit var layoutInternet:RelativeLayout
    lateinit var btnInternet:Button
    lateinit var shimmer:ShimmerFrameLayout
    var bookId:String?="100"
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)


        bookName=findViewById(R.id.desc_bookname)
        bookAuthor=findViewById(R.id.desc_bookauthor)
        bookPrice=findViewById(R.id.desc_bookprice)
        bookImage=findViewById(R.id.desc_bookimg)
        bookRating=findViewById(R.id.desc_bookrating)
        bookDesc=findViewById(R.id.description)
        btnfav=findViewById(R.id.btnfavourite)
        val toolbar:Toolbar=findViewById(R.id.des_toolbar)
        btnInternet=findViewById(R.id.desc_btnnointernet)
        layoutInternet=findViewById(R.id.desc_layoutnointernet)
        shimmer=findViewById(R.id.desc_shimmer_view_container)
        val descrption_activity:RelativeLayout=findViewById(R.id.description_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Description"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        shimmer.startShimmerAnimation()
        val network = CheckNetwork()
        network.registerDefaultNetworkCallback(this as Context)
        layoutInternet.visibility=View.GONE




        if(intent!=null){
            bookId=intent.getStringExtra("book_Id")
        }
        else{
            finish()

        }
        if(bookId=="100"){
            finish()
        }

        val queue=Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"

        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)



        if (GlobalVars.isNetworkConnected) {
            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    try {

                        var success = it.getBoolean("success")
                        if (success) {

                            val bookjsonObject = it.getJSONObject("book_data")
                            shimmer.stopShimmerAnimation()
                            shimmer.visibility = View.GONE

                            val bookImageUrl=bookjsonObject.getString("image")
                            Picasso.get().load(bookjsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(bookImage)
                            bookName.text = bookjsonObject.getString("name")
                            bookAuthor.text = bookjsonObject.getString("author")
                            bookPrice.text = bookjsonObject.getString("price")
                            bookRating.text = bookjsonObject.getString("rating")
                            bookDesc.text = bookjsonObject.getString("description")


                            val bookEntity=BookEntity(
                                bookId?.toInt() as Int,
                                bookName.text.toString(),
                                bookAuthor.text.toString(),
                                bookPrice.text.toString(),
                                bookRating.text.toString(),
                                bookDesc.text.toString(),
                                bookImageUrl

                            )

                            val checkFav= DBAsyncTask(applicationContext,bookEntity,1).execute()

                            val isFav=checkFav.get()

                            if(isFav){
                                btnfav.text="Remove from Favourite"
                                val favColor=ContextCompat.getColor(applicationContext,R.color.favourite)
                                btnfav.setBackgroundColor(favColor)
                            }else{
                                btnfav.text="Add to Favourite"
                                val nofavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                btnfav.setBackgroundColor(nofavColor)


                            }

                            btnfav.setOnClickListener {
                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){

                                    val async=DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result=async.get()
                                    if(result){
                                        btnfav.text="Remove from Favourite"
                                        val favColor=ContextCompat.getColor(applicationContext,R.color.favourite)
                                        btnfav.setBackgroundColor(favColor)


                                    }

                                }else{
                                    val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()
                                    if (result){
                                        btnfav.text="Add to Favourite"
                                        val nofavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                        btnfav.setBackgroundColor(nofavColor)

                                    }

                                }


                            }


                        } else {
                            shimmer.stopShimmerAnimation()
                            shimmer.visibility = View.GONE
                            descrption_activity.visibility=View.GONE
                            Toast.makeText(this@DescriptionActivity,"Some Error Occurred!!",Toast.LENGTH_SHORT).show()



                        }
                    } catch (e: Exception) {
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        descrption_activity.visibility=View.GONE
                        Toast.makeText(this@DescriptionActivity,"Some Error Occurred!!",Toast.LENGTH_SHORT).show()

                    }


                }, Response.ErrorListener {
                    shimmer.stopShimmerAnimation()
                    shimmer.visibility = View.GONE
                    descrption_activity.visibility=View.GONE
                    Toast.makeText(this@DescriptionActivity,"Some Error Occurred!!",Toast.LENGTH_SHORT).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["context type"] = "application/json"
                        headers["token"] = "72c79d10f45224"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }else
        {


                descrption_activity.visibility = View.GONE
                Handler().postDelayed({
                    shimmer.stopShimmerAnimation()
                    shimmer.visibility = View.GONE
                    layoutInternet.visibility = View.VISIBLE


                }, 1000)
                btnInternet.setOnClickListener {
                    recreate()
                    layoutInternet.visibility = View.GONE
                }




        }



    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }

    class DBAsyncTask(val context:Context, val bookEntity: BookEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){


        val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1->{
                    val book:BookEntity?= db.booDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!=null

                }
                2->{

                    db.booDao().insertBook(bookEntity)
                    db.close()
                    return true

                }
                3->{

                    db.booDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }

            }
            return false
        }

    }



}

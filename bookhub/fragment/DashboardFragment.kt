package com.internshala.bookhub.fragment


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import com.internshala.bookhub.R
import com.internshala.bookhub.adapter.DashboardRecyclerAdapter
import com.internshala.bookhub.adapter.Global
import com.internshala.bookhub.model.Book
import com.internshala.bookhub.util.GlobalVars
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */


class DashboardFragment : Fragment(){
    lateinit var recyclerDashboard:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btninternet:Button
    lateinit var layinternet:RelativeLayout
    var position:Int=0
    var top:Int=0


    val bookInfoList= arrayListOf<Book>()
        /*Book("P.S.I Love You", "Cecelia abern", "Rs.299", "4.5", R.drawable.ps_ily),
        Book("The Great gatsby", "F. Scoot Fitzgerald", "Rs. 399", "4.1",R.drawable.great_gatsby),
        Book("Anna Karenina", "Leo Tolstoy", "Rs.199","4.3",R.drawable.anna_kare),
        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
        Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
        Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
        Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
        Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)

    )*/
    lateinit var recycleradapter: DashboardRecyclerAdapter
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this com.internshala.bookhub.fragment

        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)



        bookInfoList.clear()
        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)
        layoutManager=LinearLayoutManager(activity)

        val shimmer:ShimmerFrameLayout=view.findViewById(R.id.shimmer_view_container)
        layinternet=view.findViewById(R.id.layoutnointernet)
        btninternet=view.findViewById(R.id.btnnointernet)
        layinternet.visibility=View.GONE

        shimmer.startShimmerAnimation()

        val queue= Volley.newRequestQueue(activity as Context)
        val url= "http://13.235.250.119/v1/book/fetch_books/"
    if(GlobalVars.isNetworkConnected) {

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                try {
                    var success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)


                        }

                        shimmer.stopShimmerAnimation()
                        shimmer.visibility=View.GONE
                        recycleradapter =
                            DashboardRecyclerAdapter(activity as Context, bookInfoList)
                        recyclerDashboard.adapter = recycleradapter
                        recyclerDashboard.layoutManager = layoutManager



                    } else {

                        Toast.makeText(activity as Context, "Some Error Occurred!!", Toast.LENGTH_LONG)
                            .show()
                    }

                }catch (e:JSONException){
                    Toast.makeText(activity as Context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {

                Toast.makeText(activity as Context,"Volley Error Occurred",Toast.LENGTH_SHORT).show()

                println("Error is $it")
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["context type"] = "application/json"
                    headers["token"] = "72c79d10f45224"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }else
    {
        Handler().postDelayed({
            shimmer.stopShimmerAnimation()
            shimmer.visibility=View.GONE
            layinternet.visibility=View.VISIBLE


        },1000)
        btninternet.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.detach(this)
                ?.attach(this)
                ?.commit()
            layinternet.visibility=View.GONE
        }


    }




        return view

    }

    override fun onPause() {
        super.onPause()
        position= (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val v:View?=layoutManager.getChildAt(0)
        if (v==null) {
            top = 0
        }
        else
        {
            top=(v.top - layoutManager.paddingTop)
        }
    }





    override fun onResume() {


        if (Global.main_flag==1) {
            Global.main_flag=0
            recyclerDashboard.adapter = recycleradapter


        }


        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position,top)



        super.onResume()


    }







}

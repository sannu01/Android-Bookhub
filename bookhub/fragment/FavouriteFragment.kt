package com.internshala.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.bookhub.R
import com.internshala.bookhub.activity.MainActivity
import com.internshala.bookhub.adapter.FavouriteRecyclerAdapter
import com.internshala.bookhub.adapter.Global
import com.internshala.bookhub.database.BookDatabase
import com.internshala.bookhub.database.BookEntity
import com.internshala.bookhub.util.CheckNetwork
import com.internshala.bookhub.util.GlobalVars

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment() : Fragment() {

    lateinit var recyclerFavorite:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:FavouriteRecyclerAdapter
    var dbBookList= listOf<BookEntity>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this com.internshala.bookhub.fragment

        val view=inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerFavorite= view.findViewById(R.id.recyclerFavourite)

        layoutManager = GridLayoutManager(activity as Context, 2)

        dbBookList=RetrieveFavourite(activity as Context).execute().get()


        if(activity!=null){
            recyclerAdapter= FavouriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavorite.adapter= recyclerAdapter
            recyclerFavorite.layoutManager=layoutManager
        }

        return view
    }

    class RetrieveFavourite(val context:Context):AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntity> {

            val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            return db.booDao().getAllBooks()
        }

    }

    override fun onResume() {
        if (Global.fav_flag==1) {
            Global.fav_flag=0
            dbBookList = RetrieveFavourite(activity as Context).execute().get()
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavorite.adapter = recyclerAdapter
        }
        super.onResume()
    }

}

package com.internshala.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.bookhub.R
import com.internshala.bookhub.activity.DescriptionActivity
import com.internshala.bookhub.database.BookDatabase
import com.internshala.bookhub.database.BookEntity
import com.internshala.bookhub.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val texBookName: TextView = view.findViewById(R.id.txtRecyclerRowItem)
        val textAuthor: TextView = view.findViewById(R.id.txtAuthor)
        val textBookPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtBookRating: TextView = view.findViewById(R.id.textrating)
        val imgBook: ImageView = view.findViewById(R.id.imgrecyclerRowItem)
        val imgfav: ImageView = view.findViewById(R.id.imgfav)
        val llcontent: LinearLayout = view.findViewById(R.id.llcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.texBookName.text = book.bookName
        holder.textAuthor.text = book.bookAuthor
        holder.textBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
//        holder.imgBook.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBook)
        val bookEntity = BookEntity(
            book.bookId?.toInt() as Int,
            book.bookName,
            book.bookAuthor,
            book.bookPrice,
            book.bookRating,
            "Description",
            book.bookImage

        )
        val checkFav = DescriptionActivity.DBAsyncTask(context, bookEntity, 1).execute()

        val isFav = checkFav.get()
        if (isFav) {
            holder.imgfav.setImageResource(R.drawable.ic_fav)

        }
            holder.llcontent.setOnClickListener {
                val intent = Intent(context, DescriptionActivity::class.java)
                intent.putExtra("book_Id", book.bookId)
                Global.main_flag=1
                context.startActivity(intent)


            }
        holder.imgfav.setOnClickListener {

            if(!DescriptionActivity.DBAsyncTask(context, bookEntity, 1).execute().get()){

                val async= DescriptionActivity.DBAsyncTask(context, bookEntity, 2).execute()
                val result=async.get()
                if(result){
                    holder.imgfav.setImageResource(R.drawable.ic_fav)
                }

            }else{
                val async= DescriptionActivity.DBAsyncTask(context, bookEntity, 3).execute()
                val result=async.get()
                if (result){
                    holder.imgfav.setImageResource(R.drawable.ic_unfav)
                }

            }
        }


    }

        class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
            AsyncTask<Void, Void, Boolean>() {


            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

            override fun doInBackground(vararg params: Void?): Boolean {

                when (mode) {

                    1 -> {
                        val book: BookEntity? =
                            db.booDao().getBookById(bookEntity.book_id.toString())
                        db.close()
                        return book != null

                    }
                    2 -> {

                        db.booDao().insertBook(bookEntity)
                        db.close()
                        return true

                    }
                    3 -> {

                        db.booDao().deleteBook(bookEntity)
                        db.close()
                        return true

                    }

                }
                return false
            }

        }



}
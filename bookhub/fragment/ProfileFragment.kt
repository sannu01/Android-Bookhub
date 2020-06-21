package com.internshala.bookhub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.internshala.bookhub.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    lateinit var imgt:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this com.internshala.bookhub.fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        imgt=view.findViewById(R.id.testimg)

        imgt.setOnClickListener {
            imgt.setImageResource(R.drawable.ic_fav)
        }

        return view
    }

}

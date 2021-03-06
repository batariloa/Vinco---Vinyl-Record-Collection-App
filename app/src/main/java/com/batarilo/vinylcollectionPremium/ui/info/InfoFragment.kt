package com.batarilo.vinylcollectionPremium.ui.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.batarilo.vinylcollectionPremium.R
import com.batarilo.vinylcollectionPremium.data.model.Record
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InfoFragment : Fragment() {
    private lateinit var viewCurrent:View
    private var record:Record? = null
    private val viewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {

           if(arguments?.getSerializable(RECORD_PARAM) is Record)
               record = arguments?.getSerializable(RECORD_PARAM) as Record
        }

        record?.let { it1 -> viewModel.addToHistory(it1) }
    }

    private fun initializeView(){

        Glide.with(viewCurrent.context)
            .load(record?.cover_image)
            .placeholder(R.drawable.empty_record)
            .into(viewCurrent.findViewById(R.id.image_record))

        val year = if(record?.year==null || record?.year==""){
            "Year N/A"
        } else record?.year
        Log.d("INFO", "NOTE OF THIS VIEW IS ${record?.note}")


        viewCurrent.findViewById<TextView>(R.id.tv_title_info).text = record?.title
        viewCurrent.findViewById<TextView>(R.id.tv_year_info).text = year
        viewCurrent.findViewById<TextView>(R.id.tv_country_info).text = record?.country
        viewCurrent.findViewById<TextView>(R.id.tv_genre_info).text =
            record?.genre.toString().substring(1, record?.genre.toString().length-1)
        viewCurrent.findViewById<TextView>(R.id.tv_label_info).text =
            record?.label.toString().substring(1, record?.label.toString().length-1)
        viewCurrent.findViewById<TextView>(R.id.tv_formats).text =
            record?.format.toString().substring(1,record?.format.toString().length-1)


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewCurrent = inflater.inflate(R.layout.fragment_info, container, false)
        initializeView()
        setUpButtons()




        return viewCurrent
    }

    private fun setUpButtons(){

        val buttonCollection = viewCurrent.findViewById<ImageButton>(R.id.btn_add_to_collection)
        val buttonWishlist = viewCurrent.findViewById<ImageButton>(R.id.btn_add_to_wishlist)
        viewModel.recordInCollectionExists(record, buttonCollection)
        viewModel.recordInWishlistExists(record, buttonWishlist)

        buttonCollection.setOnClickListener{
            viewModel.addRecordToCollection(record, buttonCollection)
        }
        buttonWishlist.setOnClickListener {
            viewModel.addRecordToWishlist(record, buttonWishlist)
        }

    }
    companion object {
        const val RECORD_PARAM = "record"

        @JvmStatic
        fun newInstance(record: Record) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RECORD_PARAM, record )
                }
            }
    }
}
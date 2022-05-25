package com.batarilo.vinylcollection.ui.wishlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batarilo.vinylcollection.R
import com.batarilo.vinylcollection.ui.info.InfoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment(), RecordAdapterWishlist.OnRecordListenerWishlist {

    private lateinit var viewCurrent:View
    val viewModel: WishlistViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewCurrent = inflater.inflate(R.layout.fragment_my_collection, container, false)
        setupRecyclerView()
        loadWishList()


        val src =viewCurrent.findViewById<SearchView>(R.id.sv_record)

        src.setOnClickListener { src.isIconified = false }

        src.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                   searchWishlist(p0)
                }
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
        return viewCurrent
    }

    override fun onRecordClicked(position: Int) {
        val bundle = Bundle().also {
            it.putSerializable(InfoFragment.RECORD_PARAM, viewModel.recordAdapter.records[position].record)
        }
        Navigation.findNavController(viewCurrent)
            .navigate(R.id.infoFragment, bundle)

    }

    override fun onRemoveClicked(position: Int) {
        viewModel.deleteRecord(position)
     }

    override fun onAddToNotesClicked(position: Int) {
        viewModel.setRecordNote(viewCurrent.context, position).apply {
            show()
            setOnDismissListener {
                loadWishList()
                viewModel.recordAdapter.notifyItemChanged(position)
            }
        }
    }
    private fun loadWishList(){
        viewModel.readAllFromWishlist()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchWishlist(query:String){
        viewModel.searchWishlist(query)
        viewModel.recordAdapter.notifyDataSetChanged()
        }

    private fun setupRecyclerView() = viewCurrent.findViewById<RecyclerView>(R.id.rv_record)?.apply {
        viewModel.recordAdapter = RecordAdapterWishlist(this@WishlistFragment)
        adapter = viewModel.recordAdapter
        layoutManager = LinearLayoutManager(activity)

    }


}
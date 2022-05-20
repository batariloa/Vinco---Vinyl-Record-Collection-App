package com.batarilo.vinylcollection.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batarilo.vinylcollection.R
import com.batarilo.vinylcollection.ui.search.recycle.RecordAdapterSearch
import com.batarilo.vinylcollection.ui.info.InfoFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(), RecordAdapterSearch.OnRecordListenerSearch {

    lateinit var navigationToggled: ActionBarDrawerToggle

   private lateinit var viewCurrent: View
    private val viewModel:SearchViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewCurrent  = inflater.inflate(R.layout.fragment_search, container, false)
        setupRecyclerView()
        viewModel.newSearch("")


        val src =viewCurrent.findViewById<SearchView>(R.id.sv_record)

        src.setOnClickListener { src.isIconified = false }

        src.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    viewModel.newSearch(p0)
                    setupRecyclerView()

                }
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })


        return viewCurrent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(navigationToggled.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() = viewCurrent.findViewById<RecyclerView>(R.id.rv_record)?.apply {
        viewModel.recordAdapterSearch = RecordAdapterSearch(this@SearchFragment)
        adapter = viewModel.recordAdapterSearch
        layoutManager = LinearLayoutManager(activity)

    }


    override fun onRecordClicked(position: Int) {
        println("RECORD ADAPTER RECORDS " + viewModel.recordAdapterSearch.records)
        println(" "+ viewModel.recordAdapterSearch.records[position].toString() + " clicked record")

        val bundle = Bundle().also {
           it.putSerializable(InfoFragment.RECORD_PARAM,viewModel.recordAdapterSearch.records[position])
        }
        Navigation.findNavController(viewCurrent)
            .navigate(R.id.infoFragment, bundle)


    }

    override fun onCollectedClicked(position: Int) {


        val clickedRecord = viewModel.recordAdapterSearch.records[position]
        viewModel.addRecordToCollection(clickedRecord)

    }

    override fun onAddToWishListClicked(position: Int) {

        val clickedRecord = viewModel.recordAdapterSearch.records[position]
        viewModel.addRecordToWishlist(clickedRecord)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
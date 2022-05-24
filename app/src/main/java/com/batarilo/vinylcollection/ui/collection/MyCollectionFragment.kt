package com.batarilo.vinylcollection.ui.collection

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
import com.batarilo.vinylcollection.ui.HomeActivity
import com.batarilo.vinylcollection.ui.collection.recycle.RecordAdapterCollection
import com.batarilo.vinylcollection.ui.dialog.NoteDialog
import com.batarilo.vinylcollection.ui.info.InfoFragment


class MyCollectionFragment : Fragment(), RecordAdapterCollection.OnRecordListenerCollection {




    lateinit var viewCurrent:View
    val viewModel: MyCollectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewCurrent = inflater.inflate(R.layout.fragment_my_collection, container, false)
        setupRecyclerView()
        loadCollection()

        val src =viewCurrent.findViewById<SearchView>(R.id.sv_record)

        src.setOnClickListener { src.isIconified = false }

        src.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    viewModel.searchCollection(p0)
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


    private fun loadCollection(){
       viewModel.readAllFromCollection()
    }


    private fun setupRecyclerView() = viewCurrent.findViewById<RecyclerView>(R.id.rv_record)?.apply {
        viewModel.recordAdapter = RecordAdapterCollection(this@MyCollectionFragment)
        adapter = viewModel.recordAdapter
        layoutManager = LinearLayoutManager(activity)

    }


    override fun onRecordClicked(position: Int) {
        val bundle = Bundle().also {
            it.putSerializable(InfoFragment.RECORD_PARAM,
                viewModel.recordAdapter.getItemId(position))
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
            setOnDismissListener{
                loadCollection()
            }
        }

    }




}
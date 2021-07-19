package com.priceline.app.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priceline.app.PricelineApplication
import com.priceline.app.adapter.DetailListAdapter
import com.priceline.app.api.common.ApiException
import com.priceline.app.api.common.Resource
import com.priceline.app.databinding.BooksDetailListFragmentBinding
import com.priceline.app.model.BookListDetailModel
import com.priceline.app.utils.MyDividerItemDecoration
import com.priceline.app.view.viewmodel.BooksListViewModel
import kotlinx.android.synthetic.main.books_detail_list_fragment.*
import java.util.*

class BooksDetailsListFragment : Fragment() {
    private var booksNameListViewModel: BooksListViewModel? = null
    private lateinit var booksNameListFragmentBinding: BooksDetailListFragmentBinding
    private var arrayList = ArrayList<BookListDetailModel>()
    private var mAdapter: DetailListAdapter? = null
    private var listName: String? = null // list encode name
    private var refresh: Boolean = true // to laod the fresh data

    //region fragment override methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        booksNameListViewModel = BooksListViewModel.create(activity)
        PricelineApplication.appComponent?.inject(booksNameListViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        booksNameListFragmentBinding = BooksDetailListFragmentBinding.inflate(inflater, container, false)
        return booksNameListFragmentBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listName = arguments?.getString("list_name")
        header_title.text = listName
        val manager = LinearLayoutManager(activity)
        manager.orientation = RecyclerView.VERTICAL
        booksNameListFragmentBinding!!.recyclerView.layoutManager = manager
        booksNameListFragmentBinding!!.recyclerView.setHasFixedSize(true)
        setHasOptionsMenu(true)
        initializeRecyclerView()
        onClickListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refresh = false
    }

    //endregion

    // Setup recyclerview
    private fun initializeRecyclerView() {
        mAdapter = DetailListAdapter(arrayList) { resultsItem: BookListDetailModel? -> null }
        booksNameListViewModel?.let { updateUi(it.detailList) }
        booksNameListFragmentBinding!!.recyclerView.adapter = mAdapter
        booksNameListFragmentBinding!!.recyclerView.addItemDecoration(MyDividerItemDecoration(activity, LinearLayoutManager.VERTICAL, 1))
        booksNameListFragmentBinding!!.recyclerView.itemAnimator = DefaultItemAnimator()
    }

    // udpate the ui using observer
    private fun updateUi(liveData: LiveData<Resource<ArrayList<BookListDetailModel>>>) {
        // Update the list when the data changes
        liveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    booksNameListFragmentBinding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    // also can show the error screen with retry button
                    booksNameListFragmentBinding.progressBar.visibility = View.GONE
                    if (it.exception.kind === ApiException.Kind.NETWORK)
                        Toast.makeText(activity, it.exception.errorModel.errorMessage, Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(activity, it.exception.errorModel.errorMessage, Toast.LENGTH_LONG).show()


                }
                Resource.Status.SUCCESS -> {
                    booksNameListFragmentBinding.progressBar.visibility = View.GONE
                    mAdapter!!.updateData(it.getData())
                }
            }

        })
        booksNameListViewModel!!.loadDetailList(listName, refresh)
    }

    // on click listener handling
    private fun onClickListener() {
        arrow_back_img.setOnClickListener(View.OnClickListener {
            val fm = activity?.supportFragmentManager
            if (fm?.backStackEntryCount!! > 0) {
                fm?.popBackStack()
            }
        })
    }

}
package com.kars.codeforcesmobile.profile.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.RetrofitClient
import com.kars.codeforcesmobile.profile.adapter.RecentSubmissionAdapter
import com.kars.codeforcesmobile.profile.data.CodeforcesSubmissions
import com.kars.codeforcesmobile.profile.viewmodels.RecentSubmissionsViewModel
import kotlinx.android.synthetic.main.recent_submissions_fragment.*
import retrofit2.Retrofit

class RecentSubmissionsFragment : Fragment() {
    private lateinit var viewModel: RecentSubmissionsViewModel
    private var userHandleName: String? = null
    private val args: RecentSubmissionsFragmentArgs by navArgs()
    private var isScrolling: Boolean = false
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private val PAGE_COUNT: Int = 10
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: RecentSubmissionAdapter
    private lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RecentSubmissionsViewModel::class.java)
        userHandleName = args.userHandle
        retrofit = RetrofitClient.retrofitClientCodeForces!!
        viewModel.getSubmissions(userHandleName!!, PAGE_COUNT, retrofit)
        return inflater.inflate(R.layout.recent_submissions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecentSubmissionAdapter()
        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recentSubmissionsRecyclerView.adapter = adapter
        recentSubmissionsRecyclerView.layoutManager = layoutManager
        recentSubmissionsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = layoutManager.childCount
                val totalItems = layoutManager.itemCount
                val scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
                Log.e("Pagination", "Total Items : $totalItems")
                Log.e("Pagination", "Current Items : $currentItems")
                Log.e("Pagination", "scrolled Items : $scrolledOutItems")
                if (!isLoading && !isLastPage) {
                    if (isScrolling
                        && (currentItems + scrolledOutItems >= totalItems)
                        && scrolledOutItems >= 0
                        && totalItems >= PAGE_COUNT
                    ) {
                        fetchData()
                    }
                }
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if(it!=null){
                isLoading = it
                if(it){
                    progressSubmissions.visibility = View.VISIBLE
                } else {
                    progressSubmissions.visibility = View.GONE
                }
            }
        })
        viewModel.isLastPage.observe(viewLifecycleOwner, {
            if(it!=null){
                isLastPage = it
            }
        })
        viewModel.submissionsList.observe(viewLifecycleOwner, {
            if(it!=null){
                adapter.fillData(it)
                viewModel.from.value = viewModel.from.value?.plus(PAGE_COUNT)
                Log.e("From", viewModel.from.value.toString())
            }
        })
    }

    private fun fetchData(){
        viewModel.getSubmissions(userHandleName!!, PAGE_COUNT, retrofit)
    }

    override fun onPause() {
        super.onPause()
        viewModel.from.value = 1
        viewModel.submissionsList.value = null
        viewModel.isLastPage.value = false
        viewModel.isLoading.value = false
    }
}
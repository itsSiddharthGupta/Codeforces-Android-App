package com.kars.codeforcesmobile.profile.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.RetrofitClient
import com.kars.codeforcesmobile.profile.adapter.RatingChangeAdapter
import com.kars.codeforcesmobile.profile.viewmodels.RatingChangesViewModel
import kotlinx.android.synthetic.main.rating_changes_fragment.*

class RatingChangesFragment : Fragment() {
    private lateinit var viewModel: RatingChangesViewModel
    private lateinit var adapter: RatingChangeAdapter
    private var userHandle: String? = null
    val args: RatingChangesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RatingChangesViewModel::class.java)
        adapter = RatingChangeAdapter()
        userHandle = args.userHandle
        Log.e("Argument User", userHandle+"")
        if(userHandle!=null)
            viewModel.getRatingChanges(userHandle!!, RetrofitClient.retrofitClientCodeForces!!)
        return inflater.inflate(R.layout.rating_changes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        viewModel.ratingChangeList.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.fillData(it.asReversed())
            }
        })
    }
}
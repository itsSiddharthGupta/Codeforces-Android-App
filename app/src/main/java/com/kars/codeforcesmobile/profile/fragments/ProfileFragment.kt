package com.kars.codeforcesmobile.profile.fragments

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.RetrofitClient
import com.kars.codeforcesmobile.profile.data.CodeforcesUserProfile
import com.kars.codeforcesmobile.profile.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.*
import retrofit2.Retrofit

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private var userHandleName: String? = null
    private lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        retrofit = RetrofitClient.retrofitClientCodeForces!!
        userHandleName = context?.getSharedPreferences("user_handle", Context.MODE_PRIVATE)
            ?.getString("handle", null)
        if (userHandleName != null) {
            viewModel.getUserProfile(userHandleName!!, retrofit)
        }
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userHandleName == null) {
            noHandleLayout.visibility = View.VISIBLE
        } else {
            noHandleLayout.visibility = View.GONE
            profileView.visibility = View.VISIBLE
        }
        viewModel.userProfile.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.status == "OK") {
                    updateUI(view, it.result?.get(0)!!)
                }
                if (it.status == "FAILED") {
                    Snackbar.make(
                        view,
                        it.comment!!,
                        4000
                    ).show()

                }
            }
        })
        recentSubmissions.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToRecentSubmissionsFragment(
                userHandleName
            )
            findNavController().navigate(action)
        }
        ratingChanges.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToRatingChangesFragment(
                userHandleName
            )
            findNavController().navigate(action)
        }
        myPreferences.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_preferencesFragment) }
        submitUserHandle.setOnClickListener {
            val userHandle = userHandleEdittext.text.toString()
            if (userHandle.trim().isEmpty()) {
                return@setOnClickListener
            }
            viewModel.getUserProfile(userHandle, retrofit)
        }
    }

    private fun updateUI(view: View, profile: CodeforcesUserProfile.Profile) {
        noHandleLayout.visibility = View.GONE
        profileView.visibility = View.VISIBLE
        txtUserContribution.text = SpannableStringBuilder().bold { append("Contribution : ") }.append(profile.contribution.toString())
        txtUserHandle.text = SpannableStringBuilder().append(profile.handle)
        txtUserRank.text = SpannableStringBuilder().bold { append("Rank : ") }.append(profile.rank)
        txtUserRating.text = SpannableStringBuilder().bold { append("Rating : ") }.append(profile.rating.toString())
        Glide.with(view.context).load("https:${profile.titlePhoto}").into(imgUserProfileIcon)
        if (userHandleName == null) {
            userHandleName = profile.handle
            view.context.getSharedPreferences("user_handle", Context.MODE_PRIVATE).edit()
                .putString("handle", profile.handle).apply()
        }
        profileShimmer.visibility = View.GONE
        cardView.visibility = View.VISIBLE
    }
}
package com.kars.codeforcesmobile.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.kars.codeforcesmobile.R
import com.kars.codeforcesmobile.USER_PREFERENCES
import com.kars.codeforcesmobile.USER_PREFERENCES_NOTIFICATION
import com.kars.codeforcesmobile.profile.viewmodels.PreferencesViewModel
import kotlinx.android.synthetic.main.preferences_fragment.*

class PreferencesFragment : Fragment() {

    private lateinit var viewModel: PreferencesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.preferences_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val isNotificationEnabled: Boolean? =
            context?.getSharedPreferences("user_preferences", 0)?.getBoolean("Notification", false)
        if (isNotificationEnabled != null && isNotificationEnabled)
            switchEnableNotification.isChecked = true
        switchEnableNotification.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            context?.getSharedPreferences(USER_PREFERENCES, 0)?.edit()
                ?.putBoolean(USER_PREFERENCES_NOTIFICATION, b)?.apply()
        }
    }

}
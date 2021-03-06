package com.batarilo.vinylcollectionPremium.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.batarilo.vinylcollectionPremium.R


class SettingsFragment : PreferenceFragmentCompat() {

    val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_settings, rootKey)

        val myPref = findPreference("delete_cache") as Preference?
        myPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            openDialogCache()
            true
        }

    }

    private fun openDialogCache(){

        AlertDialog.Builder(context)
            .setTitle("Delete cache")
            .setMessage("Are you sure you want to erase all cache data?") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteCache()
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }




}
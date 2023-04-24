package com.apaul9.cinescope.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.apaul9.cinescope.R
import com.apaul9.cinescope.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.apply {

            // Based on the shared preferences, check the radio buttons
            if (sharedPrefs.getBoolean("sortVoteHL", false)) {
                sortVoteHL.isChecked = true
            } else if (sharedPrefs.getBoolean("sortVoteLH", false)) {
                sortVoteLH.isChecked = true
            }

            if (sharedPrefs.getBoolean("latest", false)) {
                latest.isChecked = true
            } else if (sharedPrefs.getBoolean("oldest", false)) {
                oldest.isChecked = true
            }

            if (sharedPrefs.getBoolean("safe", false)) {
                safeResultSpinner.setSelection(0)
            } else if (sharedPrefs.getBoolean("notSafe", false)) {
                safeResultSpinner.setSelection(1)
            }


            saveButton.setOnClickListener(View.OnClickListener {
                val editor = sharedPrefs.edit()
                if (sortVoteHL.isChecked) {
                    editor.putBoolean("sortVoteHL", true)
                } else if (sortVoteLH.isChecked) {
                    editor.putBoolean("sortVoteLH", true)
                }

                if (latest.isChecked) {
                    editor.putBoolean("latest", true)
                } else if (oldest.isChecked) {
                    editor.putBoolean("oldest", true)
                }

                // Safe Search Spinner that has Safe and Not Safe
                if (safeResultSpinner.selectedItem.toString() == "Safe") {
                    editor.putBoolean("safe", true)
                } else if (safeResultSpinner.selectedItem.toString() == "Not Safe") {
                    editor.putBoolean("notSafe", true)
                }
                editor.apply()

                // Navigate back to Dashboard
                val action = SettingsFragmentDirections.actionSettingsFragmentToNavigationDashboard()
                view?.findNavController()?.navigate(action)

            })
            resetBtn.setOnClickListener(View.OnClickListener {
                // Uncheck all radio buttons
                sortVoteHL.isChecked = false
                sortVoteLH.isChecked = false
                latest.isChecked = false
                oldest.isChecked = false
                safeResultSpinner.setSelection(0)

                val editor = sharedPrefs.edit()
                editor.clear()
                editor.apply()
            })
        }

        return binding.root

    }
}
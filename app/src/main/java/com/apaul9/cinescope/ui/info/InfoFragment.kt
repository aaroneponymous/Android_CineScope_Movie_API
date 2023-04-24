package com.apaul9.cinescope.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apaul9.cinescope.BuildConfig
import com.apaul9.cinescope.R
import com.apaul9.cinescope.databinding.FragmentInfoBinding



class InfoFragment : Fragment(R.layout.fragment_info) {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        binding.apply {

            appName.text = getString(R.string.app_name)
            appVersion.text = BuildConfig.VERSION_NAME
            buildType.text = BuildConfig.BUILD_TYPE
            apiKey.text = BuildConfig.apikey


        }


        return binding.root


    }

}
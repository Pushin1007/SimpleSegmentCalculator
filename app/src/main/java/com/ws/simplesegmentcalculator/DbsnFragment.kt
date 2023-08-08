package com.ws.simplesegmentcalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.ws.simplesegmentcalculator.databinding.FragmentDbsnBinding


class DbsnFragment : Fragment() {
    private var _binding: FragmentDbsnBinding? = null
    val binding: FragmentDbsnBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDbsnBinding.inflate(inflater, container, false)
        return binding.root

        val actionBar: ActionBar? = (activity as MainActivity?)!!.supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        fun newInstance(): DbsnFragment = DbsnFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
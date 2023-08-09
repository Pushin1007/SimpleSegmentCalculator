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



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBackToMain.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commit()
        }
    }
    companion object {
        fun newInstance(): DbsnFragment = DbsnFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
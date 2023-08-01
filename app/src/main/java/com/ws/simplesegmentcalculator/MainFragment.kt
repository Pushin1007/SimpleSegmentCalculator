package com.ws.simplesegmentcalculator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.ws.simplesegmentcalculator.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var aGrad: Double = 0.0
    private var a: Double = 0.0
    private var r: Double = 0.0

    private lateinit var parentActivity: MainActivity // Получаем родительскую активити
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity =
            requireActivity() as MainActivity //  со встроенной проверкой актвити на null)
    }

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appCompatImageView.setOnClickListener {

            var outerDiameter = binding.inputOuterDiameterVal.text.toString().toDoubleOrNull()
            var ringWidth = binding.inputRingWidthVal.text.toString().toDoubleOrNull()
            var AllowancePerSide = binding.inputAllowancePerSideVal.text.toString().toDoubleOrNull()
            var countSegment = binding.inputCountSegmentVal.text.toString().toIntOrNull()


            if (countSegment != null) {
                if (countSegment != 0) {
                    aGrad = (360 / countSegment).toDouble()//угол сегмента в градусах
                    a = aGrad * (Math.PI / 180) //угол сегмента в радианах
                } else {
                    Toast.makeText(parentActivity, "not 0", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(parentActivity, "enter", Toast.LENGTH_SHORT).show()
            }

            if (outerDiameter != null) {
                if (outerDiameter != 0.0) {
                    r = outerDiameter / 2
                } else {
                    Toast.makeText(parentActivity, "not 0", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(parentActivity, "enter", Toast.LENGTH_SHORT).show()
            }

            binding.calculateAngle.setText("$aGrad\u00B0")

            binding.calculateWidth.setText(String.format("%.1f", (2 * Math.tan(a / 2) * r)))
            binding.calculateHeight.setText(
                String.format(
                    "%.1f",
                    (r - (r - ringWidth!! * Math.cos(a / 2)))
                )
            )


        }
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
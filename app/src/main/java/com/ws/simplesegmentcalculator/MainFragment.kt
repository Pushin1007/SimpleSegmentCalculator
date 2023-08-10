package com.ws.simplesegmentcalculator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ws.simplesegmentcalculator.databinding.FragmentMainBinding


class MainFragment : Fragment() {


    private var countSegment: Int? = 0 // Количество сегментов
    private var aGrad: Double = 0.0//угол сегмента в градусах
    private var a2Rad: Double = 0.0 //половинный угол сегмента в радианах
    private var radiusBlank: Double = 0.0//наружный радиус заготовки с припуском
    private var widthBlank: Double = 0.0//ширина кольца (заготовки) с припуском
    private var legthSegment: Double = 0.0//Длина сегмента
    private var widthSegment: Double = 0.0//Высота сегмента
    private var totalLegth: Double = 0.0//минимальная длина заготовки

    private lateinit var segment: Segment

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appCompatImageView.setOnClickListener {
            keybordHide(
                parentActivity,
                binding.focusConteiner
            )// скрываем клавиатуру и убираем фокус
            //получаем данные введенные пользователем
            val outerDiameter =
                binding.inputOuterDiameterVal.text.toString().toDoubleOrNull() //наружный диаметр
            val ringWidth =
                binding.inputRingWidthVal.text.toString().toDoubleOrNull() // ширина кольца
            var AllowancePerSide =
                binding.inputAllowancePerSideVal.text.toString()
                    .toDoubleOrNull() // припуск на обработку на сторону
            countSegment =
                binding.inputCountSegmentVal.text.toString()
                    .toIntOrNull() // количество сегментов в кольце

//проверяем введенные данные
            if (countSegment != null) {
                if (countSegment != 0) {

                    aGrad = calcAngleOfSegment(countSegment!!) / 2
                    a2Rad = convertGradToRad(aGrad)
                } else {
                    Toast.makeText(
                        parentActivity,
                        R.string.сountOfSegmentcannotbe0,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(parentActivity, R.string.enterCountOfSegment, Toast.LENGTH_SHORT)
                    .show()
            }

            if (outerDiameter != null) {
                if (outerDiameter != 0.0) {
                    radiusBlank = outerDiameter / 2
                } else {
                    Toast.makeText(
                        parentActivity,
                        R.string.outerDiametercannotbe0,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(parentActivity, R.string.enterOuterDiameter, Toast.LENGTH_SHORT)
                    .show()
            }


            if (ringWidth != null) {
                if (ringWidth != 0.0) {
                    if (outerDiameter != null) {
                        if (AllowancePerSide == null) {
                            AllowancePerSide = 0.0
                        }
                        radiusBlank = outerDiameter / 2 + AllowancePerSide
                        widthBlank = ringWidth + 2 * AllowancePerSide
                    }
                } else {
                    Toast.makeText(parentActivity, R.string.ringWidthcannotbe0, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(parentActivity, R.string.enterRingWidth, Toast.LENGTH_SHORT).show()
            }

            legthSegment = calcLengthOfSegment(a2Rad, radiusBlank)
            widthSegment = calcHeightOfSegment(a2Rad, radiusBlank, widthBlank)
            totalLegth = calcTotalLegth(countSegment!!, a2Rad, legthSegment, widthSegment)

            val totalLegthString: String =
                getString(R.string.textTotalLegth) +
                        " " +
                        doubleToString(totalLegth) +
                        " " +
                        getString(R.string.mm)
            // заполняем данными схему
            binding.calculateAngle.setText("$aGrad\u00B0") //u00B0  это знак радиуса
            binding.calculateLength.setText(doubleToString(legthSegment))
            binding.calculateHeight.setText(doubleToString(widthSegment))
            binding.textTotal.setText(totalLegthString)

        }

        binding.buttonInfo.setOnClickListener {
            // Toast.makeText(parentActivity, "ggg", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DbsnFragment.newInstance()).commit()
        }

        binding.buttonTotalLegthInfo.setOnClickListener {
            val dialogFragmentTotalLegth = DialogFragmentTotalLegth()
            dialogFragmentTotalLegth.show(childFragmentManager, "TAG")
        }
    }

    private fun doubleToString(value: Double): String { //преобразование из Double  в String од ин знак после запятой
        return String.format("%.1f", value)
    }

    private fun convertGradToRad(angleGrad: Double): Double { // конвертируем градусы в радианы
        return angleGrad * (Math.PI / 180)
    }

    // вычисляем угол сегмента в градусах
    private fun calcAngleOfSegment(countSegment: Int): Double {
        return (360 / countSegment).toDouble()
    }

    // Вычисляем длину сегмента
    private fun calcLengthOfSegment(
        angleRad: Double, radius: Double
    ): Double {
        return 2 * Math.tan(angleRad) * radius
    }

    // Вычисляем высоту сегмента
    private fun calcHeightOfSegment(angleRad: Double, radius: Double, width: Double): Double {

        val innerRadius = radius - width
        val heightOfSegment = width + innerRadius - innerRadius * Math.cos(angleRad)
        return heightOfSegment
    }

    // Вычисляем высоту сегмента
    private fun calcTotalLegth(
        countSegment: Int,
        angleRad: Double,
        legthSegment: Double,
        widthSegment: Double
    ): Double {
        val a = legthSegment - widthSegment * Math.tan(angleRad)
        val b = 3.5 * Math.cos(angleRad)

        return (countSegment + 1) * a + countSegment * b + 100
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    // Скрыть клавиатуру и убирать фокус из View элемента
    fun keybordHide(yourActivity: Activity, mSearchView: View) {
        val inputMethodManager =
            yourActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            yourActivity.currentFocus?.getWindowToken(), 0
        )
        mSearchView.post { mSearchView.clearFocus() }
    }


}



package com.ws.simplesegmentcalculator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
    private var aGrad: Double = 0.0        // угол сегмента в градусах
    private var a2Rad: Double = 0.0        // половинный угол сегмента в радианах
    private var radiusBlank: Double = 0.0  // наружный радиус заготовки с припуском
    private var widthBlank: Double = 0.0   // ширина кольца (заготовки) с припуском
    private var legthSegment: Double = 0.0 // Длина сегмента
    private var widthSegment: Double = 0.0 // Высота сегмента
    private var totalLegth: Double = 0.0   // минимальная длина заготовки

    // true = дюймы, false = миллиметры
    private var isInchMode: Boolean = false

    private lateinit var prefs: SharedPreferences
    private lateinit var segment: Segment
    private lateinit var parentActivity: MainActivity

    companion object {
        private const val PREFS_NAME = "calculator_prefs"
        private const val KEY_INCH_MODE = "inch_mode"

        private const val MM_TO_INCH = 1.0 / 25.4
        private const val INCH_TO_MM = 25.4

        fun newInstance(): MainFragment = MainFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = requireActivity() as MainActivity
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding
        get() = _binding!!

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

        // Восстанавливаем сохранённое состояние кнопки
        isInchMode = prefs.getBoolean(KEY_INCH_MODE, false)
        updateInchButtonAppearance()

        // Кнопка переключения мм / дюймы
        binding.buttonInch.setOnClickListener {
            isInchMode = !isInchMode
            prefs.edit().putBoolean(KEY_INCH_MODE, isInchMode).apply()
            updateInchButtonAppearance()
        }

        // Нажатие на схему — расчёт
        binding.appCompatImageView.setOnClickListener {
            keybordHide(parentActivity, binding.focusConteiner)

            // Считываем введённые значения
            var outerDiameter = binding.inputOuterDiameterVal.text.toString().toDoubleOrNull()
            var ringWidth = binding.inputRingWidthVal.text.toString().toDoubleOrNull()
            var allowancePerSide = binding.inputAllowancePerSideVal.text.toString().toDoubleOrNull()
            countSegment = binding.inputCountSegmentVal.text.toString().toIntOrNull()

            // Если режим дюймов — переводим входные данные в мм для расчёта
            if (isInchMode) {
                outerDiameter = outerDiameter?.times(INCH_TO_MM)
                ringWidth = ringWidth?.times(INCH_TO_MM)
                allowancePerSide = allowancePerSide?.times(INCH_TO_MM)
            }

            // Проверяем количество сегментов
            if (countSegment != null) {
                if (countSegment != 0) {
                    aGrad = calcAngleOfSegment(countSegment!!) / 2
                    a2Rad = convertGradToRad(aGrad)
                } else {
                    Toast.makeText(parentActivity, R.string.сountOfSegmentcannotbe0, Toast.LENGTH_SHORT).show()
                }
            } else {
                countSegment = 0
                Toast.makeText(parentActivity, R.string.enterCountOfSegment, Toast.LENGTH_SHORT).show()
            }

            // Проверяем наружный диаметр
            if (outerDiameter != null) {
                if (outerDiameter != 0.0) {
                    radiusBlank = outerDiameter / 2
                } else {
                    Toast.makeText(parentActivity, R.string.outerDiametercannotbe0, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(parentActivity, R.string.enterOuterDiameter, Toast.LENGTH_SHORT).show()
            }

            // Проверяем ширину кольца
            if (ringWidth != null) {
                if (ringWidth != 0.0) {
                    if (outerDiameter != null) {
                        if (allowancePerSide == null) allowancePerSide = 0.0
                        radiusBlank = outerDiameter / 2 + allowancePerSide
                        widthBlank = ringWidth + 2 * allowancePerSide
                    }
                } else {
                    Toast.makeText(parentActivity, R.string.ringWidthcannotbe0, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(parentActivity, R.string.enterRingWidth, Toast.LENGTH_SHORT).show()
            }

            // Вычисляем результаты (всегда в мм внутри)
            legthSegment = calcLengthOfSegment(a2Rad, radiusBlank)
            widthSegment = calcHeightOfSegment(a2Rad, radiusBlank, widthBlank)
            totalLegth = calcTotalLegth(countSegment!!, a2Rad, legthSegment, widthSegment)

            // Если режим дюймов — переводим результаты обратно в дюймы для отображения
            val displayLength = if (isInchMode) legthSegment * MM_TO_INCH else legthSegment
            val displayWidth = if (isInchMode) widthSegment * MM_TO_INCH else widthSegment
            val displayTotal = if (isInchMode) totalLegth * MM_TO_INCH else totalLegth
            val unitLabel = if (isInchMode) getString(R.string.inch) else getString(R.string.mm)

            val totalLegthString = getString(R.string.textTotalLegth) + " " +
                    doubleToString(displayTotal) + " " + unitLabel

            binding.calculateAngle.setText("$aGrad\u00B0")
            binding.calculateLength.setText(doubleToString(displayLength))
            binding.calculateHeight.setText(doubleToString(displayWidth))
            binding.textTotal.setText(totalLegthString)
        }

        binding.buttonInfo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DbsnFragment.newInstance()).commit()
        }

        binding.buttonTotalLegthInfo.setOnClickListener {
            val dialogFragmentTotalLegth = DialogFragmentTotalLegth()
            dialogFragmentTotalLegth.show(childFragmentManager, "TAG")
        }
    }

    /** Обновляем внешний вид кнопки в зависимости от режима */
    private fun updateInchButtonAppearance() {
        if (isInchMode) {
            // Режим дюймов — кнопка светло-серая, текст тёмный
            binding.buttonInch.setBackgroundColor(Color.parseColor("#CCCCCC"))
            binding.buttonInch.setTextColor(Color.parseColor("#333333"))
        } else {
            // Режим мм — кнопка прозрачная (стандартный outlined стиль)
            binding.buttonInch.setBackgroundColor(Color.TRANSPARENT)
            binding.buttonInch.setTextColor(Color.parseColor("#6A6A6A"))
        }
    }

    private fun doubleToString(value: Double): String {
        return String.format("%.1f", value)
    }

    private fun convertGradToRad(angleGrad: Double): Double {
        return angleGrad * (Math.PI / 180)
    }

    private fun calcAngleOfSegment(countSegment: Int): Double {
        return (360 / countSegment).toDouble()
    }

    private fun calcLengthOfSegment(angleRad: Double, radius: Double): Double {
        return 2 * Math.tan(angleRad) * radius
    }

    private fun calcHeightOfSegment(angleRad: Double, radius: Double, width: Double): Double {
        val innerRadius = radius - width
        return width + innerRadius - innerRadius * Math.cos(angleRad)
    }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun keybordHide(yourActivity: Activity, mSearchView: View) {
        val inputMethodManager =
            yourActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(yourActivity.currentFocus?.windowToken, 0)
        mSearchView.post { mSearchView.clearFocus() }
    }
}

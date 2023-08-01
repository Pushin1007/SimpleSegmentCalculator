package com.ws.simplesegmentcalculator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.ws.simplesegmentcalculator.databinding.FragmentMainBinding

class MainFragment : Fragment() {


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

            var outerDiameter =binding.inputOuterDiameterVal.text.toString().toDoubleOrNull()
            var ringWidth =binding.inputRingWidthVal.text.toString().toDoubleOrNull()
            var AllowancePerSide =binding.inputAllowancePerSideVal.text.toString().toDoubleOrNull()
            var countSegment =binding.inputCountSegmentVal.text.toString().toDoubleOrNull()


            binding.calculateAngle.setText("$outerDiameter\u00B0")

            /*
    public void calculateSegments(View view) {

        EditText numSeg = findViewById(R.id.numSeg);
        EditText diamEdit = findViewById(R.id.diamEdit);
        EditText widthEdit = findViewById(R.id.widthEdit);


        Double a=0.0; //угол сегмента в радианах
        Double aGrad=0.0; //угол сегмента в градусах
        Double l=0.0; // длина сегмента
        Double h=0.0; //высота сегмента
        Double r= Double.parseDouble(String.valueOf(diamEdit.getText())) / 2;

        aGrad = 360/Double.parseDouble(String.valueOf(numSeg.getText()));

        a = aGrad * (Math.PI / 180);
        l = 2 * Math.tan(a / 2) * r;
        h = r - (r - Double.parseDouble(String.valueOf(widthEdit.getText()))) * Math.cos(a / 2);

        TextView angleCalc = findViewById(R.id.angleCalc);
        TextView lengthCalc = findViewById(R.id.lengthCalc);
        TextView widthCalc = findViewById(R.id.widthCalc);

        //str = new DecimalFormat("#.0#").format(d)

        angleCalc.setText(new DecimalFormat("#.0#").format(aGrad));
        lengthCalc.setText(new DecimalFormat("#.0#").format(l));
        widthCalc.setText(new DecimalFormat("#.0#").format(h));



    }
             */
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
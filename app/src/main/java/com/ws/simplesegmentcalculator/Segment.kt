package com.ws.simplesegmentcalculator

import android.content.Context
import android.widget.Toast

class Segment (
   // var context: Context,
    var outerDiameter :Double?,
    var ringWidth:Double?,
    var AllowancePerSide :Double?,
    var countSegment : Int?
){

    //угол сегмента в градусах
    private var aGrad: Double =0.0
    private var a2Rad: Double = 0.0 //половинный угол сегмента в радианах
    private var radiusBlank: Double = 0.0//наружный радиус заготовки с припуском
    private var widthBlank: Double = 0.0//ширина кольца (заготовки) с припуском


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

        var innerRadius = radius - width
        val heightOfSegment = width + innerRadius - innerRadius * Math.cos(angleRad)
        return heightOfSegment
    }

}

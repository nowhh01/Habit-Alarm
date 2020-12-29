package com.gl.habitalarm.utils

import android.graphics.Canvas
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class LineXAxisRenderer(
    viewPortHandler: ViewPortHandler?,
    xAxis: XAxis?,
    trans: Transformer?
) : XAxisRenderer(viewPortHandler, xAxis, trans) {
    override fun drawLabel(
        c: Canvas,
        formattedLabel: String,
        x: Float,
        y: Float,
        anchor: MPPointF,
        angleDegrees: Float
    ) {
        val lines = formattedLabel.split("\n".toRegex()).toTypedArray()
        Utils.drawXAxisValue(
            c,
            lines[0], x, y, mAxisLabelPaint, anchor, angleDegrees
        )
        for (i in 1 until lines.size) {
            Utils.drawXAxisValue(
                c,
                lines[i],
                x,
                y + mAxisLabelPaint.textSize * i,
                mAxisLabelPaint,
                anchor,
                angleDegrees
            )
        }
    }
}
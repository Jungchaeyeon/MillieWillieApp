package com.makeusteam.milliewillie.ui.decorators

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.makeusteam.milliewillie.R

/**
 * Span to draw a dot centered under a section of text
 */
class CustomDotSpan : LineBackgroundSpan {
    private var radius: Float =3F
    private var color: Int =0
    private var index : Float =0F
    private var resources: Resources

    constructor(radius: Float, color: Int, index : Float, resources: Resources) {
        this.radius = radius
        this.color = color
        this.index = index
        this.resources = resources
    }

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        charSequence: CharSequence,
        start: Int, end: Int, lineNum: Int,
    ) {
        val oldColor = paint.color
        if (color != 0) {
            paint.color = color
        }
        if(index >1.8){
            paint.color = resources.getColor(R.color.icon_gray)
//            canvas.drawCircle(((left + right) / 4).toFloat(), bottom.toFloat(), radius, paint)
//            canvas.drawCircle(((left + right) / 3).toFloat(), bottom.toFloat(), radius, paint)
//            canvas.drawCircle(((left + right) / 4).toFloat(), bottom.toFloat(), radius, paint)
            canvas.drawCircle((left + right)*0.4.toFloat(), 1.75F*(bottom+top).toFloat(), 3F, paint)
            canvas.drawCircle(((left + right) / 2).toFloat(), 1.75F*(bottom+top).toFloat(), 3F, paint)
            canvas.drawCircle(((left + right)*0.6).toFloat(), 1.75F*(bottom+top).toFloat(), 3F, paint)
        }
        else{
        canvas.drawCircle(((left + right) / 2).toFloat(), (bottom*(index.minus(0.15F))), radius, paint)}
        paint.color = oldColor
    }

    companion object {
        /**
         * Default radius used
         */
        const val DEFAULT_RADIUS = 3f
    }
}
package com.makeus.milliewillie.ui.decorators

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.makeus.milliewillie.R

/**
 * Span to draw a dot centered under a section of text
 */
class CustomLineSpan : LineBackgroundSpan {
    private var radius: Float =3F
    private var color: Int =0
    private var index : Float =0F
    private var resources: Resources

    constructor(radius: Float, color: Int, index : Float, resources: Resources) {
        this.radius = radius
        this.color = color
        this.index = index
        this.resources =resources
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
        //canvas.drawCircle(((left + right) / 2).toFloat(), bottom + (radius*(index*4)), radius, paint)
        paint.strokeWidth= 5F
        if(index >2.0){
//            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_calendar_more)
//            canvas.drawBitmap(imageBitmap,0F,bottom.toFloat(),paint)
            paint.color = resources.getColor(R.color.text_gray)
            canvas.drawCircle((left + right)*0.4.toFloat(), 2.1F*(bottom+top).toFloat(), radius, paint)
            canvas.drawCircle(((left + right) / 2).toFloat(), 2.1F*(bottom+top).toFloat(), radius, paint)
            canvas.drawCircle(((left + right)*0.6).toFloat(), 2.1F*(bottom+top).toFloat(), radius, paint)
        }
        else{
        canvas.drawLine(0F, (bottom*index), right.toFloat(), (bottom*index), paint)
        paint.color = oldColor
        }
    }

    companion object {
        /**
         * Default radius used
         */
        const val DEFAULT_RADIUS = 3f
    }
}
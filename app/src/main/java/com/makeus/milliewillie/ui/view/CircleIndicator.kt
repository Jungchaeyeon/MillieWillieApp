package com.makeus.milliewillie.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import com.makeus.milliewillie.R

class CircleIndicator : LinearLayout{

    private var mContext: Context? = null

    private var mDefaultCircle: Int = 0
    private var mSelectCircle: Int = 0
    private var mUseCircle: Int = 0

    private var imageDot: MutableList<ImageView> = mutableListOf()

    // 4.5dp 를 픽셀 단위로 바꿉니다.
    private val temp = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 8.0f, resources.displayMetrics
    )

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    /**
     * 기본 점 생성
     * @param count 점의 갯수
     * @param defaultCircle 기본 점의 이미지
     * @param selectCircle 선택된 점의 이미지
     * @param position 선택된 점의 포지션
     */
    fun createDotPanel(count: Int, defaultCircle: Int, selectCircle: Int, position: Int) {
        this.removeAllViews()

        mDefaultCircle = defaultCircle
        mSelectCircle = selectCircle
        mUseCircle = R.drawable.indicator_dot_use

        for (i in 0 until count) {
            imageDot.add(ImageView(mContext).apply {
                if (i < count - 1) {
                    setPadding(0, 0, temp.toInt(), 0)
                }
            })
            this.addView(imageDot[i])
        }

        //인덱스 선택
        selectDot(position)
    }

    fun customCreateDotPanel(count: Int, defaultCircle: Int, selectCircle: Int) {
        this.removeAllViews()

        mDefaultCircle = defaultCircle
        mSelectCircle = selectCircle
        mUseCircle = R.drawable.indicator_dot_use

        for (i in 0 until count) {
            imageDot.add(ImageView(mContext).apply {
                if (i < count - 1) {
                    setPadding(0, 0, temp.toInt(), 0)
                }
            })
            this.addView(imageDot[i])
        }
        selectDots(0)
    }
    /**
     * 선택된 점 표시
     * @param position
     */
    fun selectDot(position: Int) {
        imageDot.forEachIndexed { index, imageView ->
            imageView.setImageResource(if (index == position) mSelectCircle else mDefaultCircle)
        }
    } fun selectDots(nums: Int) {
        imageDot.forEachIndexed { index, imageView ->
            imageView.setImageResource(if (index < nums) mSelectCircle else mDefaultCircle)
        }
    }
    fun selectDotsTwice(nums: Int, uNums:Int) {
//        imageDot.forEachIndexed { index, imageView ->
//            imageView.setImageResource(if (index < rNums) mSelectCircle else mDefaultCircle)
//            imageView.setImageResource(if (index < uNums) mUseCircle else mSelectCircle)
//            imageView.setImageResource(if(index<uNums))
//        }
        imageDot.forEachIndexed {index, imageView->
                if(index<uNums) imageView.setImageResource(mUseCircle)
                else if(index<nums) imageView.setImageResource(mSelectCircle)
                else imageView.setImageResource(mDefaultCircle)
        }

    }
}
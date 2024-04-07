package cn.edu.bistu.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import cn.edu.bistu.R


@SuppressLint("Recycle")
class ArcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var mHeight = 0

    /**
     * 弧形高度
     */
    private val mArcHeight: Int

    /**
     * 背景颜色
     */
    private val mBgColor: Int
    private val mPaint: Paint
    private val mContext: Context

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView)
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcView_arcHeight, 0)
        mBgColor = typedArray.getColor(R.styleable.ArcView_bgColor, Color.parseColor("#303F9F"))
        mContext = context
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.style = Paint.Style.FILL
        mPaint.setColor(mBgColor)
        val rect = Rect(0, 0, mWidth, mHeight - mArcHeight)
        canvas.drawRect(rect, mPaint)
        val path = Path()
        path.moveTo(0F, (mHeight - mArcHeight).toFloat())
        path.quadTo(
            (mWidth / 2).toFloat(), mHeight.toFloat(), mWidth.toFloat(),
            (mHeight - mArcHeight).toFloat()
        )
        canvas.drawPath(path, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        }
        setMeasuredDimension(mWidth, mHeight)
    }
}
package com.faker.netease.tagedseekbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar


/**
 *
 * created by reol at 2019-05-16
 */
class TaggedSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var primaryColor = 0
    private var emptyColor = 0

    private var progressWidth = 0f
    private var progressCorner = 0f
    private var thumbRadius = 0f
    private var thumbStrokeWidth = 0f

    private var tagPadding = 0f
    private var tagCorner = 0f
    private var tagWidth = 0f
    private var tagHeight = 0f
    private var indicatorHeight = 0f
    private var tagTextSize = 0f
    private var tagTextColor = 0

    var progress = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var tagSuffix = "%"
    //----------------------------
    private var maxProgress = 100f
    //----------------------------

    private val mainPaint = Paint()
    private val bgPaint = Paint()
    private val thumbPaint = Paint()
    private val textPaint = Paint()

    private var bgRect = RectF()
    private var progressRect = RectF()
    private var tagRect = RectF()
    private val path = Path()

    private var cx = 0f
    private var cy = 0f

    init {
        initAttr(context, attrs)
        initPaint()
    }


    private fun initPaint() {
        mainPaint.apply {
            color = primaryColor
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        bgPaint.apply {
            color = emptyColor
            isAntiAlias = true
        }
        thumbPaint.apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
        }
        textPaint.apply {
            color = tagTextColor
            textAlign = Paint.Align.CENTER
            textSize = tagTextSize
        }
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TaggedSeekBar)
        progress = ta.getFloat(R.styleable.TaggedSeekBar_progress, 0f)
        primaryColor = ta.getColor(R.styleable.TaggedSeekBar_primaryColor, Color.parseColor("#05c3f9"))
        emptyColor = ta.getColor(R.styleable.TaggedSeekBar_emptyColor, Color.parseColor("#cccccc"))
        progressWidth = ta.getDimension(R.styleable.TaggedSeekBar_progressWidth, 10f)
        progressCorner = ta.getDimension(R.styleable.TaggedSeekBar_progressCorner, 10f)
        thumbRadius = ta.getDimension(R.styleable.TaggedSeekBar_thumbRadius, progressWidth * 2)
        thumbStrokeWidth =
            ta.getDimension(R.styleable.TaggedSeekBar_thumbStrokeWidth, progressWidth / 2)

        indicatorHeight =
            ta.getDimension(R.styleable.TaggedSeekBar_tagIndicatorHeight, thumbRadius + thumbStrokeWidth)

        tagWidth = ta.getDimension(R.styleable.TaggedSeekBar_tagWidth, thumbRadius * 8)
        tagHeight = ta.getDimension(R.styleable.TaggedSeekBar_tagHeight, thumbRadius * 4)
        tagCorner = ta.getDimension(R.styleable.TaggedSeekBar_tagCorner, thumbRadius)
        tagTextSize = ta.getDimension(R.styleable.TaggedSeekBar_tagTextSize, thumbRadius * 2)
        tagTextColor = ta.getColor(R.styleable.TaggedSeekBar_tagTextColor, Color.WHITE)

        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = 0
        var height = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        width = when (widthMode) {
            MeasureSpec.AT_MOST -> widthSize
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.UNSPECIFIED -> widthSize
            else -> widthSize
        } // progress bar should be as long as possible

        height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.UNSPECIFIED -> heightSize
            MeasureSpec.AT_MOST -> (paddingBottom + paddingTop + thumbRadius * 2 + thumbStrokeWidth * 2 + indicatorHeight + tagHeight).toInt()
            else -> heightSize
        } // height can be 'wrap_content'

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val maxAdditionWidth = if (tagWidth / 2 > (thumbRadius + thumbStrokeWidth)) {
                tagWidth / 2
            } else {
                thumbRadius + thumbStrokeWidth
            }

            bgRect.left = 0f + paddingLeft + maxAdditionWidth
            bgRect.right = width.toFloat() - paddingRight - maxAdditionWidth
            bgRect.bottom =
                height.toFloat() - paddingBottom - (thumbRadius + thumbStrokeWidth - progressWidth / 2)
            bgRect.top = bgRect.bottom - progressWidth

            drawRoundRect(bgRect, progressCorner, progressCorner, bgPaint)

            progressRect.left = bgRect.left
            progressRect.bottom = bgRect.bottom
            progressRect.top = bgRect.top
            progressRect.right =
                bgRect.left + (bgRect.right - bgRect.left) * (progress / maxProgress)

            drawRoundRect(progressRect, progressCorner, progressCorner, mainPaint)

            cx = progressRect.right
            cy = progressRect.bottom - progressWidth / 2

            drawCircle(cx, cy, thumbRadius + thumbStrokeWidth, mainPaint)
            drawCircle(cx, cy, thumbRadius, thumbPaint)

            val iy = cy - thumbRadius - thumbStrokeWidth - tagPadding
            path.moveTo(cx, iy)
            path.lineTo(cx - indicatorHeight / 2, iy - indicatorHeight)
            path.lineTo(cx + indicatorHeight / 2, iy - indicatorHeight)
            path.close()

            drawPath(path, mainPaint)
            path.reset()

            tagRect.left = cx - tagWidth / 2
            tagRect.right = cx + tagWidth / 2
            tagRect.bottom = iy - indicatorHeight
            tagRect.top = tagRect.bottom - tagHeight

            drawRoundRect(tagRect, tagCorner, tagCorner, mainPaint)

            val tagText = progress.toInt().toString() + tagSuffix
//            val textWidth = textPaint.measureText(tagText)
            val fMatrix = textPaint.fontMetrics
            val textY =
                tagRect.top + tagHeight / 2 + (Math.abs(fMatrix.ascent) - fMatrix.descent) / 2
            drawText(tagText, cx, textY, textPaint)
        }
    }

    private var touchStartX = 0f
    private var touchStartCX = 0f
    // 支持点击拖动进度：SeekBar功能

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (inClickArea(event.x, event.y)) {
                    touchStartX = event.x
                    touchStartCX = cx
                    return true
                } else {
                    return false
                }
            }
            MotionEvent.ACTION_UP -> {
                progressListener?.progressChanged(progress)
            }
            MotionEvent.ACTION_MOVE -> {
                // invalidate view by set progress
                val d = event.x - touchStartX
                progress = calcProgressByDrag(d)
                progressListener?.progressChanging(progress)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun calcProgressByDrag(dragDistance: Float): Float{
        val result = (touchStartCX + dragDistance - bgRect.left)/(bgRect.right - bgRect.left) * 100f
        return when  {
            result > 100f -> 100f
            result < 0f -> 0f
            else -> result
        }
    }

    private fun inClickArea(x: Float, y: Float): Boolean {
        //only tag area and thumb area can be dragged
        val isInTagArea = (x in tagRect.left..tagRect.right) && (y in tagRect.top..tagRect.bottom)
        val isInThumbArea = (x > cx - thumbStrokeWidth - thumbRadius && x < cx + thumbStrokeWidth + thumbRadius)
                && (y > cy - thumbStrokeWidth - thumbRadius && y < cy + thumbStrokeWidth + thumbRadius)

        return isInTagArea || isInThumbArea
    }

    private var progressListener: OnProgressChangedListener? = null

    fun setOnProgressChangedListener(listener: OnProgressChangedListener){
        this.progressListener = listener
    }

    interface OnProgressChangedListener{
        fun progressChanging(progress: Float)
        fun progressChanged(progress: Float)
    }

}

fun RectF.toLogString(): String {
    return "l:$left; r:$right; t:$top; b:$bottom;"
}
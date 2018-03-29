package demo.moqi.sample.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import demo.moqi.sample.R

/** 自定义背景样式的 Button
 * Created by reol on 28/03/2018.
 */


class BetterButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private var solid = Color.WHITE
    private var solidPressed = Color.WHITE
    private var strokeWidth = 0
    private var strokeColor = Color.WHITE
    private var strokeColorPressed = Color.WHITE
    private var textColorPressed = textColors.defaultColor
    private var corners = 0f
    private var radiusLT = 0f
    private var radiusLB = 0f
    private var radiusRT = 0f
    private var radiusRB = 0f
    private var clickAnim = 1

    private lateinit var enlargeAnim: AnimatorSet
    private lateinit var zoomAnim: AnimatorSet

    init {
        getAttrs(context, attrs)

        this.background = generateBackgroundDrawable()
        val textSelector = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_enabled)),
                intArrayOf(textColorPressed, textColors.defaultColor))
        this.setTextColor(textSelector)

        if (clickAnim == 1){
            initAnim()
            this.setOnTouchListener { _, event ->
                when(event.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        zoomAnim.start()
                    }

                    MotionEvent.ACTION_UP -> {
                        enlargeAnim.start()
                    }
                }
                return@setOnTouchListener false
            }
        }
    }

    /**
     * 初始化点击效果动画：目前只有点击缩小
     */
    private fun initAnim() {
        val a1 = ObjectAnimator.ofFloat(this, "scaleX", 0.9f, 1.0f)
        val a2 = ObjectAnimator.ofFloat(this, "scaleY", 0.9f, 1.0f)
        enlargeAnim = AnimatorSet()
        enlargeAnim.playTogether(a1, a2)
        enlargeAnim.duration = 100

        val a3 = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.9f)
        val a4 = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.9f)
        zoomAnim = AnimatorSet()
        zoomAnim.playTogether(a3,a4)
        zoomAnim.duration = 100
    }

    /**
     * 根据xml中的一些参数生成背景
     */
    private fun generateBackgroundDrawable(): StateListDrawable {
        val bgNormal = GradientDrawable()
        bgNormal.setColor(solid)
        if (strokeWidth > 0) {
            bgNormal.setStroke(strokeWidth, strokeColor)
        }
        val bgPressed = GradientDrawable()
        bgPressed.setColor(solidPressed)
        if (strokeWidth > 0) {
            bgPressed.setStroke(strokeWidth, strokeColorPressed)
        }

        if (corners != 0f) {
            bgNormal.cornerRadius = corners
            bgPressed.cornerRadius = corners
        } else {
            bgNormal.cornerRadii = floatArrayOf(radiusLT, radiusLT, radiusRT, radiusRT, radiusRB, radiusRB, radiusLB, radiusLB)
            bgPressed.cornerRadii = floatArrayOf(radiusLT, radiusLT, radiusRT, radiusRT, radiusRB, radiusRB, radiusLB, radiusLB)
        }

        return StateListDrawable().let {
            it.addState(intArrayOf(android.R.attr.state_pressed), bgPressed)
            it.addState(intArrayOf(android.R.attr.state_enabled), bgNormal)
            it
        }
    }

    private fun getAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BetterButton)
        solid = typedArray.getColor(R.styleable.BetterButton_solid, Color.WHITE)
        solidPressed = typedArray.getColor(R.styleable.BetterButton_solidPressed, Color.WHITE)
        strokeColor = typedArray.getColor(R.styleable.BetterButton_strokeColor, Color.WHITE)
        strokeColorPressed = typedArray.getColor(R.styleable.BetterButton_strokeColorPressed, Color.WHITE)
        textColorPressed = typedArray.getColor(R.styleable.BetterButton_textColorPressed, textColors.defaultColor)
        strokeWidth = typedArray.getDimension(R.styleable.BetterButton_strokeWidth, 0f).toInt()
        corners = typedArray.getDimension(R.styleable.BetterButton_corners, 0f)
        radiusLT = typedArray.getDimension(R.styleable.BetterButton_radiusLeftTop, 0f)
        radiusLB = typedArray.getDimension(R.styleable.BetterButton_radiusLeftBottom, 0f)
        radiusRT = typedArray.getDimension(R.styleable.BetterButton_radiusRightTop, 0f)
        radiusRB = typedArray.getDimension(R.styleable.BetterButton_radiusRightBottom, 0f)
        clickAnim = typedArray.getInt(R.styleable.BetterButton_clickAnim, 0)

        typedArray.recycle()
    }

}
package dora.widget

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import dora.widget.titlebar.R

open class DoraNavTitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0, defStyleRes: Int = 0)
                                        : DoraTitleBar(context, attrs, defStyleAttr, defStyleRes) {

    private var onCloseClickListener: OnCloseClickListener? = null
    private val closeIconView = AppCompatImageView(context)
    private var closeIconBox = FrameLayout(context)
    private var closeIconMarginStart: Int = dp2px(context, 3f)
        set(value) {
            field = value
            requestLayout()
        }
    private var closeIconBoxPadding: Int = dp2px(context, 12f)
        set(value) {
            field = value
            val iconLp = LayoutParams(closeIconSize, closeIconSize)
            iconLp.marginStart = field
            iconLp.addRule(ALIGN_PARENT_START)
            iconLp.addRule(CENTER_VERTICAL)
            closeIconView.layoutParams = iconLp
        }
    var closeIcon: Drawable = createCloseIcon(context)
        set(value) {
            field = value
            closeIconView.background = field
        }
    @ColorInt var closeIconTint: Int = Color.TRANSPARENT
        set(value) {
            field = value
            closeIconView.imageTintList = ColorStateList.valueOf(closeIconTint)
        }
    var closeIconSize: Int = dp2px(context, 16f)
        set(value) {
            field = value
            val iconLp = LayoutParams(closeIconSize, closeIconSize)
            iconLp.marginStart = closeIconMarginStart
            iconLp.addRule(END_OF, R.id.dview_titlebar_back)
            iconLp.addRule(CENTER_VERTICAL)
            closeIconView.layoutParams = iconLp
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DoraNavTitleBar)
        closeIcon = a.getDrawable(R.styleable.DoraNavTitleBar_dview_closeIcon) ?: closeIcon
        closeIconTint = a.getColor(R.styleable.DoraNavTitleBar_dview_closeIconTint, closeIconTint)
        closeIconSize = a.getDimensionPixelSize(R.styleable.DoraNavTitleBar_dview_closeIconSize, closeIconSize)
        closeIconMarginStart = a.getDimensionPixelSize(R.styleable.DoraNavTitleBar_dview_closeIconMarginStart, closeIconMarginStart)
        closeIconBoxPadding = a.getDimensionPixelOffset(R.styleable.DoraNavTitleBar_dview_closeIconBoxPadding, closeIconBoxPadding)
        a.recycle()
        initView(context)
        initListener(context)
    }

    private fun initView(context: Context) {
        val closeIconBoxLp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        closeIconBoxLp.marginStart = backIconMarginStart
        closeIconBoxLp.addRule(ALIGN_PARENT_START)
        closeIconBoxLp.addRule(CENTER_VERTICAL)
        closeIconView.background = closeIcon
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, applyWrapContentSize(heightMeasureSpec, dp2px(context, 48f)))
        measureChild(closeIconBox, widthMeasureSpec, heightMeasureSpec)
    }

    private fun initListener(context: Context) {
        closeIconBox.setOnClickListener {
            onCloseClickListener?.onCloseClick(closeIconView)
            if (isClickBackIconClose) {
                (context as Activity).finish()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawChild(canvas, closeIconBox, drawingTime)
    }

    interface OnCloseClickListener {

        /**
         * 左侧的返回键按钮被点击。
         */
        fun onCloseClick(icon: AppCompatImageView)
    }

    fun setOnCloseClickListener(listener: OnCloseClickListener) {
        onCloseClickListener = listener
    }

    /**
     * 设置wrap_content的情况下，给定默认宽高。
     *
     * @param expected 期望的值
     */
    private fun applyWrapContentSize(measureSpec: Int, expected: Int): Int {
        var measureSpec = measureSpec
        val mode: Int = MeasureSpec.getMode(measureSpec)
        if (mode == MeasureSpec.UNSPECIFIED
            || mode == MeasureSpec.AT_MOST
        ) {
            measureSpec = MeasureSpec.makeMeasureSpec(expected, MeasureSpec.EXACTLY)
        }
        return measureSpec
    }

    /**
     * 创建默认的返回按钮。
     */
    private fun createCloseIcon(context: Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_dview_titlebar_close) ?: BitmapDrawable()
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }
}
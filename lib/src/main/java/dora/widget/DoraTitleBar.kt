package dora.widget

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import dora.widget.titlebar.R

/**
 * 简易标题栏。
 */
open class DoraTitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    private var onIconClickListener: OnIconClickListener? = null
    val titleView = AppCompatTextView(context)
    val iconView = AppCompatImageView(context)
    private var isShowIcon: Boolean = true
    var icon: Drawable = createDefaultIcon(context)
        set(value) {
            field = value
            iconView.background = field
        }
    var iconSize: Int = dp2px(context, 16f)
        set(value) {
            field = value
            val iconLp = LayoutParams(iconSize, iconSize)
            iconLp.marginStart = iconMarginStart
            iconLp.addRule(ALIGN_PARENT_START)
            iconLp.addRule(CENTER_VERTICAL)
            iconView.layoutParams = iconLp
        }
    var iconMarginStart: Int = dp2px(context, 12f)
        set(value) {
            field = value
            val iconLp = LayoutParams(iconSize, iconSize)
            iconLp.marginStart = field
            iconLp.addRule(ALIGN_PARENT_START)
            iconLp.addRule(CENTER_VERTICAL)
            iconView.layoutParams = iconLp
        }
    var isClickIconClose: Boolean = true
        set(value) {
            field = value
        }
    private var isShowTitle: Boolean = true
    var title: String = context.getString(R.string.dview_titlebar_default_title)
        set(value) {
            field = value
            titleView.text = title
        }
    @ColorInt var titleTextColor: Int = Color.WHITE
        set(value) {
            field = value
            titleView.setTextColor(titleTextColor)
        }
    var titleTextSize: Int = sp2px(context, 18f)
        set(value) {
            field = value
            titleView.textSize = px2sp(context, field.toFloat())
        }
    var isTitleTextBold: Boolean = true
        set(value) {
            field = value
            if (field) titleView.typeface = Typeface.DEFAULT_BOLD else titleView.typeface = Typeface.DEFAULT
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DoraTitleBar)
        isShowIcon = a.getBoolean(R.styleable.DoraTitleBar_dview_isShowIcon, isShowIcon)
        icon = a.getDrawable(R.styleable.DoraTitleBar_dview_icon) ?: icon
        iconSize = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_iconSize, iconSize)
        iconMarginStart = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_iconMarginStart, iconMarginStart)
        isClickIconClose = a.getBoolean(R.styleable.DoraTitleBar_dview_isClickIconClose, isClickIconClose)
        isShowTitle = a.getBoolean(R.styleable.DoraTitleBar_dview_isShowTitle, isShowTitle)
        title = a.getString(R.styleable.DoraTitleBar_dview_title) ?: title
        titleTextColor = a.getColor(R.styleable.DoraTitleBar_dview_titleTextColor, titleTextColor)
        titleTextSize = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_titleTextSize, titleTextSize)
        isTitleTextBold = a.getBoolean(R.styleable.DoraTitleBar_dview_isTitleTextBold, isTitleTextBold)
        a.recycle()
        initView(context)
        initStyle(context)
        initListener(context)
    }

    private fun initView(context: Context) {
        val iconLp = LayoutParams(iconSize, iconSize)
        iconLp.marginStart = iconMarginStart
        iconLp.addRule(ALIGN_PARENT_START)
        iconLp.addRule(CENTER_VERTICAL)
        val titleLp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        titleLp.addRule(CENTER_IN_PARENT)
        if (isShowIcon) addView(iconView, iconLp)
        if (isShowTitle) addView(titleView, titleLp)
        iconView.background = icon
        titleView.text = title
        titleView.textSize = px2sp(context, titleTextSize.toFloat())
        titleView.setTextColor(titleTextColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, applyWrapContentSize(heightMeasureSpec, dp2px(context, 48f)))
        if (isShowIcon) measureChild(iconView, widthMeasureSpec, heightMeasureSpec)
        if (isShowTitle) measureChild(titleView, widthMeasureSpec, heightMeasureSpec)
    }

    private fun initListener(context: Context) {
        iconView.setOnClickListener {
            onIconClickListener?.onIconStartClick(iconView)
            if (isClickIconClose) {
                (context as Activity).finish()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isShowIcon) drawChild(canvas, iconView, drawingTime)
        if (isShowTitle) drawChild(canvas, titleView, drawingTime)
    }

    interface OnIconClickListener {
        fun onIconStartClick(icon: AppCompatImageView)
    }

    fun setOnIconClickListener(listener: OnIconClickListener) {
        onIconClickListener = listener
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
            measureSpec = MeasureSpec.makeMeasureSpec(expected, View.MeasureSpec.EXACTLY)
        }
        return measureSpec
    }

    /**
     * 标题栏默认背景色，推荐R.color.colorPrimary,将覆盖android:background的值。
     */
    @ColorRes open fun defaultBackgroundColor(): Int {
        return android.R.color.black
    }

    private fun initStyle(context: Context) {
        setBackgroundColor(ContextCompat.getColor(context, defaultBackgroundColor()))
    }

    private fun createDefaultIcon(context: Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.ic_dview_titlebar_back) ?: BitmapDrawable()
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    private fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    private fun px2sp(context: Context, pxVal: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return pxVal / scale
    }
}
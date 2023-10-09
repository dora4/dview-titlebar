package dora.widget

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import dora.widget.titlebar.R
import java.util.Locale

/**
 * 简易标题栏。
 */
open class DoraTitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var onIconClickListener: OnIconClickListener? = null
    val titleView = AppCompatTextView(context)
    val backIconView = AppCompatImageView(context)
    private var backIconBox = FrameLayout(context)
    /**
     * 存放右边所有菜单按钮的容器。
     */
    private val menuIconContainer = LinearLayoutCompat(context)
    private var isShowBackIcon: Boolean = true
    var backIcon: Drawable = createBackIcon(context)
        set(value) {
            field = value
            backIconView.background = field
        }
    @ColorInt var backIconTint: Int = Color.TRANSPARENT
        set(value) {
            field = value
            backIconView.imageTintList = ColorStateList.valueOf(backIconTint)
        }
    var backIconSize: Int = dp2px(context, 16f)
        set(value) {
            field = value
            val iconLp = LayoutParams(backIconSize, backIconSize)
            iconLp.marginStart = backIconMarginStart
            iconLp.addRule(ALIGN_PARENT_START)
            iconLp.addRule(CENTER_VERTICAL)
            backIconView.layoutParams = iconLp
        }
    var backIconBoxPadding: Int = dp2px(context, 6f)
        set(value) {
            field = value
            requestLayout()
        }
    var menuIconBoxPadding: Int = dp2px(context, 3f)
        set(value) {
            field = value
            requestLayout()
        }
    var backIconMarginStart: Int = dp2px(context, 12f)
        set(value) {
            field = value
            val iconLp = LayoutParams(backIconSize, backIconSize)
            iconLp.marginStart = field
            iconLp.addRule(ALIGN_PARENT_START)
            iconLp.addRule(CENTER_VERTICAL)
            backIconView.layoutParams = iconLp
        }
    var menuIconMarginEnd: Int = dp2px(context, 12f)
        set(value) {
            field = value
        }
    var isClickBackIconClose: Boolean = true
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
    private val menuBoxList: MutableList<FrameLayout> = arrayListOf()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DoraTitleBar)
        isShowBackIcon = a.getBoolean(R.styleable.DoraTitleBar_dview_isShowBackIcon, isShowBackIcon)
        backIcon = a.getDrawable(R.styleable.DoraTitleBar_dview_backIcon) ?: backIcon
        backIconTint = a.getColor(R.styleable.DoraTitleBar_dview_backIconTint, backIconTint)
        backIconSize = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_backIconSize, backIconSize)
        backIconMarginStart = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_backIconMarginStart, backIconMarginStart)
        menuIconMarginEnd = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_menuIconMarginEnd, menuIconMarginEnd)
        backIconBoxPadding = a.getDimensionPixelOffset(R.styleable.DoraTitleBar_dview_backIconBoxPadding, backIconBoxPadding)
        menuIconBoxPadding = a.getDimensionPixelOffset(R.styleable.DoraTitleBar_dview_menuIconBoxPadding, menuIconBoxPadding)
        isClickBackIconClose = a.getBoolean(R.styleable.DoraTitleBar_dview_isClickBackIconClose, isClickBackIconClose)
        isShowTitle = a.getBoolean(R.styleable.DoraTitleBar_dview_isShowTitle, isShowTitle)
        title = a.getString(R.styleable.DoraTitleBar_dview_title) ?: title
        titleTextColor = a.getColor(R.styleable.DoraTitleBar_dview_titleTextColor, titleTextColor)
        titleTextSize = a.getDimensionPixelSize(R.styleable.DoraTitleBar_dview_titleTextSize, titleTextSize)
        isTitleTextBold = a.getBoolean(R.styleable.DoraTitleBar_dview_isTitleTextBold, isTitleTextBold)
        a.recycle()
        initView(context)
        initListener(context)
    }

    private fun initView(context: Context) {
        val titleLp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        titleLp.addRule(CENTER_IN_PARENT)
        val backIconBoxLp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        backIconBoxLp.marginStart = backIconMarginStart
        backIconBoxLp.addRule(ALIGN_PARENT_START)
        backIconBoxLp.addRule(CENTER_VERTICAL)
        val menuIconContainerLp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        menuIconContainerLp.marginEnd = menuIconMarginEnd
        menuIconContainerLp.addRule(ALIGN_PARENT_END)
        menuIconContainerLp.addRule(CENTER_VERTICAL)
        backIconBox = wrapButton(true, backIconView)
        if (isShowBackIcon) addView(backIconBox, backIconBoxLp)
        if (isShowTitle) addView(titleView, titleLp)
        menuIconContainer.gravity = Gravity.CENTER_VERTICAL
        menuIconContainer.orientation = LinearLayoutCompat.HORIZONTAL
        addView(menuIconContainer, menuIconContainerLp)
        backIconView.background = backIcon
        titleView.text = title
        titleView.textSize = px2sp(context, titleTextSize.toFloat())
        titleView.setTextColor(titleTextColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, applyWrapContentSize(heightMeasureSpec, dp2px(context, 48f)))
        if (isShowBackIcon) measureChild(backIconBox, widthMeasureSpec, heightMeasureSpec)
        if (isShowTitle) measureChild(titleView, widthMeasureSpec, heightMeasureSpec)
    }

    private fun initListener(context: Context) {
        backIconBox.setOnClickListener {
            onIconClickListener?.onIconBackClick(backIconView)
            if (isClickBackIconClose) {
                (context as Activity).finish()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isShowBackIcon) drawChild(canvas, backIconBox, drawingTime)
        if (isShowTitle) drawChild(canvas, titleView, drawingTime)
    }

    interface OnIconClickListener {

        /**
         * 左侧的返回键按钮被点击。
         */
        fun onIconBackClick(icon: AppCompatImageView)

        /**
         * 右侧的菜单按钮被点击。
         */
        fun onIconMenuClick(position: Int, icon: AppCompatImageView)
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
     * 支持阿拉伯地区从右向左的布局。
     */
    private fun isRtl() : Boolean {
        return TextUtilsCompat.getLayoutDirectionFromLocale(context.resources.configuration.locale) ==
                ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * 创建默认的返回按钮。
     */
    private fun createBackIcon(context: Context): Drawable {
        val backIcon = ContextCompat.getDrawable(context, R.drawable.ic_dview_titlebar_back)
        return  backIcon?: BitmapDrawable()
    }

    private fun createMenuButton(@DrawableRes iconResId: Int, width: Int, height: Int) : AppCompatImageView {
        val menuButton = AppCompatImageView(context)
        menuButton.layoutParams = LayoutParams(width, height)
        menuButton.setImageResource(iconResId)
        return menuButton
    }

    fun addMenuButton(@DrawableRes iconResId: Int) : DoraTitleBar {
        val defaultIconSize = dp2px(context, 24f)
        addMenuButton(iconResId, defaultIconSize)
        return this
    }

    fun addMenuButton(@DrawableRes iconResId: Int, iconSize: Int) : DoraTitleBar {
        addMenuButton(createMenuButton(iconResId, iconSize, iconSize))
        return this
    }

    fun addMenuButton(menuIconView: AppCompatImageView) : DoraTitleBar {
        val menuBox = wrapButton(false, menuIconView)
        menuBox.setOnClickListener {
            if (menuBox.childCount > 0) {
                val imageView = menuBox.getChildAt(0) as AppCompatImageView
                menuBoxList.forEachIndexed { index, frameLayout ->
                    if (menuBox == frameLayout) {
                        onIconClickListener?.onIconMenuClick(index, imageView)
                    }
                }
            }
        }
        // 添加到最前面去，这是因为索引从右边向左边递增
        menuIconContainer.addView(menuBox, 0)
        menuBoxList.add(menuBox)
        return this
    }

    /**
     * 增加按钮的点击区域范围。
     */
    private fun wrapButton(isBackButton: Boolean, iconView: AppCompatImageView) : FrameLayout {
        val box = FrameLayout(context)
        if (isBackButton) {
            iconView.id = R.id.dview_titlebar_back
            iconView.imageTintList = ColorStateList.valueOf(backIconTint)
            if (isRtl()) {
                iconView.rotation = 180f
            }
            box.setPadding(backIconBoxPadding, backIconBoxPadding, backIconBoxPadding, backIconBoxPadding)
            val lp = FrameLayout.LayoutParams(backIconSize, backIconSize)
            box.addView(iconView, lp)
        } else {
            box.setPadding(menuIconBoxPadding, menuIconBoxPadding, menuIconBoxPadding, menuIconBoxPadding)
            box.addView(iconView)
        }
        return box
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
package dora.widget

import android.content.Context
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DoraMarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(
    context, attrs, defStyle
) {
    init {
        init()
    }

    private fun init() {
        setSingleLine()
        ellipsize = TruncateAt.MARQUEE
    }

    override fun isFocused(): Boolean {
        return true
    }
}
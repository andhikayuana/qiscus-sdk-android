package com.qiscus.sdk.chat.presentation.uikit.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.qiscus.sdk.chat.presentation.uikit.R
import kotlinx.android.synthetic.main.view_qiscus_comment_composer.view.*

/**
 * @author yuana
 * @since 10/26/17
 */

class QiscusCommentComposer : FrameLayout {

    private var mView: View? = null
    private var iconAttachment: Drawable? = null
    private var iconEmoticon: Drawable? = null

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflateView()

        val a = this.context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.QiscusCommentComposer,
                0, 0
        )

        try {

//            TODO : styleable
            iconAttachment = a.getDrawable(R.styleable.QiscusCommentComposer_btnAttachmentIcon)
            iconEmoticon = a.getDrawable(R.styleable.QiscusCommentComposer_btnEmoticonIcon)

            initDefaultAttrs()

        } finally {
            a.recycle()
        }
    }

    private fun initDefaultAttrs() {
        if (iconAttachment == null) iconAttachment = ContextCompat.getDrawable(context, R.drawable.ic_qiscus_attach_file)
        if (iconEmoticon == null) iconEmoticon = ContextCompat.getDrawable(context, R.drawable.ic_qiscus_insert_emoticon)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        displayAttrs()
    }

    fun setAction(callback: QiscusCommentComposerListener) {
        commentComposerButtonSend.setOnClickListener {
            if (commentComposerTextField.text.isNotBlank()) {
                callback.onClickSend(it, commentComposerTextField.text.toString())
                commentComposerTextField.setText("")
            }
        }
        commentComposerAttachFile.setOnClickListener {
            callback.onClickAttachment(it)
        }
        commentComposerInsertEmoticon.setOnClickListener {
            callback.onClickInsertEmoticon(it)
        }

    }

    private fun displayAttrs() {
//        todo
        commentComposerAttachFile.setImageDrawable(iconAttachment)
        commentComposerInsertEmoticon.setImageDrawable(iconEmoticon)
        invalidateAndRequestLayout()
    }

    private fun inflateView() {
        mView = getInflater().inflate(R.layout.view_qiscus_comment_composer, this)
    }

    private fun getInflater(): LayoutInflater {
        return context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private fun invalidateAndRequestLayout() {
        invalidate()
        requestLayout()
    }

    interface QiscusCommentComposerListener {

        fun onClickSend(v: View?, message: String)

        fun onClickAttachment(v: View?)

        fun onClickInsertEmoticon(v: View?)
    }
}

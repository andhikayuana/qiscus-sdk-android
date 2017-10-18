package com.qiscus.sdk.chat.presentation.model

import android.text.Spannable
import com.qiscus.sdk.chat.core.Qiscus
import com.qiscus.sdk.chat.domain.model.*
import com.qiscus.sdk.chat.domain.repository.UserRepository
import com.qiscus.sdk.chat.presentation.MentionClickHandler
import com.qiscus.sdk.chat.presentation.util.toReadableText
import com.qiscus.sdk.chat.presentation.util.toSpannable
import java.util.*

/**
 * Created on : October 05, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
open class CommentReplyViewModel
@JvmOverloads constructor(comment: Comment,
                          account: Account = Qiscus.instance.component.dataComponent.accountRepository.getAccount().blockingGet(),
                          userRepository: UserRepository = Qiscus.instance.component.dataComponent.userRepository,
                          mentionAllColor: Int,
                          mentionOtherColor: Int,
                          mentionMeColor: Int,
                          mentionClickListener: MentionClickHandler? = null)
    : CommentViewModel(comment, account, userRepository, mentionAllColor, mentionOtherColor, mentionMeColor, mentionClickListener) {

    val repliedComment by lazy {
        val payload = comment.type.payload
        CommentViewModel(
                Comment(CommentId(payload.optString("replied_comment_id")),
                        payload.optString("replied_comment_message"),
                        User(payload.optString("replied_comment_sender_email"), payload.optString("replied_comment_sender_username")),
                        Date(), comment.room, CommentState.ON_SERVER,
                        CommentType(payload.optString("replied_comment_type"),
                                payload.optJSONObject("replied_comment_payload"))),
                account, userRepository, mentionAllColor, mentionOtherColor, mentionMeColor, mentionClickListener
        )
    }

    override fun determineReadableMessage(): String {
        return comment.type.payload.optString("text", comment.message).toReadableText(userRepository)
    }

    override fun determineSpannableMessage(): Spannable {
        return comment.type.payload.optString("text", comment.message)
                .toSpannable(account, userRepository, mentionAllColor, mentionOtherColor,
                        mentionMeColor, mentionClickListener)
    }
}
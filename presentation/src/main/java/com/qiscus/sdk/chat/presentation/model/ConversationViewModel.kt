package com.qiscus.sdk.chat.presentation.model

import com.qiscus.sdk.chat.domain.model.Room

/**
 * Created on : October 04, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
data class ConversationViewModel(var room: Room, var lastComment: CommentViewModel? = null)
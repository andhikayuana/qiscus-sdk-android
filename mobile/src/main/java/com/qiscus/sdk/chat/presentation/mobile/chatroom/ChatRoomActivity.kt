/*
 * Copyright (c) 2016 Qiscus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiscus.sdk.chat.presentation.mobile.chatroom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.qiscus.sdk.chat.core.Qiscus
import com.qiscus.sdk.chat.domain.interactor.comment.PostComment
import com.qiscus.sdk.chat.domain.model.Room
import com.qiscus.sdk.chat.presentation.listcomment.ListCommentContract
import com.qiscus.sdk.chat.presentation.mobile.R
import com.qiscus.sdk.chat.presentation.model.CommentViewModel
import com.qiscus.sdk.chat.presentation.sendcomment.SendCommentContract
import com.qiscus.sdk.chat.presentation.uikit.comment.CommentAdapter
import com.qiscus.sdk.chat.presentation.uikit.widget.QiscusCommentComposer
import kotlinx.android.synthetic.main.activity_chat_room.*

/**
 * Created on : August 19, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
fun Context.chatRoomIntent(room: Room): Intent {
    return Intent(this, ChatRoomActivity::class.java).apply {
        putExtra(INTENT_ROOM_ID, room.id)
    }
}

fun Context.chatRoomIntent(roomId: String): Intent {
    return Intent(this, ChatRoomActivity::class.java).apply {
        putExtra(INTENT_ROOM_ID, roomId)
    }
}

private const val INTENT_ROOM_ID = "room_id"

class ChatRoomActivity : AppCompatActivity(), ListCommentContract.View, SendCommentContract.View,
        QiscusCommentComposer.QiscusCommentComposerListener {

    private lateinit var listCommentPresenter: ListCommentContract.Presenter
    private lateinit var sendCommentPresenter: SendCommentContract.Presenter

    private val adapter = CommentAdapter()

    private val useCaseFactory = Qiscus.instance.useCaseFactory
    private val commentFactory = Qiscus.instance.commentFactory
    private val postComment = useCaseFactory.postComment()

    private var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        getIntentExtras()

        initAdapter()

        initRecyclerView()

        iniAction()

        init()
    }

    private fun iniAction() {
        commentComposer.setAction(this)
    }

    override fun onClickSend(v: View?, message: String) {
        sendMessage(message)
    }

    override fun onClickInsertEmoticon(v: View?) {
        Toast.makeText(applicationContext, "insert emoticon", Toast.LENGTH_SHORT).show()
    }

    private fun sendMessage(msg: String) {
        val comment = commentFactory.createTextComment(roomId!!, msg)
        postComment.execute(PostComment.Params(comment))
    }

    private fun initRecyclerView() {
        commentRecyclerView.adapter = adapter
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.setHasFixedSize(true)
    }

    private fun initAdapter() {
        adapter.registerViewHolderFactory(TextViewHolderFactory(this))
        adapter.registerViewHolderFactory(ImageViewHolderFactory(this))
        adapter.registerViewHolderFactory(DefaultViewHolderFactory(this))
    }

    private fun getIntentExtras() {
        roomId = intent.getStringExtra(INTENT_ROOM_ID)
        if (roomId == null) {
            throw RuntimeException("Please provide room id!")
        }
    }

    private fun init() {
        val chatRoomActivityComponent = ChatRoomActivityComponent(this)
        listCommentPresenter = chatRoomActivityComponent.listCommentPresenter
        sendCommentPresenter = chatRoomActivityComponent.sendCommentPresenter

        listCommentPresenter.setRoomId(roomId!!)
        listCommentPresenter.start()
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun addComment(commentViewModel: CommentViewModel) {
        adapter.addOrUpdate(commentViewModel)
        commentRecyclerView.smoothScrollToPosition(adapter.itemCount)
    }

    override fun updateComment(commentViewModel: CommentViewModel) {
        adapter.addOrUpdate(commentViewModel)
    }

    override fun removeComment(commentViewModel: CommentViewModel) {
        adapter.removeComment(commentViewModel)
    }

    override fun clearTextField() {
        Log.d("ZETRA", "clearTextField")
    }

    override fun onStop() {
        super.onStop()
        listCommentPresenter.stop()
        sendCommentPresenter.stop()
    }
}

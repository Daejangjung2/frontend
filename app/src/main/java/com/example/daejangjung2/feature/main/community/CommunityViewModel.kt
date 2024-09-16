package com.example.daejangjung2.feature.main.community

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityViewModel: ViewModel() {
    private val _posts = MutableLiveData<List<CommunityPost>>()
    val posts: LiveData<List<CommunityPost>> get() = _posts

    init {
        // 샘플 데이터 추가
        _posts.value = listOf(
            CommunityPost("Title 1", "Content 1"),
            CommunityPost("Title 2", "Content 2")
        )
    }
}

// CommunityPost 데이터 클래스
data class CommunityPost(val title: String, val content: String)
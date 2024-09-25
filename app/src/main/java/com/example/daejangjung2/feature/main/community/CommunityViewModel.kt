package com.example.daejangjung2.feature.main.community

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.data.model.response.PostContent
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.PostCallAllRepository
import com.example.daejangjung2.domain.repository.PostCallLocationRepository
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val postcallallRepository: PostCallAllRepository,
    private val postCallLocationRepository: PostCallLocationRepository,
) : ViewModel() {

    private val _posts = MutableLiveData<List<PostCallAllResponse>>()  // 기존 LiveData에 PostCallAllResponse 타입 적용
    val posts: LiveData<List<PostCallAllResponse>> get() = _posts

    private val _posts_location = MutableLiveData<List<PostContent>>()  // 기존 LiveData에 PostCallLocationResponse 타입 적용
    val posts_location: LiveData<List<PostContent>> get() = _posts_location

    init {
        // 초기값으로 샘플 데이터 추가 (PostCallAllResponse 타입으로 변환)
        _posts.value = emptyList()

        // _posts_location에 대한 샘플 데이터 추가
        _posts_location.value = emptyList()  //빈 리스트로 초기화
    }

    // API를 통해 모든 게시물을 불러오는 함수
    fun fetchAllPosts() {
        viewModelScope.launch {
            when (val response = postcallallRepository.postcallall()) {
                is ApiResponse.Success -> {
                    val postData: List<PostCallAllResponse> = response.body.data
                    // LiveData에 새로운 리스트로 업데이트
                    _posts.value = postData
                    Log.d("community", "성공")
                }
                is ApiResponse.Failure -> {
                    // 실패 처리 (오류 시 빈 리스트 할당)
                    _posts.value = emptyList()
                    Log.d("community", "실패")
                }
                else -> {
                    // 기타 예외 처리
                    Log.d("community", "예상치 못한 응답: ${response}")
                }
            }
        }
    }

    // API를 통해 위치당 게시물을 불러오는 함수
    fun fetchLocationPosts(location: String) {
        viewModelScope.launch {
            when (val response = postCallLocationRepository.postcalllocation(location, 0, 100)) {
                is ApiResponse.Success -> {
                    val postData: List<PostContent> = response.body.data.content
                    // LiveData에 새로운 리스트로 업데이트
                    _posts_location.value = postData
                    Log.d("community", "성공")
                }
                is ApiResponse.Failure -> {
                    // 실패 처리 (오류 시 빈 리스트 할당)
                    _posts.value = emptyList()
                    Log.d("community", "실패")
                }
                else -> {
                    // 기타 예외 처리
                    Log.d("community", "예상치 못한 응답: ${response}")
                }
            }
        }
    }

    // 새로운 게시물 추가 함수 (PostCallAllResponse 구조에 맞게 추가)
    fun addPost(post: PostCallAllResponse) {
        val currentPosts = _posts.value ?: emptyList()
        _posts.value = currentPosts + post
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return CommunityViewModel(
                    DaejangjungApplication.container.postcallallRepository,
                    DaejangjungApplication.container.postcalllocationRepository,
                ) as T
            }
        }
    }
}

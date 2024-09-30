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
import com.example.daejangjung2.data.model.response.CommunityComment
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.PostCallAllRepository
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val postcallallRepository: PostCallAllRepository,
) : ViewModel() {

    private val _posts = MutableLiveData<List<PostCallAllResponse>>()  // 기존 LiveData에 PostCallAllResponse 타입 적용
    val posts: LiveData<List<PostCallAllResponse>> get() = _posts

    private val _posts_location = MutableLiveData<List<PostCallLocationResponse>>()  // 기존 LiveData에 PostCallLocationResponse 타입 적용
    val posts_location: LiveData<List<PostCallLocationResponse>> get() = _posts_location

    init {
        // 초기값으로 샘플 데이터 추가 (PostCallAllResponse 타입으로 변환)
        _posts.value = listOf(
            PostCallAllResponse(
                createdAt = "2024-09-22T21:50:59.430156565",
                updateAt = "2024-09-22T21:50:59.430156565",
                postId = 100,
                communityComments = listOf(
                    CommunityComment(
                        id = 1,
                        content = "첫 번째 댓글입니다."
                    )
                ),
                image_url = "https://i.namu.wiki/i/Ly37hgjTsuT2ddZR7PfFDokzFLFG3NQxEowwDcTXfacRVN76wLukOUUshNM-tkys-mJwRyanAmyX0Sf5nzW9PQ.webp",
                title = "Mountain View 1",
                contents = "Beautiful view of the mountains.",
                location = "Seoul",
                view = 10
            ),
            PostCallAllResponse(
                createdAt = "2024-09-23T10:30:12.123456789",
                updateAt = "2024-09-23T10:30:12.123456789",
                postId = 101,
                communityComments = listOf(
                    CommunityComment(
                        id = 2,
                        content = "두 번째 댓글입니다."
                    )
                ),
                image_url = "https://i.namu.wiki/i/xyz1234567890.jpg",
                title = "Ocean View",
                contents = "Beautiful view of the ocean.",
                location = "Busan",
                view = 50
            ),
            PostCallAllResponse(
                createdAt = "2024-09-24T15:15:45.567891234",
                updateAt = "2024-09-24T15:15:45.567891234",
                postId = 102,
                communityComments = listOf(
                    CommunityComment(
                        id = 3,
                        content = "세 번째 댓글입니다."
                    )
                ),
                image_url = "https://i.namu.wiki/i/abc12345.jpg",
                title = "City Skyline",
                contents = "Amazing city skyline view.",
                location = "Incheon",
                view = 75
            ),
            PostCallAllResponse(
                createdAt = "2024-09-25T12:45:10.987654321",
                updateAt = "2024-09-25T12:45:10.987654321",
                postId = 103,
                communityComments = listOf(
                    CommunityComment(
                        id = 4,
                        content = "네 번째 댓글입니다."
                    )
                ),
                image_url = "https://i.namu.wiki/i/def123456.jpg",
                title = "Countryside",
                contents = "Relaxing countryside view.",
                location = "Jeju",
                view = 30
            ),
            PostCallAllResponse(
                createdAt = "2024-09-26T08:22:35.123456789",
                updateAt = "2024-09-26T08:22:35.123456789",
                postId = 104,
                communityComments = listOf(
                    CommunityComment(
                        id = 5,
                        content = "다섯 번째 댓글입니다."
                    )
                ),
                image_url = "https://i.namu.wiki/i/ghi7890123.jpg",
                title = "Desert Sunset",
                contents = "A stunning sunset in the desert.",
                location = "Ulsan",
                view = 90
            )
        )


        // _posts_location에 대한 샘플 데이터 추가
        _posts_location.value = emptyList()  //빈 리스트로 초기화
    }

    // API를 통해 모든 게시물을 불러오는 함수
    fun fetchAllPosts() {
        viewModelScope.launch {
            when (val response = postcallallRepository.postcallall()) {
                is ApiResponse.Success -> {
                    val postData: List<PostCallAllResponse> = response.body.data!!
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
            when (val response = postcallallRepository.postcalllocation(location, 0, 100)) {
                is ApiResponse.Success -> {
                    val postData: List<PostCallLocationResponse> = response.body.data!!
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
                ) as T
            }
        }
    }
}

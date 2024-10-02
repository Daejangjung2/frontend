package com.example.daejangjung2.feature.main.community.detailpost

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.data.model.request.ModifyRequest
import com.example.daejangjung2.data.model.response.ModifyResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.ModifyRepository
import kotlinx.coroutines.launch

class EditPostViewModel(
    private val modifyRepository: ModifyRepository
) : ViewModel() {

    // LiveData 변수 선언
    val title = MutableLiveData<String>()   // 제목
    val content = MutableLiveData<String>() // 내용
    val location = MutableLiveData<String>() // 위치
    val imageUrl = MutableLiveData<String>() // 이미지 URL

    // 데이터 설정 함수
    fun setData(postTitle: String, postContent: String, postLocation: String, postImageUrl: String) {
        title.value = postTitle
        content.value = postContent
        location.value = postLocation
        imageUrl.value = postImageUrl
    }

    private val _modifyResult = MutableLiveData<Boolean>()
    val modifyResult: LiveData<Boolean> get() = _modifyResult

    private val _modifyPost = MutableLiveData<ModifyResponse>()
    val modifyPost: LiveData<ModifyResponse> get() = _modifyPost

    // 게시물 수정 함수
    fun updatePost(postId: Int, title: String, contents: String, location: String, imageUrl: String) {
        viewModelScope.launch {
            val modifyRequest = ModifyRequest(postId, title, contents, location, imageUrl)
            val response = modifyRepository.modify(modifyRequest)
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("EditPost", "게시물 수정 성공")
                    _modifyResult.value = response.body.success
                    _modifyPost.value = response.body.data
                }
                is ApiResponse.Failure -> {
                    _modifyResult.value = false
                    Log.d("EditPost", "게시물 수정 실패: ${response}")
                }
                else -> {
                    _modifyResult.value = false
                    Log.d("EditPost", "게시물 수정 예상치 못한 응답: ${response}")
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return EditPostViewModel(
                    DaejangjungApplication.container.modifyRepository
                ) as T
            }
        }
    }
}

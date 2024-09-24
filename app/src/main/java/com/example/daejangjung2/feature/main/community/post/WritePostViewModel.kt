package com.example.daejangjung2.feature.main.community.post

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.CommunityRepository
import com.example.daejangjung2.feature.main.community.CommunityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WritePostViewModel(
    private val communityRepository: CommunityRepository
): ViewModel() {
    // 제목을 관리하는 MutableLiveData
    val title = MutableLiveData<String>()

    // 내용을 관리하는 MutableLiveData
    val content = MutableLiveData<String>()

    // 위치를 관리하는 MutableLiveData
    val location = MutableLiveData<String>()

    // 글 작성 완료 버튼 클릭 시 호출될 함수
    fun submitPost(createRequest: CreateRequest) {
        // 서버에 게시글 요청
        viewModelScope.launch {
            Log.d("writepost", "실행")
            when (val response = communityRepository.create(createRequest)) {
                is ApiResponse.Success -> {
                    // 성공 처리 (예: 사용자에게 성공 메시지 전달)
                    Log.d("writepost", "성공")
                }
                is ApiResponse.Failure -> {
                    // 실패 처리 (예: 에러 메시지 출력)
                    Log.d("writepost", "실패 응답 코드: ${response.responseCode}, 에러 메시지: ${response.error}")
                }
                else -> {
                    Log.d("writepost", "오류")
                }
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return WritePostViewModel(
                    DaejangjungApplication.container.communityRepository,
                ) as T
            }
        }
    }
}


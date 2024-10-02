package com.example.daejangjung2.feature.main.community.detailpost

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.data.model.request.CommentDeleteRequest
import com.example.daejangjung2.data.model.request.CommentModifyRequest
import com.example.daejangjung2.data.model.request.CommentRequest
import com.example.daejangjung2.data.model.request.DeleteRequest
import com.example.daejangjung2.data.model.request.ModifyRequest
import com.example.daejangjung2.data.model.response.CommentResponse
import com.example.daejangjung2.data.model.response.CommunityComment
import com.example.daejangjung2.data.model.response.DetailPostResponse
import com.example.daejangjung2.data.model.response.ModifyResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.CommentDeleteRepository
import com.example.daejangjung2.domain.repository.CommentModifyRepository
import com.example.daejangjung2.domain.repository.CommentRepository
import com.example.daejangjung2.domain.repository.DeleteRepository
import com.example.daejangjung2.domain.repository.ModifyRepository
import com.example.daejangjung2.domain.repository.PostDetailRepository
import kotlinx.coroutines.launch

class DetailPostViewModel(
    private val repository: PostDetailRepository,
    private val commentDeleteRepository: CommentDeleteRepository,
    private val commentModifyRepository: CommentModifyRepository,
    private val commentRepository: CommentRepository,
    private val modifyRepository: ModifyRepository,
    private val deleteRepository: DeleteRepository
) : ViewModel() {

    // 세부 게시물 정보를 저장하는 LiveData
    private val _detailPost = MutableLiveData<DetailPostResponse>()
    val detailPost: LiveData<DetailPostResponse> get() = _detailPost

    // 댓글 정보를 저장하는 LiveData
    private val _comment = MutableLiveData<CommentResponse>()
    val comment: LiveData<CommentResponse> get() = _comment

    private val _comments = MutableLiveData<List<CommunityComment>>()
    val comments: LiveData<List<CommunityComment>> get() = _comments

    // 세부 게시물 정보를 저장하는 LiveData
    private val _modifyPost = MutableLiveData<ModifyResponse>()
    val modifyPost: LiveData<ModifyResponse> get() = _modifyPost

    private val _modifyresult = MutableLiveData<Boolean>()
    val modifyresult: LiveData<Boolean> get() = _modifyresult

    // 삭제 결과를 저장하는 LiveData
    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    private val _commentdeletResult = MutableLiveData<Boolean>()
    val commentdeleteresult: LiveData<Boolean> get() = _commentdeletResult

    // 게시물 ID를 통해 상세 게시물 정보를 가져오는 함수
    fun loadDetailPost(postId: Int) {
        viewModelScope.launch {
            val response = repository.postdetail(postId)
            when (response) {
                is ApiResponse.Success -> {
                    _detailPost.value = response.body.data
                    Log.d("detailpost", "성공")
                }
                is ApiResponse.Failure -> {
                    // 실패 처리 - 로그 출력 및 UI 반영 등
                    // 필요한 경우 에러 메시지를 LiveData로 업데이트
                    Log.d("detailpost", "실패 : ${response} ${postId}")
                }
                is ApiResponse.NetworkError -> {
                    // 네트워크 오류 처리
                    Log.d("detailpost", "오류")
                }
                is ApiResponse.Unexpected -> {
                    // 예기치 못한 오류 처리
                    Log.d("detailpost", "예상치 못한 상황")
                }
            }
        }
    }

    // 댓글 삭제 함수
    fun deleteComment(postId: Int, commentId: Int) {
        viewModelScope.launch {
            // CommentDeleteRequest 생성
            val commentDeleteRequest = CommentDeleteRequest(commentId)
            val response = commentDeleteRepository.commentdelete(commentDeleteRequest)
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("detailpost", "댓글 삭제 성공")
                    // 댓글 삭제 후 게시물 다시 로드
                    _commentdeletResult.value = response.body.success
                    loadDetailPost(postId)
                }
                is ApiResponse.Failure -> {
                    Log.d("detailpost", "댓글 삭제 실패: ${response}")
                    _commentdeletResult.value = false
                }
                else -> {
                    Log.d("detailpost", "댓글 삭제 예상치 못한 응답: ${response}")
                    _commentdeletResult.value = false
                }
            }
        }
    }

    // 댓글 수정 함수
    fun modifyComment(postId: Int, commentId: Int, comment: String) {
        viewModelScope.launch {
            // CommentmodifyRequest 생성
            val commentmodifyRequest = CommentModifyRequest(postId, commentId, comment)
            val response = commentModifyRepository.commentmodify(commentmodifyRequest)
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("detailpost", "댓글 수정 성공")
                    // 댓글 삭제 후 게시물 다시 로드
                    loadDetailPost(postId)
                }
                is ApiResponse.Failure -> {
                    Log.d("detailpost", "댓글 수정 실패: ${response}")
                }
                else -> {
                    Log.d("detailpost", "댓글 수정 예상치 못한 응답: ${response}")
                }
            }
        }
    }

    // 댓글 추가 함수
    fun addComment(postId: Int, commentText: String) {
        viewModelScope.launch {
            // 댓글 추가 요청 생성
            val commentRequest = CommentRequest(postId, commentText)
            val response = commentRepository.comment(commentRequest)
            when (response) {
                is ApiResponse.Success -> {
                    val newDetailPost = response.body.data
                    if (newDetailPost != null) {
                        _comment.value = newDetailPost  // 전체 게시물 정보를 업데이트
                        _comments.value = newDetailPost.communityComment  // 댓글 목록 업데이트

                        loadDetailPost(postId)
                        Log.d("detailpost", "댓글 추가 성공: $commentText")
                    }
                }
                is ApiResponse.Failure -> {
                    Log.d("detailpost", "댓글 추가 실패: ${response}")
                }
                else -> {
                    Log.d("detailpost", "댓글 추가 예상치 못한 응답: ${response}")
                }
            }
        }
    }

    // 게시물 삭제 함수
    fun deletePost(postId: Int) {
        viewModelScope.launch {
            val deleteRequest = DeleteRequest(postId)
            val response = deleteRepository.delete(deleteRequest)
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("detailpost", "게시물 삭제 성공: ${response.body.message}")
                    _deleteResult.value = response.body.success
                }
                is ApiResponse.Failure -> {
                    Log.d("detailpost", "게시물 삭제 실패: ${response}")
                    _deleteResult.value = false
                }
                else -> {
                    Log.d("detailpost", "게시물 삭제 예상치 못한 응답: ${response}")
                    _deleteResult.value = false
                }
            }
        }
    }

    // 게시물 수정 함수
    fun updatePost(postId: Int, title: String, contents: String, location: String, image_url: String) {
        viewModelScope.launch {
            val modifyRequest = ModifyRequest(postId, title, contents, location, image_url)
            val response = modifyRepository.modify(modifyRequest)
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("detailpost", "게시물 수정 성공")
                    _modifyresult.value = response.body.success
                    _modifyPost.value = response.body.data  // 수정된 게시물 정보 업데이트
                }
                is ApiResponse.Failure -> {
                    _modifyresult.value = false
                    Log.d("detailpost", "게시물 수정 실패: ${response}")
                }
                else -> {
                    _modifyresult.value = false
                    Log.d("detailpost", "게시물 수정 예상치 못한 응답: ${response}")
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
                return DetailPostViewModel(
                    DaejangjungApplication.container.detailpostRepository,
                    DaejangjungApplication.container.commentdeleteRepository,
                    DaejangjungApplication.container.commentmodifyRepository,
                    DaejangjungApplication.container.commentRepository,
                    DaejangjungApplication.container.modifyRepository,
                    DaejangjungApplication.container.deleteRepository
                ) as T
            }
        }
    }
}

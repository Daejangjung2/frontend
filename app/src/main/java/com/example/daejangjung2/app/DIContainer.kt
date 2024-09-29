package com.example.daejangjung2.app

import android.app.Application
import android.content.Context
import com.example.daejangjung2.data.datasource.local.LocalAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommentDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommentDeleteDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommentModifyDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommunityDataSource
import com.example.daejangjung2.data.datasource.network.NetworkDeleteDataSource
import com.example.daejangjung2.data.datasource.network.NetworkDetailPostDataSource
//import com.example.daejangjung2.data.datasource.network.NetworkDetailPostDataSource
import com.example.daejangjung2.data.datasource.network.NetworkMapDataSource
import com.example.daejangjung2.data.datasource.network.NetworkModifyDataSource
import com.example.daejangjung2.data.datasource.network.NetworkPostCallAllDataSource
import com.example.daejangjung2.data.datasource.network.NetworkPostCallLocationDataSource
import com.example.daejangjung2.data.repository.DefaultAuthRepository
import com.example.daejangjung2.data.repository.DefaultCommentDeleteRepository
import com.example.daejangjung2.data.repository.DefaultCommentModifyRepository
import com.example.daejangjung2.data.repository.DefaultCommentRepository
import com.example.daejangjung2.data.repository.DefaultCommunityRepository
import com.example.daejangjung2.data.repository.DefaultDeleteRepository
import com.example.daejangjung2.data.repository.DefaultDetailPostRepository
//import com.example.daejangjung2.data.repository.DefaultDetailPostRepository
import com.example.daejangjung2.data.repository.DefaultMapRepository
import com.example.daejangjung2.data.repository.DefaultModifyRepository
import com.example.daejangjung2.data.repository.DefaultPostCallAllRepository
import com.example.daejangjung2.data.repository.DefaultPostCallLocationRepository
import com.example.daejangjung2.data.retrofit.AuthRetrofit
import com.example.daejangjung2.data.retrofit.AuthService
import com.example.daejangjung2.data.retrofit.CommentDeleteService
import com.example.daejangjung2.data.retrofit.CommentModifyService
import com.example.daejangjung2.data.retrofit.CommentService
import com.example.daejangjung2.data.retrofit.CommunityService
import com.example.daejangjung2.data.retrofit.DefaultRetrofit
import com.example.daejangjung2.data.retrofit.DeleteService
//import com.example.daejangjung2.data.retrofit.DetailPostService
import com.example.daejangjung2.data.retrofit.MapService
import com.example.daejangjung2.data.retrofit.ModifyService
import com.example.daejangjung2.data.retrofit.PostCallAllService
import com.example.daejangjung2.data.retrofit.PostCallLocationService
import com.example.daejangjung2.data.retrofit.PostDetailService
import com.example.daejangjung2.domain.repository.AuthRepository
import com.example.daejangjung2.domain.repository.CommentDeleteRepository
import com.example.daejangjung2.domain.repository.CommentModifyRepository
import com.example.daejangjung2.domain.repository.CommentRepository
import com.example.daejangjung2.domain.repository.CommunityRepository
import com.example.daejangjung2.domain.repository.DeleteRepository
//import com.example.daejangjung2.domain.repository.DetailPostRepository
import com.example.daejangjung2.domain.repository.MapRepository
import com.example.daejangjung2.domain.repository.ModifyRepository
import com.example.daejangjung2.domain.repository.PostCallAllRepository
import com.example.daejangjung2.domain.repository.PostCallLocationRepository
import com.example.daejangjung2.domain.repository.PostDetailRepository
import retrofit2.create

class DIContainer(
    application: Application,
    deviceId: String,
) {
    val sharedPref = application.getSharedPreferences(
        LocalAuthDataSource.AUTH_TOKEN_STORE_NAME, Context.MODE_PRIVATE)

    private val localAuthDataSource: LocalAuthDataSource =
        LocalAuthDataSource(sharedPref)

    private val authClient = AuthRetrofit.createInstance()
    private val authService = authClient.create(AuthService::class.java)
    private val networkAuthDataSource = NetworkAuthDataSource(authService)
    val authRepository: AuthRepository =
        DefaultAuthRepository(localAuthDataSource, networkAuthDataSource)

    // 공통 accessToken 적용 retrofit
    private val default = DefaultRetrofit.createInstance(authRepository)

    private val mapService = default.create(MapService::class.java)
    private val networkMapDataSource = NetworkMapDataSource(mapService)
    val mapRepository: MapRepository = DefaultMapRepository(networkMapDataSource)

    // 커뮤니티
    private val communityService = default.create(CommunityService::class.java)
    private val networkCommunityDataSource = NetworkCommunityDataSource(communityService)
    val communityRepository: CommunityRepository = DefaultCommunityRepository(networkCommunityDataSource)

    // 모든 포스트 부르기
    private val postcallallService = default.create(PostCallAllService::class.java)
    private val networkpostcallallDataSource = NetworkPostCallAllDataSource(postcallallService)
    val postcallallRepository: PostCallAllRepository = DefaultPostCallAllRepository(networkpostcallallDataSource)

    // 지역별 포스트 부르기
    private val postcalllocationService = default.create(PostCallLocationService::class.java)
    private val networkpostcalllocationDataSource = NetworkPostCallLocationDataSource(postcalllocationService)
    val postcalllocationRepository: PostCallLocationRepository = DefaultPostCallLocationRepository(networkpostcalllocationDataSource)

    // 포스트 세부사항 부르기
    private val detailpostService = default.create(PostDetailService::class.java)
    private val networkdetailpostDataSource = NetworkDetailPostDataSource(detailpostService)
    val detailpostRepository: PostDetailRepository = DefaultDetailPostRepository(networkdetailpostDataSource);

    // 포스트 수정
    private val modifyService = default.create(ModifyService::class.java)
    private val networkmodifyDataSource = NetworkModifyDataSource(modifyService)
    val modifyRepository: ModifyRepository = DefaultModifyRepository(networkmodifyDataSource)

    // 포스트 삭제
    private val deleteservice = default.create(DeleteService::class.java)
    private val networkdeleteDataSource = NetworkDeleteDataSource(deleteservice)
    val deleteRepository: DeleteRepository = DefaultDeleteRepository(networkdeleteDataSource)

    // 댓글 추가
    private val commentservice = default.create(CommentService::class.java)
    private val networkcommentDataSource = NetworkCommentDataSource(commentservice)
    val commentRepository: CommentRepository = DefaultCommentRepository(networkcommentDataSource)

    // 댓글 수정
    private val commentmodifyservice = default.create(CommentModifyService::class.java)
    private val networkcommentmodifyDataSource = NetworkCommentModifyDataSource(commentmodifyservice)
    val commentmodifyRepository: CommentModifyRepository = DefaultCommentModifyRepository(networkcommentmodifyDataSource)

    // 댓글 삭제
    private val commentdeleteservice = default.create(CommentDeleteService::class.java)
    private val networkcommentdeleteDataSource = NetworkCommentDeleteDataSource(commentdeleteservice)
    val commentdeleteRepository: CommentDeleteRepository = DefaultCommentDeleteRepository(networkcommentdeleteDataSource)

    val isLogin = authRepository.isLogin
}
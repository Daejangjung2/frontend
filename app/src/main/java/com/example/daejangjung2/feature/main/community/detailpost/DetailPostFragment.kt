package com.example.daejangjung2.feature.main.community.detailpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.daejangjung2.R
import com.example.daejangjung2.data.model.response.PostCallAllResponse

class DetailPostFragment : Fragment() {

    private lateinit var post: PostCallAllResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            post = it.getParcelable(ARG_POST) ?: PostCallAllResponse()  // 전달받은 게시물 데이터
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailPostBinding.inflate(inflater, container, false)

        // 전달받은 게시물 데이터를 UI에 바인딩
        binding.postTitle.text = post.title
        binding.postLocation.text = post.location
        Glide.with(binding.postImage.context)
            .load(post.image_url ?: R.drawable.placeholder_image)
            .into(binding.postImage)

        return binding.root
    }

    companion object {
        private const val ARG_POST = "post"

        // DetailPostFragment를 생성하고 데이터 전달하는 메소드
        fun newInstance(post: PostCallAllResponse) =
            DetailPostFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_POST, post)
                }
            }
    }
}

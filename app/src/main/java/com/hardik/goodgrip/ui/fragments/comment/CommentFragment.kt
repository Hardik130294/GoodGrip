package com.hardik.goodgrip.ui.fragments.comment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hardik.goodgrip.R
import com.hardik.goodgrip.adapter.CommentAdapter
import com.hardik.goodgrip.databinding.FragmentCommentBinding
import com.hardik.goodgrip.databinding.ItmeBottomSheetCommentDetailsBinding
import com.hardik.goodgrip.models.CommentResponseItem
import com.hardik.goodgrip.ui.MainActivity
import com.hardik.goodgrip.ui.MainViewModel
import com.hardik.goodgrip.util.Constants.Companion.PARAM_POST_ID
import com.hardik.goodgrip.util.Resource

private const val ARG_PARAM_POST_ID = PARAM_POST_ID

class CommentFragment : Fragment() {
    private val TAG = CommentFragment::class.java.simpleName
    private var paramPostId: Int? = null

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel
    lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramPostId = it.getInt(ARG_PARAM_POST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_comment, container, false)
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MainActivity).viewModel
        showProgressBar()
        setUpRecyclerView()

        viewModel.getComments()
        viewModel.comments.observe(viewLifecycleOwner){
//            it.data?.iterator()?.forEach { comment -> viewModel.saveComment(comment) }
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { viewModel.saveComment(it) }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { message ->
                        Log.e(TAG, "An error occurred $message")
                        Toast.makeText(activity, "An error occurred $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
        val observer: Observer<List<CommentResponseItem>> = Observer { response ->
            commentAdapter.differ.submitList(response.toList())
            binding.recyclerview.setPadding(0, 0, 0, 0)
//            hideProgressBar()
            (activity as MainActivity).supportActionBar?.title = "Comment ${response.size}"
        }

        if (paramPostId != null) {
            viewModel.getSavedComments(paramPostId!!).observe(viewLifecycleOwner, observer)
        } else {
            viewModel.getSavedComments().observe(viewLifecycleOwner, observer)
        }

        commentAdapter.setOnItemClickListener {
            bottomSheetCommentDetails(it)
        }
    }

    private fun setUpRecyclerView() {
        commentAdapter = CommentAdapter()
        binding.recyclerview.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            addOnScrollListener(this@CommentFragment.scrollListener)
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotABeginning = firstVisibleItemPosition >= 0
//            val totalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotABeginning && !isScrolling //&& totalMoreThanVisible
            if (shouldPaginate) {
                viewModel.getUsers()
                isScrolling = false
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_POST_ID, param1)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetCommentDetails(commentResponseItem: CommentResponseItem) {
        val dialogView = layoutInflater.inflate(R.layout.itme_bottom_sheet_comment_details, null)
        val bsBinding = ItmeBottomSheetCommentDetailsBinding.bind(dialogView)

        viewModel.getCommentDetails(commentId = commentResponseItem.id)?.observe(viewLifecycleOwner){
            if (it!= null) {
                bsBinding.tvCommentName.apply {
                    ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                    marqueeRepeatLimit = -1
                    setHorizontallyScrolling(true)
                    isSingleLine = true
                    isSelected = true
                    text = "${it.commentName}"
                    setTextColor(ContextCompat.getColorStateList(context,  android.R.color.holo_green_dark )  ?: throw IllegalArgumentException("Color resource not found") )
                }

                bsBinding.tvUserId.text = "Id: ${it.userId}"
                bsBinding.tvUserCity.text = "City: ${it.userCity }"
                bsBinding.tvName.text = "Name: ${it.userName}"
                bsBinding.tvUserEmail.text = "Email: ${it.userEmail}"

                bsBinding.tvPostId.text = "Id: ${it.postId}"
                bsBinding.tvPostTitle.text = "Title: ${it.postTitle}"

                bsBinding.tvCommentId.text = "Id: ${it.commentId}"
                bsBinding.tvCommentEmail.text = "Email: ${it.commentEmail}"

            }
        }

        val dialog = BottomSheetDialog(requireContext())
        dialog.window?.attributes?.also {
//            width = resources.displayMetrics.widthPixels - (48 * resources.displayMetrics.density).toInt() // Adjust width
//            width = WindowManager.LayoutParams.WRAP_CONTENT
//            height = WindowManager.LayoutParams.WRAP_CONTENT // Adjust height as needed
//            gravity = Gravity.CENTER // Adjust the gravity as needed

            // Set margins and padding
//            horizontalMargin = 16 * resources.displayMetrics.density
//            verticalMargin = 16 * resources.displayMetrics.density

            // Set padding
//            dialogView.setPadding(
//                24 * resources.displayMetrics.density.toInt(),
//                24 * resources.displayMetrics.density.toInt(),
//                24 * resources.displayMetrics.density.toInt(),
//                24 * resources.displayMetrics.density.toInt()
//            )
        }
        dialog.setContentView(bsBinding.root)
//        dialog.window?.setBackgroundDrawableResource(android.R.drawable.screen_background_light_transparent) // Set your background drawable here
        dialog.show()
    }
}
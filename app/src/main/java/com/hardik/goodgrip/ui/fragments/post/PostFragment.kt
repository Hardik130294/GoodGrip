package com.hardik.goodgrip.ui.fragments.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.R
import com.hardik.goodgrip.adapter.PostAdapter
import com.hardik.goodgrip.databinding.FragmentPostBinding
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.PostResponseItem
import com.hardik.goodgrip.ui.MainActivity
import com.hardik.goodgrip.ui.MainViewModel
import com.hardik.goodgrip.util.Constants
import com.hardik.goodgrip.util.Constants.Companion.PARAM_POST_ID
import com.hardik.goodgrip.util.Constants.Companion.PARAM_USER_ID
import com.hardik.goodgrip.util.Resource

private const val ARG_PARAM_USER_ID = PARAM_USER_ID

class PostFragment : Fragment() {
    private val TAG = PostFragment::class.java.simpleName
    private var paramUserId: Int? = null

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel
    lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramUserId = it.getInt(ARG_PARAM_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_post, container, false)
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        showProgressBar()
        setUpRecyclerView()

        viewModel.getPosts()
        viewModel.posts.observe(viewLifecycleOwner){
//            it.data?.iterator()?.forEach { post -> viewModel.savePost(post) }
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { viewModel.savePost(it) }
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
        val observer: Observer<List<PostResponseItem>> = Observer { response ->
            postAdapter.differ.submitList(response.toList())
            binding.recyclerview.setPadding(0, 0, 0, 0)
//            hideProgressBar()
            (activity as MainActivity).supportActionBar?.title = "Post ${response.size}"
        }

        if (paramUserId != null) {
            viewModel.getSavedPosts(paramUserId!!).observe(viewLifecycleOwner, observer)
        } else {
            viewModel.getSavedPosts().observe(viewLifecycleOwner, observer)
        }

        postAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_postFragment_to_commentFragment,
                Bundle().apply { putInt(PARAM_POST_ID,it.id)})
        }
    }

    private fun setUpRecyclerView() {
        postAdapter = PostAdapter()
        binding.recyclerview.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            addOnScrollListener(this@PostFragment.scrollListener)
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
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_USER_ID, param1)
                }
            }
    }
}
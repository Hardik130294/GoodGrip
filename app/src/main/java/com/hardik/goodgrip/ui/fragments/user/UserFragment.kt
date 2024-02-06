package com.hardik.goodgrip.ui.fragments.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.R
import com.hardik.goodgrip.adapter.UserAdapter
import com.hardik.goodgrip.databinding.FragmentUserBinding
import com.hardik.goodgrip.models.UserResponseItem
import com.hardik.goodgrip.ui.MainActivity
import com.hardik.goodgrip.ui.MainViewModel
import com.hardik.goodgrip.util.Constants.Companion.PARAM_USER
import com.hardik.goodgrip.util.Resource

private const val ARG_PARAM_USER = PARAM_USER

class UserFragment : Fragment() {
    private val TAG = UserFragment::class.java.simpleName

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel
    lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        showProgressBar()
        setUpRecyclerView()
        userAdapter.setOnItemClickListener {
//            findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it))
//            findNavController().navigate(UserFragmentDirections.actionUserFragmentToUserDetailsFragment())
            findNavController().navigate(
                R.id.action_userFragment_to_userDetailsFragment,
                Bundle().apply { putSerializable(ARG_PARAM_USER, it) as? UserResponseItem })
        }
        /*
                // this is for api
                viewModel.users.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Success->{
                            hideProgressBar()
                            response.data?.let {userResponse ->
        //                        userAdapter.differ.submitList(userResponse.toList())
        //                        showRecyclerView()
        //                        val totalPage = userResponse.totalResults / QUERY_PAGE_SIZE * 2
        //                        isLastPage = viewModel.usersPage == totalPage
                                if (isLastPage) {
                                    Log.e(TAG, "onViewCreated: $isLastPage", )
                                    binding.recyclerview.setPadding(0, 0, 0, 0)
                                }
                            }
                        }
                        is Resource.Error->{
                            hideProgressBar()
                            response.message?.let { message ->
                                Log.e(TAG, "An error occurred $message")
                                Toast.makeText(activity, "An error occurred $message", Toast.LENGTH_LONG).show()
                            }
                        }
                        is Resource.Loading->{
                            showProgressBar()
                        }
                    }
                }*/

        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner){
//            it.data?.iterator()?.forEach { user -> viewModel.saveUser(user) }
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { viewModel.saveUser(it) }
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
        viewModel.getSavedUsers().observe(viewLifecycleOwner) { response ->
            userAdapter.differ.submitList(response.toList())
            binding.recyclerview.setPadding(0, 0, 0, 0)
//            hideProgressBar()
            (activity as MainActivity).supportActionBar?.title = "User ${response.size}"
        }

    }

    private fun setUpRecyclerView() {
        userAdapter = UserAdapter()
        binding.recyclerview.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            addOnScrollListener(this@UserFragment.scrollListener)
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
}
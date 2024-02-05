package com.hardik.goodgrip.ui.fragments.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.goodgrip.R
import com.hardik.goodgrip.adapter.PhotoAdapter
import com.hardik.goodgrip.adapter.PostAdapter
import com.hardik.goodgrip.databinding.FragmentPhotoBinding
import com.hardik.goodgrip.models.PhotoResponseItem
import com.hardik.goodgrip.ui.MainActivity
import com.hardik.goodgrip.ui.MainViewModel
import com.hardik.goodgrip.util.Constants.Companion.PARAM_ALBUM_ID


private const val ARG_PARAM_ALBUM_ID = PARAM_ALBUM_ID

class PhotoFragment : Fragment() {
    private val TAG = PhotoFragment::class.java.simpleName
    private var paramAlbumId: Int? = null

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel
    lateinit var photoAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramAlbumId = it.getInt(ARG_PARAM_ALBUM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_photo, container, false)
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        showProgressBar()
        setUpRecyclerView()

        val observer: Observer<List<PhotoResponseItem>> = Observer { response ->
            photoAdapter.differ.submitList(response.toList())
            binding.recyclerview.setPadding(0, 0, 0, 0)
            hideProgressBar()
        }

        if (paramAlbumId != null) {
            viewModel.getSavedPhotos(paramAlbumId!!).observe(viewLifecycleOwner, observer)
        } else {
            viewModel.getSavedPhotos().observe(viewLifecycleOwner, observer)
        }

        photoAdapter.setOnItemClickListener {
//            findNavController().navigate(
//                R.id.action_userFragment_to_userDetailsFragment,
//                Bundle().apply { putSerializable(ARG_PARAM1, it) as? UserResponseItem })
        }
    }

    private fun setUpRecyclerView() {
        photoAdapter = PhotoAdapter()
        binding.recyclerview.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            addOnScrollListener(this@PhotoFragment.scrollListener)
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
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_ALBUM_ID, param1)
                }
            }
    }
}
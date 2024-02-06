package com.hardik.goodgrip.ui.fragments.todo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.hardik.goodgrip.R
import com.hardik.goodgrip.adapter.AlbumAdapter
import com.hardik.goodgrip.adapter.TodoAdapter
import com.hardik.goodgrip.databinding.FragmentTodoBinding
import com.hardik.goodgrip.databinding.ItemAlertDialogBinding
import com.hardik.goodgrip.models.AlbumResponseItem
import com.hardik.goodgrip.models.TodoResponseItem
import com.hardik.goodgrip.ui.MainActivity
import com.hardik.goodgrip.ui.MainViewModel
import com.hardik.goodgrip.util.Constants.Companion.PARAM_USER_ID
import com.hardik.goodgrip.util.Resource

private const val ARG_PARAM_USER_ID = PARAM_USER_ID

class TodoFragment : Fragment() {
    private val TAG = TodoFragment::class.java.simpleName
    private var paramUserId: Int? = null

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel
    lateinit var todoAdapter: TodoAdapter

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
//        return inflater.inflate(R.layout.fragment_todo, container, false)
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        showProgressBar()
        setUpRecyclerView()

        viewModel.getTodos()
        viewModel.todos.observe(viewLifecycleOwner){
//            it.data?.iterator()?.forEach { todo -> viewModel.saveTodo(todo) }
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { viewModel.saveTodo(it) }
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
        val observer: Observer<List<TodoResponseItem>> = Observer { response ->
            todoAdapter.differ.submitList(response.toList())
            binding.recyclerview.setPadding(0, 0, 0, 0)
//            hideProgressBar()
            (activity as MainActivity).supportActionBar?.title = "Todo ${response.size}"
        }

        if (paramUserId != null) {
            viewModel.getSavedTodos(paramUserId!!).observe(viewLifecycleOwner, observer)
        } else {
            viewModel.getSavedTodos().observe(viewLifecycleOwner, observer)
        }

        todoAdapter.setOnItemClickListener {
            showCustomAlertDialog(it.id)
        }
    }

    private fun setUpRecyclerView() {
        todoAdapter = TodoAdapter()
        binding.recyclerview.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            addOnScrollListener(this@TodoFragment.scrollListener)
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
            TodoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_USER_ID, param1)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun showCustomAlertDialog(todoId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.item_alert_dialog, null)
        val adBinding = ItemAlertDialogBinding.bind(dialogView)

        viewModel.getTodoDetails(todoId = todoId).observe(viewLifecycleOwner){todoDetails ->
            todoDetails?.let {
                adBinding.apply {
                    tvTodoName.text = todoDetails.todoTitle
                    tvUserId.text = "UserId: ${todoDetails.userId}"
                    tvName.text = "Name: ${todoDetails.userName}"
                    tvTaskStatus.text = "Task : ${ContextCompat.getString(requireContext(), if (todoDetails.completed) R.string.completed else R.string.uncompleted)}"
                    lavTaskStatus.visibility = if (todoDetails.completed) View.VISIBLE else View.GONE
                }
            }
        }

        adBinding.mbPositive.setOnClickListener {
            viewModel.updateTodoCompleted(todoId = todoId, isCompleted = true)
        }

        adBinding.mbNegative.setOnClickListener {
            viewModel.updateTodoCompleted(todoId = todoId, isCompleted = false)
        }
//        val dialog = Dialog(requireContext())
//        dialog.setContentView(adBinding.root)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        //dialog.window?.setBackgroundDrawableResource(android.R.drawable.screen_background_light_transparent) // Set your background drawable here
//        dialog.show()


        val builder = AlertDialog.Builder(requireContext())
        builder.setView(adBinding.root)
        val alertDialog = builder.create()
//        alertDialog.window?.setBackgroundDrawableResource(android.R.drawable.screen_background_light_transparent) // Set your background drawable here
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Set your background drawable here
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}
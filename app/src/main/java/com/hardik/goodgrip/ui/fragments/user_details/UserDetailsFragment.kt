package com.hardik.goodgrip.ui.fragments.user_details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hardik.goodgrip.R
import com.hardik.goodgrip.databinding.FragmentUserDetailsBinding
import com.hardik.goodgrip.models.UserResponseItem
import com.hardik.goodgrip.util.Constants.Companion.PARAM_USER

private const val ARG_PARAM_USER = PARAM_USER

class UserDetailsFragment : Fragment() {
    private val TAG = UserDetailsFragment::class.java.simpleName

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private var param1: UserResponseItem? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM_USER, UserResponseItem::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_user_details, container, false)
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appBarLayout: AppBarLayout = requireActivity().findViewById(R.id.appBarLayout)
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)

        val scrollView: ScrollView = binding.root
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (oldScrollY > scrollY || scrollY == 0) {
                appBarLayout.setExpanded(true)//show
            } else {
                appBarLayout.setExpanded(false)//hide
            }
        }

        Glide
            .with(this)
            .load("https://100k-faces.glitch.me/random-image")
//            .centerCrop()
//            .apply(RequestOptions.circleCropTransform())
//            .apply(RequestOptions().transform(RoundedCorners(20)))
            .fitCenter()
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground))
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.sivProfile)
        param1?.let {

            binding.tvName.text = it.name
            binding.tvUsername.text = it.username
            binding.tvPhone.text = it.phone
            binding.tvEmail.text = it.email
            binding.tvWebsite.text = it.website
            binding.tvStreet.text = it.address.street
            binding.tvSuite.text = it.address.suite
            binding.tvCity.text = it.address.city
            binding.tvZipcode.text = it.address.zipcode
            binding.tvLngLat.text = it.address.geo.lat + "," + it.address.geo.lng
            binding.tvCompanyName.text = it.company.name
            binding.tvCatchPhrase.text = it.company.catchPhrase
            binding.tvBs.text = it.company.bs

        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) = UserDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM_USER, param1)
            }
        }
    }
}
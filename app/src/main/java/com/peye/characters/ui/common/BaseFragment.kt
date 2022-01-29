package com.peye.characters.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.peye.characters.R

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    protected abstract val layoutId: Int
    protected lateinit var binding: VB

    @StringRes
    protected open val actionBarTitle = R.string.app_name

    /**
     * Called on views' creation (and recreation!)
     */
    protected abstract fun bindViewModel(binding: VB)

    @CallSuper
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setActionBarTitle(getString(actionBarTitle))

        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel(binding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedElementTransition()
    }

    private fun setSharedElementTransition() {
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(R.transition.screen)
    }

    protected fun setActionBarTitle(actionBarTitle: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.let { actionBar ->
            actionBar.title = actionBarTitle
        }
    }

    protected fun <T> observe(stream: LiveData<T>, observer: (event: T) -> Unit) =
        stream.observe(viewLifecycleOwner::getLifecycle, observer)

    protected fun navigate(@IdRes resId: Int) {
        findNavController().navigate(resId)
    }

    protected fun navigate(directions: NavDirections, vararg extras: Pair<View, String>) {
        if (extras.isEmpty()) {
            findNavController().navigate(directions)
        } else {
            val sharedElements = extras.map { it.first to it.second }.toTypedArray()
            findNavController().navigate(directions, FragmentNavigatorExtras(*sharedElements))
        }
    }
}

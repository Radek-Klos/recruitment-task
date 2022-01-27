package com.peye.characters.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    protected abstract val layoutId: Int
    private lateinit var binding: VB

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

        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel(binding)
    }

    protected fun <T> observe(stream: LiveData<T>, observer: (event: T) -> Unit) =
        stream.observe(viewLifecycleOwner::getLifecycle, observer)
}

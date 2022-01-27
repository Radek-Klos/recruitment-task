package com.peye.characters.ui.charsdisplay

import com.peye.characters.R
import com.peye.characters.databinding.FragmentCharactersDisplayBinding
import com.peye.characters.ui.common.BaseFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CharactersDisplayFragment : BaseFragment<FragmentCharactersDisplayBinding>() {

    override val layoutId = R.layout.fragment_characters_display

    private val viewModel by stateViewModel<CharactersDisplayViewModel>()

    override fun bindViewModel(binding: FragmentCharactersDisplayBinding) {
        binding.viewModel = this.viewModel
    }
}

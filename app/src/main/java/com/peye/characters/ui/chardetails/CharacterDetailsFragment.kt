package com.peye.characters.ui.chardetails

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.peye.characters.R
import com.peye.characters.databinding.FragmentCharacterDetailsBinding
import com.peye.characters.ui.common.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CharacterDetailsFragment : BaseFragment<FragmentCharacterDetailsBinding>() {

    override val layoutId = R.layout.fragment_character_details

    private val args: CharacterDetailsFragmentArgs by navArgs()

    private val viewModel by viewModel<CharacterDetailsViewModel> { parametersOf(args.character) }

    override fun bindViewModel(binding: FragmentCharacterDetailsBinding) {
        binding.viewModel = viewModel

        observe(viewModel.characterName) { characterName ->
            setActionBarTitle(characterName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }
}

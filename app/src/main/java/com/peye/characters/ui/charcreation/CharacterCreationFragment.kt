package com.peye.characters.ui.charcreation

import com.peye.characters.R
import com.peye.characters.databinding.FragmentCharacterCreationBinding
import com.peye.characters.ui.common.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.peye.characters.ui.charcreation.CharacterCreationViewModel.Event

class CharacterCreationFragment : BaseFragment<FragmentCharacterCreationBinding>() {

    override val layoutId = R.layout.fragment_character_creation

    override val actionBarTitle = R.string.character_creation_screen_title

    private val viewModel by viewModel<CharacterCreationViewModel>()

    override fun bindViewModel(binding: FragmentCharacterCreationBinding) {
        binding.viewModel = viewModel

        observe(viewModel.eventStream) { event ->
            if (event is Event.NavigateBackSaveCompleted) {
                navigate(R.id.toCharacterDisplay)
            }
        }
    }
}

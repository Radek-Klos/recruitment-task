package com.peye.characters.ui.charsdisplay

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peye.characters.R
import com.peye.characters.databinding.FragmentCharactersDisplayBinding
import com.peye.characters.domain.entity.Character
import com.peye.characters.ui.common.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.peye.characters.ui.charsdisplay.CharactersDisplayViewModel.Event

class CharactersDisplayFragment : BaseFragment<FragmentCharactersDisplayBinding>() {

    override val layoutId = R.layout.fragment_characters_display

    private val viewModel by viewModel<CharactersDisplayViewModel>()

    override fun bindViewModel(binding: FragmentCharactersDisplayBinding) {
        binding.viewModel = this.viewModel

        observe(viewModel.eventStream) { event ->
            when (event) {
                null -> return@observe
                is Event.NavigateToCharDetails ->
                    navigateToCharacterDetails(event.character, event.itsPosition)
                is Event.NavigateToCharCreation ->
                    navigate(R.id.toCharacterCreation)
            }
            viewModel.consumeIssuedEvent()
        }
    }

    private fun navigateToCharacterDetails(character: Character, viewPositionOnList: Int) {
        val selectedCharacterViewHolder =
            binding.charactersList.findViewHolderForAdapterPosition(viewPositionOnList) as RecyclerView.ViewHolder
        val portraitView = selectedCharacterViewHolder.itemView.findViewById<View>(R.id.portrait)
        val viewTransitionName = character.portraitImageUrl.toString()
        val navigateDirections = CharactersDisplayFragmentDirections.toCharacterDetails(character)

        navigate(navigateDirections, portraitView to viewTransitionName)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCharacters()
    }
}

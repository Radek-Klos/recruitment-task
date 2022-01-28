package com.peye.characters.ui.charsdisplay

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.peye.characters.BR
import com.peye.characters.R
import com.peye.characters.domain.entity.Character
import com.peye.characters.domain.interactor.GetCharactersUseCase
import com.peye.characters.ui.common.NonNullMutableLiveData
import com.peye.characters.ui.common.SingleLaunchStrategy.*
import com.peye.characters.ui.common.safeSingleLaunch
import com.peye.characters.ui.common.singleLaunch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import me.tatarka.bindingcollectionadapter2.ItemBinding
import timber.log.Timber

class CharactersDisplayViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle,  // TODO: needed?
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val loading = getCharactersUseCase.isLoading.asLiveData(Dispatchers.Main)

    val characters = NonNullMutableLiveData(emptyList<Character>())
    val charactersBinding = ItemBinding.of<Character>(BR.item, R.layout.item_character)
        .bindExtra(BR.viewModel, this)

    val onCurrentlyLoadedCharsViewed: (Int) -> Unit = ::onCurrentlyLoadedCharsViewed

    init {
        loadCharacters()
    }

    private fun loadCharacters() = safeSingleLaunch(ioDispatcher, REPLACE, ::onLoadingFailure) {
        getCharactersUseCase.startCharactersFetching().collect {
            characters.postValue(it.toList())
        }
    }

    private fun onCurrentlyLoadedCharsViewed(renderedItemsCount: Int) = singleLaunch(ioDispatcher) {
        if (shouldNotStartNewLoading(renderedItemsCount)) {
            return@singleLaunch
        }
        getCharactersUseCase.loadMoreCharactersIfPossible()
    }

    private fun shouldNotStartNewLoading(displayedItemsCount: Int): Boolean {
        val alreadyLoadedNextResults = characters.value.size > displayedItemsCount
        val isLoadingInProgress = loading.value == true
        return isLoadingInProgress || alreadyLoadedNextResults
    }

    private fun onLoadingFailure(throwable: Throwable) {
        Timber.e("onLoadingFailure $throwable")
    }
}

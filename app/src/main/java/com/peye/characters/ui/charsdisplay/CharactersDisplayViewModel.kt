package com.peye.characters.ui.charsdisplay

import androidx.lifecycle.MutableLiveData
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
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val loading = getCharactersUseCase.isLoading.asLiveData(Dispatchers.Main)

    val characters = NonNullMutableLiveData(emptyList<Character>())
    val charactersBinding = ItemBinding.of<Character>(BR.item, R.layout.item_character)
        .bindExtra(BR.viewModel, this)

    val onCurrentlyLoadedCharsViewed: (Int) -> Unit = ::onCurrentlyLoadedCharsViewed

    val eventStream = MutableLiveData<Event?>()

    fun loadCharacters() = safeSingleLaunch(ioDispatcher, ::onLoadingFailure, REPLACE) {
        if (loading.value == true || characters.value.isNotEmpty()) return@safeSingleLaunch
        getCharactersUseCase.startCharactersFetching().collect {
            characters.postValue(it.toList())
        }
    }

    private fun onCurrentlyLoadedCharsViewed(renderedItemsCount: Int) = safeSingleLaunch(ioDispatcher, ::onLoadingFailure) {
        if (shouldNotStartNewLoading(renderedItemsCount)) {
            return@safeSingleLaunch
        }
        getCharactersUseCase.loadMoreCharactersIfPossible()
    }

    private fun shouldNotStartNewLoading(renderedItemsCount: Int): Boolean {
        val alreadyLoadedNextResults = characters.value.size > renderedItemsCount
        val isLoadingInProgress = loading.value == true
        return isLoadingInProgress || alreadyLoadedNextResults
    }

    private fun onLoadingFailure(throwable: Throwable) {
        Timber.d("onLoadingFailure $throwable")
    }

    fun onCharacterClicked(clickedCharacter: Character) = singleLaunch {
        val clickedCharacterId = characters.value.indexOf(clickedCharacter)
        val navigationEvent = Event.NavigateToCharDetails(clickedCharacter, clickedCharacterId)
        eventStream.postValue(navigationEvent)
    }

    fun consumeIssuedEvent() {
        // In practice there are many ways of handling the fact LiveData is really designed just to
        // hold the state, not events. I personally found myself best suited with subclassing
        // LiveData doing everything under its hood, though it's much out of the scope of this task.
        eventStream.value = null
    }

    fun onAddNewCharacterClicked() = singleLaunch {
        val navigationEvent = Event.NavigateToCharCreation
        eventStream.postValue(navigationEvent)
    }

    sealed class Event {
        data class NavigateToCharDetails(val character: Character, val itsPosition: Int) : Event()
        object NavigateToCharCreation : Event()
    }
}

package com.peye.characters.ui.charcreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peye.characters.domain.interactor.AddCharacterUseCase
import com.peye.characters.ui.common.NonNullMutableLiveData
import com.peye.characters.ui.common.safeSingleLaunch
import com.peye.characters.ui.common.singleLaunch
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber

class CharacterCreationViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val addCharacterUseCase: AddCharacterUseCase
) : ViewModel() {

    val nameInput = MutableLiveData<String>()
    val statusInput = MutableLiveData<String>()
    val locationInput = MutableLiveData<String>()
    val originInput = MutableLiveData<String>()

    val nameInputErroneous = NonNullMutableLiveData(false)
    val statusInputErroneous = NonNullMutableLiveData(false)
    val locationInputErroneous = NonNullMutableLiveData(false)
    val originInputErroneous = NonNullMutableLiveData(false)

    val saving = NonNullMutableLiveData(false)

    val eventStream = MutableLiveData<Event?>()

    fun onSaveAndExitClicked() = singleLaunch {
        var isGoodToGo = checkInputAndSetResult(nameInput, nameInputErroneous)
        isGoodToGo = isGoodToGo and checkInputAndSetResult(statusInput, statusInputErroneous)
        isGoodToGo = isGoodToGo and checkInputAndSetResult(locationInput, locationInputErroneous)
        isGoodToGo = isGoodToGo and checkInputAndSetResult(originInput, originInputErroneous)

        if (isGoodToGo) {
            saving.postValue(true)
            eventStream.postValue(Event.NavigateBackSaveCompleted)
        }
    }

    private fun checkInputAndSetResult(
        input: LiveData<String>,
        isInvalidResult: MutableLiveData<Boolean>
    ): Boolean {
        val isInputValid = input.value.isValidInput()
        isInvalidResult.postValue(isInputValid.not())
        return isInputValid
    }

    private suspend fun performCharSaving() = safeSingleLaunch(ioDispatcher, ::onSavingFailure) {
        // TODO: implement (use AddCharacterUseCase)
    }

    private fun onSavingFailure(throwable: Throwable) {
        Timber.d("onSavingFailure $throwable")
    }

    private fun String?.isValidInput() =
        addCharacterUseCase.isValidInput(this)

    sealed class Event {
        object NavigateBackSaveCompleted : Event()
    }
}

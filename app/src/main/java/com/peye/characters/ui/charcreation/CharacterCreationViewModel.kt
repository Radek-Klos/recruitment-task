package com.peye.characters.ui.charcreation

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peye.characters.domain.interactor.AddCharacterUseCase
import com.peye.characters.ui.common.NonNullMutableLiveData
import com.peye.characters.ui.common.safeSingleLaunch
import com.peye.characters.ui.common.singleLaunch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import timber.log.Timber

class CharacterCreationViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val addCharacterUseCase: AddCharacterUseCase
) : ViewModel() {

    val nameInput = NonNullMutableLiveData("")
    val statusInput = NonNullMutableLiveData("")
    val locationInput = NonNullMutableLiveData("")
    val originInput = NonNullMutableLiveData("")

    val nameInputErroneous = NonNullMutableLiveData(false)
    val statusInputErroneous = NonNullMutableLiveData(false)
    val locationInputErroneous = NonNullMutableLiveData(false)
    val originInputErroneous = NonNullMutableLiveData(false)

    val saving = NonNullMutableLiveData(false)

    val eventStream = MutableLiveData<Event?>()

    fun onSaveAndExitClicked() = singleLaunch(Dispatchers.Main) {
        var isGoodToGo = checkInputAndSetResult(nameInput, nameInputErroneous)
        isGoodToGo = isGoodToGo and checkInputAndSetResult(statusInput, statusInputErroneous)
        isGoodToGo = isGoodToGo and checkInputAndSetResult(locationInput, locationInputErroneous)
        isGoodToGo = isGoodToGo and checkInputAndSetResult(originInput, originInputErroneous)

        if (isGoodToGo) {
            performCharSaving()
        }
    }

    @MainThread
    private fun checkInputAndSetResult(
        inputLiveData: LiveData<String>,
        isInvalidResult: MutableLiveData<Boolean>
    ): Boolean {
        val isInputValid = inputLiveData.value.isValidInput()
        isInvalidResult.value = isInputValid.not()
        return isInputValid
    }

    private suspend fun performCharSaving() = safeSingleLaunch(ioDispatcher, ::onSavingFailure) {
        saving.postValue(true)
        delay(1_300) // Pretend we're doing some extensive work
        addCharacterUseCase.addNewCharacter(
            itsName = nameInput.value,
            itsStatus = statusInput.value,
            itsLocation = locationInput.value,
            itsOrigin = originInput.value
        )
        saving.postValue(false)

        eventStream.postValue(Event.NavigateBackSaveCompleted)
    }

    private fun onSavingFailure(throwable: Throwable) {
        Timber.d(throwable, "onSavingFailure")
    }

    private fun String?.isValidInput() =
        addCharacterUseCase.isValidInput(this)

    sealed class Event {
        object NavigateBackSaveCompleted : Event()
    }
}

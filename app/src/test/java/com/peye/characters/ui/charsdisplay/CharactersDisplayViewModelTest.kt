package com.peye.characters.ui.charsdisplay

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.peye.characters.MainCoroutineRule
import com.peye.characters.domain.CharacterRepository
import com.peye.characters.domain.entity.Character
import com.peye.characters.domain.interactor.GetCharactersUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharactersDisplayViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val repositoryMock = mockk<CharacterRepository>(relaxed = true).apply {
        every { isRequestInProgress } returns flow {
            emit(true)
        }
    }

    @SpyK
    var getCharactersUseCaseMock = GetCharactersUseCase(repositoryMock)

    @RelaxedMockK
    lateinit var loadingObserver: Observer<Boolean>

    @RelaxedMockK
    lateinit var eventStreamObserver: Observer<CharactersDisplayViewModel.Event?>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `doesn't perform another load when some is already progressing`() = runBlockingTest {
        // given
        val testSubject = CharactersDisplayViewModel(testDispatcher, getCharactersUseCaseMock)
        testSubject.loading.observeForever(loadingObserver)

        // when
        testSubject.loadCharacters()

        // then
        coVerify(exactly = 0) { getCharactersUseCaseMock.startCharactersFetching() }
    }

    @Test
    fun `doesn't order to go into details of a portrait-less character`() = runBlockingTest {
        // given
        val portraitLessCharacter = CHARACTER_WITHOUT_PORTRAIT
        val testSubject = CharactersDisplayViewModel(testDispatcher, getCharactersUseCaseMock)
        testSubject.eventStream.observeForever(eventStreamObserver)
        clearMocks(eventStreamObserver)    // reset calls' count

        // when
        testSubject.onCharacterClicked(portraitLessCharacter)

        // then
        verify(exactly = 0) { eventStreamObserver.onChanged(any()) }
    }

    companion object {
        private val CHARACTER_WITHOUT_PORTRAIT = Character("a", "b", "c", "d", null)
    }
}

package ru.magnum.mediadiary.data.presentation.details

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.SuspendRule
import ru.magnum.mediadiary.data.repository.FakeMediaRepository
import ru.magnum.mediadiary.domain.model.AppError
import ru.magnum.mediadiary.domain.model.AppException
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.presentation.details.MediaDetailUiState
import ru.magnum.mediadiary.presentation.details.MediaDetailViewModel


@OptIn(ExperimentalCoroutinesApi::class)
class MediaDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = SuspendRule()

    private lateinit var repository: FakeMediaRepository
    private lateinit var viewModel: MediaDetailViewModel

    @Before
    fun setup(){
        repository = FakeMediaRepository()
        viewModel = MediaDetailViewModel(repository)
    }

    @Test
    fun `loadMediaItem sets success state when repository returns item`() = runTest {
        val item = testMediaDetails()
        repository.mediaDetails = item

        viewModel.loadMediaItem(id = item.id)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state is MediaDetailUiState.Success)
        assertEquals(item, (state as MediaDetailUiState.Success).item)
    }

    @Test
    fun `loadMediaItem sets error state when repository throws exception`() = runTest {
        repository.exception = AppException(AppError.Network)

        viewModel.loadMediaItem(id = 1)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state is MediaDetailUiState.Error)
        assertEquals(R.string.error_internet, (state as MediaDetailUiState.Error).message)
    }

    @Test
    fun `updateStatus saves item with new status`() = runTest {
        val item = testMediaDetails(watchStatus = WatchStatus.WANT_TO_WATCH)
        repository.mediaDetails = item

        viewModel.loadMediaItem(item.id)
        advanceUntilIdle()

        viewModel.updateStatus(WatchStatus.WATCHING)
        advanceUntilIdle()

        val savedItem = repository.savedItem

        assertEquals(WatchStatus.WATCHING, savedItem?.watchStatus)
    }

    @Test
    fun `updateStatus deletes item when same status clicked`() = runTest {
        val item = testMediaDetails(watchStatus = WatchStatus.WATCHING)
        repository.mediaDetails = item

        viewModel.loadMediaItem(item.id)
        advanceUntilIdle()

        viewModel.updateStatus(WatchStatus.WATCHING)
        advanceUntilIdle()

        assertEquals(item, repository.deletedItem)

        val state = viewModel.uiState.value
        assertTrue(state is MediaDetailUiState.Success)

        val updatedItem = (state as MediaDetailUiState.Success).item
        assertNull(updatedItem.watchStatus)
        assertNull(updatedItem.userRating)
        assertNull(updatedItem.watchDate)
        assertNull(updatedItem.userNote)
    }

    @Test
    fun `updateRating saves updated user rating`() = runTest {
        val item = testMediaDetails(watchStatus = WatchStatus.WATCHED)
        repository.mediaDetails = item

        viewModel.loadMediaItem(item.id)
        advanceUntilIdle()

        viewModel.updateRating(9)
        advanceUntilIdle()

        assertEquals(9, repository.savedItem?.userRating)
    }

    @Test
    fun `updateDate saves updated watch date`() = runTest {
        val item = testMediaDetails(watchStatus = WatchStatus.WATCHED)
        repository.mediaDetails = item

        viewModel.loadMediaItem(item.id)
        advanceUntilIdle()

        viewModel.updateDate(123456L)
        advanceUntilIdle()

        assertEquals(123456L, repository.savedItem?.watchDate)
    }

    @Test
    fun `updateNote saves updated note`() = runTest {
        val item = testMediaDetails(watchStatus = WatchStatus.WATCHED)
        repository.mediaDetails = item

        viewModel.loadMediaItem(item.id)
        advanceUntilIdle()

        viewModel.updateNote("Отличный фильм")
        advanceUntilIdle()

        assertEquals("Отличный фильм", repository.savedItem?.userNote)
    }

    private fun testMediaDetails(
        watchStatus: WatchStatus? = null
    ): MediaDetails {
        return MediaDetails(
            id = 1,
            title = "Test movie",
            year = 2026,
            description = "Description",
            type = MediaType.MOVIE,
            rating = 8.5,
            poster = "poster",
            genres = listOf("триллер"),
            ageRating = "16+",
            director = "Director",
            actors = "Actor",
            countries = "США",
            length = "120",
            watchStatus = watchStatus,
            userRating = null,
            watchDate = null,
            addedAt = 0L,
            userNote = null
        )
    }
}
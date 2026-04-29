package ru.magnum.mediadiary.data.presentation.details

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.SuspendRule
import ru.magnum.mediadiary.data.FakeMediaRepository
import ru.magnum.mediadiary.domain.model.AppError
import ru.magnum.mediadiary.domain.model.AppException
import ru.magnum.mediadiary.domain.model.MediaDetails
import ru.magnum.mediadiary.domain.model.MediaType
import ru.magnum.mediadiary.domain.model.WatchStatus
import ru.magnum.mediadiary.presentation.collection.CollectionViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModelTest {

    @get:Rule
    val mainDispatcherRule = SuspendRule()

    private lateinit var repository: FakeMediaRepository
    private lateinit var viewModel: CollectionViewModel

    @Before
    fun setup(){
        repository = FakeMediaRepository()
        viewModel = CollectionViewModel(repository)
    }


    @Test
    fun `first load shows want to watch items`() = runTest {
        val item = testMediaDetails(
            id = 1,
            watchStatus = WatchStatus.WANT_TO_WATCH
        )

        repository.wantToWatchItems.value = listOf(item)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(WatchStatus.WANT_TO_WATCH, state.selectedTab)
        assertEquals(listOf(item), state.items)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun `changeTab loads items for selected status`() = runTest {
        val watchingItem = testMediaDetails(
            id = 2,
            watchStatus = WatchStatus.WATCHING
        )

        repository.watchingItems.value = listOf(watchingItem)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.changeTab(WatchStatus.WATCHING)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(WatchStatus.WATCHING, state.selectedTab)
        assertEquals(listOf(watchingItem), state.items)
        assertFalse(state.isLoading)
    }

    @Test
    fun `changeTab clears selected items`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.toggleDeletion(1)
        viewModel.requestDeleteSelected()

        assertTrue(viewModel.uiState.value.selectedItems.contains(1))
        assertTrue(viewModel.uiState.value.isDeleteDialogVisible)

        viewModel.changeTab(WatchStatus.WATCHED)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state.selectedItems.isEmpty())
        assertFalse(state.isDeleteDialogVisible)
        assertEquals(WatchStatus.WATCHED, state.selectedTab)
    }

    @Test
    fun `toggleDeletion selects and unselects item`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.toggleDeletion(10)

        assertTrue(viewModel.uiState.value.selectedItems.contains(10))

        viewModel.toggleDeletion(10)

        assertTrue(viewModel.uiState.value.selectedItems.isEmpty())
    }

    @Test
    fun `requestDeleteSelected does nothing when no selected items`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.requestDeleteSelected()

        assertFalse(viewModel.uiState.value.isDeleteDialogVisible)
    }

    @Test
    fun `confirmDeleteSelected deletes selected ids`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.toggleDeletion(1)
        viewModel.toggleDeletion(2)
        viewModel.requestDeleteSelected()

        viewModel.confirmDeleteSelected()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(listOf(1, 2), repository.deletedIds)
        assertTrue(state.selectedItems.isEmpty())
        assertFalse(state.isDeleteDialogVisible)
    }

    @Test
    fun `confirmDeleteSelected sets error when delete fails`() = runTest {
        repository.deleteItemsException = AppException(AppError.Network)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.toggleDeletion(1)
        viewModel.requestDeleteSelected()

        viewModel.confirmDeleteSelected()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(R.string.error_internet, state.errorMessage)
        assertFalse(state.isDeleteDialogVisible)
    }

    @Test
    fun `loadItems sets error when repository fails`() = runTest {
        repository.exception = AppException(AppError.Network)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(R.string.error_internet, state.errorMessage)
    }

    private fun createViewModel(): CollectionViewModel {
        return CollectionViewModel(repository)
    }

    private fun testMediaDetails(
        id: Int,
        watchStatus: WatchStatus
    ): MediaDetails {
        return MediaDetails(
            id = id,
            title = "Movie $id",
            year = 2024,
            description = "Description",
            type = MediaType.MOVIE,
            rating = 8.0,
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
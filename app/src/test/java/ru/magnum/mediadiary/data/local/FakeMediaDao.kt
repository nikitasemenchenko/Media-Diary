package ru.magnum.mediadiary.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeMediaDao : MediaDao {

    private val items = LinkedHashMap<Int, MediaItem>()
    private val itemsFlow = MutableStateFlow<List<MediaItem>>(emptyList())

    var insertIgnoreResult: Long? = null

    fun setItems(newItems: List<MediaItem>) {
        items.clear()
        newItems.forEach { item ->
            items[item.id] = item
        }
        emit()
    }

    fun getStoredItem(id: Int): MediaItem? {
        return items[id]
    }

    override suspend fun insert(item: MediaItem) {
        items[item.id] = item
        emit()
    }

    override suspend fun insertIgnore(item: MediaItem): Long {
        insertIgnoreResult?.let { return it }

        if (items.containsKey(item.id)) {
            return -1L
        }

        items[item.id] = item
        emit()
        return item.id.toLong()
    }

    override suspend fun update(item: MediaItem) {
        items[item.id] = item
        emit()
    }

    override suspend fun delete(item: MediaItem) {
        items.remove(item.id)
        emit()
    }

    override suspend fun findById(id: Int): MediaItem? {
        return items[id]
    }

    override fun getAllItems(): Flow<List<MediaItem>> {
        return itemsFlow
    }

    override fun getItemsByStatus(status: MovieStatus): Flow<List<MediaItem>> {
        return itemsFlow.map { list ->
            list.filter { item -> item.watchStatus == status }
        }
    }

    override fun getCollectionStats(): Flow<MediaStats> {
        return itemsFlow.map { list ->
            MediaStats(
                total = list.size,
                watched = list.count { it.watchStatus == MovieStatus.WATCHED },
                watching = list.count { it.watchStatus == MovieStatus.WATCHING },
                wantToWatch = list.count { it.watchStatus == MovieStatus.WANT_TO_WATCH }
            )
        }
    }

    override fun getTypes(): Flow<List<TypeCount>> {
        return itemsFlow.map { list ->
            list
                .mapNotNull { item -> item.type?.name }
                .groupingBy { it }
                .eachCount()
                .map { (type, count) ->
                    TypeCount(type = type, count = count)
                }
        }
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        ids.forEach { id ->
            items.remove(id)
        }
        emit()
    }

    private fun emit() {
        itemsFlow.value = items.values.toList()
    }
}
package ru.magnum.mediadiary.data.remote

import ru.magnum.mediadiary.data.remote.dto.KinopoiskSearchDetailedResponse
import ru.magnum.mediadiary.data.remote.dto.KinopoiskSearchResponse

class FakeKinopoiskApi : KinopoiskApi {

    var exception: Throwable? = null

    var detailResponse: KinopoiskSearchDetailedResponse? = null

    var searchResponse: KinopoiskSearchResponse = KinopoiskSearchResponse(
        docs = emptyList()
    )

    var trendingMoviesResponse: KinopoiskSearchResponse = KinopoiskSearchResponse(
        docs = emptyList()
    )

    var trendingMoviesCallCount: Int = 0

    override suspend fun multiSearch(
        page: Int,
        limit: Int,
        query: String?
    ): KinopoiskSearchResponse {
        exception?.let { throw it }
        return searchResponse
    }

    override suspend fun getById(id: Int): KinopoiskSearchDetailedResponse {
        exception?.let { throw it }
        return detailResponse ?: error("no detailResponse")
    }

    override suspend fun getTrendingMovies(
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        type: String
    ): KinopoiskSearchResponse {
        exception?.let { throw it }
        trendingMoviesCallCount++
        return trendingMoviesResponse
    }

    override suspend fun getTrendingSeries(
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        type: String
    ): KinopoiskSearchResponse {
        exception?.let { throw it }
        return KinopoiskSearchResponse(emptyList())
    }

    override suspend fun getTrendingAnime(
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        type: String
    ): KinopoiskSearchResponse {
        exception?.let { throw it }
        return KinopoiskSearchResponse(emptyList())
    }

    override suspend fun getTrendingCartoons(
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        type: String
    ): KinopoiskSearchResponse {
        exception?.let { throw it }
        return KinopoiskSearchResponse(emptyList())
    }

    override suspend fun getTrendingAnimatedSeries(
        page: Int,
        limit: Int,
        sortField: String,
        sortType: String,
        type: String
    ): KinopoiskSearchResponse {
        exception?.let { throw it }
        return KinopoiskSearchResponse(emptyList())
    }
}
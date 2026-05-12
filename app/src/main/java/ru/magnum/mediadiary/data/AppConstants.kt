import kotlin.math.round

object AppConstants {
    const val BASE_URL = "https://api.poiskkino.dev/v1.4/"
    const val DB_NAME = "mediadiary_database"
    const val TIMEOUT_MILLIS = 5_000L
    const val SEARCH_DELAY = 600L


    object ApiConstants {
        const val MOVIE = "movie"
        const val TV_SERIES = "tv-series"
        const val CARTOON = "cartoon"
        const val ANIME = "anime"
        const val ANIMATED_SERIES = "animated-series"
        const val DIRECTOR = "director"
        const val ACTOR = "actor"
    }

    object Limits {
        const val GENRES_LIMIT = 3
        const val ACTORS_LIMIT = 6
    }
}

fun Double.roundToOneSign(): Double = round(this * 10) / 10
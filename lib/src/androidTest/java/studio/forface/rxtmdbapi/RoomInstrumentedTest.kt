package studio.forface.rxtmdbapi

import android.util.Log
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.junit.After
import org.junit.Test
import studio.forface.rxtmdbapi.room.TmdbDatabase
import studio.forface.rxtmdbapi.room.updateOrInsertAsync
import studio.forface.rxtmdbapi.tmdb.Session
import studio.forface.rxtmdbapi.tmdb.TmdbApi

/**
 * @author 4face Studio (Davide Giuseppe Farella).
 */

private const val TAG = "RoomInstrumentedTest"

class RoomInstrumentedTest {

    private val tmdbApi = TmdbApi( TMDB_API_KEY, USER_SESSION_ID )
    private val localDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getTargetContext(), TmdbDatabase::class.java ).build()

    private val moviesDao   by lazy { localDb.moviesDao }
    private val peopleDao   by lazy { localDb.peopleDao }
    private val sessionsDao by lazy { localDb.sessionsDao }
    private val tvShowsDao  by lazy { localDb.tvShowsDao }

    @After
    fun closeDb() {
        localDb.close()
    }

    // Movies.
    @Test fun movies() {
        val movie = tmdbApi.movies.getDetails(299536 )
                .blockingGet()

        moviesDao.updateOrInsertAsync( movie )
                .blockingAwait()

        val byId = moviesDao.get( 299536 )
                .blockingGet()

        println( byId )
    }

    // People.
    @Test fun people() {
        var id = 0
        val person = tmdbApi.search.searchPeople("dicaprio")
                .map { it.results.first() }
                .doAfterSuccess { id = it.id }
                .blockingGet()

        peopleDao.updateOrInsertAsync( person )
                .blockingAwait()

        val byId = peopleDao.get( id )
                .blockingGet()

        println( byId )
    }

    // TvShows.
    @Test fun thShows() {
        val tvShow = tmdbApi.tvShows.getDetails( 31911 )
                .blockingGet()

        tvShowsDao.updateOrInsertAsync( tvShow )
                .blockingAwait()

        val byId = tvShowsDao.get( 31911 )
                .blockingGet()

        println( byId )
    }

    // Sessions.
    @Test fun writeAndReadSession() {
        val session = tmdbApi.auth.createGuessSession()
                .blockingGet()

        with(session) {
            if (success) sessionsDao.insert( this )
        }

        println( sessionsDao.get().blockingGet() )
    }

    @Test fun logWithSession() {
        val mockSession = Session(USER_SESSION_ID, false)
        sessionsDao.insert( mockSession )

        val api = TmdbApi( TMDB_API_KEY, sessionsDao.get().blockingGet().toString() )

        val page = api.account.getRatedMovies()
                .blockingGet()

        println( page.results.first() )
    }


    private fun println( any: Any ) {
        Log.d( TAG, any.toString() )
    }

}

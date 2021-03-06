package studio.forface.rxtmdbapi

import android.content.Context
import android.util.Log
import androidx.test.InstrumentationRegistry
import org.junit.Test
import studio.forface.rxtmdbapi.tmdb.TmdbApi
import studio.forface.rxtmdbapi.utils.Sorting


/**
 * @author 4face Studio (Davide Giuseppe Farella).
 */

private const val TAG = "TmdbInstrumentedTest"

class TmdbInstrumentedTest {

    private val context: Context = InstrumentationRegistry.getTargetContext()
    private val tmdbApi by lazy {
        TmdbApi( TMDB_API_KEY, TMDB_API_ACCESS_TOKEN )
    }
    private val tmdbAuth            get() = tmdbApi.auth
    private val tmdbAuthV4          get() = tmdbApi.authV4

    @Test fun auth() {
        val session = tmdbAuth.createUserSessionWithUserAuthentication( context )
                .blockingGet()

        Log.d( TAG, session.toString() )
    }

    @Test fun authV4() {
        //tmdbAuthV4.preloadToken().blockingAwait()
        val token = tmdbAuthV4.authenticate( context )
                .blockingGet()

        val result = tmdbApi.accountV4.getFavoriteMovies(
                sortBy = Sorting.CreationDate.ASCENDING
        ).blockingGet()

        Log.d( TAG, "token: ${token.fullString()}" )
        Log.d( TAG, result.toString() )
    }

}
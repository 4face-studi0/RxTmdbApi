@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package studio.forface.rxtmdbapi.room

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import studio.forface.rxtmdbapi.tmdb.Session

/**
 * @author 4face Studio (Davide Giuseppe Farella).
 */

@Dao
interface SessionsDao: SingleEntityDao<Session> {

    @Query("SELECT * from ${Session.TABLE_NAME} LIMIT 1")
    override fun get(): Single<Session>

    @Query("SELECT * from ${Session.TABLE_NAME} LIMIT 1")
    override fun observe(): Flowable<Session>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert( session: Session)

    @Update
    override fun update( session: Session)

    @Delete
    override fun delete( session: Session)

}
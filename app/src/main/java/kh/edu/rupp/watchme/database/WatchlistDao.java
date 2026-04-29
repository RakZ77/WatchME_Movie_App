package kh.edu.rupp.watchme.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import kh.edu.rupp.watchme.models.Watchlist;

@Dao
public interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Watchlist watchList);

    @Delete
    void delete(Watchlist movie);
    @Query("SELECT * FROM watch_list WHERE userId = :userId")
    LiveData<List<Watchlist>> getAll(String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM watch_list WHERE id = :movieId AND userId = :userId)")
    LiveData<Boolean> isInWatchlist(int movieId, String userId);

}

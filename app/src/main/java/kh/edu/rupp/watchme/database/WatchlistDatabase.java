package kh.edu.rupp.watchme.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kh.edu.rupp.watchme.models.Watchlist;

@Database(entities = {Watchlist.class}, version = 4, exportSchema = false)
public abstract class WatchlistDatabase extends RoomDatabase {
    public abstract WatchlistDao watchlistDao();
    public static WatchlistDatabase INSTANCE;

    public static synchronized WatchlistDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (WatchlistDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WatchlistDatabase.class, "watch_list_db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

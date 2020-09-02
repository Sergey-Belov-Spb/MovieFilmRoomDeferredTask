package com.example.moviefilmroomdeferredtask.data.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class},version = 2,exportSchema = false)
abstract class MovieRoomDatabase extends RoomDatabase {
    abstract MovieDao movieDao();

    private static volatile MovieRoomDatabase INSTANCE;
    private static final int NUMBER_OF_TREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool (NUMBER_OF_TREADS);

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Поскольку мы не изменяли таблицу, здесь больше ничего не нужно делать.
        }
    };

    static MovieRoomDatabase getDatabase (final Context context){
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_database")
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(sMovieDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    private static final Callback sMovieDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                MovieDao dao = INSTANCE.movieDao();
                dao.deleteAll();

                /*Movie movie = new Movie();
                movie.id = 1;
                movie.title = "Hello 1";
                movie.picUrl = "None 1";
                dao.insert(movie);
                movie = new Movie();
                movie.id = 2;
                movie.title = "Hello 2";
                movie.picUrl = "None 2";
                dao.insert(movie);*/
            });
        }
    };
}

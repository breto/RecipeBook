package com.breto.recipebook.persistence.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.breto.recipebook.AppExecutors;
import com.breto.recipebook.persistence.dao.RecipeDao;
import com.breto.recipebook.persistence.entity.RecipeEntity;

@Database(entities = {RecipeEntity.class}, version = 1)
public abstract class RecipeBookDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    private static volatile RecipeBookDatabase INSTANCE;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static RecipeBookDatabase getDatabase(final Context context, final AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (RecipeBookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeBookDatabase.class, "recipebook_database")
                            .addCallback(recipebookDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }


    private static RecipeBookDatabase.Callback recipebookDatabaseCallback =

            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RecipeDao mDao;

        PopulateDbAsync(RecipeBookDatabase db) {
            mDao = db.recipeDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            RecipeEntity recipeEntity = new RecipeEntity("Hello");
            recipeEntity.setUrl("url one");
            mDao.insert(recipeEntity);
            recipeEntity = new RecipeEntity("World");
            recipeEntity.setUrl("url two");
            mDao.insert(recipeEntity);
            return null;
        }
    }

}

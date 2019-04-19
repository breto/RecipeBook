package com.breto.recipebook.persistence.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.breto.recipebook.persistence.dao.RecipeDao;
import com.breto.recipebook.persistence.database.RecipeBookDatabase;
import com.breto.recipebook.persistence.entity.RecipeEntity;

import java.util.List;

public class RecipeRepository {
    //    A Repository manages query threads and allows you to use multiple backends.
//    In the most common example, the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.
    private RecipeDao recipeDao;
    private LiveData<List<RecipeEntity>> allRecipes;
    private LiveData<RecipeEntity> recipe;

    public RecipeRepository(Application application) {
        RecipeBookDatabase db = RecipeBookDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    public LiveData<List<RecipeEntity>> getAllRecipes() {
        return allRecipes;
    }

    public void insert (RecipeEntity recipeEntity) {
        new insertAsyncTask(recipeDao).execute(recipeEntity);
    }

    public LiveData<RecipeEntity> getRecipe(int id){
        return recipeDao.loadRecipe(id);
    }

    private static class insertAsyncTask extends AsyncTask<RecipeEntity, Void, Void> {

        private RecipeDao mAsyncTaskDao;

        insertAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RecipeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}

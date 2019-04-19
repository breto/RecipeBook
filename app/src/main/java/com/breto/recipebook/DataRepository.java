package com.breto.recipebook;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.breto.recipebook.persistence.database.RecipeBookDatabase;
import com.breto.recipebook.persistence.entity.RecipeEntity;

import java.util.List;


/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final RecipeBookDatabase mDatabase;
    private MediatorLiveData<List<RecipeEntity>> mObservableRecipes;

    private DataRepository(final RecipeBookDatabase database) {
        mDatabase = database;
        mObservableRecipes = new MediatorLiveData<>();

        mObservableRecipes.addSource(mDatabase.recipeDao().loadAllRecipes(),
                productEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableRecipes.postValue(productEntities);
                    }
                });
    }

    public static DataRepository getInstance(final RecipeBookDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<RecipeEntity>> getProducts() {
        return mObservableRecipes;
    }

    public LiveData<RecipeEntity> loadRecipe(final int productId) {
        return mDatabase.recipeDao().loadRecipe(productId);
    }

//    public LiveData<List<CommentEntity>> loadComments(final int productId) {
//        return mDatabase.commentDao().loadComments(productId);
//    }
//
    public LiveData<List<RecipeEntity>> searchRecipes(String query) {
        return mDatabase.recipeDao().searchAllRecipes(query);
    }
}

package com.breto.recipebook.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.breto.recipebook.persistence.entity.RecipeEntity;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    LiveData<List<RecipeEntity>> loadAllRecipes();

    @Insert
    void insert(RecipeEntity recipeEntity);

    @Query("delete from recipes")
    void deleteAll();

    @Query("select * from recipes order by name")
    LiveData<List<RecipeEntity>> getAllRecipes();

    @Query("select * from recipes where id = :id")
    LiveData<RecipeEntity> loadRecipe(int id);

    @Query("SELECT recipes.* FROM recipes WHERE name MATCH :query")
    LiveData<List<RecipeEntity>> searchAllRecipes(String query);

}

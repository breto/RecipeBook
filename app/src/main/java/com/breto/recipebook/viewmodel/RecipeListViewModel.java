package com.breto.recipebook.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.breto.recipebook.BasicApp;
import com.breto.recipebook.DataRepository;
import com.breto.recipebook.persistence.entity.RecipeEntity;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<RecipeEntity>> mObservableProducts;

    public RecipeListViewModel(Application application) {
        super(application);

        mObservableProducts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableProducts.setValue(null);

        mRepository = ((BasicApp) application).getRepository();
        LiveData<List<RecipeEntity>> products = mRepository.getProducts();

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource(products, mObservableProducts::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<RecipeEntity>> getRecipes() {
        return mObservableProducts;
    }

    public LiveData<List<RecipeEntity>> searchRecipes(String query) {
        return mRepository.searchRecipes(query);
    }
}

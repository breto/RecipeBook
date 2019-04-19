package com.breto.recipebook.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.breto.recipebook.BasicApp;
import com.breto.recipebook.DataRepository;
import com.breto.recipebook.persistence.entity.RecipeEntity;

public class RecipeViewModel extends AndroidViewModel {

    private final LiveData<RecipeEntity> mObservableRecipe;

    public ObservableField<RecipeEntity> product = new ObservableField<>();

    private final int mProductId;

    //private final LiveData<List<CommentEntity>> mObservableComments;

    public RecipeViewModel(@NonNull Application application, DataRepository repository,
                           final int productId) {
        super(application);
        mProductId = productId;

        //mObservableComments = repository.loadComments(mProductId);
        mObservableRecipe = repository.loadRecipe(mProductId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
//    public LiveData<List<CommentEntity>> getComments() {
//        return mObservableComments;
//    }

    public LiveData<RecipeEntity> getObservableProduct() {
        return mObservableRecipe;
    }

    public void setProduct(RecipeEntity product) {
        this.product.set(product);
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mProductId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mProductId = productId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RecipeViewModel(mApplication, mRepository, mProductId);
        }
    }
}

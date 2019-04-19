package com.breto.recipebook.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.breto.recipebook.R;
import com.breto.recipebook.databinding.RecipeItemBinding;
import com.breto.recipebook.persistence.entity.RecipeEntity;

import java.util.List;
import java.util.Objects;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    List<? extends RecipeEntity> mRecipeList;

    @Nullable
    private final RecipeClickCallback mRecipeClickCallback;

    public RecipeAdapter(@Nullable RecipeClickCallback clickCallback) {
        mRecipeClickCallback = clickCallback;
        setHasStableIds(true);
    }

    public void setProductList(final List<? extends RecipeEntity> recipeList) {
        if (mRecipeList == null) {
            mRecipeList = recipeList;
            notifyItemRangeInserted(0, recipeList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mRecipeList.size();
                }

                @Override
                public int getNewListSize() {
                    return recipeList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mRecipeList.get(oldItemPosition).getId() == recipeList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    RecipeEntity newRecipe = recipeList.get(newItemPosition);
                    RecipeEntity oldRecipe = mRecipeList.get(oldItemPosition);
                    return newRecipe.getId() == oldRecipe.getId()
                            && Objects.equals(newRecipe.getName(), oldRecipe.getName())
                            && Objects.equals(newRecipe.getUrl(), oldRecipe.getUrl());
                }
            });
            mRecipeList = recipeList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecipeItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recipe_item, parent, false);
        binding.setCallback(mRecipeClickCallback);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.binding.setRecipe(mRecipeList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    @Override
    public long getItemId(int position) {
        return mRecipeList.get(position).getId();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        final RecipeItemBinding binding;

        public RecipeViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

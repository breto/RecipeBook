package com.breto.recipebook.ui;

import com.breto.recipebook.persistence.entity.RecipeEntity;

public interface RecipeClickCallback {
    void onClick(RecipeEntity product);
}

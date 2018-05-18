package com.nis.frameworkapp.recipies.ui.list;


import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.databinding.RecipeListBinder;
import com.nis.frameworkapp.recipies.data.model.Recipe;
import com.nis.frameworkapp.recipies.data.model.RecipeList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter
        .RecipeAdapterViewHolder> {

    private Context mContext;
    private RecipeAdapterOnItemClickHandler mClickHandler;
    private RecipeList mRecipeList;

    public RecipeListAdapter(Context context, RecipeAdapterOnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item, parent, false);
        view.setFocusable(true);
        RecipeListBinder recipeListBinder = DataBindingUtil.bind(view);
        return new RecipeAdapterViewHolder(recipeListBinder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipeList.recipes.get(position);
        recipe.position = position;
        holder.recipeListBinder.setRecipe(recipe);
        holder.recipeListBinder.setOnClickHandler(mClickHandler);
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeList) return 0;
        return mRecipeList.recipes.size();
    }

    public interface RecipeAdapterOnItemClickHandler {
        void onItemClick(Recipe selectedItem);
    }

    public void swapRecipeList(final RecipeList newRecipeList) {
        if (mRecipeList == null) {
            mRecipeList = newRecipeList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mRecipeList.recipes.size();
                }

                @Override
                public int getNewListSize() {
                    return newRecipeList.recipes.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mRecipeList.recipes.get(oldItemPosition).id ==
                            newRecipeList.recipes.get(oldItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Recipe newRecipe = newRecipeList.recipes.get(newItemPosition);
                    Recipe oldRecipe = mRecipeList.recipes.get(oldItemPosition);
                    return newRecipe.id == oldRecipe.id
                            && newRecipe.title.equals(oldRecipe.title);
                }
            });
            mRecipeList = newRecipeList;
            result.dispatchUpdatesTo(this);
        }
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {
       public RecipeListBinder recipeListBinder;

        RecipeAdapterViewHolder(RecipeListBinder binder) {
            super(binder.getRoot());
            this.recipeListBinder = binder;
        }
    }

    public static class LoadImageBindingAdapter {
        @BindingAdapter("imageUrl")
        public static void loadImage(ImageView view, String url){
            Glide.with(view)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view);
        }
    }
}

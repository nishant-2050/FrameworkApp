package com.nis.frameworkapp.recipies.ui.list;


import android.content.Context;
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
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipeList.recipes.get(position);
        holder.title.setText(recipe.title);
        holder.ingredients.setText(recipe.ingredients);
        Glide.with(mContext)
                .load(recipe.thumbnail)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeList) return 0;
        return mRecipeList.recipes.size();
    }

    public interface RecipeAdapterOnItemClickHandler {
        void onItemClick(int selectedItem);
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

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, ingredients;
        private ImageView thumbnail;

        RecipeAdapterViewHolder(View view) {
            super(view);
            this.title = view.findViewById(R.id.id_title);
            this.ingredients = view.findViewById(R.id.id_ingr);
            this.thumbnail = view.findViewById(R.id.id_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onItemClick(getAdapterPosition());
        }
    }
}

package com.java.challengemeli.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.java.challengemeli.R;
import com.java.challengemeli.databinding.CellCategoryBinding;
import com.java.challengemeli.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewCategories extends RecyclerView.Adapter<AdapterRecyclerViewCategories.ViewHolderCategory> {

    private List<Category> categoryList;
    private ListenerSearchCategories listenerSearchCategories;

    AdapterRecyclerViewCategories(ListenerSearchCategories listenerSearchCategories) {
        this.categoryList = new ArrayList<>();
        this.listenerSearchCategories = listenerSearchCategories;
    }

    @NonNull
    @Override
    public ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CellCategoryBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.cell_category, parent, false);
        return new ViewHolderCategory(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategory holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    void updateList(List<Category> categoryList) {
        if (categoryList != null) {
            this.categoryList.clear();
            this.categoryList.addAll(categoryList);
            notifyDataSetChanged();
        }
    }

    class ViewHolderCategory extends RecyclerView.ViewHolder {
        private CellCategoryBinding binding;

        ViewHolderCategory(@NonNull CellCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category) {
            binding.setCategory(category);
            binding.getRoot().setOnClickListener(v -> listenerSearchCategories.onClickCategory(category));
            binding.executePendingBindings();
        }
    }

    public interface ListenerSearchCategories {
        void onClickCategory(Category category);
    }
}

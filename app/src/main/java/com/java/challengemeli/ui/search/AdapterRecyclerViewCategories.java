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

    //Inflo la vista y configuro el data binding dentro de la celda
    @NonNull
    @Override
    public ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CellCategoryBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.cell_category, parent, false);
        return new ViewHolderCategory(binding);
    }

    //Busco el objeto segun la posicion actual y se la paso al bind para los siguientes pasos
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategory holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    //Contador de items en la lista
    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    //Metodo para actualizar la lista y notificar cambios al adapter.
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

        //Seteo el objeto dentro del data binding y seteo posible click en la categoria
        void bind(Category category) {
            binding.setCategory(category);
            binding.getRoot().setOnClickListener(v -> listenerSearchCategories.onClickCategory(category));
            binding.executePendingBindings();
        }
    }

    //Escuchador en posible item de la categoria
    public interface ListenerSearchCategories {
        void onClickCategory(Category category);
    }
}

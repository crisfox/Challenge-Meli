package com.java.challengemeli.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.java.challengemeli.BR;
import com.java.challengemeli.R;
import com.java.challengemeli.databinding.CellSearchBinding;
import com.java.challengemeli.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolderProduct> {

    private List<Product> productList;
    private ListenerOnClick listenerOnClick;

    AdapterRecyclerView(ListenerOnClick listenerOnClick) {
        this.listenerOnClick = listenerOnClick;
        this.productList = new ArrayList<>();
    }

    //Inflo la vista y configuro el data binding dentro de la celda
    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CellSearchBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.cell_search, parent, false);
        return new ViewHolderProduct(binding);
    }

    //Busco el objeto segun la posicion actual y se la paso al bind para los siguientes pasos
    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduct holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    //Contador de items en la lista
    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    //Metodo para actualizar la lista y notificar cambios al adapter.
    void updateList(List<Product> productList) {
        if (productList != null) {
            this.productList.clear();
            this.productList.addAll(productList);
            notifyDataSetChanged();
        }
    }

    class ViewHolderProduct extends RecyclerView.ViewHolder {
        private final CellSearchBinding binding;

        ViewHolderProduct(CellSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        //Seteo el objeto dentro del data binding y seteo posible click en el producto
        void bind(Product product) {
            binding.setVariable(BR.product, product);
            binding.executePendingBindings();
            this.binding.getRoot().setOnClickListener(v -> listenerOnClick.onClickProduct(product));
        }
    }

    //Escuchador en posible item de el producto
    public interface ListenerOnClick {
        void onClickProduct(Product product);
    }
}

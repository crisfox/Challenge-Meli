package com.java.challengemeli.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.challengemeli.R;
import com.java.challengemeli.core.ChallengeActivity;
import com.java.challengemeli.core.ViewModelFactory;
import com.java.challengemeli.databinding.ActivityHomeBinding;
import com.java.challengemeli.entities.Product;
import com.java.challengemeli.ui.details.DetailActivity;
import com.java.challengemeli.ui.search.SearchActivity;

public class HomeActivity extends ChallengeActivity implements AdapterRecyclerView.ListenerOnClick {

    public static final int REQUEST_CODE_SEARCH = 5;

    private HomeViewModel viewModel;
    private ActivityHomeBinding binding;
    private AdapterRecyclerView adapterRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setupBindings();
    }

    private void setupBindings() {
        //Creo el View Model
        viewModel = ViewModelProviders.of(this, new ViewModelFactory()).get(HomeViewModel.class);

        setupRecyclerView();

        //Seteo el view model dentro del data binding
        binding.setViewModelSearch(viewModel);

        //Preparo el posible destino en el caso de presionar dentro del Search
        binding.textViewSearch.setOnClickListener(v -> {
            if (binding.textViewSearch.getText() == getString(R.string.buscar_en_mercado_libre)) {
                SearchActivity.navigateWithResult(this, null, REQUEST_CODE_SEARCH);
            } else {
                SearchActivity.navigateWithResult(this, binding.textViewSearch.getText().toString(), REQUEST_CODE_SEARCH);
            }
        });
        observers();
    }

    //Configuro Recycler View con su escuchador y seteo el adapter.
    private void setupRecyclerView() {
        this.adapterRecyclerView = new AdapterRecyclerView(this);
        binding.recyclerViewList.setAdapter(adapterRecyclerView);
        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    //Configuro los posibles cambios del View Model
    private void observers() {

        //Observo la llegada de la lista al view Model
        viewModel.getMutableLiveDataListProducts().observe(this, productList -> {
            if (productList != null) {
                binding.recyclerViewList.setVisibility(View.VISIBLE);
                adapterRecyclerView.updateList(productList);
                binding.animationViewErrorConection.setVisibility(View.GONE);
                binding.animationViewErrorSearch.setVisibility(View.GONE);
            }
        });

        //Observo la llegada del posible error de conexion o error en servicio.
        viewModel.getMutableLiveDataLoadError().observe(this, isError -> {
            if (isError != null) {
                binding.animationViewErrorConection.setVisibility(isError ? View.VISIBLE : View.GONE);
                binding.recyclerViewList.setVisibility(isError ? View.GONE : View.VISIBLE);
            }
        });

        //Observo los cambios de Loading para realizar los cambios necesarios
        viewModel.getMutableLiveDataLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    startProgressDialog();
                    binding.animationViewErrorConection.setVisibility(View.GONE);
                    binding.animationViewErrorSearch.setVisibility(View.GONE);
                    binding.recyclerViewList.setVisibility(View.GONE);
                } else {
                    stopProgressDialog();
                }
            }
        });

        //Observo los cambios del View Model en el caso de que los resultados de busqueda sean vacias
        viewModel.getMutableLiveDataSearchError().observe(this, isSearchError -> {
            if (isSearchError != null) {
                binding.animationViewErrorSearch.setVisibility(isSearchError ? View.VISIBLE : View.GONE);
                if (isSearchError){
                    binding.recyclerViewList.setVisibility(View.GONE);
                }
            }
        });
    }

    //Metodo para setear el texto ingresado en el Search superior
    private void setTextSearch(String textSearch) {
        if (textSearch.isEmpty()) {
            binding.textViewSearch.setText(getString(R.string.buscar_en_mercado_libre));
        } else {
            binding.textViewSearch.setText(textSearch);
        }
    }

    //Seteo la llegada de posible activity con resultados, en este caso el seteo y busqueda del texto a buscar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_SEARCH) {
            String textSearch;
            if (data != null && data.getExtras() != null) {
                textSearch = data.getExtras().getString("search");
                if (textSearch != null) {
                    startProgressDialog();
                    viewModel.getSearchProducts(textSearch);
                    setTextSearch(textSearch);
                    binding.recyclerViewList.smoothScrollToPosition(0);
                    binding.animationViewWelcomeConstraint.setVisibility(View.GONE);
                }
            }

        }
    }

    //Metodo del Escuchador dentro del Adapter, lleva a la descripcion del Producto
    @Override
    public void onClickProduct(Product product) {
        DetailActivity.navigate(this, product);
    }

}

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
        viewModel = ViewModelProviders.of(this, new ViewModelFactory()).get(HomeViewModel.class);

        setupRecyclerView();

        binding.setViewModelSearch(viewModel);
        binding.textViewSearch.setOnClickListener(v -> {
            if (binding.textViewSearch.getText() == getString(R.string.buscar_en_mercado_libre)) {
                SearchActivity.navigateWithResult(this, null, REQUEST_CODE_SEARCH);
            } else {
                SearchActivity.navigateWithResult(this, binding.textViewSearch.getText().toString(), REQUEST_CODE_SEARCH);
            }
        });
        observers();
    }

    private void setupRecyclerView() {
        this.adapterRecyclerView = new AdapterRecyclerView(this);
        binding.recyclerViewList.setAdapter(adapterRecyclerView);
        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void observers() {
        viewModel.getMutableLiveDataListProducts().observe(this, productList -> {
            if (productList != null) {
                binding.recyclerViewList.setVisibility(View.VISIBLE);
                adapterRecyclerView.updateList(productList);
                binding.animationViewErrorConection.setVisibility(View.GONE);
                binding.animationViewErrorSearch.setVisibility(View.GONE);
            }
        });

        viewModel.getMutableLiveDataLoadError().observe(this, isError -> {
            if (isError != null) {
                binding.animationViewErrorConection.setVisibility(isError ? View.VISIBLE : View.GONE);
                binding.recyclerViewList.setVisibility(isError ? View.GONE : View.VISIBLE);
            }
        });

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

        viewModel.getMutableLiveDataSearchError().observe(this, isSearchError -> {
            if (isSearchError != null) {
                binding.animationViewErrorSearch.setVisibility(isSearchError ? View.VISIBLE : View.GONE);
                if (isSearchError){
                    binding.recyclerViewList.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setTextSearch(String textSearch) {
        if (textSearch.isEmpty()) {
            binding.textViewSearch.setText(getString(R.string.buscar_en_mercado_libre));
        } else {
            binding.textViewSearch.setText(textSearch);
        }
    }

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

    @Override
    public void onClickProduct(Product product) {
        DetailActivity.navigate(this, product);
    }

}

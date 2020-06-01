package com.java.challengemeli.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.challengemeli.R;
import com.java.challengemeli.core.ChallengeActivity;
import com.java.challengemeli.core.ViewModelFactory;
import com.java.challengemeli.databinding.ActivitySearchBinding;
import com.java.challengemeli.entities.Category;

import java.util.Objects;

import static com.java.challengemeli.ui.home.HomeActivity.REQUEST_CODE_SEARCH;

public class SearchActivity extends ChallengeActivity implements AdapterRecyclerViewCategories.ListenerSearchCategories {

    private static final String TEXT_SEARCH = "search";
    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private AdapterRecyclerViewCategories adapterRecyclerView;

    public static void navigateWithResult(Activity activity, String searchText, int requestCode) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(TEXT_SEARCH, searchText);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        viewModel = ViewModelProviders.of(this, new ViewModelFactory()).get(SearchViewModel.class);
        viewModel.getServiceListCategories();

        setupToolbar();
        setupRecyclerView();
        getSearchTextIntent();
        setupEditTextSearch();

        observers();
    }

    private void setupEditTextSearch() {
        binding.editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                navigateActivityMain();
                return true;
            }
            return false;
        });
        binding.editTextSearch.requestFocus();
    }

    private void getSearchTextIntent() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(TEXT_SEARCH) != null && !Objects.equals(getIntent().getExtras().getString(TEXT_SEARCH), getString(R.string.buscar_en_mercado_libre))) {
                binding.editTextSearch.setText(getIntent().getExtras().getString(TEXT_SEARCH));
            } else {
                binding.editTextSearch.setText("");
            }
        }
    }

    private void setupRecyclerView() {
        this.adapterRecyclerView = new AdapterRecyclerViewCategories(this);
        binding.recyclerViewList.setAdapter(adapterRecyclerView);
        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void observers() {
        viewModel.getMutableLiveDataListCategory().observe(this, productList -> {
            if (productList != null) {
                binding.recyclerViewList.setVisibility(View.VISIBLE);
                adapterRecyclerView.updateList(productList);
                binding.animationViewErrorConection.setVisibility(View.GONE);
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
                    binding.recyclerViewList.setVisibility(View.GONE);
                } else {
                    stopProgressDialog();
                }
            }
        });
    }

    private void navigateActivityMain() {
        Intent intent = new Intent();
        intent.putExtra("search", binding.editTextSearch.getText().toString());
        setResult(REQUEST_CODE_SEARCH, intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onClickCategory(Category category) {
        binding.editTextSearch.setText(category.getName());
        navigateActivityMain();
    }
}

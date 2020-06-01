package com.java.challengemeli.ui.search;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.java.challengemeli.R;
import com.java.challengemeli.core.ChallengeActivity;
import com.java.challengemeli.core.ViewModelFactory;
import com.java.challengemeli.databinding.ActivitySearchBinding;
import com.java.challengemeli.entities.Category;

public class SearchActivity extends ChallengeActivity implements AdapterRecyclerViewCategories.ListenerSearchCategories {

    private static final String TEXT_SEARCH = "search";
    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private AdapterRecyclerViewCategories adapterRecyclerView;

    public static void navigateWithResult(Activity activity,String searchText, int requestCode) {
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

        setSupportActionBar(binding.toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.adapterRecyclerView = new AdapterRecyclerViewCategories(this);
        binding.recyclerViewList.setAdapter(adapterRecyclerView);
        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().getString(TEXT_SEARCH) != null && !getIntent().getExtras().getString(TEXT_SEARCH).equals("Buscar en Mercado Libre")){
                binding.editTextSearch.setText(getIntent().getExtras().getString(TEXT_SEARCH));
            }else {
                binding.editTextSearch.setText("");
            }
        }

        binding.editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                navigateActivityMain();
                return true;
            }
            return false;
        });
        binding.editTextSearch.requestFocus();

        observers();
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
                if (isLoading){
                    startProgressDialog();
                }else {
                    stopProgressDialog();
                }
                if (isLoading) {
                    binding.animationViewErrorConection.setVisibility(View.GONE);
                    binding.recyclerViewList.setVisibility(View.GONE);
                }
            }
        });
    }

    private void navigateActivityMain() {
        Intent intent = new Intent();
        intent.putExtra("search",binding.editTextSearch.getText().toString());
        setResult(5,intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onClickCategory(Category category) {
        binding.editTextSearch.setText(category.getName());
        navigateActivityMain();
    }
}

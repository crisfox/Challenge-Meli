package com.java.challengemeli.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.java.challengemeli.R;
import com.java.challengemeli.core.ChallengeActivity;
import com.java.challengemeli.databinding.ActivityDetailBinding;
import com.java.challengemeli.entities.Product;

public class DetailActivity extends ChallengeActivity {

    private final static String EXTRA_PRODUCT = "extra.product";
    private ActivityDetailBinding binding;

    public static void navigate(Activity activity, Product product) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Product product = getIntentProduct();

        //Seteo el boton de back
        binding.imageViewBack.setOnClickListener(v -> onBackPressed());

        //Seteo el objeto a la variable del data binding
        binding.setProduct(product);
        binding.executePendingBindings();
    }

    //Traigo el objeto enviado en la pantalla anterior
    private Product getIntentProduct() {
        Product product = null;
        if (getIntent().getExtras() != null) {
            product = getIntent().getExtras().getParcelable(EXTRA_PRODUCT);
            if (product != null) {
                setupButtonShare(product.getPermalink());
                setupButtonBuy(product.getPermalink());
            }
        }
        return product;
    }

    //Preparo boton comprar, dirije a la app o web de Meli
    private void setupButtonBuy(String url) {
        binding.buttonBuy.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));
            startActivity(sendIntent);
        });
    }

    //Preparo boton compartir texto, comparte solo la URL para que puedan ingresar desde web o app.
    private void setupButtonShare(String url) {
        binding.buttonShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
    }
}

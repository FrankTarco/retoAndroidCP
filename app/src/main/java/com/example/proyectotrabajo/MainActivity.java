package com.example.proyectotrabajo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectotrabajo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reemplazarFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
           if(R.id.home == item.getItemId()){
               mensajeAlert("Entro al home");
               reemplazarFragment(new HomeFragment());
           }
           else if(R.id.dulceria == item.getItemId()){
               mensajeAlert("Entro al dulceria");
               reemplazarFragment(new DulceriaFragment());
           }
           else{
               mensajeAlert("Entro al login");
               reemplazarFragment(new LoginFragment());
           }
            return true;
        });

    }

    private void reemplazarFragment(Fragment f){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,f);
        fragmentTransaction.commit();
    }

    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
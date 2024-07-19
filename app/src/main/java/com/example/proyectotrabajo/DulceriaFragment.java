package com.example.proyectotrabajo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectotrabajo.adapter.CandyAdapter;
import com.example.proyectotrabajo.entity.Candy;
import com.example.proyectotrabajo.entity.Items;
import com.example.proyectotrabajo.service.CandyStoreService;
import com.example.proyectotrabajo.util.ConnectionRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DulceriaFragment extends Fragment implements CandyAdapter.OnQuantityChangeListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CandyStoreService service;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DulceriaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DulceriaFragment newInstance(String param1, String param2) {
        DulceriaFragment fragment = new DulceriaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ConnectionRest.getConnection().create(CandyStoreService.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Items lista;
    CandyAdapter adapter;
    Button btnFinalizar;
    RecyclerView recicler;
    TextView total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dulceria, container, false);

        btnFinalizar = view.findViewById(R.id.btnFinalizarCompra);
        recicler = view.findViewById(R.id.rcLstCandy);
        total = view.findViewById(R.id.tvTotal);
        recicler.setLayoutManager(new LinearLayoutManager(getContext()));

        listado();
        updateTotalAmount();

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPagar(v);
            }
        });

        return view;
    }

    private void listado(){
        Call<Items> call = service.listarCandyStore();
        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if(response.isSuccessful()){
                    lista = response.body();
                    if(lista != null){
                        adapter = new CandyAdapter(lista, getContext(),DulceriaFragment.this);
                        recicler.setAdapter(adapter);
                    }
                    else{
                        mensajeToastShort("La lista esta vacia");
                    }

                }
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                mensajeToastShort("Cayo la data");
            }
        });
    }

    public void mensajeToastShort(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuantityChanged() {
        updateTotalAmount();
        mensajeToastShort("Se esta modificando el listener");
    }

    private void updateTotalAmount() {
        double totalAmount = 0.0;
        if (lista != null && lista.getItems() != null) {
            for (Candy bean : lista.getItems()) {
                if (bean != null) { // Verifica si bean no es null
                    totalAmount += bean.getTotalPrice(); // Asumiendo que getTotalPrice() está definido en Candy
                }
            }
        }
        total.setText(String.valueOf("Total a pagar: "+ totalAmount));
    }

    private void abrirPagar(View anchorView){
        LayoutInflater inflater = getLayoutInflater(); // Use getLayoutInflater() instead
        View popupView = inflater.inflate(R.layout.pop_pago, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,true);

        //TOMAR DATOS
        EditText txtTarjeta = popupView.findViewById(R.id.txtNumeroTarjeta);
        EditText txtFechaExpi = popupView.findViewById(R.id.txtFechaExpliracion);
        EditText txtCvv = popupView.findViewById(R.id.txtCvv);
        EditText txtEmail = popupView.findViewById(R.id.txtCorreoElectronico);
        EditText txtNombre = popupView.findViewById(R.id.txtNombre);
        EditText txtTipoDocumento = popupView.findViewById(R.id.txtTipoDoc);
        EditText txtDocumento = popupView.findViewById(R.id.txtDocumento);
        Button btnTerminarPago = popupView.findViewById(R.id.btnTerminarCompra);
        Button btnCancelar = popupView.findViewById(R.id.btnCancelarCompra);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userName = prefs.getString("userName", "");
        String userEmail = prefs.getString("userEmail", "");

        txtNombre.setText(userName);
        txtEmail.setText(userEmail);

        btnTerminarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tarjeta,fechaEx,cvv,email,nombre,tipoDoc,doc;
                tarjeta = txtTarjeta.getText().toString();
                fechaEx = txtFechaExpi.getText().toString();
                cvv = txtCvv.getText().toString();
                email = txtEmail.getText().toString();
                nombre = txtNombre.getText().toString();
                tipoDoc = txtTipoDocumento.getText().toString();
                doc = txtDocumento.getText().toString();

                mensajeToastShort("Se recepciondo datos:" + tarjeta+fechaEx+cvv+email+nombre+tipoDoc+doc);
                popupWindow.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // To make outside touches work
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

}
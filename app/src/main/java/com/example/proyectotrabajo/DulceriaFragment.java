package com.example.proyectotrabajo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectotrabajo.adapter.CandyAdapter;
import com.example.proyectotrabajo.entity.Candy;
import com.example.proyectotrabajo.entity.Complete;
import com.example.proyectotrabajo.entity.Items;
import com.example.proyectotrabajo.entity.Respuesta;
import com.example.proyectotrabajo.service.CandyStoreService;
import com.example.proyectotrabajo.service.CompleteService;
import com.example.proyectotrabajo.util.ConnectionRest;
import com.example.proyectotrabajo.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DulceriaFragment extends Fragment implements CandyAdapter.OnQuantityChangeListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CandyStoreService service;
    private CompleteService completeService;
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
        completeService = ConnectionRest.getConnection().create(CompleteService.class);
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
    Respuesta objRespuesta;

    double totalAmount = 0.0;

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

    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    @Override
    public void onQuantityChanged() {
        updateTotalAmount();
    }

    private void updateTotalAmount() {

        double monto = 0.0;
        if (lista != null && lista.getItems() != null) {
            for (Candy bean : lista.getItems()) {
                if (bean != null) {
                    monto += bean.getTotalPrice();
                }
            }
        }
        totalAmount = monto;
        total.setText(String.valueOf("Total a pagar: "+ monto));
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
        Spinner spnTipoDoc = popupView.findViewById(R.id.txtTipoDoc);
        TextView tvTitle = popupView.findViewById(R.id.tvCompraTitle);

        tvTitle.setText("Su monto a pagar es S/" + totalAmount);

        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("DNI");
        tipos.add("CC.EE");
        tipos.add("OTROS");
        ArrayAdapter<String> adaptadorTipoDoc = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tipos);
        adaptadorTipoDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDoc.setAdapter(adaptadorTipoDoc);

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
                tipoDoc = spnTipoDoc.getSelectedItem().toString();
                doc = txtDocumento.getText().toString();

                if(!tarjeta.matches(ValidacionUtil.TARJETA)){
                    mensajeAlert("LA TARJETA DEBE TENER 16 DIGITOS");
                }
                else if(!fechaEx.matches(ValidacionUtil.FECHAVENCIMIENTO)){
                    mensajeAlert("El formato es MM/dd");
                }
                else if(!cvv.matches(ValidacionUtil.CVV)){
                    mensajeAlert("SOLO 3 DIGITOS");
                }
                else if(!email.matches(ValidacionUtil.CORREO)){
                    mensajeAlert("El formato del correo debe ser correcto");
                }
                else if(!nombre.matches(ValidacionUtil.NOMBRES)){
                    mensajeAlert("El nombre debe ser de 3 a 60 caracteres");
                }
                else if(!doc.matches(ValidacionUtil.DNI)){
                    mensajeAlert("EL DNI debe tener 8 digitos");
                }
                else{
                    Random random = new Random();
                    int randomNumber = 100000000 + random.nextInt(900000000);
                    Complete bean = new Complete();
                    bean.setName(nombre);
                    bean.setDni(doc);
                    bean.setMail(email);
                    bean.setOperation_date(String.valueOf(randomNumber));
                    completarRegistro(bean);
                    popupWindow.dismiss();
                }

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

    private void completarRegistro(Complete obj){

        Call<Respuesta> call = completeService.completarCompra(obj);
        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if(response.isSuccessful()){
                    objRespuesta = response.body();
                    if(objRespuesta != null){
                        resetCart();
                        mensajeAlert("Compra exitosa " + objRespuesta.getResul_code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });

    }

    private void resetCart() {
        totalAmount = 0.0;
        total.setText("Total a pagar: 0.0");
        if (lista != null && lista.getItems() != null) {
            for (Candy bean : lista.getItems()) {
                if (bean != null) {
                    bean.setQuantity(0);
                }
            }
        }
        recicler.getAdapter().notifyDataSetChanged();
    }

}
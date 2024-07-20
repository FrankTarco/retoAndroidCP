package com.example.proyectotrabajo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectotrabajo.R;
import com.example.proyectotrabajo.entity.Candy;
import com.example.proyectotrabajo.entity.Items;

public class CandyAdapter extends RecyclerView.Adapter<CandyAdapter.ViewHolder> {

    private Items lista;
    private Context context;
    private OnQuantityChangeListener onQuantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CandyAdapter(Items lista, Context context, OnQuantityChangeListener quantityChangeListener) {
        this.lista = lista;
        this.context = context;
        this.onQuantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public CandyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_candystore,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandyAdapter.ViewHolder holder, int position) {

        Candy item = lista.getItems().get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return lista.getItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre, descripcion, precio;
        private EditText edtCantidad;
        private ImageView decrease, add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.itemName);
            descripcion = itemView.findViewById(R.id.itemDescription);
            precio = itemView.findViewById(R.id.itemPrecio);
            edtCantidad = itemView.findViewById(R.id.edtCantidad);
            decrease = itemView.findViewById(R.id.btnDecrease);
            add = itemView.findViewById(R.id.btnAdd);
        }

        public void bind(Candy obj){
            nombre.setText(obj.getName());
            descripcion.setText(obj.getDescription());
            precio.setText(obj.getPrice());
            edtCantidad.setText(String.valueOf(obj.getQuantity()));

            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Integer.parseInt(edtCantidad.getText().toString()) > 0){
                        obj.setQuantity(obj.getQuantity()-1);
                        edtCantidad.setText(String.valueOf(obj.getQuantity()));
                        if (onQuantityChangeListener != null) {
                            onQuantityChangeListener.onQuantityChanged();
                        }
                    }
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obj.setQuantity(obj.getQuantity()+1);
                    edtCantidad.setText(String.valueOf(obj.getQuantity()));
                    if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onQuantityChanged();
                    }
                }
            });
        }
    }

}

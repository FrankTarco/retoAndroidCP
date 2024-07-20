package com.example.proyectotrabajo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.proyectotrabajo.entity.Premier;
import com.example.proyectotrabajo.entity.Premieres;
import com.example.proyectotrabajo.service.PremierService;
import com.example.proyectotrabajo.util.ConnectionRest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{

    private ImageSlider imageSlider;
    private ArrayList<SlideModel> imageList = new ArrayList<>();
    private PremierService service;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Premieres lstPremieres;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ConnectionRest.getConnection().create(PremierService.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = view.findViewById(R.id.image_slider);
        listaImagenes();
        return view;
    }

    private void listaImagenes(){
        Call<Premieres> call = service.listarPremieres();
        call.enqueue(new Callback<Premieres>() {
            @Override
            public void onResponse(Call<Premieres> call, Response<Premieres> response) {
                if(response.isSuccessful()){
                    lstPremieres = response.body();
                    if(lstPremieres != null && lstPremieres.getPremieres() != null ){
                        ArrayList<SlideModel> imageList = new ArrayList<>();
                        for(Premier bean: lstPremieres.getPremieres()){
                            imageList.add(new SlideModel(bean.getImage(), bean.getDescription(),ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(imageList);

                        imageSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                reemplazarFragment(new LoginFragment());
                            }

                            @Override
                            public void doubleClick(int i) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Premieres> call, Throwable t) {
                mensajeToastShort("No se ingreso");
            }
        });
    }

    private void reemplazarFragment(Fragment f){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        fragmentTransaction.replace(R.id.frame_layout,f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void mensajeToastShort(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
package com.example.proyectotrabajo.util;

public class ValidacionUtil {

    public static final String TEXTO = "[a-zA-ZáéíóúñüÁÉÍÓÚÑÜ\\s]{2,20}";
    public static final String DNI = "[0-9]{8}";
    public static final String TARJETA = "[0-9]{16}";
    public static final String CORREO = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    public static final String FECHAVENCIMIENTO = "^(0?[1-9]|1[0-2])/([0-2]?[1-9]|3[01])$";
    public static final String CVV = "[0-9]{3}";
    public static final String NOMBRES = "[a-zA-ZáéíóúñüÁÉÍÓÚÑÜ\\s]{3,60}";


}

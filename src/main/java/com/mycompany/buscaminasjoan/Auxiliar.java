/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author villa
 */
public class Auxiliar extends Personas {

    private File archivo;

    public Auxiliar(ArrayList<Persona> autores) {
        super(autores);
    }

    public static ArrayList<Persona> leerJugadores(String archivo) {
        //ArrayList<Persona> personal = new ArrayList<>();
        ArrayList<Persona> listaJugadores = new ArrayList<>();

        try {
            File f = new File(archivo);
            System.out.println("Leyendo archivo: " + f.getAbsolutePath());
            Scanner lector = new Scanner(f);

            while (lector.hasNext()) {
                String linea = lector.nextLine();
                System.out.println("Leyendo línea: " + linea);
                String[] datosPelicula = linea.split(";");

                String nombre = datosPelicula[0];
                String tiempo = datosPelicula[1];
                String dificultad = datosPelicula[2];
                String foto = datosPelicula[3];

                listaJugadores.add(new Persona(nombre, tiempo, dificultad, foto));
            }
            lector.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaJugadores;
    }

    public static void grabarJugadores(ArrayList<Persona> lista) {
        try {
            File f = new File("./src/main/java/com/mycompany/buscaminasjoan/ficheros/jugadores.txt");
            FileWriter fw = new FileWriter(f);

            for (Persona persona : lista) {
                fw.write(persona.getNombre());
                fw.write(";");
                fw.write(persona.getTiempo());
                fw.write(";");
                fw.write(persona.getDificultad());
                fw.write(";");
                fw.write(persona.getFoto());
                fw.write("\n");
            }

            fw.close();
            System.out.println("Fichero escrito correctamente");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

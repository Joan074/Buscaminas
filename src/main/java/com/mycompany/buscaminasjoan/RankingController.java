/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import com.mycompany.buscaminasjoan.Persona;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author joavilrod2
 */
public class RankingController implements Initializable {

    @FXML
    private Button botonFacil;
    @FXML
    private Button botonDificil;
    @FXML
    private Button botonIntermedio;
    @FXML
    private TableView<Persona> tableRanking;

    private ObservableList<Persona> personas;
    @FXML
    private TableColumn<Persona, String> idNombre;
    @FXML
    private TableColumn<Persona, String> idTiempo;
    @FXML
    private TableColumn<Persona, String> idFoto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        idNombre.setCellValueFactory(cellData -> cellData.getValue().NombreProperty());
        idTiempo.setCellValueFactory(cellData -> cellData.getValue().TiempoProperty());

        idFoto.setCellValueFactory(cellData -> cellData.getValue().FotoProperty());
        idFoto.setCellFactory(columna -> {
            return new TableCell<Persona, String>() {
                private ImageView view = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        //Carga el archivo que contiene la imagen
                        File imageFile = new File(item);
                        String fileLocation = imageFile.toURI().toString();
                        Image image = new Image(fileLocation, 40, 40, true, true);

                        //envia la imagen
                        view.setImage(image);
                        setGraphic(view);
                    }
                }

            };

        });
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = FXCollections.observableArrayList(personas);
        tableRanking.setItems(this.personas);
    }

    @FXML
    private void rankingFacil(ActionEvent event) {
        mostrarRankingPorDificultad("facil");
    }

    @FXML
    private void rankingDificil(ActionEvent event) {
        mostrarRankingPorDificultad("dificil");
    }

    @FXML
    private void rankingIntermedio(ActionEvent event) {
        mostrarRankingPorDificultad("intermedio");
    }

    //Filtra las personas por dificultad (Metodo encontrado en StackOverflow y modificado segun mis necesidades)
    private void mostrarRankingPorDificultad(String dificultad) {
        
        //Filtra las personas de la lista 
        List<Persona> filtradas = personas.stream()
                //contiene solo los elementos persona
                .filter(persona -> persona.getDificultad().equals(dificultad))
                /*guarda los elementos persona y los convierte de nuevo en una lista*/
                 .sorted(Comparator.comparingInt(persona -> convertirTiempoASegundos(persona.getTiempo())))
                .collect(Collectors.toList());
        tableRanking.setItems(FXCollections.observableArrayList(filtradas));
    }
    
     private int convertirTiempoASegundos(String tiempo) {
        String[] partes = tiempo.split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);
        return minutos * 60 + segundos;
    }

}
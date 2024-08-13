/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author villa
 */
public class MenuprincipalController implements Initializable {

    @FXML
    private ImageView imagenFondo;
    @FXML
    private Button botonLista;
    @FXML
    private Button botonJugar;
    @FXML
    private Button botonSalir;
    @FXML
    private ImageView imagenJugar;
    @FXML
    private ImageView imagenLista;
    @FXML
    private ImageView imagenSalir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar la imagen de fondo
        File imageFile = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/menu.jpeg");
        String fileLocation = imageFile.toURI().toString();
        Image image = new Image(fileLocation, 520, 526, true, true);
        imagenFondo.setImage(image);

        // Configurar la imagen del botón "Jugar"
        File imageJugar = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/tronco.png");
        String fileLocationJugar = imageJugar.toURI().toString();
        Image imageJugarImg = new Image(fileLocationJugar, 208, 55, false, false);
        imagenJugar.setImage(imageJugarImg);

        // Añadir imagen al botón "Jugar"
        ImageView jugarImageView = new ImageView(imageJugarImg);
        botonJugar.setGraphic(jugarImageView);

        // Configurar la imagen del botón "Lista"
        File imageLista = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/tronco.png");
        String fileLocationLista = imageLista.toURI().toString();
        Image imageListaImg = new Image(fileLocationLista, 208, 55, false, false);
        imagenLista.setImage(imageListaImg);

        // Añadir imagen al botón "Lista"
        ImageView listaImageView = new ImageView(imageListaImg);
        botonLista.setGraphic(listaImageView);

        // Configurar la imagen del botón "Salir"
        File imageSalir = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/tronco.png");
        String fileLocationSalir = imageSalir.toURI().toString();
        Image imageSalirImg = new Image(fileLocationSalir, 208, 55, false, false);
        imagenSalir.setImage(imageSalirImg);

        // Añadir imagen al botón "Salir"
        ImageView salirImageView = new ImageView(imageSalirImg);
        botonSalir.setGraphic(salirImageView);

        // Acción del botón "Salir"
        botonSalir.setOnAction(e -> {
            Stage stage = (Stage) botonSalir.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void empezarJugar(ActionEvent event) throws IOException {

        FXMLLoader miCargador = new FXMLLoader(
                getClass().getClassLoader().getResource(
                        "com/mycompany/buscaminasjoan/buscaminas.fxml")
        );

        Parent root = miCargador.load();

        BuscaminasController controladorPersona
                = miCargador.<BuscaminasController>getController();

        // Crear el cargador de FXML para TableviewController
        FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("table.fxml"));
        Parent tableRoot = tableLoader.load();
        
        TableViewController tableController = tableLoader.getController();
        
        controladorPersona.setTableViewController(tableController);

        //llamo al metodo initPersona
        Scene scene = new Scene(root, 990, 760);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Buscaminas - Joan Villanueva");
        stage.initModality(Modality.APPLICATION_MODAL); //la ventana se muestra en modal
        stage.showAndWait(); //espera a que se cierre la segunda ventana
    }

    @FXML
    private void listaJugadores(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(
                getClass().getClassLoader().getResource(
                        "com/mycompany/buscaminasjoan/table.fxml")
        );

        Parent root = miCargador.load();

        TableViewController controladorPersona
                = miCargador.<TableViewController>getController();

        Scene scene = new Scene(root, 610, 410);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Lista de Jugadores - Joan Villanueva");
        stage.initModality(Modality.APPLICATION_MODAL); //la ventana se muestra en modal
        stage.showAndWait(); //espera a que se cierre la segunda ventana
    }
}

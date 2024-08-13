module com.mycompany.buscaminasjoan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
      requires java.desktop;

    opens com.mycompany.buscaminasjoan to javafx.fxml;
    exports com.mycompany.buscaminasjoan;
    
    
}

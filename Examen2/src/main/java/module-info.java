module co.edu.poli.Examen2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.Examen2.adaptadores.entrada.javafx to javafx.fxml;

    exports co.edu.poli.Examen2.vista;
}

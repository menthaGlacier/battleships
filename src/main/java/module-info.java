module ru.metapunk.battleships {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.metapunk.battleships to javafx.fxml;
    exports ru.metapunk.battleships;
}
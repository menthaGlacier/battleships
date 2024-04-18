module ru.metapunk.battleships {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.metapunk.battleships to javafx.fxml;
    exports ru.metapunk.battleships;
    exports ru.metapunk.battleships.net;
    exports ru.metapunk.battleships.net.observer;
    exports ru.metapunk.battleships.net.dto.request;
    exports ru.metapunk.battleships.net.dto.response;
    exports ru.metapunk.battleships.controller;
    exports ru.metapunk.battleships.model.board to javafx.fxml;
    exports ru.metapunk.battleships.model.tile.cell to javafx.fxml;
    opens ru.metapunk.battleships.controller to javafx.fxml;
}
package jobpostergui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class RootLayoutController {
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private void initialize() {
        statusLabel.setTextFill(Color.web("#000000"));
        statusLabel.setText("status: idle");
    }
    
    public Label getStatusLabel(){
        return statusLabel;
    }
    
}

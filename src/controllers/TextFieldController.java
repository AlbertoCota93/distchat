package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class TextFieldController implements Initializable {
	@FXML
	private TextArea text;
	@FXML
	private Button conButton;

	EventHandler<KeyEvent> spaceHandler = new EventHandler<KeyEvent>(){
		@Override
		public void handle(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getCode().equals(KeyCode.SPACE)|| e.getCode().equals(KeyCode.ENTER)){
				System.out.println("SPAAAAACE");
			}
		}
	};

	EventHandler<MouseEvent> btnHandler = new EventHandler<MouseEvent>(){

		@Override
		public void handle(MouseEvent m) {
			// TODO Auto-generated method stub
			System.out.println(m.getClickCount());
		}

	};
	@FXML
	private void keyPressed(KeyEvent event){
		text.setOnKeyPressed(spaceHandler);

	}

	@FXML
	private void buttonPressed(MouseEvent m){
	  conButton.setOnMousePressed(btnHandler);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}

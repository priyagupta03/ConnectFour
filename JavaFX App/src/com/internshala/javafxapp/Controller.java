package com.internshala.javafxapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	@FXML
	public Label welcomeLabel;
	@FXML
	public ChoiceBox<String> choiceBox;
	@FXML
	public TextField userInput;
	@FXML
	public Button convertButton;

	private static final String C_TO_F="Celsius to Fahrenheit";
	private static final String F_TO_C="Fahrenheit to Celsius";

	private boolean isC_TO_F = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	choiceBox.getItems().add(C_TO_F);
	choiceBox.getItems().add(F_TO_C);

	choiceBox.setValue(C_TO_F);

	choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

		if(newValue.equals(isC_TO_F)){
			isC_TO_F=true;
		}else{
			isC_TO_F=false;
		}

	});

	convertButton.setOnAction(event -> {
		convert();
	});

	}

	private void convert() {
		String input= userInput.getText();
		float enteredTemperature=0.0f;
		try {
			enteredTemperature = Float.parseFloat(input);
		}catch (Exception ex){
			warnUser();
			return;
		}

		float newTemperature=0.0f;

		if(isC_TO_F){
			newTemperature=(enteredTemperature*9/5)+32;
		}else {
			newTemperature=(enteredTemperature-32)*5/9;
		}

		display(newTemperature);

	}

	private void warnUser() {
		Alert alert1 = new Alert(Alert.AlertType.ERROR);
		alert1.setHeaderText("Invalid Temperature Entered!");
		alert1.setContentText("Enter a valid temperature.");
		alert1.show();
	}

	private void display(float newTemperature) {
		String unit = isC_TO_F? "F":"C";
		System.out.println("The new temperature is " + newTemperature + unit);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Result");
		alert.setContentText("The new temperature is " + newTemperature + unit);
		alert.show();


	}
}

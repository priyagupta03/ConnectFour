package com.internshala.javafxapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;


public class MyMain extends Application {

	public static void main(String[] args){
		System.out.println("Main");

		launch(args);
	}

	@Override
	public void init() throws Exception {
		System.out.println("Initiate!");
		super.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Start");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("app_layout.fxml"));

		VBox rootNode = loader.load();

		MenuBar menuBar= creatMenu();
		rootNode.getChildren().add(0,menuBar);


		Scene scene = new Scene(rootNode);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Temperature Converter");
		primaryStage.show();

	}
	private MenuBar creatMenu(){

		Menu fileMenu= new Menu("File");
		MenuItem newMenuItem= new MenuItem("New");
		newMenuItem.setOnAction(event -> System.out.println("New menu item clicked!"));

		SeparatorMenuItem separatorMenuItem= new SeparatorMenuItem();

		MenuItem quitMenuItem= new MenuItem("Quit");
		quitMenuItem.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
		fileMenu.getItems().addAll(newMenuItem, separatorMenuItem,quitMenuItem);

		Menu helpMenu=new Menu("Help");
		MenuItem aboutMenuItem= new MenuItem("About");
		aboutMenuItem.setOnAction(event -> aboutApp());
		helpMenu.getItems().addAll(aboutMenuItem);


		MenuBar menuBar =new MenuBar();
		menuBar.getMenus().addAll(fileMenu,helpMenu);
		return menuBar;
	}

	private void aboutApp() {
		Alert alertDialog= new Alert(Alert.AlertType.INFORMATION);

		alertDialog.setTitle("About App");
		alertDialog.setHeaderText("Information");
		alertDialog.setContentText("Hey! I am a new programmer trying to build a new project on javaFX. So, here I am presenting a game for you.");

		ButtonType yesBtn=new ButtonType("YES");
		ButtonType noBtn= new ButtonType("NO");

		alertDialog.getButtonTypes().setAll(yesBtn,noBtn);

		Optional<ButtonType> clickedBtn= alertDialog.showAndWait();

		if(clickedBtn.isPresent()&&clickedBtn.get()==yesBtn){
			System.out.println("Yes button clicked!");
		}else{
			System.out.println("No button clicked!");
		}
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stop");
		super.stop();
	}
}

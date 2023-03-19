package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {


	private static final  int columns=7;
	private static final int rows=6;
	private static final int circleDiameter=80;
	private static final String discColor1="#24303e";
	private static final String discColor2="4caa88";

	private static String playerOne="Player One";
	private static String playerTwo="Player Two";

	private boolean isPlayerOneTurn =true;

	private  boolean isAllowedToInsert=true;

	private Disc[][] insertedDiscArray= new Disc[rows][columns];

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane;

	@FXML
	public Label playerNameLabel;

	@FXML
	public TextField playerOneTextField,playerTwoTextField;

	@FXML
	public Button setNamesButton;

	public void createPlayground(){

		Shape rectangleWithHoles = createGameStructuralGrid();
		rootGridPane.add(rectangleWithHoles,0,1);

		List<Rectangle> rectangleList =createClickableItems();
		for (Rectangle rectangle: rectangleList) {
			rootGridPane.add(rectangle,0,1);

		}
		setNamesButton.setOnAction(event -> {
			System.out.println(playerOneTextField.getText());
			System.out.println(playerTwoTextField.getText());

		});

	}

	private  Shape createGameStructuralGrid(){
		Shape rectangleWithHoles =new Rectangle((columns+1)*circleDiameter,(rows+1)*circleDiameter);

		for (int row=0; row < rows; row++){
			for (int col=0; col < columns; col++){
				Circle circle=new Circle();
				circle.setRadius(circleDiameter/2);
				circle.setCenterX(circleDiameter/2);
				circle.setCenterY(circleDiameter/2);
				circle.setSmooth(true);

				circle.setTranslateX(col*(circleDiameter+5) + circleDiameter/4);
				circle.setTranslateY(row*(circleDiameter+5) + circleDiameter/4);
				rectangleWithHoles= Shape.subtract(rectangleWithHoles,circle);

			}
		}

		rectangleWithHoles.setFill(Color.WHITE);
		return rectangleWithHoles;
	}

	private List<Rectangle> createClickableItems(){
		List<Rectangle> rectangleList= new ArrayList<>();

		for (int col = 0; col <columns ; col++) {
			Rectangle rectangle= new Rectangle(circleDiameter,(rows+1)*circleDiameter);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col*(circleDiameter+5) + circleDiameter/4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

			final int column=col;
			rectangle.setOnMouseClicked(event -> {
				if(isAllowedToInsert){
					isAllowedToInsert=false;
					insertDisc(new Disc(isPlayerOneTurn),column);
				}

			});
			rectangleList.add(rectangle);

		}
		return rectangleList;

	}


	private void insertDisc(Disc disc, int column){

		int row=rows-1;
		 while (row>=0){
		 	if(getDiscIfPresent(row,column)==null)
		 		break;
		 	    row--;
		 }
		 if(row<0)
		 	return;

		int currentRow= row;
		insertedDiscArray[row][column]=disc;
		insertedDiscsPane.getChildren().add(disc);

		disc.setTranslateX(column*(circleDiameter+5) + circleDiameter/4);
		TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row*(circleDiameter+5) + circleDiameter/4);
		translateTransition.setOnFinished(event -> {

			isAllowedToInsert=true;
			if(gameEnded(currentRow,column)){
				gameOver();
				return;

			}

			isPlayerOneTurn=!isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn? playerOne:playerTwo);
		});
		translateTransition.play();
	}

	private boolean gameEnded(int row,int column){

		List<Point2D> verticalLine= IntStream.rangeClosed(row-3,row+3)
										.mapToObj(r-> new Point2D(r,column))
											.collect(Collectors.toList());

		List<Point2D> horizontalLine= IntStream.rangeClosed(column-3,column+3)
				.mapToObj(c-> new Point2D(row,c))
				.collect(Collectors.toList());

		Point2D startPoint1= new Point2D(row-3,column+3);
		List<Point2D> diagonalLine1= IntStream.rangeClosed(0,6).
										mapToObj(i-> startPoint1.add(i,-i)).
											collect(Collectors.toList());

		Point2D startPoint2= new Point2D(row-3,column-3);
		List<Point2D> diagonalLine2= IntStream.rangeClosed(0,6).
				mapToObj(i-> startPoint2.add(i,i)).
				collect(Collectors.toList());

		boolean isEnded= checkCombination(verticalLine)|| checkCombination(horizontalLine)
										|| checkCombination(diagonalLine1) || checkCombination(diagonalLine2);
		return isEnded;
	}

	private boolean checkCombination(List<Point2D> points) {

		int chain = 0;

		for (Point2D point : points) {
			int rowIndexForArray = (int) point.getX();
			int columnIndexForArray = (int) point.getY();

			Disc disc = getDiscIfPresent(rowIndexForArray,columnIndexForArray);
			if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
				chain++;
				if (chain == 4) {
					return true;
				}
				} else {
					chain = 0;
				}
			}
		return false;
	}


	private Disc getDiscIfPresent(int row, int column){

		if(row<0 || row>=rows || column<0 || column>=columns)
			return null;

		return insertedDiscArray[row][column];

	}

	private void gameOver(){
		String winner = isPlayerOneTurn ? playerOne : playerTwo;

			System.out.println("The winner is: "+ winner);

		Alert alert= new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four");
		alert.setHeaderText("The winner is " + winner);
		alert.setContentText("Want to play again");

		ButtonType yesBtn= new ButtonType("Yes");
		ButtonType noBtn= new ButtonType("No,Exit");
		alert.getButtonTypes().setAll(yesBtn,noBtn);

		Platform.runLater(()->{
			Optional <ButtonType> btnClicked =alert.showAndWait();
			if(btnClicked.isPresent()&&btnClicked.get()== yesBtn){

				resetGame();
			}else{
				Platform.exit();
				System.exit(0);
			}
		});

		}

	public void resetGame() {
		insertedDiscsPane.getChildren().clear();
		for (int row = 0; row < insertedDiscArray.length ; row++) {
			for (int col = 0; col < insertedDiscArray[row].length ; col++) {
				insertedDiscArray[row][col]=null;
			}
		}
		isPlayerOneTurn= true;
		playerNameLabel.setText(playerOne);

		createPlayground();
	}

	private static class Disc extends Circle{

		private  final boolean isPlayerOneMove;
		public Disc(boolean isPlayerOneMove){
			this.isPlayerOneMove=isPlayerOneMove;
			setRadius(circleDiameter/2);
			setFill(isPlayerOneMove? Color.valueOf(discColor1): Color.valueOf(discColor2));
			setCenterX(circleDiameter/2);
			setCenterY(circleDiameter/2);

		}


	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}

/*
 * WhackACrab.java
 * Wed Sep-4-2019
 * @author Amanda Sproha
 * 
 * A game that allows the user to click on randomly appearing
 * crabs while a timer counts down and the score is kept
 * 
 */


//Imports for code
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WhackACrab extends Application 
{
	private int nRows = 5;
	private int nCols = 6;
	private Scene scene;
	private Button[][] crabs;
	private BorderPane rootPane;
	private FlowPane controlPane;
	private GridPane gamePane;
	private Timeline timerCount;
	private Timeline inTimer;
	private double crabCount;
	private int currentTime;
	int tempTime;
	private Label lblTime;
	private Label lblScore;
	private Button bnPlay;
	private ImageView imgCrab;
	private int[] bnLoc = new int[2];
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		setupPanes();
		setupControl();

		disableButtons(false);

		scene = new Scene(rootPane, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//Sets up the three panes: BorderPane, FlowPane, GridPane
	public void setupPanes() 
	{
		rootPane = new BorderPane();
		controlPane = new FlowPane();
		gamePane = new GridPane();

		gamePane.setLayoutX(70);
		gamePane.setLayoutY(60);
		gamePane.setHgap(30);
		gamePane.setVgap(15);

		rootPane.getChildren().add(controlPane);
		rootPane.getChildren().add(gamePane);

		setupButtons();
	}

	//Sets up the "Play" button
	//Adds the Labels to the screen
	public void setupControl()
	{
		bnPlay = new Button();
		bnPlay.setText("Play");
		bnPlay.setOnAction(e -> {
			if (bnPlay.getText() == "Play")
			{
				play();
				bnPlay.setText("Pause");
				disableButtons(true);
			}
			else if (bnPlay.getText() == "Pause")
			{
				pause();
				bnPlay.setText("Resume");
			}
			else if (bnPlay.getText() == "Resume")
			{
				resume();
				bnPlay.setText("Pause");
			}
		});

		bnPlay.setLayoutX(0);
		bnPlay.setLayoutY(0);
		controlPane.getChildren().add(bnPlay);

		lblScore = new Label("Score: " + crabCount); 

		controlPane.getChildren().add(lblScore);
		currentTime = 15;
		lblTime = new Label("Time: " + currentTime);
		controlPane.getChildren().add(lblTime);
	}

	//Creates an array of buttons 
	//Adds the buttons to the gridpane
	//Event handeler allows player to click on buttons
	public void setupButtons() 
	{	
		imgCrab = new ImageView("Crab.gif");		
		crabs = new Button[nRows][nCols];
		for (int r = 0; r < nRows; ++r)
		{
			for (int c = 0; c < nCols; ++c)
			{
				crabs[r][c] = new Button();
				int[] loc = new int[2];
				loc[0] = r;
				loc[1] = c;
				crabs[r][c].setMinSize(70, 70);
				gamePane.add(crabs[r][c], r, c);


				crabs[r][c].setOnAction(e -> {
					if (loc[0] == bnLoc[0] && loc[1] == bnLoc[1])
					{
						crabCount += 1;
						lblScore.setText("Score:" + crabCount);
					}
					else 
					{
						crabCount -= 0.5;
						lblScore.setText("Score: " + crabCount);
					}
				});
			}
		}
	}

	//Creates a Timer for the game
	//Keeps track of intervals so a crab can be added
	public void setupTimers()
	{
		timerCount = new Timeline(new KeyFrame(Duration.millis(1000), 
				e-> {
					--currentTime;
					lblTime.setText("Time: " + currentTime);
					if (currentTime <= 0) {
						timerCount.stop();
						stop();
					}
				}));
		timerCount.setCycleCount(Animation.INDEFINITE);
		timerCount.play();

		inTimer = new Timeline(new KeyFrame(Duration.millis(1000), 
				e ->  {
					randomLoc();
					if (currentTime == 0)
						inTimer.stop();

				}));
		inTimer.setCycleCount(Animation.INDEFINITE);
		inTimer.play();
	}

	//Picks a random location to add a crab to 
	public void randomLoc()
	{
		Random random = new Random();
		bnLoc[0] = random.nextInt(nRows);
		bnLoc[1] = random.nextInt(nCols);

		crabs[bnLoc[0]][bnLoc[1]].setGraphic(imgCrab);
	}

	//Stops the user from clicking on a button
	public void disableButtons(boolean game) 
	{
		for (int r = 0; r < nRows; ++r)
		{
			for (int c = 0; c < nCols; ++c)
			{
				if (game == false)
					crabs[r][c].setDisable(true);
				else {
					crabs[r][c].setDisable(false);
				}
			}
		}
	}


	//Starts the game
	public void play()
	{
		currentTime = 15;
		setupTimers();
		crabCount = 0;
		lblScore.setText("Score: " + crabCount);

	}

	//Allows the game to be paused
	public void pause()
	{
		timerCount.pause();
		inTimer.pause();
		disableButtons(false);
	}

	//Allows the game to be resumed 
	public void resume()
	{
		timerCount.play();
		inTimer.play();
		disableButtons(true);
	}

	//Ends the game
	public void stop()
	{		
		disableButtons(false);
		bnPlay.setText("Play");

	}

	public static void main(String[] args)
	{
		launch(args);
	}


}

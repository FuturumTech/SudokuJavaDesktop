package sudoku.userinterface;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sudoku.constants.GameState;
import sudoku.problemDomain.Cordinates;
import sudoku.problemDomain.SudokuGame;

import java.awt.*;
import java.util.HashMap;

/**
 * Control the window
 */
public class UserInterfaceImpl implements IUserInterfaceContract.UserView, EventHandler<KeyEvent> {

    //JavaFx containers:
    private final Stage stage;
    private final Group root;

    //Tracking 81 (9x9) different text field using HashMap resulting in O(1) time complexity
    private HashMap<Cordinates, SudokuTextField> textFieldCoordinates;

    //Controller/presenter - passing value between frontend and backend:
    private IUserInterfaceContract.EventListener listener;

    //For single IU screen it is ok to initialise a style information
    //if more than one screen, move to a class

    //size of the window
    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    //distance between window and board
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_AND_Y = 576;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 145, 140);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(225,221, 240);

    private static final String SUDOKUTITLE = "Sudoku";

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initialiseUserInterFace();
    }

    private void initialiseUserInterFace() {
        drawBackground();
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    private void drawBackground() {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    /**
     * 1. Draw each TextField based on x and y values.
     * 2. Add TextField condinates (x,y) to the HashMap
     * @param root
     */
    private void drawTextFields(Group root) {
        //start position for drawing numbers
        final int xOrigin = 50;
        final int yOrigin = 50;

        //increment value for each loop
        final int xAndYDelta = 64;

        //O(n^2) Runtime Complexity - 9x9 = 81 in this case
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int x = xOrigin +xIndex * xAndYDelta;
                int y = yOrigin + yIndex * xAndYDelta;
                //draw it
                SudokuTextField title = new SudokuTextField(xIndex, yIndex);

                //style information
                styleSudokuTile(title, x, y);

                //listening on user input, implements EventHandler<ActionEvent>
                //passing "this" - current instance of UserInterFaceImpl will result
                //in invoking handle(ActionEvent actionEvent) method
                title.setOnKeyPressed(this);

                textFieldCoordinates.put(new Cordinates(xIndex, yIndex), title);

                root.getChildren().add(title);
            }
        }
    }

    /**
     * Helper method for styling a sudoku title number
     * @param title
     * @param x
     * @param y
     */
    private void styleSudokuTile(SudokuTextField title, int x, int y) {
        Font numberFont = new Font (32);

        title.setFont(numberFont);
        title.setAlignment(Pos.CENTER);

        title.setLayoutX(x);
        title.setPrefHeight(64);
        title.setPrefWidth(64);
        title.setBackground(Background.EMPTY); //transparent
    }

    /**
     * Title that goes below sudoku game
     * @param root
     */
    private void drawTitle(Group root) {
        Text title = new Text(235, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);

    }

    /**
     * Background of sudokuboard, offset from the window by BOARD_PADDING
     * @param root
     */
    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);
        boardBackground.setFill(BOARD_BACKGROUND_COLOR);
        root.getChildren().add(boardBackground);
    }

    /**
     * Using starting x (grow from left to right) and y (grow from top to bottom) offset value
     * to draw lines for sudoku grid
     * Each square will be 64x64 so this number is added each time
     * @param root
     */
    private void drawGridLines(Group root) {
        int xAndY = 114;
        int index = 0;
        while (index < 8) {
            int gridLineThickness;
            if (index == 2 || index == 5) gridLineThickness = 3; //for separating each 3x3 box
            else gridLineThickness = 2;

            Rectangle verticalLine = getLine(
                    xAndY + 64 * index,
                    BOARD_PADDING,
                    BOARD_X_AND_Y,
                    gridLineThickness
             );
            Rectangle horizontalLine = getLine(
                    BOARD_PADDING,
                    xAndY + 64 * index,
                    gridLineThickness,
                    BOARD_X_AND_Y
            );
            root.getChildren().addAll(
                    verticalLine,
                    horizontalLine
            );
            index++;
        }
    }
     /**
     * Method to reduce repetitious code.
     *
     * X, Y, Height, Width,
     * @return A Rectangle to specification
     */
    private Rectangle getLine(double x,
                              double y,
                              double height,
                              double width) {
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setX(y);
        line.setHeight(height);
        line.setWidth(width);
        line.setFill(Color.BLACK);
        return line;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getText().matches("[0-9]")) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField) event.getSource()).setText("");
            }
        }
        event.consume(); //not to propage through the rest of the application
    }

    private void handleInput(int value, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {

    }

    /**
     * Update UI each time the user enter data (which can be 0 to delete a number)
     * @param x
     * @param y
     * @param input
     */
    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField title = textFieldCoordinates.get(new Cordinates(x,y));
        String value = Integer.toString(input);

        if(value.equals("0")) value = "";

        title.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for(int xIndex = 0; xIndex <9; xIndex++){
            for (int yIndex = 0; yIndex < 9; yIndex++){
                TextField tile = textFieldCoordinates.get(new Cordinates(xIndex, yIndex));

                String value = Integer.toString(
                        game.getCopyOfGridState()[xIndex][yIndex] //immutable copy
                );
                if(value.equals("0")) value = "";

                tile.setText(value);

                if(game.getGameState() == GameState.NEW){
                    if(value.equals("")) {
                        //using CSS to highlight empty squares
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    }else{
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                    }
                }
            }
        }
    }

    /**
     * When game logic indicates that the game completed properly, alert is shown
     * @param message
     */
    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);

        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }
}

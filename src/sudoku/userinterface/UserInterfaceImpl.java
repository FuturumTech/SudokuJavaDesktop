package sudoku.userinterface;

import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sudoku.problemDomain.Cordinates;
import sudoku.problemDomain.SudokuGame;

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
    private IUserInterfaceContract.EventListenr listener;

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
        drawBackGround(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();

    }
    private void drawTextFields(Group root) {
    }
    private void drawTitle(Group root) {
    }

    private void drawSudokuBoard(Group root) {
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
                              int width) {
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setX(y);
        line.setHeight(height);
        line.setWidth(width);
        line.setFill(Color.BLACK);
        return line;
    }

    private void drawBackGround(Group root) {

    }
    

    @Override
    public void handle(KeyEvent keyEvent) {

    }

    @Override
    public void setListener(IUserInterfaceContract.EventListenr listener) {

    }

    @Override
    public void updateSquare(int x, int y, int input) {

    }

    @Override
    public void updateBoard(SudokuGame game) {

    }

    @Override
    public void showDialog(String message) {

    }

    @Override
    public void showError(String message) {

    }
}

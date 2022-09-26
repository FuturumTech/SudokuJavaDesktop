package sudoku.userinterface;

import sudoku.problemDomain.SudokuGame;

public interface IUserInterfaceContract {
    interface EventListener{
        void onSudokuInput(int x, int y, int input);
        void onDialogClick();
    }

    interface UserView{
        void setListener(IUserInterfaceContract.EventListener listener);
        //update a single square after user input
        void updateSquare(int x, int y, int input);
        //update the entire board, such as after game completion or initial execution of the program
        void updateBoard(SudokuGame game);
        void showDialog(String message);
        void showError(String message);

    }
}

package sudoku.problemDomain;

import java.io.IOException;

public interface IStorage {
    void updateGameDate(SudokuGame game) throws IOException;
    SudokuGame getGameDate() throws IOException;
}

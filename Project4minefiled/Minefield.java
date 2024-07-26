import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Minefield {
    /**
     Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    private  int rows,cols,flags;

    private boolean mines = false;

    private Cell[][] board;


    /*
     * Class Variable Section
     *
     */

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     *
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */


    public Minefield(int rows, int columns, int flags) {
        this.rows = rows;
        this.cols = columns;
        this.flags = flags;

        board = new Cell[rows][cols];
        for(int i = 0; i < rows;i++){
            for(int j = 0; j < cols;j++){
                board[i][j] = new Cell(false,"-");
            }
        }

    }


    /**
     * evaluateField
     *
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     *
     */
    public void evaluateField() {
        int countMines = 0;

        for(int i = 0; i < rows;i++){
            for( int j = 0; j < cols; j++){
                countMines = 0;
                if(!board[i][j].getStatus().equals("M")){
                    for(int x = -1; x< 2; x++){
                        for(int y = -1; y< 2; y++){ // check if we need a case for when x andd y are = 0 since thats a mine
                            if(i+x>= 0 && j+y >= 0&& x+i< rows && j+y< cols ){ // checking to make sure your in bounds
                                if(board[i+x][j+y].getStatus().equals("M")){
                                    countMines++;
                                }
                            }
                        }
                    }
                    board[i][j].setStatus(Integer.toString(countMines)); // places number on board
                }
            }
        }
    }

    /**
     * createMines
     *
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {

        Random rand = new Random();

        while(mines> 0){
            int randRow = rand.nextInt(rows); // rand rows
            int randCol = rand.nextInt(cols); // rand col

            if (randRow == x && randCol == y) {  // for the case that the random mine is repeated
                continue; // skip it and keep iterating
            }
            if(!board[randRow][randCol].getStatus().equals("M") && !board[randRow][randCol].getRevealed()){ // if coordinate not equal to  mine then set it to mine
                board[randRow][randCol].setStatus("M");
            }
            mines--;

        }

    }

    /**
     * guess
     *
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if(inBounds(x,y)){// in bounds
            Cell pickedCell = board[x][y];

            if(flag){
                if(flags> 0 ){
                    pickedCell.setStatus("F"); // set status
                    pickedCell.setRevealed(true);
                    flags--;
                }
                else {
                    System.out.println("Out of Flags");
                }
            }
            else if (pickedCell.getStatus().equals("0")) {
                revealZeroes(x,y);
            }
            else if (pickedCell.getStatus().equals("M")) {
                pickedCell.setRevealed(true);
                return true;
            }
            pickedCell.setRevealed(true);
        }
        return false;
    }

    /**
     * gameOver
     *
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */


    public boolean gameOver() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!board[i][j].getRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean inBounds(int x, int y){
        if(x>= 0 && y >= 0 && x < rows && y < cols){
            return true;
        }
        return false;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen <int []> zeroStack = new Stack1Gen<>();
        zeroStack.push(new int[]{x,y});
        while(!zeroStack.isEmpty()) {
            int[] coordinates = zeroStack.pop();
            int curX = coordinates[0];
            int curY = coordinates[1];
            if (inBounds(x, y)) {

                board[curX][curY].setRevealed(true);

                if (inBounds(x - 1, y) &&
                        !board[x - 1][y].getRevealed() &&
                        board[x - 1][y].getStatus().equals("0")) {
                    zeroStack.push(new int[]{x - 1, y});
                }


                if (inBounds(x + 1, y) &&
                        !board[x + 1][y].getRevealed() &&
                        board[x + 1][y].getStatus().equals("0")) {
                    zeroStack.push(new int[]{x + 1, y});

                }


                if (inBounds(x, y + 1) &&
                        !board[x][y + 1].getRevealed() &&
                        board[x][y + 1].getStatus().equals("0")) {
                    zeroStack.push(new int[]{x, y + 1});

                }

                if (inBounds(x, y - 1) &&
                        !board[x][y - 1].getRevealed() &&
                        board[x][y - 1].getStatus().equals("0")) {
                    zeroStack.push(new int[]{x, y - 1});

                }


            }
        }


    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> startArea = new Q1Gen<>();
        startArea.add(new int[]{x, y});
        while (startArea.length() != 0) {
            int[] coordinates = startArea.remove();
            int curX = coordinates[0];
            int curY = coordinates[1];
            board[curX][curY].setRevealed(true);
            if (board[x][y].getStatus().equals("M")) {
                    break;
                }
                if (inBounds(x - 1, y) && !board[x - 1][y].getRevealed()) {
                    startArea.add(new int[]{x - 1, y});

                }
                if (inBounds(x + 1, y) && !board[x + 1][y].getRevealed()) {
                    startArea.add(new int[]{x + 1, y});
                }

                if (inBounds(x, y + 1) && !board[x][y + 1].getRevealed()) {
                    startArea.add(new int[]{x, y + 1});

                }

                if (inBounds(x, y - 1) && !board[x][y - 1].getRevealed()) {
                    startArea.add(new int[]{x, y - 1});
                }
            }
        }


    /**
     * For both printing methods utilize the ANSI colour codes provided!
     *
     *
     *
     *
     *
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void debug() {
        for(int i = 0; i<rows;i++){
            for(int j = 0; j < cols; j++){
                System.out.print(board[i][j].getStatus()+ " ");
            }
            System.out.println();
        }

    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {

        StringBuilder boardOutput = new StringBuilder();
        boardOutput.append("  ");
        for(int i = 0; i < cols;i++){
            boardOutput.append(ANSI_GREY_BACKGROUND + i + " ");
        }
        boardOutput.append("\n");

        for(int i = 0; i<rows;i++){
            for(int j = 0; j < cols; j++){
                if(j==0){
                    boardOutput.append(ANSI_GREY_BACKGROUND + (i) + " ");

                }
                if(board[i][j].getRevealed()) {

                    if (board[i][j].getStatus().equals("M")) {
                        boardOutput.append(ANSI_RED_BRIGHT + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("F")) {
                        boardOutput.append(ANSI_YELLOW + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("0")) {
                        boardOutput.append(ANSI_YELLOW + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    } else if (board[i][j].getStatus().equals("1")) {
                        boardOutput.append(ANSI_BLUE + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    } else if (board[i][j].getStatus().equals("2")) {
                        boardOutput.append(ANSI_GREEN + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    } else if (board[i][j].getStatus().equals("3")) {
                        boardOutput.append(ANSI_PURPLE + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("4")) {
                        boardOutput.append(ANSI_CYAN + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("5")) {
                        boardOutput.append(ANSI_YELLOW_BRIGHT + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("6")) {
                        boardOutput.append(ANSI_BLUE_BRIGHT + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("7")) {
                        boardOutput.append(ANSI_RED_BRIGHT + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("8")) {
                        boardOutput.append(ANSI_GREEN + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if (board[i][j].getStatus().equals("9")) {
                        boardOutput.append(ANSI_PURPLE + board[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    boardOutput.append(" ");
                }
                else {
                    boardOutput.append("- ");
                }
            }
            boardOutput.append("\n");
        }

        return boardOutput.toString();
    }
}
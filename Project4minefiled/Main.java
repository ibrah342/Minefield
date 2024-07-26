//Import Section

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 *
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

import java.util.Scanner;

public class Main {
    /**
     *
     * Main Method used to play mind sweeper
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Minefield minefield = null;

        System.out.println("Choose Game Mode!!");
        System.out.println("Options:");
        System.out.println("1: easy");
        System.out.println("2: medium");
        System.out.println("3: hard");
        int input = scan.nextInt();
        if (input == 1) {
            minefield = new Minefield(5, 5, 5);
            System.out.println(minefield);
            System.out.println("Enter X cor to start:");
            int x = scan.nextInt();
            System.out.println("Enter Y cor to start:");
            int y = scan.nextInt();
            minefield.createMines(x, y, 5);
            minefield.evaluateField();
            minefield.revealStartingArea(x, y);
        }
        if (input == 2) {
            minefield = new Minefield(9, 9, 12);
            System.out.println(minefield);
            System.out.println("Enter X cor to start:");
            int x = scan.nextInt();
            System.out.println("Enter Y cor to start:");
            int y = scan.nextInt();
            minefield.createMines(x, y, 12);
            minefield.evaluateField();
            minefield.revealStartingArea(x, y);

        }
        if (input == 3) {
            minefield = new Minefield(20, 20, 40);
            System.out.println(minefield);
            System.out.println("Enter X cor to start:");
            int x = scan.nextInt();
            System.out.println("Enter Y cor to start:");
            int y = scan.nextInt();
            minefield.createMines(x, y, 40);
            minefield.evaluateField();
            minefield.revealStartingArea(x, y);
        }
        System.out.println("Enter 0 for debug mode or any number to skip");
        int input4 = scan.nextInt();
        if (input4 == 0) {
            minefield.debug();
        }

//        int input2 = scan.nextInt();
//        if(input== 0) {
//            Minefield minefield = new Minefield(20, 20, 40);
//            System.out.println(minefield);
//        }
        while (!minefield.gameOver()) {
            Scanner guess = new Scanner(System.in);
            System.out.println(minefield.toString());
            System.out.println("Put in X coordinate");
            int x = scan.nextInt();
            System.out.println("Put in Y coordinate");
            int y = scan.nextInt();
            if(minefield.inBounds(0, 20)){
                System.out.println("Out of bounds");
            }
            System.out.println("Enter 1 for Flag or Enter 2 to reveal");
            int input3 = scan.nextInt();
            if (input3 == 1) { // make it so it checks if debug == input4
                minefield.guess(x, y, true);
                minefield.debug();
                System.out.println(minefield);
            } else if (input3 == 2) {
                if (minefield.guess(x, y, false)) {
                    System.out.println("You hit a mine! YOU LOSE");
                    System.out.println(minefield);
                    break;
                }


            } else{
                minefield.debug();
                System.out.println(minefield);
                System.out.println("You Win!!!");
            }

        }
    }
}



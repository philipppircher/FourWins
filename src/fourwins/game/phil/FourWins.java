package fourwins.game.phil;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class FourWins {
    // Member variables
    //
    private static final int MAXROWELEMENTS = 6;
    private static final int MAXCOLUMNELEMENTS = 7;
    private static final char SIGNPLAYER = 'X';
    private static final char SIGNCOMPUTER = 'O';
    private static final String NAMEPLAYER = "Spieler";
    private static final String NAMECOMPUTER = "Computer";
    private static char[][] fields = new char[MAXROWELEMENTS][MAXCOLUMNELEMENTS];

    public static void main(String[] args) {
        startNewGame();
    }

    private static void initFields() {
        for (int i = 0; i < MAXROWELEMENTS; i++) {
            for (int j = 0; j < MAXCOLUMNELEMENTS; j++) {
                fields[i][j] = ' ';
            }
        }
    }

    private static void drawFields() {
        System.out.println(" 0 1 2 3 4 5 6 ");
        for (int i = 0; i < MAXROWELEMENTS; i++) {
            for (int j = 0; j <= MAXCOLUMNELEMENTS; j++) {

                if (j != MAXCOLUMNELEMENTS)
                    System.out.print("|" + fields[i][j]);
                else
                    System.out.println("|");
            }
        }
    }

    private static void startNewGame() {
        final boolean ISQUIT = false;       // Endlossloop
        boolean isPlayerOnTurn = true;      // Player starts with first turn

        System.out.println("Willkommen bei Vier gewinnt");
        initFields();
        do {
            drawFields();

            if (isPlayerOnTurn) {
                playerPlaceSign();
                //checkGameWinnerOrDraw(SIGNPLAYER, NAMEPLAYER);
                isPlayerOnTurn = false;
            } else {
                computerPlaceSign();
                //checkGameWinnerOrDraw(SIGNCOMPUTER, NAMECOMPUTER);
                isPlayerOnTurn = true;
            }

        } while (!ISQUIT);  // Endlessloop!
    }

    private static void playerPlaceSign() {
        int column = 0;
        boolean isSignSetToFields = false;

        do {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println(NAMEPLAYER + " ist dran");
                System.out.print("Gib Spalte ein (0 bis " + (MAXCOLUMNELEMENTS - 1) + "): ");
                column = sc.nextInt();

                if ((column < 0 || column > MAXCOLUMNELEMENTS - 1)) {
                    System.err.println("Wert liegt außerhalb 0 bis " + (MAXCOLUMNELEMENTS - 1) +
                            "! Bitte nur Werte zwischen 0 bis " + MAXCOLUMNELEMENTS + " eingeben\n");
                } else {
                    isSignSetToFields = checkPlaceAndSet(column, SIGNPLAYER, NAMEPLAYER);
                }
            } catch (InputMismatchException e) {
                System.err.println("Ungültiger Buchstabenwert! Bitte nur Werte zwischen 0 bis "
                        + MAXCOLUMNELEMENTS + " eingeben\n");
            }
        } while (!isSignSetToFields);
    }

    private static void computerPlaceSign() {
        computerPlaceRandomSign();
    }

    private static void computerPlaceRandomSign() {
        int column;
        Random rand = new Random();
        do {
            column = rand.nextInt(MAXCOLUMNELEMENTS);
        } while (!checkPlaceAndSet(column, SIGNCOMPUTER, NAMECOMPUTER));
        System.out.println(NAMECOMPUTER + " ist dran, setzt auf Spalte " + column);
    }

    private static boolean checkPlaceAndSet(final int COLUMN, final char SIGN, final String NAME) {
        boolean isSignPlaced = false;
        if (fields[0][COLUMN] == ' ') {     // Check top (row 0) of column is free -> set bottom with SIGN
            for (int i = MAXROWELEMENTS - 1; i >= 0; i--) {
                if (fields[i][COLUMN] == ' ') {
                    fields[i][COLUMN] = SIGN;
                    isSignPlaced = true;
                    checkGameWinnerOrDraw(COLUMN, SIGN, NAME);
                    break;
                }
            }
        } else if (NAME.equals("Spieler"))
            System.out.println("Spalte " + COLUMN + " ist voll");
        return isSignPlaced;
    }

    private static boolean checkAllColumnsAreFull() {
        if ((fields[0][0] != ' ') && (fields[0][1] != ' ') && (fields[0][2] != ' ') && (fields[0][3] != ' ') &&
                (fields[0][4] != ' ') && (fields[0][5] != ' ') && (fields[0][6] != ' ')) {
            System.out.println("Unentschieden! Alle Felder sind voll. Spiel wird neugestartet");
            return true;
        }
        return false;
    }

    private static void checkGameWinnerOrDraw(final int COLUMN, final char SIGN, final String NAME) {
        if (checkIsFourInLineVertical(COLUMN, SIGN) || checkIsFourInLineHorizontal(COLUMN, SIGN) ||
                checkIsFourInLineDiagonal(COLUMN, SIGN) || checkAllColumnsAreFull()) {
            System.out.println("Gewonnen!!! " + NAME + " ist Sieger");
            drawFields();
            startNewGame();
        }
    }

    private static boolean checkIsFourInLineVertical(final int COLUMN, final char SIGN) {
        boolean isWinner = false;
        int countSignsToWin = 0;

        for (int i = MAXROWELEMENTS - 1; i >= 0; i--) {
            if (fields[i][COLUMN] == SIGN) {
                countSignsToWin++;
                if (countSignsToWin == 4) {
                    isWinner = true;
                    break;  // Exit for-loop and return isWinner
                }
            } else {
                countSignsToWin = 0;
            }
        }
        return isWinner;
    }

    private static boolean checkIsFourInLineHorizontal(final int COLUMN, final Character SIGN) {
        boolean isWinner = false;
        int countSignsToWin = 0;

        for (int i = MAXROWELEMENTS - 1; i >= 0; i--) {
            for (int j = MAXCOLUMNELEMENTS - 1; j >= 0; j--) {
                if (fields[i][j] == SIGN) {
                    countSignsToWin++;
                    if (countSignsToWin == 4) {
                        isWinner = true;
                        break;      // Exit for-loop and return isWinner
                    }
                } else {
                    countSignsToWin = 0;
                }
            }
        }
        return isWinner;
    }


    private static boolean checkIsFourInLineDiagonal(final int COLUMN, final Character SIGN) {
        boolean isWinner = false;

        // DIAGONAL Row[0]Column[0] to Row[5]Column[6] (LEFT UP TO DOWN RIGHT)
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 3; j++) {
                if (fields[i][j] == SIGN && fields[i + 1][j + 1] == SIGN
                        && fields[i + 2][j + 2] == SIGN && fields[i + 3][j + 3] == SIGN) {
                    isWinner = true;
                }
            }
        }
        // DIAGONAL Row[0]Column[6] to Row[5]Column[0] (RIGHT UP TO DOWN LEFT)
        for (int i = 0; i <= 2; i++) {
            for (int j = 6; j >= 3; j--) {
                if (fields[i][j] == SIGN && fields[i + 1][j - 1] == SIGN
                        && fields[i + 2][j - 2] == SIGN && fields[i + 3][j - 3] == SIGN) {
                    isWinner = true;
                }
            }
        }
        return isWinner;
    }
}





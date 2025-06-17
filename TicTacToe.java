package Java;

import java.util.Scanner;
import java.util.Random;

class TicTacToe {
    private static final char PLAYER_X = 'X'; // Symbol für Spieler 1
    private static final char PLAYER_O = 'O'; // Symbol für Spieler 2/Bot
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static int playerXWins = 0; // Anzahl der Siege von Spieler X
    private static int playerOWins = 0; // Anzahl der Siege von Spieler O/Bot

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            char[][] board = {
                    { ' ', ' ', ' ' },
                    { ' ', ' ', ' ' },
                    { ' ', ' ', ' ' }
            };

            clearConsole(); // Konsole leeren

            System.out.print("Möchten Sie eine Spielerklärung sehen? (j/n): ");
            String showInstructions = scanner.next().toLowerCase();
            if (showInstructions.equals("j")) {
                printInstructions(); // Spielerklärung ausgeben
            }

            printBoard(board); // Leeres Spielfeld ausgeben

            System.out.print("1 oder 2 Spieler: ");
            int playerCount = scanner.nextInt();

            if (playerCount == 1) {
                playGame(board, scanner, true); // Spieler gegen Bot
            } else if (playerCount == 2) {
                playGame(board, scanner, false); // Zwei Spieler gegeneinander
            } else {
                System.out.println("Ungültige Option");
                continue;
            }

            System.out.println("\nSpielstand:");
            System.out.println("Spieler 1 (X) hat " + playerXWins + " Mal gewonnen.");
            System.out.println("Spieler 2 (O) hat " + playerOWins + " Mal gewonnen.");

            System.out.print("\nMöchten Sie nochmals spielen? (j/n): ");
            String response = scanner.next().toLowerCase();
            if (!response.equals("j")) {
                break;
            }
        }

        scanner.close();

        // Gesamtsieger ausgeben
        if (playerXWins > playerOWins) {
            System.out.println("Spieler 1 (X) hat das Spiel gewonnen!");
        } else if (playerOWins > playerXWins) {
            System.out.println("Spieler 2 (O) hat das Spiel gewonnen!");
        } else {
            System.out.println("Das Spiel endete unentschieden.");
        }
    }

    // Methode zur Ausgabe der Spielerklärung
    public static void printInstructions() {
        System.out.println("Willkommen zu Tic Tac Toe!");
        System.out.println(
                "Ziel des Spiels ist es, drei eigene Symbole in einer Reihe, Spalte oder Diagonale zu platzieren.");
        System.out.println("Spieler 1 verwendet das Symbol 'X', Spieler 2 (oder der Bot) verwendet das Symbol 'O'.");
        System.out.println("Um einen Zug zu machen, geben Sie die Koordinaten des gewünschten Feldes ein (z.B. A1).");
        System.out.println("Viel Spass beim Spielen!\n");
    }

    // Methode zur Löschung der Konsole
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Methode zur Steuerung des Spielablaufs
    public static void playGame(char[][] board, Scanner scanner, boolean singlePlayer) {
        Random rand = new Random();
        boolean playerXTurn = true;
        long startTime = System.currentTimeMillis(); // Startzeit des Spiels

        while (true) {
            clearConsole();
            printBoard(board);
            if (playerXTurn) {
                playerMove(board, scanner, PLAYER_X); // Zug von Spieler X
            } else {
                if (singlePlayer) {
                    botMove(board, rand, PLAYER_O); // Zug vom Bot
                } else {
                    playerMove(board, scanner, PLAYER_O); // Zug von Spieler O
                }
            }

            char winner = isWinner(board, playerXTurn ? PLAYER_X : PLAYER_O);
            if (winner != ' ') { // Überprüfen, ob jemand gewonnen hat
                clearConsole();
                printBoardWithWinnerHighlighted(board, winner); // Spielfeld mit grüner Gewinner-Reihe ausgeben
                if (playerXTurn) {
                    System.out.println("Spieler 1 (X) hat gewonnen!");
                    playerXWins++; // Anzahl der Siege von Spieler X erhöhen
                } else {
                    System.out.println(singlePlayer ? "Bot (O) hat gewonnen!" : "Spieler 2 (O) hat gewonnen!");
                    playerOWins++; // Anzahl der Siege von Spieler O/Bot erhöhen
                }
                long endTime = System.currentTimeMillis(); // Endzeit des Spiels
                long duration = (endTime - startTime) / 1000; // Dauer des Spiels in Sekunden
                System.out.println("\nDieses Spiel hat " + duration + " Sekunden gedauert.");
                break;
            }

            if (isBoardFull(board)) { // Überprüfen, ob das Spielfeld voll ist (Unentschieden)
                clearConsole();
                printBoard(board);
                System.out.println("Unentschieden!");
                long endTime = System.currentTimeMillis(); // Endzeit des Spiels
                long duration = (endTime - startTime) / 1000; // Dauer des Spiels in Sekunden
                System.out.println("\nDieses Spiel hat " + duration + " Sekunden gedauert.");
                break;
            }

            playerXTurn = !playerXTurn; // Wechsel des Spielerzugs
        }
    }

    // Methode zur Eingabe des Spielerzugs
    public static void playerMove(char[][] board, Scanner scanner, char symbol) {
        String player = (symbol == PLAYER_X) ? "Spieler 1 (X)" : "Spieler 2 (O)";
        while (true) {
            System.out.print(player + ", geben Sie Ihren Zug ein (z.B. A1): ");
            String move = scanner.next().toUpperCase();
            if (move.length() == 2 && move.charAt(0) >= 'A' && move.charAt(0) <= 'C' && move.charAt(1) >= '1'
                    && move.charAt(1) <= '3') {
                int row = move.charAt(1) - '1';
                int col = move.charAt(0) - 'A';

                if (board[row][col] == ' ') { // Überprüfen, ob das Feld frei ist
                    board[row][col] = symbol;
                    break;
                } else {
                    System.out.println("Feld bereits belegt, bitte erneut versuchen.");
                }
            } else {
                System.out.println("Ungültiger Zug, bitte erneut versuchen.");
            }
        }
    }

    // Methode zur Generierung des Bot-Zugs
    public static void botMove(char[][] board, Random rand, char symbol) {
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (board[row][col] != ' '); // Finden eines freien Feldes

        board[row][col] = symbol;
        System.out.println("Bot (O) hat seinen Zug gemacht.");
    }

    // Methode zur Überprüfung, ob ein Spieler gewonnen hat
    public static char isWinner(char[][] board, char symbol) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                    (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                return symbol;
            }
        }
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
            return symbol;
        }
        return ' ';
    }

    // Methode zur Überprüfung, ob das Spielfeld voll ist
    public static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Methode zur Ausgabe des aktuellen Spielfelds
    public static void printBoard(char[][] board) {
        System.out.println("  A   B   C");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[i][j]);
                if (j < 2)
                    System.out.print(" ║");
            }
            System.out.println();
            if (i < 2)
                System.out.println(" ═══╬═══╬═══");
        }
    }

    // Methode zur Ausgabe des Spielfelds mit grüner Gewinner-Reihe
    public static void printBoardWithWinnerHighlighted(char[][] board, char winner) {
        System.out.println("  A   B   C");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == winner && isWinningPosition(board, i, j, winner)) {
                    System.out.print(" " + ANSI_GREEN + board[i][j] + ANSI_RESET);
                } else {
                    System.out.print(" " + board[i][j]);
                }
                if (j < 2)
                    System.out.print(" ║");
            }
            System.out.println();
            if (i < 2)
                System.out.println(" ═══╬═══╬═══");
        }
    }

    // Hilfsmethode zur Überprüfung, ob eine Position Teil der gewinnenden Reihe,
    private static boolean isWinningPosition(char[][] board, int row, int col, char symbol) {
        // Überprüfe Reihe
        if (board[row][0] == symbol && board[row][1] == symbol && board[row][2] == symbol)
            return true;

        // Überprüfe Spalte
        if (board[0][col] == symbol && board[1][col] == symbol && board[2][col] == symbol)
            return true;

        // Überprüfe Hauptdiagonale
        if (row == col && board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol)
            return true;

        // Überprüfe Nebendiagonale
        if (row + col == 2 && board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)
            return true;

        return false;
    }
}
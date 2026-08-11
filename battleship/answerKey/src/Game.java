import java.util.Random;
import java.util.Scanner;

public class Game {
    final Random random = new Random();
    final Scanner scanner = new Scanner(System.in);
    final Battleship battleship = new Battleship(random);

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    void play() {
        while (true) {
            int choice;
            while (true) {
                System.out.println("What would you like to do?");
                System.out.println("(1) New Game");
                System.out.println("(2) Quit");
                System.out.print("> ");

                choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    scanner.nextLine();
                    break;
                }

                System.out.println();
            }

            if (choice == 2) {
                System.out.println("Goodbye!");
                break;
            }

            battleship.resetGame(random);
            status = "";
            while (!battleship.gameOver()) {
                playGame();
            }
        }
    }

    String status = "";
    void playGame() {
        printBoard();
        System.out.println(status);

        String pos;
        int row, col;
        while (true) {
            System.out.print("> ");
            pos = scanner.nextLine().trim().toUpperCase();
            col = pos.charAt(0) - 'A';
            if (col < 0 || col >= Battleship.BOARD_SIZE) {
                System.out.println("invalid column " + pos.charAt(0) + ". Try A4 or E10, etc");
                continue;
            }

            try {
                // we subtract one because we have to zero-index
                row = Integer.parseInt(pos.substring(1)) - 1;
            } catch (NumberFormatException e) {
                System.out.println("invalid row " + pos.substring(1) + ". Try C3 or J1, etc");
                continue;
            }

            if (row < 0 || row >= Battleship.BOARD_SIZE) {
                System.out.println("invalid row " + pos.substring(1) + ". Try F5 or B7, etc");
                continue;
            }

            break;
        }

        MoveResult result = battleship.strike(row, col);
        if (result == MoveResult.DUPLICATE) {
            status = "You already hit " + pos;
        } else if (result == MoveResult.GAME_OVER) {
            printBoard();
            System.out.println("You win!");
        } else if (result == MoveResult.HIT) {
            status = "Hit!";
        } else if (result == MoveResult.MISSED) {
            status = "Miss!";
        } else if (result == MoveResult.SUNK) {
            status = "Sunk!";
        } else {
            throw new IllegalStateException("unreachable");
        }
    }


    void printBoard() {
        System.out.println("    BATTLESHIP    ");
        // header: "  1  2  3  4  5  6 ..."
        System.out.print("  ");
        for (int i = 0; i < Battleship.BOARD_SIZE; i++) {
            System.out.printf("%-2d ", i + 1);
        }
        System.out.println();

        for (int y = 0; y < Battleship.BOARD_SIZE; y++) {
            System.out.print((char) ('A' + y));

            for (int x = 0; x < Battleship.BOARD_SIZE; x++) {
                Ship ship = battleship.getShip(x, y);
                boolean isHit = battleship.isHit(x, y);

                System.out.print(" ");
                if (ship == null) {
                    if (isHit) {
                        System.out.print("  ");
                    } else {
                        System.out.print("~~");
                    }
                } else {
                    if (battleship.isSunk(ship)) {
                        System.out.print("XX");
                    } else if (isHit) {
                        System.out.print("xx");
                    } else {
                        System.out.print("~~");
                    }
                }
            }

            System.out.println();
        }
        System.out.println("ocean(~~)  missed(  )  hit(xx)  sunk(XX)");
    }
}

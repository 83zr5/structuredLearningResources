import java.util.*;
import java.util.function.Consumer;

public class BattleshipTests {
    public static void main(String[] args) {
        testCase("getIndicesWorks", BattleshipTests::getIndicesWorks);
        testCase("placeShipWorks", BattleshipTests::placeShipWorks);
        testCase("setupWorks", BattleshipTests::setupWorks);
        testCase("isSunkWorks", BattleshipTests::isSunkWorks);
        testCase("strikeMissWorks", BattleshipTests::strikeMissWorks);
        testCase("hitAndSinkWorks", BattleshipTests::hitAndSinkWorks);
    }

    static void getIndicesWorks() {
        var game = new Battleship();

        var pos = new ShipPlacement(Ship.Carrier, 1, 3, true);
        // HEY!! try setting a breakpoint here
        var indices = game.getShipIndices(pos);
        if (!Arrays.equals(indices, new int[]{31, 41, 51, 61, 71})) {
            throw new AssertionError("expected [31, 41, 51, 61, 71], instead found " + Arrays.toString(indices));
        }

        pos = new ShipPlacement(Ship.Cruiser, 5, 6, false);
        indices = game.getShipIndices(pos);
        if (!Arrays.equals(indices, new int[]{65, 66, 67})) {
            throw new AssertionError("expected [5, 6, 7], instead found " + Arrays.toString(indices));
        }
    }

    static void placeShipWorks() {
        var game = new Battleship();

        var pos = new ShipPlacement(Ship.Carrier, 8, 0, false);
        var didPlaceShip = game.placeShip(pos);
        if (didPlaceShip) {
            throw new AssertionError("cannot place ship here, it would go out of bounds!");
        }

        pos = new ShipPlacement(Ship.Destroyer, 0, 9, true);
        didPlaceShip = game.placeShip(pos);
        if (didPlaceShip) {
            throw new AssertionError("cannot place ship here, it would go out of bounds!");
        }


        pos = new ShipPlacement(Ship.Carrier, 4, 2, false);
        didPlaceShip = game.placeShip(pos);
        if (!didPlaceShip) {
            throw new AssertionError("this should be able to be placed here!");
        }
        for (int idx : game.getShipIndices(pos)) {
            Ship ship = game.board[idx];
            if (ship != pos.ship) {
                throw new AssertionError("all parts of the ship should be CARRIER after placing a carrier!");
            }
        }

        pos = new ShipPlacement(Ship.Destroyer, 5, 1, true);
        didPlaceShip = game.placeShip(pos);
        if (didPlaceShip) {
            throw new AssertionError("this should can't be placed here, it would overlap the previous carrier!");
        }
    }

    static void setupWorks(Random random) {
        // this test should pass by default, it exists only
        // to make sure the game doesn't break while fixing the other tests
        var game = new Battleship(random);
        boolean gameOver = game.gameOver();
        if (gameOver) {
            throw new AssertionError("gameOver should be false at the start of the game!");
        }

        Map<Ship, ShipPlacement> shipPlacements = game.getShipPlacements();
        if (shipPlacements.size() != 5) {
            throw new AssertionError("there should be 5 placed ships!");
        }

        Set<Ship> floatingShips = game.getFloatingShips();
        if (floatingShips.size() != 5) {
            throw new AssertionError("there should be 5 floating ships!");
        }
    }

    static void isSunkWorks(Random random) {
        var game = new Battleship(random);
        var pos = game.getShipPlacements().values().iterator().next();
        for (int idx : game.getShipIndices(pos)) {
            game.getHit()[idx] = true;
        }

        if (!game.isSunk(pos.ship)) {
            throw new AssertionError("isSunk(ship) should return true after striking all of it's parts!");
        }
    }

    static void strikeMissWorks(Random random) {
        var game = new Battleship();
        var result = game.strike(5, 5);
        if (result != MoveResult.MISSED) {
            throw new AssertionError("striking a missed tile should return Missed");
        }

        result = game.strike(5, 5);
        if (result != MoveResult.DUPLICATE) {
            throw new AssertionError("striking a tile twice should return DUPLICATE!");
        }
    }

    static void hitAndSinkWorks() {
        var game = new Battleship();
        ShipPlacement pos1 = new ShipPlacement(Ship.Battleship, 5, 5, false);
        boolean didPlaceShip = game.placeShip(pos1);
        if (!didPlaceShip) {
            throw new AssertionError("this ship should have been placed here!");
        }

        ShipPlacement pos2 = new ShipPlacement(Ship.Cruiser, 5, 6, true);
        didPlaceShip = game.placeShip(pos2);
        if (!didPlaceShip) {
            throw new AssertionError("the cruiser should have been placed here!");
        }

        MoveResult result;
        for (int i = 0; i < pos1.ship.length - 1; i++) {
            result = game.strike(5 + i, 5);
            if (result != MoveResult.HIT) {
                throw new AssertionError("this should hit the placed battleship! instead got " + result);
            }
        }

        result = game.strike(8, 5);
        if (result != MoveResult.SUNK) {
            throw new AssertionError("this ship should have been sunk the placed battleship! instead got " + result);
        }

        for (int i = 0; i < pos2.ship.length - 1; i++) {
            result = game.strike(5, 6 + i);
            if (result != MoveResult.HIT) {
                throw new AssertionError("this should hit the placed cruiser! instead got " + result);
            }
        }
        result = game.strike(5, 8);
        if (result != MoveResult.GAME_OVER) {
            throw new AssertionError("this ship should have been sunk the placed cruiser & the game should end! instead got " + result);
        }
    }

    static void testCase(String testCaseName, Runnable testCase) {
        try {
            testCase.run();
            System.out.println("[%s] PASSED".formatted(testCaseName));
        } catch (Throwable t) {
            System.out.println("[%s] FAILED".formatted(testCaseName));
            System.out.println(t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }
    }
    static void testCase(String testCaseName, Consumer<Random> testCase) {
        Random random = new Random(42);
        try {
            testCase.accept(random);
            System.out.println("[%s] PASSED".formatted(testCaseName));
        } catch (Throwable t) {
            System.out.println("[%s] FAILED".formatted(testCaseName));
            System.out.println(t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }
    }
}

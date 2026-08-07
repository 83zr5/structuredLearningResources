import java.util.*;

public class Battleship {
    public static final int BOARD_SIZE = 10;
    protected boolean[] hit;
    protected Ship[] board;
    protected Map<Ship, ShipPlacement> ships;
    protected Set<Ship> floatingShips;

    protected boolean gameOver;

    protected Battleship() {
        gameOver = false;
        hit = new boolean[BOARD_SIZE * BOARD_SIZE];
        board = new Ship[BOARD_SIZE * BOARD_SIZE];
        ships = new HashMap<Ship, ShipPlacement>();
        floatingShips = new HashSet<Ship>();
    }

    public Battleship(Random random) {
        setupGame(random);
    }

    public void resetGame(Random random) {
        setupGame(random);
    }

    public MoveResult strike(int x, int y) {
        if (gameOver) return MoveResult.GAME_OVER;

        int idx = y * BOARD_SIZE + x;
        if (hit[idx]) {
            return MoveResult.DUPLICATE;
        }
        hit[idx] = true;

        Ship ship = board[idx];
        if (ship == null) {
            // there is no ship here
            return MoveResult.MISSED;
        }

        if (isSunk(ship)) {
            floatingShips.remove(ship);
            // TODO: if there are no ships left, we need to return GAME_OVER!
            return MoveResult.SUNK;
        }

        return MoveResult.HIT;
    }

    public boolean isSunk(Ship ship) {
        ShipPlacement pos = ships.get(ship);
        if (pos == null) {
            throw new IllegalStateException("all ships should have corresponding positions!");
        }

        // TODO: write a loop that checks if the ship is sunk
        return true;
    }

    protected void setupGame(Random random) {
        gameOver = false;
        hit = new boolean[BOARD_SIZE * BOARD_SIZE];
        board = new Ship[BOARD_SIZE * BOARD_SIZE];
        ships = new HashMap<Ship, ShipPlacement>();
        floatingShips = new HashSet<Ship>();

        for (Ship ship : Ship.values()) {
            // we try to put down each ship until we find
            // somewhere that it fits. we make sure to
            // check that it doesn't overlap other ships
            // and that it doesn't go off the board
            while (true) {
                boolean vertical = random.nextBoolean();
                int x = random.nextInt(BOARD_SIZE);
                int y = random.nextInt(BOARD_SIZE);

                ShipPlacement position = new ShipPlacement(ship, x, y, vertical);
                if (placeShip(position)) {
                    break;
                }
            }
        }
    }

    /// Tries to place a ship at `(x, y)` facing N/S (vertical=true) or E/W (vertical=false)
    /// @return `false` if unable to place the ship. Otherwise, the `this.board` is updated
    ///         to hold the ship and `true` is returned.
    protected boolean placeShip(ShipPlacement pos) {
        int[] indices = getShipIndices(pos);
        // TODO: ensure that the ship doesn't leave the board
        //   and that it does not overflow any other ships

        // TODO: write a loop to ensure that this ship doesn't
        //   overlap any other ships

        for (int idx : indices) {
            board[idx] = pos.ship;
        }
        floatingShips.add(pos.ship);
        ships.put(pos.ship, pos);

        return true;
    }

    /// Returns a set of indices into `board` / `hit` that correspond
    /// to each of the parts of the ship at the position & orientation in `pos`
    protected int[] getShipIndices(ShipPlacement pos) {
        int[] indices = new int[pos.ship.length];
        // TODO: write a loop to fill `indices` with
        //   an index for each part of the ship.
        //   Remember to take orientation into account!
        // NOTE: the index in `board` for any given (x, y)
        //   pair is `y * BOARD_SIZE + x`
        return indices;
    }


    // ==========================================
    // getters & setters, don't worry about these
    // ==========================================

    public Ship getShip(int x, int y) {
        return board[y * Battleship.BOARD_SIZE + x];
    }

    public boolean isHit(int x, int y) {
        return hit[y * Battleship.BOARD_SIZE + x];
    }

    public boolean gameOver() {
        return gameOver;
    }

    public Ship[] getBoard() {
        return board;
    }

    public boolean[] getHit() {
        return hit;
    }

    public Map<Ship, ShipPlacement> getShipPlacements() {
        return ships;
    }

    public Set<Ship> getFloatingShips() {
        return floatingShips;
    }
}

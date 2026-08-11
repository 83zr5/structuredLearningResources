
public enum Ship {
    Destroyer(2),
    Cruiser(3),
    Submarine(3),
    Battleship(4),
    Carrier(5);

    public final int length;

    Ship(int length) {
        this.length = length;
    }
}

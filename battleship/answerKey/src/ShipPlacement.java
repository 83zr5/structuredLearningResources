public class ShipPlacement {
    public final Ship ship;
    public final int x;
    public final int y;
    public boolean isVertical;

    public ShipPlacement(Ship ship, int x, int y, boolean vertical) {
        this.ship = ship;
        this.x = x;
        this.y = y;
        isVertical = vertical;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ShipPlacement{");
        sb.append("ship=").append(ship);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", isVertical=").append(isVertical);
        sb.append('}');
        return sb.toString();
    }
}

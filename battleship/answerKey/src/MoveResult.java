public enum MoveResult {
    ///  If a player hit a ship, but did not sink it
    HIT,
    /// If the player both hit & sunk a ship
    SUNK,
    /// If the player did not hit a ship
    MISSED,
    /// If the player already guessed a tile
    DUPLICATE,
    /// If all ships are sunk, and the game is over
    GAME_OVER,
}

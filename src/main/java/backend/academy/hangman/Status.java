package backend.academy.hangman;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents the status of the game.
 */
@Getter
@EqualsAndHashCode
public class Status {

    /**
     * Represents the status of the last guess.
     */
    public enum GuessStatus {
        /**
         * No guess has been made yet.
         */
        NONE,
        /**
         * The letter has been guessed before.
         */
        ALREADY_GUESSED,
        /**
         * The input is invalid.
         */
        INVALID,
        /**
         * The letter has not been guessed before and is not in the word.
         */
        INCORRECT,
        /**
         * The letter has not been guessed before and is in the word.
         */
        CORRECT
    }

    /**
     * Represents the overall status of the game.
     */
    public enum GameStatus {
        /**
         * The game has not started yet or is in progress.
         */
        NONE,
        /**
         * The game has been lost.
         */
        LOSE,
        /**
         * The game has been won.
         */
        WIN
    }

    private final GuessStatus guessStatus;
    private final GameStatus gameStatus;

    /**
     * Creates a new status with the given guess and game status.
     * @param guessStatus the status of the last guess
     * @param gameStatus the overall status of the game
     */
    Status(GuessStatus guessStatus, GameStatus gameStatus) {
        this.guessStatus = guessStatus;
        this.gameStatus = gameStatus;
    }
}

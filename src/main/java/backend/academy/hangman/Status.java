package backend.academy.hangman;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Status {

    public enum GuessStatus {
        NONE,
        ALREADY_GUESSED,
        INVALID,
        INCORRECT,
        CORRECT
    }

    public enum GameStatus {
        NONE,
        LOSE,
        WIN
    }

    private final GuessStatus guessStatus;
    private final GameStatus gameStatus;

    Status(GuessStatus guessStatus, GameStatus gameStatus) {
        this.guessStatus = guessStatus;
        this.gameStatus = gameStatus;
    }
}

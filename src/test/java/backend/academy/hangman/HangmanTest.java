package backend.academy.hangman;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class HangmanTest {
    private static final DictionaryWord secretWord = new DictionaryWord("Животные", "кот", "домашнее животное, мурлыкает");
    private static final int maxMissesCount = 1;

    @Test
    void createModelTest() {
        // Arrange
        Model hangman = new Hangman();

        // Act & Arrange (initial state)
        assertThat(hangman.getMaxMissesCount()).isEqualTo(-1);
        assertThat(hangman.getStatus()).isNull();
        assertThat(hangman.getMisses()).isNull();
        assertThat(hangman.getMissesCount()).isEqualTo(-1);
        assertThat(hangman.getHits()).isNull();
        assertThat(hangman.getWordRepresentation()).isNull();
        assertThat(hangman.getSecretWordHint()).isNull();

        // Act (create model)
        hangman = hangman.createModel(secretWord, maxMissesCount);

        // Assert (after creation)
        assertThat(hangman.getMaxMissesCount()).isEqualTo(maxMissesCount);
        assertThat(hangman.getStatus()).isEqualTo(new Status(Status.GuessStatus.NONE, Status.GameStatus.NONE));
        assertThat(hangman.getMisses()).isEmpty();
        assertThat(hangman.getMissesCount()).isEqualTo(0);
        assertThat(hangman.getHits()).isEmpty();
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(new ArrayList<>(Collections.nCopies(secretWord.word().length(), '_')));
        assertThat(hangman.getSecretWordHint()).isEqualTo(secretWord.hint());
    }

    @Test
    void getMaxMissesCountTest() {
        // Arrange
        Model hangman = new Hangman();

        // Act (create model with maxMissesCount)
        hangman = hangman.createModel(secretWord, maxMissesCount);

        // Assert
        assertThat(hangman.getMaxMissesCount()).isEqualTo(maxMissesCount);

        // Act (create model with default maxMissesCount)
        hangman = hangman.createModel(secretWord, -1);

        // Assert
        assertThat(hangman.getMaxMissesCount()).isEqualTo(Constants.DEFAULT_MAX_MISSES_COUNT);
    }

    @Test
    void getSecretWordHintTest() {
        // Arrange
        Model hangman = new Hangman();

        // Act
        hangman = hangman.createModel(secretWord, maxMissesCount);

        // Assert
        assertThat(hangman.getSecretWordHint()).isEqualTo("домашнее животное, мурлыкает");
    }

    @Test
    void hitsTest() {
        // Arrange
        Model hangman = new Hangman();
        hangman = hangman.createModel(secretWord, maxMissesCount);
        final Status initialStatus = new Status(Status.GuessStatus.NONE, Status.GameStatus.NONE);
        final Status alreadyGuessedStatus = new Status(Status.GuessStatus.ALREADY_GUESSED, Status.GameStatus.NONE);
        final Status hitStatus = new Status(Status.GuessStatus.CORRECT, Status.GameStatus.NONE);
        final Status winStaus = new Status(Status.GuessStatus.CORRECT, Status.GameStatus.WIN);
        List<Character> hits = new ArrayList<>();
        List<Character> wordRepresentation = new ArrayList<>(Collections.nCopies(secretWord.word().length(), '_'));

        // Assert (initial state)
        assertThat(hangman.getStatus()).isEqualTo(initialStatus);
        assertThat(hangman.getHits()).isEqualTo(hits);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (1st guess)
        hits.add('К');
        wordRepresentation.set(0, 'К');
        hangman.guess('К');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(hitStatus);
        assertThat(hangman.getHits()).isEqualTo(hits);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (same guess)
        hangman.guess('К');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(alreadyGuessedStatus);
        assertThat(hangman.getHits()).isEqualTo(hits);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (2nd guess, lowercase)
        hits.add('Т');
        wordRepresentation.set(2, 'Т');
        hangman.guess('т');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(hitStatus);
        assertThat(hangman.getHits()).isEqualTo(hits);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (3rd guess)
        hits.add('О');
        wordRepresentation.set(1, 'О');
        hangman.guess('О');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(winStaus);
        assertThat(hangman.getHits()).isEqualTo(hits);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);
    }

    @Test
    void missesAndInvalidGuessesTest() {
        // Arrange
        Model hangman = new Hangman();
        hangman = hangman.createModel(secretWord, maxMissesCount);
        final Status initialStatus = new Status(Status.GuessStatus.NONE, Status.GameStatus.NONE);
        final Status alreadyGuessedStatus = new Status(Status.GuessStatus.ALREADY_GUESSED, Status.GameStatus.NONE);
        final Status invalidStatus = new Status(Status.GuessStatus.INVALID, Status.GameStatus.NONE);
        final Status missStatus = new Status(Status.GuessStatus.INCORRECT, Status.GameStatus.NONE);
        final Status loseStaus = new Status(Status.GuessStatus.INCORRECT, Status.GameStatus.LOSE);
        List<Character> misses = new ArrayList<>();
        List<Character> wordRepresentation = new ArrayList<>(Collections.nCopies(secretWord.word().length(), '_'));
        int missesCount = 0;

        // Assert (initial state)
        assertThat(hangman.getStatus()).isEqualTo(initialStatus);
        assertThat(hangman.getMisses()).isEqualTo(misses);
        assertThat(hangman.getMissesCount()).isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (1st miss guess)
        misses.add('А');
        missesCount++;
        hangman.guess('А');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(missStatus);
        assertThat(hangman.getMisses()).isEqualTo(misses);
        assertThat(hangman.getMissesCount()).isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (same guess)
        hangman.guess('А');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(alreadyGuessedStatus);
        assertThat(hangman.getMisses()).isEqualTo(misses);
        assertThat(hangman.getMissesCount()).isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (invalid guess)
        hangman.guess('Z');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(invalidStatus);
        assertThat(hangman.getMisses()).isEqualTo(misses);
        assertThat(hangman.getMissesCount()).isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (another invalid guess)
        hangman.guess('Ѳ');

        // Assert
        assertThat(hangman.getStatus()).isEqualTo(invalidStatus);
        assertThat(hangman.getMisses()).isEqualTo(misses);
        assertThat(hangman.getMissesCount()).isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);

        // Act (2nd miss, lowercase)
        misses.add('Ё');
        missesCount++;
        hangman.guess('ё');

        // Assert (lose state)
        assertThat(hangman.getStatus()).isEqualTo(loseStaus);
        assertThat(hangman.getMisses()).isEqualTo(misses);
        assertThat(hangman.getMissesCount()).isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation()).isEqualTo(wordRepresentation);
    }
}

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
        Model hangman = new Hangman();
        assertThat(hangman.getMaxMissesCount())
            .isEqualTo(-1);
        assertThat(hangman.getStatus())
            .isNull();
        assertThat(hangman.getMisses())
            .isNull();
        assertThat(hangman.getMissesCount())
            .isEqualTo(-1);
        assertThat(hangman.getHits())
            .isNull();
        assertThat(hangman.getWordRepresentation())
            .isNull();
        assertThat(hangman.getSecretWordHint())
            .isNull();

        hangman = hangman.createModel(secretWord, maxMissesCount);
        assertThat(hangman.getMaxMissesCount())
            .isEqualTo(maxMissesCount);
        assertThat(hangman.getStatus())
            .isEqualTo(new Status(Status.GuessStatus.NONE, Status.GameStatus.NONE));
        assertThat(hangman.getMisses())
            .isEmpty();
        assertThat(hangman.getMissesCount())
            .isEqualTo(0);
        assertThat(hangman.getHits())
            .isEmpty();
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(new ArrayList<>(Collections.nCopies(secretWord.word().length(), '_')));
        assertThat(hangman.getSecretWordHint())
            .isEqualTo(secretWord.hint());

    }

    @Test
    void getMaxMissesCountTest() {
        Model hangman = new Hangman();
        hangman = hangman.createModel(secretWord, maxMissesCount);
        assertThat(hangman.getMaxMissesCount()).isEqualTo(maxMissesCount);

        hangman = hangman.createModel(secretWord, -1);
        assertThat(hangman.getMaxMissesCount()).isEqualTo(Constants.DEFAULT_MAX_MISSES_COUNT);
    }

    @Test
    void getSecretWordHintTest() {
        Model hangman = new Hangman();
        hangman = hangman.createModel(secretWord, maxMissesCount);
        assertThat(hangman.getSecretWordHint()).isEqualTo("домашнее животное, мурлыкает");
    }

    @Test
    void hitsTest() {
        Model hangman = new Hangman();
        hangman = hangman.createModel(secretWord, maxMissesCount);
        final Status initialStatus = new Status(Status.GuessStatus.NONE, Status.GameStatus.NONE);
        final Status alreadyGuessedStatus = new Status(Status.GuessStatus.ALREADY_GUESSED, Status.GameStatus.NONE);
        final Status hitStatus = new Status(Status.GuessStatus.CORRECT, Status.GameStatus.NONE);
        final Status winStaus = new Status(Status.GuessStatus.CORRECT, Status.GameStatus.WIN);
        List<Character> hits = new ArrayList<>();
        List<Character> wordRepresentation = new ArrayList<>(Collections.nCopies(secretWord.word().length(), '_'));

        // No guesses
        assertThat(hangman.getStatus())
            .isEqualTo(initialStatus);
        assertThat(hangman.getHits())
            .isEqualTo(hits);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // 1st guess
        hits.add('К');
        wordRepresentation.set(0, 'К');
        hangman.guess('К');
        assertThat(hangman.getStatus())
            .isEqualTo(hitStatus);
        assertThat(hangman.getHits())
            .isEqualTo(hits);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // same guess
        hangman.guess('К');
        assertThat(hangman.getStatus())
            .isEqualTo(alreadyGuessedStatus);
        assertThat(hangman.getHits())
            .isEqualTo(hits);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // 2nd guessd lowercase
        hits.add('Т');
        wordRepresentation.set(2, 'Т');
        hangman.guess('т');
        assertThat(hangman.getStatus())
            .isEqualTo(hitStatus);
        assertThat(hangman.getHits())
            .isEqualTo(hits);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // 3rd guess
        hits.add('О');
        wordRepresentation.set(1, 'О');
        hangman.guess('О');
        assertThat(hangman.getStatus())
            .isEqualTo(winStaus);
        assertThat(hangman.getHits())
            .isEqualTo(hits);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);
    }

    @Test
    void missesAndInvalidGuessesTest() {
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

        // No guesses
        assertThat(hangman.getStatus())
            .isEqualTo(initialStatus);
        assertThat(hangman.getMisses())
            .isEqualTo(misses);
        assertThat(hangman.getMissesCount())
            .isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // 1st guess
        misses.add('А');
        missesCount++;
        hangman.guess('А');
        assertThat(hangman.getStatus())
            .isEqualTo(missStatus);
        assertThat(hangman.getMisses())
            .isEqualTo(misses);
        assertThat(hangman.getMissesCount())
            .isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // same guess
        hangman.guess('А');
        assertThat(hangman.getStatus())
            .isEqualTo(alreadyGuessedStatus);
        assertThat(hangman.getMisses())
            .isEqualTo(misses);
        assertThat(hangman.getMissesCount())
            .isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // invalid guesses
        hangman.guess('Z');
        assertThat(hangman.getStatus())
            .isEqualTo(invalidStatus);
        assertThat(hangman.getMisses())
            .isEqualTo(misses);
        assertThat(hangman.getMissesCount())
            .isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        hangman.guess('Ѳ');
        assertThat(hangman.getStatus())
            .isEqualTo(invalidStatus);
        assertThat(hangman.getMisses())
            .isEqualTo(misses);
        assertThat(hangman.getMissesCount())
            .isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);

        // 2nd guess (lowercase)
        misses.add('Ё');
        missesCount++;
        hangman.guess('ё');
        assertThat(hangman.getStatus())
            .isEqualTo(loseStaus);
        assertThat(hangman.getMisses())
            .isEqualTo(misses);
        assertThat(hangman.getMissesCount())
            .isEqualTo(missesCount);
        assertThat(hangman.getWordRepresentation())
            .isEqualTo(wordRepresentation);
    }
}

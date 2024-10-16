package backend.academy.hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Hangman implements Model {
    private final DictionaryWord secretWord;
    private final int maxMissesCount;
    private final Set<Character> correctLetters;
    private Status currentStatus;
    private List<Character> misses;
    private int missesCount;
    private List<Character> hits;
    private List<Character> wordRepresentation;

    private Hangman(DictionaryWord secretWord, int maxMissesCount) {
        this.secretWord = secretWord;
        this.maxMissesCount = maxMissesCount >= 0 ? maxMissesCount : Constants.DEFAULT_MAX_MISSES_COUNT;
        this.correctLetters = secretWord.word().chars()
            .mapToObj(e -> Character.toUpperCase((char) e))
            .collect(Collectors.toCollection(HashSet::new));
        this.misses = new ArrayList<>();
        this.missesCount = 0;
        this.hits = new ArrayList<>();
        this.currentStatus = new Status(Status.GuessStatus.NONE, Status.GameStatus.NONE);
        this.wordRepresentation = new ArrayList<>(Collections.nCopies(secretWord.word().length(), '_'));
    }

    public Hangman() {
        this.secretWord = null;
        this.maxMissesCount = -1;
        this.correctLetters = null;
        this.missesCount = -1;
    }

    @Override
    public Model createModel(DictionaryWord secretWord, int maxMissesCount) {
        return new Hangman(secretWord, maxMissesCount);
    }

    @Override
    public void guess(char letter) {
        char upperCaseLetter = Character.toUpperCase(letter);
        Status.GuessStatus guessStatus;
        if (!(('А' <= upperCaseLetter && upperCaseLetter <= 'Я') || upperCaseLetter == 'Ё')) {
            guessStatus = Status.GuessStatus.INVALID;
        } else if (misses.contains(upperCaseLetter) || hits.contains(upperCaseLetter)) {
            guessStatus = Status.GuessStatus.ALREADY_GUESSED;
        } else {
            if (correctLetters.contains(upperCaseLetter)) {
                addHit(upperCaseLetter);
            } else {
                addMiss(upperCaseLetter);
            }
            return;  // addHit and addMiss are changing currentStatus
        }
        currentStatus = new Status(guessStatus, currentStatus.gameStatus());
    }

    private void addHit(char letter) {
        hits.add(letter);

        for (int i = 0; i < secretWord.word().length(); ++i) {
            if (Character.toUpperCase(secretWord.word().charAt(i)) == letter) {
                wordRepresentation.set(i, letter);
            }
        }

        Set<Character> correctLettersCopy = new HashSet<>(correctLetters);
        hits.forEach(correctLettersCopy::remove);

        if (correctLettersCopy.isEmpty()) {
            currentStatus = new Status(Status.GuessStatus.CORRECT, Status.GameStatus.WIN);
        } else {
            currentStatus = new Status(Status.GuessStatus.CORRECT, currentStatus.gameStatus());
        }
    }

    private void addMiss(char letter) {
        misses.add(letter);
        missesCount++;

        if (missesCount > maxMissesCount) {
            currentStatus = new Status(Status.GuessStatus.INCORRECT, Status.GameStatus.LOSE);
        } else {
            currentStatus = new Status(Status.GuessStatus.INCORRECT, currentStatus.gameStatus());
        }
    }

    @Override
    public int getMaxMissesCount() {
        return maxMissesCount;
    }

    @Override
    public Status getStatus() {
        return currentStatus;
    }

    @Override
    public List<Character> getMisses() {
        return misses;
    }

    @Override
    public int getMissesCount() {
        return missesCount;
    }

    @Override
    public List<Character> getHits() {
        return hits;
    }

    @Override
    public List<Character> getWordRepresentation() {
        return wordRepresentation;
    }

    @Override
    public String getSecretWordHint() {
        return secretWord != null ?  secretWord.hint() : null;
    }
}

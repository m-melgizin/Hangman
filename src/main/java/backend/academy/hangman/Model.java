package backend.academy.hangman;

import java.util.List;

/**
 * Hangman game model interface.
 * <p>
 * The model is responsible for keeping the game state and performing
 * actions such as guessing a letter.
 */
public interface Model {

    /**
     * Creates a new model with the specified secret word and max misses count.
     * <p>
     * This method should be overridden by subclasses.
     *
     * @param secretWord the secret word
     * @param maxMissesCount the max misses count
     * @return a new model
     */
    Model createModel(DictionaryWord secretWord, int maxMissesCount);

    /**
     * Guesses a letter in the secret word.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @param letter the letter to guess
     */
    void guess(char letter);

    /**
     * Gets the max misses count.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the max misses count
     */
    int getMaxMissesCount();

    /**
     * Gets the game status.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the game status
     */
    Status getStatus();

    /**
     * Gets the list of missed letters.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the list of missed letters
     */
    List<Character> getMisses();

    /**
     * Gets the number of missed letters.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the number of missed letters
     */
    int getMissesCount();

    /**
     * Gets the list of guessed letters.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the list of guessed letters
     */
    List<Character> getHits();

    /**
     * Gets the representation of the secret word.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the representation of the secret word
     */
    List<Character> getWordRepresentation();

    /**
     * Gets the hint of the secret word.
     * <p>
     * The method should be overridden by subclasses.
     *
     * @return the hint of the secret word
     */
    String getSecretWordHint();
}

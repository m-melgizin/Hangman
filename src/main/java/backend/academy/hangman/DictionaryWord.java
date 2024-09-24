package backend.academy.hangman;

/**
 * Represents a word with its category and hint.
 *
 * @param category Category of the word.
 * @param word     The word itself.
 * @param hint     Hint for guessing the word.
 */
public record DictionaryWord(String category, String word, String hint) {
}

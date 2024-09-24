package backend.academy.hangman;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Defines an interface for classes that are responsible for loading and
 * retrieving words from a dictionary.
 */
public interface DictionaryReader {
    /**
     * Reads words from a file with the given name.
     *
     * @param path the name of the file to read.
     * @throws IOException if the file cannot be read.
     */
    void readFromFile(String path) throws IOException;

    /**
     * Reads words from an input stream.
     *
     * @param inputStream the input stream to read from.
     */
    void readFromInputStream(InputStream inputStream);

    /**
     * Returns a list of all categories in the dictionary.
     *
     * @return a list of all categories in the dictionary.
     */
    List<String> getCategories();

    /**
     * Returns a list of all words in the given category.
     *
     * @param category the category to retrieve words from.
     * @return a list of all words in the given category.
     */
    List<DictionaryWord> getWordsInCategory(String category);

    /**
     * Returns a random word in the given category.
     *
     * @param category the category to retrieve a random word from.
     * @return a random word in the given category.
     */
    DictionaryWord getRandomWordInCategory(String category);
}

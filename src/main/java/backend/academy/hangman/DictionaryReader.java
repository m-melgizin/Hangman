package backend.academy.hangman;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DictionaryReader {
    void readFromFile(String path) throws IOException;

    void readFromInputStream(InputStream inputStream);

    List<String> getCategories();

    List<DictionaryWord> getWordsInCategory(String category);

    DictionaryWord getRandomWordInCategory(String category);
}

package backend.academy.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

/**
 * Reads words from a CSV file.
 * The CSV file is expected to have three columns, separated by commas:
 * <ul>
 * <li>Category</li>
 * <li>Word</li>
 * <li>Hint</li>
 * </ul>
 * <p>
 * Note that this is a very basic parser, more sophisticated CSVs (quoting or
 * including commas as values) will not be parsed as intended with this approach.
 */
@Log4j2
public class CSVDictionaryReader implements DictionaryReader {
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String TAB = "\\t";
    public static final String VERTICAL_BAR = "\\|";

    private final String separator;
    private final List<String> categories;
    private final Map<String, List<DictionaryWord>> wordsByCategory; // <category, List<Word>>
    private final SecureRandom random;

    @SuppressWarnings({"MagicNumber"})
    private void readCSVFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(separator);
                if (data.length != 3) {
                    log.error("Invalid line: {}", line);
                    log.error(Arrays.toString(data));
                    continue;
                }
                String category = data[0];
                String word = data[1];
                String hint = data[2];

                DictionaryWord dictionaryWord = new DictionaryWord(category, word, hint);
                wordsByCategory.computeIfAbsent(category, k -> {
                    categories.add(category);
                    return new ArrayList<>();
                }).add(dictionaryWord);
            }
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
        }
    }

    /**
     * Creates a new instance of {@link CSVDictionaryReader}.
     *
     * @param separator The separator used in the CSV file.
     */
    public CSVDictionaryReader(String separator) {
        this.categories = new ArrayList<>();
        this.wordsByCategory = new HashMap<>();
        this.random = new SecureRandom();
        this.separator = separator;
    }

    @Override
    public void readFromFile(String path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Path.of(path))) {
            readFromInputStream(inputStream);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void readFromInputStream(InputStream inputStream) {
        readCSVFile(inputStream);
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public List<DictionaryWord> getWordsInCategory(String category) {
        return wordsByCategory.get(category);
    }

    @Override
    public DictionaryWord getRandomWordInCategory(String category) {
        List<DictionaryWord> wordsInCategory = wordsByCategory.get(category);
        if (wordsInCategory != null) {
            return wordsInCategory.get(random.nextInt(wordsInCategory.size()));
        } else {
            return null;
        }
    }
}

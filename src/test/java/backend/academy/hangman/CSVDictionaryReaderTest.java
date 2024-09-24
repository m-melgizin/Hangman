package backend.academy.hangman;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class CSVDictionaryReaderTest {

    @Nested
    class ReadFromFile {
        @Test
        void readFromEmptyFile() {
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatCode(
                () -> dictionaryReader.readFromFile("src/test/resources/empty.csv")
            ).doesNotThrowAnyException();

            assertThat(dictionaryReader.getCategories()).isEmpty();
        }

        @Test
        void readFromValidFile() {
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatCode(
                () -> dictionaryReader.readFromFile("src/test/resources/valid.csv")
            ).doesNotThrowAnyException();

            List<String> categories = new ArrayList<>();
            categories.add("Animals");
            categories.add("Fruits");
            categories.add("Techs");

            List<DictionaryWord> animals = new ArrayList<>();
            animals.add(new DictionaryWord("Animals", "cat", "meow"));
            animals.add(new DictionaryWord("Animals", "dog", "bark"));
            animals.add(new DictionaryWord("Animals", "cow", "moo"));

            List<DictionaryWord> fruits = new ArrayList<>();
            fruits.add(new DictionaryWord("Fruits", "apple", "the red one"));

            List<DictionaryWord> techs = new ArrayList<>();
            techs.add(new DictionaryWord("Techs", "pc", "desktop"));
            techs.add(new DictionaryWord("Techs", "laptop", "not pc"));

            assertThat(dictionaryReader.getCategories()).isEqualTo(categories);

            assertThat(dictionaryReader.getWordsInCategory("Animals")).isEqualTo(animals);
            assertThat(dictionaryReader.getWordsInCategory("Fruits")).isEqualTo(fruits);
            assertThat(dictionaryReader.getWordsInCategory("Techs")).isEqualTo(techs);
            assertThat(dictionaryReader.getWordsInCategory("Non existent category")).isNull();
        }

        @Test
        void readFromInvalidFile() {
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatCode(
                () -> dictionaryReader.readFromFile("src/test/resources/invalid.csv")
            ).doesNotThrowAnyException();

            assertThat(dictionaryReader.getCategories()).isEmpty();
        }

        @Test
        void readFromNonExistentFile() {
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatExceptionOfType(IOException.class).isThrownBy(
                () -> dictionaryReader.readFromFile("non/existent/path/to/file.csv")
            );
        }
    }

    @Nested
    class ReadFromInputStream {
        @Test
        void readFromEmptyInputStream() {
            InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatCode(
                () -> dictionaryReader.readFromInputStream(inputStream)
            ).doesNotThrowAnyException();

            assertThat(dictionaryReader.getCategories()).isEmpty();
        }

        @Test
        void readFromValidInputStream() {
            String input = new StringBuilder()
                .append("Animals,cat,meow\n")
                .append("Animals,dog,bark\n")
                .append("Fruits,apple,the red one\n")
                .append("Techs,pc,desktop\n")
                .append("Techs,laptop,not pc\n")
                .append("Animals,cow,moo\n")
                .toString();
            InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatCode(
                () -> dictionaryReader.readFromInputStream(inputStream)
            ).doesNotThrowAnyException();

            List<String> categories = new ArrayList<>();
            categories.add("Animals");
            categories.add("Fruits");
            categories.add("Techs");

            List<DictionaryWord> animals = new ArrayList<>();
            animals.add(new DictionaryWord("Animals", "cat", "meow"));
            animals.add(new DictionaryWord("Animals", "dog", "bark"));
            animals.add(new DictionaryWord("Animals", "cow", "moo"));

            List<DictionaryWord> fruits = new ArrayList<>();
            fruits.add(new DictionaryWord("Fruits", "apple", "the red one"));

            List<DictionaryWord> techs = new ArrayList<>();
            techs.add(new DictionaryWord("Techs", "pc", "desktop"));
            techs.add(new DictionaryWord("Techs", "laptop", "not pc"));

            assertThat(dictionaryReader.getCategories()).isEqualTo(categories);

            assertThat(dictionaryReader.getWordsInCategory("Animals")).isEqualTo(animals);
            assertThat(dictionaryReader.getWordsInCategory("Fruits")).isEqualTo(fruits);
            assertThat(dictionaryReader.getWordsInCategory("Techs")).isEqualTo(techs);
            assertThat(dictionaryReader.getWordsInCategory("Non existent category")).isNull();
        }

        @Test
        void readFromInvalidInputStream() {
            String input = new StringBuilder()
                .append("There,are,too,many,columns,here\n")
                .append("There,are\n")
                .append("too,few\n")
                .append("columns\n")
                .append("There is;an incorrect;delimiter here\n")
                .append("\n")
                .toString();
            InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThatCode(
                () -> dictionaryReader.readFromInputStream(inputStream)
            ).doesNotThrowAnyException();
        }

        @Test
        void readFromInputStreamWithIOException() throws IOException {
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            InputStream inputStream = mock(InputStream.class);
            doThrow(IOException.class).when(inputStream).read();

            assertThatCode(
                () -> dictionaryReader.readFromInputStream(inputStream)
            ).doesNotThrowAnyException();
        }
    }

    @Nested
    class GetRandomWordInCategory {

        @Test
        void getRandomWordInExistentCategory() {
            String input = new StringBuilder()
                .append("Animals,cat,meow\n")
                .append("Animals,dog,bark\n")
                .append("Fruits,apple,the red one\n")
                .append("Techs,pc,desktop\n")
                .append("Techs,laptop,not pc\n")
                .append("Animals,cow,moo\n")
                .toString();
            InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            dictionaryReader.readFromInputStream(inputStream);

            List<DictionaryWord> animals = new ArrayList<>();
            animals.add(new DictionaryWord("Animals", "cat", "meow"));
            animals.add(new DictionaryWord("Animals", "dog", "bark"));
            animals.add(new DictionaryWord("Animals", "cow", "moo"));

            List<DictionaryWord> fruits = new ArrayList<>();
            fruits.add(new DictionaryWord("Fruits", "apple", "the red one"));

            List<DictionaryWord> techs = new ArrayList<>();
            techs.add(new DictionaryWord("Techs", "pc", "desktop"));
            techs.add(new DictionaryWord("Techs", "laptop", "not pc"));

            assertThat(dictionaryReader.getRandomWordInCategory("Animals")).isIn(animals);
            assertThat(dictionaryReader.getRandomWordInCategory("Fruits")).isIn(fruits);
            assertThat(dictionaryReader.getRandomWordInCategory("Techs")).isIn(techs);
        }

        @Test
        void getRandomWordInNonExistentCategory() {
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            assertThat(dictionaryReader.getRandomWordInCategory("Non existent category")).isNull();
        }
    }
}

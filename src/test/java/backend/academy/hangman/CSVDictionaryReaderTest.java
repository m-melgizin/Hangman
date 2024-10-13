package backend.academy.hangman;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class CSVDictionaryReaderTest {

    @Nested
    class ReadFromFile {
        @Test
        void readFromEmptyFile() {
            // Arrange
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromFile("src/test/resources/empty.csv"));

            // Assert
            assertThat(thrown).doesNotThrowAnyException();
            assertThat(dictionaryReader.getCategories()).isEmpty();
        }

        @Test
        void readFromValidFile() {
            // Arrange
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            List<String> categories = List.of("Animals", "Fruits", "Techs");
            List<DictionaryWord> animals = List.of(
                new DictionaryWord("Animals", "cat", "meow"),
                new DictionaryWord("Animals", "dog", "bark"),
                new DictionaryWord("Animals", "cow", "moo")
            );
            List<DictionaryWord> fruits = List.of(
                new DictionaryWord("Fruits", "apple", "the red one")
            );
            List<DictionaryWord> techs = List.of(
                new DictionaryWord("Techs", "pc", "desktop"),
                new DictionaryWord("Techs", "laptop", "not pc")
            );

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromFile("src/test/resources/valid.csv"));

            // Assert
            assertThat(dictionaryReader.getCategories()).isEqualTo(categories);
            assertThat(dictionaryReader.getWordsInCategory("Animals")).isEqualTo(animals);
            assertThat(dictionaryReader.getWordsInCategory("Fruits")).isEqualTo(fruits);
            assertThat(dictionaryReader.getWordsInCategory("Techs")).isEqualTo(techs);
            assertThat(dictionaryReader.getWordsInCategory("Non existent category")).isNull();
        }

        @Test
        void readFromInvalidFile() {
            // Arrange
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromFile("src/test/resources/invalid.csv"));

            // Assert
            assertThat(thrown).doesNotThrowAnyException();
            assertThat(dictionaryReader.getCategories()).isEmpty();
        }

        @Test
        void readFromNonExistentFile() {
            // Arrange
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);

            // Act & Assert
            assertThatExceptionOfType(IOException.class)
                .isThrownBy(() -> dictionaryReader.readFromFile("non/existent/path/to/file.csv"));
        }
    }

    @Nested
    class ReadFromInputStream {
        @Test
        void readFromEmptyInputStream() {
            // Arrange
            InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromInputStream(inputStream));

            // Assert
            assertThat(thrown).doesNotThrowAnyException();
            assertThat(dictionaryReader.getCategories()).isEmpty();
        }

        @Test
        void readFromValidInputStream() {
            // Arrange
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
            List<String> categories = List.of("Animals", "Fruits", "Techs");
            List<DictionaryWord> animals = List.of(
                new DictionaryWord("Animals", "cat", "meow"),
                new DictionaryWord("Animals", "dog", "bark"),
                new DictionaryWord("Animals", "cow", "moo")
            );
            List<DictionaryWord> fruits = List.of(
                new DictionaryWord("Fruits", "apple", "the red one")
            );
            List<DictionaryWord> techs = List.of(
                new DictionaryWord("Techs", "pc", "desktop"),
                new DictionaryWord("Techs", "laptop", "not pc")
            );

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromInputStream(inputStream));

            // Assert
            assertThat(thrown).doesNotThrowAnyException();
            assertThat(dictionaryReader.getCategories()).isEqualTo(categories);
            assertThat(dictionaryReader.getWordsInCategory("Animals")).isEqualTo(animals);
            assertThat(dictionaryReader.getWordsInCategory("Fruits")).isEqualTo(fruits);
            assertThat(dictionaryReader.getWordsInCategory("Techs")).isEqualTo(techs);
            assertThat(dictionaryReader.getWordsInCategory("Non existent category")).isNull();
        }

        @Test
        void readFromInvalidInputStream() {
            // Arrange
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

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromInputStream(inputStream));

            // Assert
            assertThat(thrown).doesNotThrowAnyException();
        }

        @Test
        void readFromInputStreamWithIOException() throws IOException {
            // Arrange
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);
            InputStream inputStream = mock(InputStream.class);
            doThrow(IOException.class).when(inputStream).read();

            // Act
            Throwable thrown = catchThrowable(
                () -> dictionaryReader.readFromInputStream(inputStream));

            // Assert
            assertThat(thrown).doesNotThrowAnyException();
        }
    }

    @Nested
    class GetRandomWordInCategory {

        @Test
        void getRandomWordInExistentCategory() {
            // Arrange
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
            List<DictionaryWord> animals = List.of(
                new DictionaryWord("Animals", "cat", "meow"),
                new DictionaryWord("Animals", "dog", "bark"),
                new DictionaryWord("Animals", "cow", "moo")
            );
            List<DictionaryWord> fruits = List.of(
                new DictionaryWord("Fruits", "apple", "the red one")
            );
            List<DictionaryWord> techs = List.of(
                new DictionaryWord("Techs", "pc", "desktop"),
                new DictionaryWord("Techs", "laptop", "not pc")
            );

            // Act & Assert
            assertThat(dictionaryReader.getRandomWordInCategory("Animals")).isIn(animals);
            assertThat(dictionaryReader.getRandomWordInCategory("Fruits")).isIn(fruits);
            assertThat(dictionaryReader.getRandomWordInCategory("Techs")).isIn(techs);
        }

        @Test
        void getRandomWordInNonExistentCategory() {
            // Arrange
            DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.COMMA);

            // Act & Assert
            assertThat(dictionaryReader.getRandomWordInCategory("Non existent category")).isNull();
        }
    }
}

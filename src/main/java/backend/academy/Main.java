package backend.academy;

import backend.academy.hangman.CSVDictionaryReader;
import backend.academy.hangman.ConsoleView;
import backend.academy.hangman.Controller;
import backend.academy.hangman.DictionaryReader;
import backend.academy.hangman.Game;
import backend.academy.hangman.Hangman;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class Main {
    public static void main(String[] args) throws IOException {
        DictionaryReader dictionaryReader = new CSVDictionaryReader(CSVDictionaryReader.VERTICAL_BAR);
        dictionaryReader.readFromFile("src/main/resources/dictionary.csv");
        Controller game = new Game(new Hangman(), new ConsoleView(), dictionaryReader);
        game.run();
    }
}

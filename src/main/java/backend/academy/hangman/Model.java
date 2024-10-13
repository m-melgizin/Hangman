package backend.academy.hangman;

import java.util.List;

public interface Model {
    Model createModel(DictionaryWord secretWord, int maxMissesCount);

    void guess(char letter);

    int getMaxMissesCount();

    Status getStatus();

    List<Character> getMisses();

    int getMissesCount();

    List<Character> getHits();

    List<Character> getWordRepresentation();

    String getSecretWordHint();
}

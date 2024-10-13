package backend.academy.hangman;

import java.util.List;

public interface View {
    void sayHello();

    void sayGoodbye();

    String askCategory(List<String> categories);

    int askMaxMissesCount();

    boolean askForPlayAgain();

    void draw(Model model);

    char getGuess();
}

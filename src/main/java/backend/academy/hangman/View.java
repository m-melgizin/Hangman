package backend.academy.hangman;

import java.util.List;

/**
 * The game view interface.
 * <p>
 * This interface is responsible for user communication.
 */
public interface View {
    /**
     * Greet the user.
     */
    void sayHello();

    /**
     * Say goodbye to the user.
     */
    void sayGoodbye();

    /**
     * Ask the user for a game category.
     * <p>
     * The method returns a user's input.
     *
     * @param categories list of categories
     * @return a user's input
     */
    String askCategory(List<String> categories);

    /**
     * Ask the user for a maximal number of misses.
     * <p>
     * The method returns a user's input.
     *
     * @return a user's input
     */
    int askMaxMissesCount();

    /**
     * Ask the user if he/she wants to play again.
     * <p>
     * The method returns a user's input.
     *
     * @return a user's input
     */
    boolean askForPlayAgain();

    /**
     * Draw the game state.
     * <p>
     * The method prints the game state.
     *
     * @param model the game model
     */
    void draw(Model model);

    /**
     * Ask the user for a guess.
     * <p>
     * The method returns a user's input.
     *
     * @return a user's input
     */
    char getGuess();
}

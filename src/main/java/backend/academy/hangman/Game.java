package backend.academy.hangman;

/**
 * The main game class.
 * <p>
 * This class is responsible for the main game loop and interaction with the view and model.
 */
public class Game implements Controller {
    private Model model;
    private final View view;
    private final DictionaryReader dictionaryReader;

    /**
     * Creates a new game.
     *
     * @param model the model of the game
     * @param view  the view of the game
     * @param dictionaryReader the dictionary reader
     */
    public Game(Model model, View view, DictionaryReader dictionaryReader) {
        this.model = model;
        this.view = view;
        this.dictionaryReader = dictionaryReader;
    }

    @Override
    public void run() {
        view.sayHello();

        boolean playAgain = true;
        while (playAgain) {
            String category = view.askCategory(dictionaryReader.getCategories());
            int maxMissesCount = view.askMaxMissesCount();
            DictionaryWord secretWord = dictionaryReader.getRandomWordInCategory(category);
            model = model.createModel(secretWord, maxMissesCount);

            gameLoop();

            playAgain = view.askForPlayAgain();
        }

        view.sayGoodbye();
    }

    private void gameLoop() {
        while (true) {
            view.draw(model);
            Status.GameStatus gameStatus = model.getStatus().gameStatus();
            if (gameStatus != Status.GameStatus.NONE) {
                break;
            }
            char letter = view.getGuess();
            model.guess(letter);
        }
    }
}

package backend.academy.hangman;

public class Game implements Controller {
    private Model model;
    private final View view;
    private final DictionaryReader dictionaryReader;

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

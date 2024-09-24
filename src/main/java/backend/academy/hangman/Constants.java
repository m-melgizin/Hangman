package backend.academy.hangman;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("MultipleStringLiterals")
public class Constants {
    public static final int DEFAULT_MAX_MISSES_COUNT = 5;

    public static final int ALPHABET_SIZE = 33;

    public static final int DISPLAY_WIDTH = 45;
    public static final int DISPLAY_HEIGHT = 15;

    public static final int DISPLAY_WIDTH_2 = DISPLAY_WIDTH / 2;
    public static final int DISPLAY_HEIGHT_2 = DISPLAY_HEIGHT / 2;

    public static final int TITLE_ROW = 0;
    public static final int MESSAGE_ROW = TITLE_ROW + 1;
    public static final int PICTURE_ROW = MESSAGE_ROW + 1;
    public static final int PICTURE_COL = 0;
    public static final int PICTURE_HEIGHT = 10;
    public static final int PICTURE_WIDTH = 10;
    public static final int MISSES_ROW = MESSAGE_ROW + 1;
    public static final int MISSES_COL_0 = PICTURE_COL + PICTURE_WIDTH + 1;
    public static final int MISSES_PER_ROW_0 = (DISPLAY_WIDTH - MISSES_COL_0) / 2;
    public static final int MISSES_COL_1 = PICTURE_COL + PICTURE_WIDTH + 2;
    public static final int MISSES_PER_ROW_1 = (DISPLAY_WIDTH - MISSES_COL_1) / 2;
    public static final int HINT_MAX_WIDTH = DISPLAY_WIDTH - MISSES_COL_0;
    public static final int HITS_ROW = PICTURE_ROW + PICTURE_HEIGHT;

    public static final String GAME_NAME = "ВИСЕЛИЦА";
    public static final String GAME_STATUS_NONE = "";
    public static final String GAME_STATUS_LOSE = "Вы проиграли :(";
    public static final String GAME_STATUS_WIN = "Вы выиграли!";
    public static final String GUESS_STATUS_NONE = "";
    public static final String GUESS_STATUS_ALREADY_GUESSED = "Буква уже была";
    public static final String GUESS_STATUS_INVALID = "Невалидный ввод";
    public static final String GUESS_STATUS_INCORRECT = "Ошибка!";
    public static final String GUESS_STATUS_CORRECT = "Верно!";
    public static final String MISSES_TITLE = "Ошибки:";
    public static final String HINT_TITLE = "Подсказка:";

    public static final String PRESS_ENTER = "Нажмите [Enter]...";
    public static final String HELLO_MESSAGE = "ИГРА ВИСЕЛИЦА";
    public static final String GOODBYE_MESSAGE = "СПАСИБО ЗА ИГРУ!";

    public static final String CATEGORY_TEMPLATE = "%d. %s";
    public static final String RANDOM_CATEGORY = "Случайная категория";
    public static final String SELECT_CATEGORY = "Выберете категорию слов";

    public static final String DIFFICULTY_TEMPLATE = "%d. %s (%d ошибок)";
    public static final String DIFFICULTY_WITHOUT_MISSES_HINT_TEMPLATE = "%d. %s";
    public static final String EASY_DIFFICULTY = "Легко";
    public static final String NORMAL_DIFFICULTY = "Нормально";
    public static final String HARD_DIFFICULTY = "Сложно";
    public static final String ZERO_MISSES_DIFFICULTY = "Без ошибок";
    public static final String USER_DEFINED_DIFFICULTY = "Свой";
    public static final String SELECT_DIFFICULTY = "Выберете уровень сложности";
    public static final String SELECT_MISSES_COUNT = "Введите количество ошибок";

    public static final int EASY_MISSES_COUNT = 10;
    public static final int NORMAL_MISSES_COUNT = 7;
    public static final int HARD_MISSES_COUNT = 5;
    public static final int ZERO_MISSES_COUNT = 0;

    public static final String PLAY_AGAIN_QUESTION = "Хотите сыграть ещё? (Д/Н)";
    public static final String PLAY_AGAIN_YES = "Д";
    public static final String PLAY_AGAIN_NO = "Н";

    public static final String SELECT_GUESS = "Введите букву";
}

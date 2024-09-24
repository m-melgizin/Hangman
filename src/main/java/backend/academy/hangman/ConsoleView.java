package backend.academy.hangman;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * The game view console implementation.
 *
 * <p>
 * This class is responsible for user communication.
 */
public class ConsoleView implements View {

    @SuppressWarnings("MultipleStringLiterals")
    private final String[][] hangmanPictures = {
        {
            "    _____ ",
            "    |   | ",
            "        | ",
            "        | ",
            "        | ",
            "        | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "        | ",
            "        | ",
            "        | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "    |   | ",
            "        | ",
            "        | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "    |   | ",
            "    |   | ",
            "        | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "    |   | ",
            "    |   | ",
            "    |   | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "   \\|   | ",
            "    |   | ",
            "    |   | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "   \\|/  | ",
            "    |   | ",
            "    |   | ",
            "        | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "   \\|/  | ",
            "    |   | ",
            "    |   | ",
            "   /    | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "   \\|/  | ",
            "    |   | ",
            "    |   | ",
            "   / \\  | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "   \\|/  | ",
            "    |   | ",
            "    |   | ",
            "  _/ \\  | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (_)  | ",
            "   \\|/  | ",
            "    |   | ",
            "    |   | ",
            "  _/ \\_ | ",
            "________|_"
        },
        {
            "    _____ ",
            "    |   | ",
            "   (X)  | ",
            "   \\|/  | ",
            "    |   | ",
            "    |   | ",
            "  _/ \\_ | ",
            "________|_"
        }
    };

    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ScreenBuffer screenBuffer;
    private final SecureRandom random;


    /**
     * Creates a new {@link ConsoleView} that uses the standard input and output.
     */
    public ConsoleView() {
        this(System.in, System.out);
    }

    /**
     * Creates a new {@link ConsoleView} that uses the specified input and output
     * streams.
     *
     * @param inputStream the input stream
     * @param outputStream the output stream
     */

    public ConsoleView(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.screenBuffer = new ScreenBuffer(Constants.DISPLAY_HEIGHT, Constants.DISPLAY_WIDTH);
        this.random = new SecureRandom();
    }

    @Override
    public void sayHello() {
        say(Constants.HELLO_MESSAGE);
    }

    @Override
    public void sayGoodbye() {
        say(Constants.GOODBYE_MESSAGE);
    }

    @Override
    public String askCategory(List<String> categories) {
        String answer = "";
        while (answer.isEmpty()) {
            screenBuffer.clear();
            int idx = 0;
            screenBuffer.set(idx, 0, String.format(Constants.CATEGORY_TEMPLATE, idx, Constants.RANDOM_CATEGORY));
            for (String category : categories) {
                if (idx >= screenBuffer.rows()) {
                    break;
                }
                screenBuffer.set(idx + 1, 0, String.format(Constants.CATEGORY_TEMPLATE, idx + 1, category));
                idx++;
            }
            String choose = makeDialog(Constants.SELECT_CATEGORY);
            int choiceInt = -1;
            try {
                choiceInt = Integer.parseInt(choose);
                if (choiceInt < 0 || choiceInt > idx) {
                    choiceInt = -1;  // Reset to invalid if out of range
                }
            } catch (NumberFormatException e) {

            }

            if (choiceInt != -1) {
                answer = categories.get(choiceInt != 0 ? choiceInt - 1 : random.nextInt(idx));
            }
        }
        return answer;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public int askMaxMissesCount() {
        int answer = -1;
        while (answer == -1) {
            screenBuffer.clear();
            int row = 0;
            final int col = 0;
            screenBuffer.set(
                row++, col, String.format(
                    Constants.DIFFICULTY_TEMPLATE, row, Constants.EASY_DIFFICULTY, Constants.EASY_MISSES_COUNT));
            screenBuffer.set(
                row++, col, String.format(
                    Constants.DIFFICULTY_TEMPLATE, row, Constants.NORMAL_DIFFICULTY, Constants.NORMAL_MISSES_COUNT));
            screenBuffer.set(
                row++, col, String.format(
                    Constants.DIFFICULTY_TEMPLATE, row, Constants.HARD_DIFFICULTY, Constants.HARD_MISSES_COUNT));
            screenBuffer.set(
                row++, col, String.format(
                    Constants.DIFFICULTY_TEMPLATE, row, Constants.ZERO_MISSES_DIFFICULTY, Constants.ZERO_MISSES_COUNT));
            screenBuffer.set(
                row++, col, String.format(
                    Constants.DIFFICULTY_WITHOUT_MISSES_HINT_TEMPLATE, row, Constants.USER_DEFINED_DIFFICULTY));
            String choose = makeDialog(Constants.SELECT_DIFFICULTY);
            int choiceInt;
            try {
                choiceInt = Integer.parseInt(choose);
                switch (choiceInt) {
                    case 1 -> answer = Constants.EASY_MISSES_COUNT;
                    case 2 -> answer = Constants.NORMAL_MISSES_COUNT;
                    case 3 -> answer = Constants.HARD_MISSES_COUNT;
                    case 4 -> answer = Constants.ZERO_MISSES_COUNT;
                    case 5 -> answer = askCustomMaxMissesCount();
                    default -> {
                    }
                }
            } catch (Exception e) {

            }
        }
        return answer;
    }

    @Override
    public boolean askForPlayAgain() {
        boolean answer = false;
        boolean validInput = false;
        while (!validInput) {
            screenBuffer.clear(screenBuffer.rows() - 1, 0, screenBuffer.rows() - 1, screenBuffer.cols() - 1);
            String choose = makeDialog(Constants.PLAY_AGAIN_QUESTION).toUpperCase();
            if (Constants.PLAY_AGAIN_YES.equals(choose)) {
                answer = true;
                validInput = true;
            } else if (Constants.PLAY_AGAIN_NO.equals(choose)) {
                answer = false;
                validInput = true;
            }
        }
        return answer;
    }

    @Override
    public void draw(Model model) {
        screenBuffer.clear();
        Status status = model.getStatus();
        List<Character> misses = model.getMisses();
        int missesCount = misses.size();
        int maxMissesCount = model.getMaxMissesCount();
        String secretWordHint = model.getSecretWordHint();
        List<Character> wordRepresentation = model.getWordRepresentation();

        drawTitle();
        drawMessage(status);
        drawPicture(missesCount, maxMissesCount);
        drawMissesAndHint(misses, missesCount, maxMissesCount, secretWordHint);
        drawHits(wordRepresentation);
    }

    @Override
    public char getGuess() {
        char answer = ' ';
        screenBuffer.clear(screenBuffer.rows() - 1, 0, screenBuffer.rows() - 1, screenBuffer.cols() - 1);
        String choose = makeDialog(Constants.SELECT_GUESS).toUpperCase();
        if (choose.length() == 1) {
            char letter = choose.charAt(0);
            if (('А' <= letter && letter <= 'Я') || letter == 'Ё') {
                answer = letter;
            }
        }
        return answer;
    }

    private void say(String message) {
        screenBuffer.clear();
        int row = screenBuffer.rows() / 2 - 1;
        int col = screenBuffer.cols() / 2 - message.length() / 2;
        screenBuffer.set(row, col, message);
        row++;
        col = screenBuffer.cols() / 2 - Constants.PRESS_ENTER.length() / 2;
        screenBuffer.set(row, col, Constants.PRESS_ENTER);
        screenBuffer.writeToOutputStream(outputStream);
        try {
            inputStream.read();
        } catch (Exception e) {

        }
    }

    private String makeDialog(String question) {
        screenBuffer.set(screenBuffer.rows() - 1, 0, question);
        screenBuffer.writeToOutputStream(outputStream);
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
        return scanner.next();
    }

    private int askCustomMaxMissesCount() {
        int answer = -1;
        while (answer < 0 || answer > Constants.ALPHABET_SIZE) {
            screenBuffer.clear();
            String answerString = makeDialog(Constants.SELECT_MISSES_COUNT);
            try {
                answer = Integer.parseInt(answerString);
            } catch (NumberFormatException e) {
                answer = -1;
            }
        }
        return answer;
    }

    private void drawTitle() {
        int row = Constants.TITLE_ROW;
        int col = screenBuffer.cols() / 2 - Constants.GAME_NAME.length() / 2;
        screenBuffer.set(row, col, Constants.GAME_NAME);
    }

    private void drawMessage(Status status) {
        String message;
        if (status.gameStatus() != Status.GameStatus.NONE) {
            message = switch (status.gameStatus()) {
                case LOSE -> Constants.GAME_STATUS_LOSE;
                case WIN -> Constants.GAME_STATUS_WIN;
                default -> Constants.GAME_STATUS_NONE;
            };
        } else {
            message = switch (status.guessStatus()) {
                case ALREADY_GUESSED -> Constants.GUESS_STATUS_ALREADY_GUESSED;
                case INVALID -> Constants.GUESS_STATUS_INVALID;
                case INCORRECT -> Constants.GUESS_STATUS_INCORRECT;
                case CORRECT -> Constants.GUESS_STATUS_CORRECT;
                default -> Constants.GUESS_STATUS_NONE;
            };
        }
        int row = Constants.MESSAGE_ROW;
        int col = screenBuffer.cols() / 2 - message.length() / 2;
        screenBuffer.set(row, col, message);
    }

    @SuppressWarnings("MagicNumber")
    private void drawPicture(int missesCount, int maxMissesCount) {
        int pictureIdx;
        if (missesCount == 0 && maxMissesCount != 0) {
                pictureIdx = 0;
        } else {
            float coefficient = 10.f * missesCount / maxMissesCount;
            pictureIdx = Math.min(Math.round(coefficient), hangmanPictures.length - 1);
        }

        String[] picture = hangmanPictures[pictureIdx];
        int row = Constants.PICTURE_ROW;
        int col = Constants.PICTURE_COL;
        for (String pictureRow : picture) {
            screenBuffer.set(row, col, pictureRow);
            row++;
        }
    }

    private void drawMissesAndHint(List<Character> misses, int missesCount, int maxMissesCount, String hint) {
        int row = Constants.MISSES_ROW;
        int col = Constants.MISSES_COL_0;
        screenBuffer.set(row, col, Constants.MISSES_TITLE);

        row++;
        int missIdx = 0;
        int rowIdx = 0;
        while (missIdx < maxMissesCount) {
            int missesPerRow = rowIdx % 2 == 0 ? Constants.MISSES_PER_ROW_0 : Constants.MISSES_PER_ROW_1;
            col = rowIdx % 2 == 0 ? Constants.MISSES_COL_0 : Constants.MISSES_COL_1;
            for (int i = 0; i < missesPerRow; ++i) {
                if (missIdx >= maxMissesCount) {
                    break;
                }
                char miss = missIdx < missesCount ? misses.get(missIdx) : '_';
                screenBuffer.set(row + rowIdx, col, miss);
                missIdx++;
                col += 2;
            }
            rowIdx++;
        }

        if (missesCount >= maxMissesCount) {
            col = Constants.MISSES_COL_0;
            screenBuffer.set(row + rowIdx, col, Constants.HINT_TITLE);
            rowIdx++;

            int hintOffset = 0;
            int hintRowsCount = hint.length() / Constants.HINT_MAX_WIDTH;
            if (hint.length() % Constants.HINT_MAX_WIDTH != 0) {
                hintRowsCount++;
            }
            for (int i = 0; i < hintRowsCount; ++i) {
                screenBuffer.set(row + rowIdx, col, hint.substring(hintOffset));
                hintOffset += Constants.HINT_MAX_WIDTH;
                rowIdx++;
            }
        }
    }

    private void drawHits(List<Character> wordRepresentation) {
        String wordRepresentationString = wordRepresentation.stream()
            .map(Object::toString)
            .collect(Collectors.joining(" "));
        int row = Constants.HITS_ROW;
        int col = screenBuffer.cols() / 2 - wordRepresentationString.length() / 2;
        screenBuffer.set(row, col, wordRepresentationString);
    }

    /**
     * The screen buffer class.
     * <p>
     * This class is responsible for drawing on the screen.
     */
    private static class ScreenBuffer {
        @Getter private final int rows;
        @Getter private final int cols;
        private final char[][] buffer;

        /**
         * The screen buffer constructor.
         * <p>
         * This constructor is responsible for initializing the screen buffer.
         * @param rows the screen buffer rows
         * @param cols the screen buffer columns
         */
        ScreenBuffer(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            buffer = new char[rows][cols];
            for (char[] row : buffer) {
                Arrays.fill(row, ' ');
            }
        }

        /**
         * Clears the screen buffer.
         * <p>
         * This method is responsible for clearing the screen buffer by filling it with spaces.
         */
        public void clear() {
            for (char[] row : buffer) {
                Arrays.fill(row, ' ');
            }
        }

        /**
         * Clears the screen buffer rectangle.
         * <p>
         * This method is responsible for clearing the screen buffer rectangle by filling it with spaces.
         * @param top the top row of the rectangle
         * @param left the left column of the rectangle
         * @param bottom the bottom row of the rectangle
         * @param right the right column of the rectangle
         */
        public void clear(int top, int left, int bottom, int right) {
            if (0 <= top && top <= rows && 0 <= left && left <= cols
                && 0 <= bottom && bottom <= rows && 0 <= right && right <= cols) {
                int actualTop = Integer.min(top, bottom);
                int actualLeft = Integer.min(left, right);
                int actualBottom = Integer.max(top, bottom);
                int actualRight = Integer.max(left, right);
                for (int i = actualTop; i <= actualBottom; ++i) {
                    for (int j = actualLeft; j <= actualRight; ++j) {
                        buffer[i][j] = ' ';
                    }
                }
            } else {
                throw new IndexOutOfBoundsException(
                    String.format("clear(top=%d, left=%d, bottom=%d, right=%d), rows=%d, cols%d",
                        top, left, bottom, right, rows, cols));
            }
        }

        /**
         * Gets the character at the specified position.
         * <p>
         * This method is responsible for getting the character at the specified position.
         * @param row the row of the character
         * @param col the column of the character
         * @return the character at the specified position
         * @throws IndexOutOfBoundsException if the row or column is out of range
         */
        public char get(int row, int col) {
            if (0 <= row && row < rows && 0 <= col && col < cols) {
                return buffer[row][col];
            }
            throw new IndexOutOfBoundsException(
                String.format("get(row=%d, col=%d), rows=%d, cols=%d", row, col, rows, cols));
        }

        /**
         * Sets the character at the specified position.
         * <p>
         * This method is responsible for setting the character at the specified position.
         * @param row the row of the character
         * @param col the column of the character
         * @param value the character to set
         * @throws IndexOutOfBoundsException if the row or column is out of range
         */
        public void set(int row, int col, char value) {
            if (0 <= row && row < rows && 0 <= col && col < cols) {
                buffer[row][col] = value;
            } else {
                throw new IndexOutOfBoundsException(
                    String.format("set(row=%d, col=%d, value='%s'), rows=%d, cols=%d", row, col, value, rows, cols));
            }
        }

        /**
         * Sets the string at the specified position.
         * <p>
         * This method is responsible for setting the string at the specified position.
         * The string is written to the screen buffer rectangle from left to right.
         * If the length of the string is greater than the width of the screen buffer
         * rectangle, the excess characters are ignored.
         * @param row the row of the string
         * @param col the column of the string
         * @param value the string to set
         * @throws IndexOutOfBoundsException if the row or column is out of range
         */
        public void set(int row, int col, String value) {
            if (0 <= row && row < rows && 0 <= col && col < cols) {
                for (int colIndex = col, valueIndex = 0;
                     colIndex < cols && valueIndex < value.length();
                     ++colIndex, ++valueIndex) {
                    buffer[row][colIndex] = value.charAt(valueIndex);
                }
            } else {
                throw new IndexOutOfBoundsException(
                    String.format("set(row=%d, col=%d, value=\"%s\"), rows=%d, cols=%d", row, col, value, rows, cols));
            }
        }

        /**
         * Writes the content of the screen buffer to the output stream.
         * <p>
         * This method is responsible for writing the content of the screen buffer to the output stream.
         * The content of the screen buffer is written as a UTF-8 encoded string.
         * @param outputStream the output stream to write to
         */
        public void writeToOutputStream(OutputStream outputStream) {
            try {
                outputStream.write(this.toString().getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {

            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (char[] row : buffer) {
                builder.append(row).append('\n');
            }
            return builder.toString();
        }
    }
}

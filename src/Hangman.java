import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Hangman {


    public static final List<String> WORD_LIST = new ArrayList<>();

    public static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static final String PATH = "src/nouns.txt";

    public static void main(String[] args) throws IOException {

        // reading file with words
        loadWords(PATH);

        // main process
        while (true) {
            System.out.println("Начать новую игру или выйти? ('н' для новой игры и 'в' для выхода.)");
            String choice = READER.readLine().toLowerCase();

            if ("н".equals(choice)) {
                startGame();
            } else if ("в".equals(choice)) {
                READER.close();
                System.exit(0);
            } else {
                System.out.println("Попробуйте еще раз.");
            }

        }

    }

    public static void loadWords(String path) throws IOException {
        try (BufferedReader file = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = file.readLine()) != null) {
                WORD_LIST.add(line);
            }
        }
    }

    public static void startGame() throws IOException {
        // getting random word
        Random random = new Random();
        int x = random.nextInt(WORD_LIST.size());
        String word = WORD_LIST.get(x);
        StringBuilder maskedWord = new StringBuilder("*".repeat(word.length()));

        Set<Character> usedLetters = new LinkedHashSet<>();

        StringBuilder[] hang = new StringBuilder[8];
        resetHang(hang);

        //starting game
        int mistakes = 0;
        printHang(hang);
        System.out.println("\n" + maskedWord);


        while (true) {
            System.out.println("Введите букву:");
            char letter = READER.readLine().toLowerCase().charAt(0);

            if (!isRussianLetter(letter)) {
                System.out.println("Вы ввели неправильный символ. Попробуйте еще раз.");
                continue;
            }


            if (word.contains(String.valueOf(letter)) && !usedLetters.contains(letter)) {
                System.out.println("Правильно угадал!");
                changeMaskedWord(maskedWord, word, String.valueOf(letter));


                if (String.valueOf(maskedWord).equals(word)) {
                    System.out.println("Ура ты победил!!!! UwU");
                    System.out.println("Правильное слово:");
                    System.out.println(word);
                    break;
                }

            } else if (usedLetters.contains(letter)) {
                System.out.println("Такая буква уже была введена!");

            } else {
                System.out.println("Ух ошибочка.");
                mistakes++;
                changeHang(mistakes, hang);
            }

            usedLetters.add(letter);

            printHang(hang);

            System.out.println("'" + maskedWord + "'");
            System.out.println("Кол-во ошибок: " + mistakes);
            System.out.println("Использованные буквы: " + usedLetters);

            if (mistakes == 6) {
                System.out.println("Увы, ты проиграл(((");
                break;
            }
        }

    }

    private static boolean isRussianLetter(char letter) {
        letter = Character.toLowerCase(letter);
        return (letter >= 'а' && letter <= 'я') || letter == 'ё';
    }

    public static void printHang(StringBuilder[] hang) {
        for (StringBuilder i : hang) {
            System.out.println(i);
        }
    }

    public static void resetHang(StringBuilder[] hang) {
        hang[0] = new StringBuilder("      _______");
        hang[1] = new StringBuilder("     |/      |");
        hang[2] = new StringBuilder("     |");
        hang[3] = new StringBuilder("     |");
        hang[4] = new StringBuilder("     |");
        hang[5] = new StringBuilder("     |");
        hang[6] = new StringBuilder("     |");
        hang[7] = new StringBuilder("   __|___");
    }

    public static void changeHang(int mistakes, StringBuilder[] hang) {
        switch (mistakes) {
            case 1:
                hang[2].append("      (_)");
                break;
            case 2:
                hang[3].append("       |");
                break;
            case 3:
                hang[3].setCharAt(12, '\\');
                break;
            case 4:
                hang[3].append("/");
                break;
            case 5:
                hang[4].append("       |");
                hang[5].append("      /");
                break;
            case 6:
                hang[5].append(" \\");
                break;
        }
    }

    public static void changeMaskedWord(StringBuilder maskedWord, String word, String letter) {
        int index = word.indexOf(letter);

        while (index != -1) {
            maskedWord.setCharAt(index, letter.charAt(0));
            index = word.indexOf(letter, index + 1);
        }
    }


}


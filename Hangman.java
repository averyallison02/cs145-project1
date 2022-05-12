// CS 145
// Avery Allison

import java.util.Scanner;

public class Hangman {

    public static int tries = 15; // this value only for allowing game loop to start
    public static int canCheck;
    public static char guess;
    public static String toCheck;
    public static String[] aToCheck;
    public static int[] intToCheck;
    public static boolean solved = false;
    public static final boolean testingMode = true;

    public static void main(String[] args) {

        // for checking if a new game has started
        boolean newGame = true;

        boolean firstGuess = true;

        String secretWord = ""; // value fixes compiler complaining about initialization
        char[] charSecretWord = new char['0'];
        char[] charCurrentWord = new char['0'];
        String currentWord = "";

        // game loop
        while (true) {

            // generate a new word if it is a new game
            if (newGame) {

                HangmanWord word = new HangmanWord();

                // get word and arrays
                secretWord = word.getFullWord();
                currentWord = word.getUserWord();
                charSecretWord = secretWord.toCharArray();
                charCurrentWord = currentWord.toCharArray();

                setDifficulty();

                // if in testing mode, show the answer word
                if (testingMode) {
                    System.out.println("The secret word is: " + secretWord);
                }

                newGame = false;
                firstGuess = true;
                solved = false;
            }

            // two different displays for currentWord
            if (firstGuess) {

                System.out.println("The word is: " + currentWord);
                firstGuess = false;
            } else {

                System.out.println("The updated word is: " + currentWord);
            }

            // take a valid guess
            takeGuess(charSecretWord);

            // if not solving directly, check guess at chosen spaces and apply to current word
            if (guess != '!') {

                currentWord = tryGuess(charCurrentWord, charSecretWord);
                charCurrentWord = currentWord.toCharArray();
            } else {

                currentWord = trySolve(currentWord, secretWord);
            }

            // if current word is the secret word, solved becomes true
            if (currentWord.equals(secretWord)) {solved = true;}

            // if solved is true or tries == 0 run game end procedure
            if (solved || (tries == 0)) {newGame = gameEnd();}
        }
    }

    // set difficulty ints
    public static void setDifficulty() {

        Scanner sc = new Scanner(System.in);
        boolean validDif = false;

        while (!validDif) {

            // prompt user for valid difficulty
            System.out.print("Enter your difficulty: Easy (e), Intermediate (i), or Hard (h) ");
            char dIn = sc.nextLine().charAt(0);

            // make sure dIn is a letter and if so make it lowercase
            if (Character.isLetter(dIn)) {
                dIn = Character.toLowerCase(dIn);
            }

            // set tries and canCheck according to dIn
            switch (dIn) {
                case 'e':

                    tries = 15;
                    canCheck = 4;
                    validDif = true;
                    break;

                case 'i':

                    tries = 12;
                    canCheck = 3;
                    validDif = true;
                    break;

                case 'h':

                    tries = 10;
                    canCheck = 2;
                    validDif = true;
                    break;

                default:

                    System.out.println("Invalid difficulty. Try Again...");
                    break;
            }
        }
    }

    // take guess and make sure it can be properly processed
    public static void takeGuess(char[] fWord) {

        Scanner sc = new Scanner(System.in);
        String strGuess;
        boolean goingToSolve = false;

        while (true) {
            // get letter guess
            System.out.println("Guesses Remaining: " + tries);
            System.out.print("Please enter the letter you want to guess: ");
            strGuess = sc.nextLine();
            guess = strGuess.charAt(0);

            // guess is '!' if user wants to solve
            if (strGuess.toLowerCase().equals("solve")) {

                guess = '!';
                goingToSolve = true;
                break;
            }

            // if not, make sure character is a letter
            else {

                if (Character.isLetter(guess)) {

                    guess = Character.toLowerCase(guess);
                    break;
                }
            }
            // if no break yet, specify invalid input
            System.out.println("Your input is not valid. Try again.");
        }

        boolean allCharsDigits;
        boolean outBounds;

        // get spaces to guess
        while (!goingToSolve) {

            allCharsDigits = true;
            outBounds = false;

            System.out.println("Guesses Remaining: " + tries);
            System.out.println("Please enter the spaces you want to check (separated by spaces):");
            toCheck = sc.nextLine();

            // make string array of spaces to check
            aToCheck = toCheck.split(" ");
            intToCheck = new int[aToCheck.length];

            // if incorrect number of spaces to check, redo loop
            if (aToCheck.length != canCheck) {

                System.out.println("Your input is not valid. Try again.");
                continue;
            }

            // make sure every char in the array is a digit
            for (int i = 0; i<aToCheck.length; i++) {
                for (int j = 0; j<aToCheck[i].length(); j++) {

                    if (!(Character.isDigit(aToCheck[i].charAt(j)))) {
                        allCharsDigits = false;
                    }
                }
            }
            if (!allCharsDigits) {

                System.out.println("Your input is not valid. Try again.");
                continue;
            }

            // populate intToCheck with int versions of aToCheck
            for (int i = 0; i < intToCheck.length; i++) {
                intToCheck[i] = Integer.parseInt(aToCheck[i]);
            }

            // check intToCheck for index out of bounds of fWord
            for (int i = 0; i < intToCheck.length; i++) {

                if (intToCheck[i] < 0 || (intToCheck[i] >= fWord.length)) {

                    System.out.println("Your input is not valid. Try again.");
                    outBounds = true;
                }
            }

            if (outBounds) {

                continue;
            }

            break;
        }
    }

    public static String tryGuess(char[] cWord, char[] fWord) {

        boolean foundLetter = false;

        // iterate over intToCheck and check if each corresponding value in cWord is guess
        for (int i = 0; i < intToCheck.length; i++) {

            if (fWord[intToCheck[i]] == guess) {

                // dont decrement guesses when done
                foundLetter = true;

                // show letter in cWord
                cWord[intToCheck[i]] = guess;
            }
        }

        // if user found a letter, dont decrement tries, do if they did not
        if (foundLetter) {

            System.out.println("Your guess is in the word!");
            return String.valueOf(cWord);

        } else {

            System.out.println("Your letter was not found in the spaces you provided.");
            tries--;
            return String.valueOf(cWord);
        }
    }

    public static String trySolve(String cWord, String fWord) {

        Scanner sc = new Scanner(System.in);
        String wordGuess;

        // prompt for word guess
        System.out.print("Please solve the word: ");
        wordGuess = sc.nextLine();
        wordGuess = wordGuess.toLowerCase();

        // if wordGuess is fWord, return to cWord
        if (wordGuess.equals(fWord)) {

            return wordGuess;
        } else {

            tries--;
            System.out.println("That is not the secret word.");
            return cWord;
        }
    }

    public static boolean gameEnd() {

        Scanner sc = new Scanner(System.in);
        char playAgain;

        // say if user solved word or not
        if (solved) {

            System.out.println("Guesses Remaining: " + tries);
            System.out.println("You have guessed the word! Congratulations");

        } else {

            System.out.println("Guesses Remaining: " + tries);
            System.out.println("You have failed to guess the word... :(");
        }

        // ask user if they want to play again
        System.out.print("Would you like to play again? Yes(y) or No(n) ");
        playAgain = sc.nextLine().charAt(0);
        Character.toLowerCase(playAgain);

        // quit if no, reset newGame if yes
        if (playAgain == 'n') {

            System.exit(0);

        } else  if (playAgain == 'y') {

            return true;
        }

        return false;
    }
}
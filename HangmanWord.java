public class HangmanWord {

    private String fullWord;
    private char[] userWord;

    // generates fullWord and userWord on construction
    public HangmanWord() {

       fullWord = RandomWord.newWord();
       userWord = new char[fullWord.length()];
       
       // fill userWord with hyphens
       for (int i = 0; i < userWord.length; i++) {
           userWord[i] = '-';
       }
    }

    // returns string value of fullWord
    public String getFullWord() {

        return fullWord;
    }

    // returns string value of userWord
    public String getUserWord() {

        return String.valueOf(userWord);
    }
}

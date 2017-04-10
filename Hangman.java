import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class HangmanC implements HandgmanManager. The constructor
 * does all that is needed to initialize a hangman game.
 * The record method does most of the work for a hangman game/
 * This program plays a game with the user via the console.
 * @author Taylor Woehrle
 * @version 1.0.0
 */
public class Hangman implements HangmanManager {
    /**The current set of words being considered.*/
    private Set<String> words;

    /**The number of guesses the user has remaining.*/
    private int guessesLeft;

    /**Current set letters that have been guessed by the user.*/
    private SortedSet<Character> guesses;

    /**The dictionary being used by the user.*/
    private List<String> dictionary;

    /**The length the target word length.*/
    private int length;

    /**The pattern to be returned to the user.*/
    private String pattern;

    /**
     * @param inputDictionary the dictionary of words used.
     * @param wordLength the target word length.
     * @param max the make number of wrong guesses the user is
     * allowed to make.
     * @throws IllegalArgumentException if length <= 0.
     */
    public Hangman(final List<String> inputDictionary, final int wordLength,
                   final int max) throws IllegalArgumentException {
        if (wordLength <= 0) {
            throw new IllegalArgumentException("Hangman constructor length"
                    + " error. The input length is " + wordLength
                        + " but the length must greater than 0");
        } else {
            this.dictionary = inputDictionary;
            this.length = wordLength;
            this.guessesLeft = max;
            String emptyPattern = "-";
            for (int i = 1; i < wordLength; i++) {
                emptyPattern = emptyPattern + "-";
            }
            this.pattern = emptyPattern;
            this.guesses = new TreeSet<Character>();
            setWords(wordLength);
        }
    }

    /** This method sets the words list. In basic the words() will only
     * contain 1 randomly selected from a list of words of a specified length.
     * @param charLength The number of characters to be selected in a word.
     * @throws IllegalArgumentException if input length is 0 or less
     */
    private void setWords(final int charLength) {
    System.out.println("SET WORDS");
        if (length <= 0) {
            throw new IllegalArgumentException("Hangman setWords length error."
                + " Input length is " + charLength + "but the length must "
                    + "greater than 0");
        } else {
            List<String> candidateList = new ArrayList<String>();
            for (int i = 0; i < this.dictionary.size(); i++) {
                if (this.dictionary.get(i).length() == this.length) {
                    candidateList.add(this.dictionary.get(i));
                }
            }
            if (candidateList.isEmpty()) {
                throw new IllegalArgumentException("setWords() error: no words"
                                + " found of length " + this.length);
            }
            int randomPick = generateRandom(candidateList.size());
            Set<String> chosenWord = new HashSet<String>();
            chosenWord.add(candidateList.get(randomPick));
            this.words = chosenWord;
        }
    }

    /**
     * Generates a random number.
     * @param max The upper bound limit of random numbers (exclusive)
     * @return a random number between [0, max]
     */
    private int generateRandom(final int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }
    //COMPLETED
    /**
     * Accesses the set of candidate goal words.
     * @return the set of candidate goal words
     */
    public Set<String> words() {
        return this.words;
    }

    //COMPLETED
    /**
     * Accesses the number of allowable wrong guesses.
     * @return the number of wrong guesses the user has left
     */
    public int guessesLeft() {
        return this.guessesLeft;
    }

    //COMPLETED
    /**
     * Accesses the set of letters already guessed by the user.
     * @return the current set of letters guessed by the user
     */
    public SortedSet<Character> guesses() {
        return this.guesses;
    }

    /**
     * Return the hangman-style display pattern of letters and dashes
     * appropriate to the current state based on the letters already
     * guessed and the goal.
     * @throws IllegalStateException if there is no goal word
     * @return the hangman-style pattern to be displayed to the user
     */
    public String pattern() {
        return this.pattern;
    }

    /**
     * This method does most of the work by recording the next
     * guess made by the user.
     * Record state changes based on new letter guess.
     * @throws IllegalStateException if no guesses left or no goal word
     * @throws IllegalArgumentException if letter is already guessed
     * @param guess the letter being guessed
     *   [Precondition: must be lower-case letter]
     *   [Precondition: must not be among letters already guessed]
     * @return the number of occurrences of the guessed letter in the goal
     */
    public int record(final char guess) {
        if (this.guessesLeft <= 0 || this.words.isEmpty()) {
            throw new IllegalStateException("Hangman.record() state exception");
        }
        if (guesses.add(guess)) {
            Iterator<String> wordItr = this.words.iterator();
            char[] hangmanWord = wordItr.next().toCharArray();
            int occuranceCount = 0;
            for (int i = 0; i < hangmanWord.length; i++) {
                if (hangmanWord[i] == (guess)) {
                    occuranceCount++;
                }
            }
            if (occuranceCount == 0) {
                this.guessesLeft = this.guessesLeft - 1;
            }
            this.updatePattern(guess);
            return occuranceCount;
        } else {
            throw new IllegalArgumentException("Hangman.record() exception "
                                       + guess + " has already been guessed");
        }
    }

    /**
     * This method updates the pattern.
     * @param updateChar the character to try and update the pattern with
     * @return True boolean if update occurs. False if nothing updates.
     */
    private boolean updatePattern(final char updateChar) {
        Iterator<String> wordItr = this.words.iterator();
        String selectedWord = wordItr.next();
        char[] selectedWordChars = selectedWord.toCharArray();
        char[] patternChars = this.pattern.toCharArray();
        boolean returnBool = false;
        for (int i = 0; i < selectedWord.length(); i++) {
            if (selectedWordChars[i] == updateChar) {
                returnBool = true;
                patternChars[i] = updateChar;
            }
        }
        this.pattern = new String(patternChars);
        return returnBool;
    }
}

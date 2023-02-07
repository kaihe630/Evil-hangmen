package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    public int length;
    public Set<String> theDictionary;
    public SortedSet<Character> guessed;
    public String word;
    public static int guessesLeft;

    public static int getGuessesLeft() {
        return guessesLeft;
    }

    public static void setGuessesLeft(int guessesLeft) {
        EvilHangmanGame.guessesLeft = guessesLeft;
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        if(wordLength == 0){
            throw new EmptyDictionaryException();
        }
        this.length = wordLength;
        this.guessed = new TreeSet<Character>();
        this.theDictionary = new HashSet<String>();
        Scanner scanner = new Scanner(dictionary);
        try{
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                line = line.toLowerCase(Locale.ROOT);
                String[] words;
                words = line.split(" ");
                for(int i = 0; i < words.length; i++){
                    if(words[i].length() == wordLength){
                        this.theDictionary.add(words[i]);
                    }
                }
            }
        }
        finally{
            scanner.close();
        }
        this.word = "";
        for(int i = 0; i < wordLength; i++){
            word+="-";
        }

        if(this.theDictionary.size() == 0){
            throw new EmptyDictionaryException();
        }


    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if(guess >='A' && guess <= 'Z')
            guess = (char) ('a'-'A' + guess);
        boolean alreadyGuessed = this.guessed.add(guess);
        if(!alreadyGuessed){
            throw new GuessAlreadyMadeException();
        }
        int exponent = ((int) Math.pow((double) 2, (double) this.length));
        Set<String>[] theSets = new Set[exponent + 1];
        int bssf = -1;

        Iterator iterator = this.theDictionary.iterator();
        while(iterator.hasNext()){
            String current = (String) iterator.next();
            BitSet matchingLetters = new BitSet(this.length);
            for(int i = 0; i < this.length; i++){
                if(current.charAt(i) == guess){
                    matchingLetters.set(i);
                }
            }
            if(matchingLetters.isEmpty()){
                if(theSets[exponent] == null){
                    theSets[exponent] = new HashSet<String>();
                }
                theSets[exponent].add(current);
                bssf = exponent;
            }
            else{
                int theLocation = (int) matchingLetters.toLongArray()[0];
                if(theSets[theLocation] == null){
                    theSets[theLocation] = new HashSet<String>();
                }
                theSets[theLocation].add(current);
                bssf = theLocation;
            }

        }
        if(bssf == -1)
            return theDictionary; //should never be reached

        for(int i = 0; i < exponent+1; i++){
            if(bssf != i) {
                if (theSets[i] != null && theSets[bssf].size() < theSets[i].size())
                    bssf = i;
                if (theSets[i] != null && theSets[bssf].size() == theSets[i].size()) {
                    int numB = 0;
                    int numI = 0;
                    Iterator iteratorB = theSets[bssf].iterator();
                    Iterator iteratorI = theSets[i].iterator();
                    String sampleB = (String) iteratorB.next();
                    String sampleI = (String) iteratorI.next();
                    for (int j = 0; j < this.length; j++) {
                        if (sampleI.charAt(j) == guess) {
                            numI++;
                        }
                        if (sampleB.charAt(j) == guess) {
                            numB++;
                        }
                    }
                    if (numI < numB) {
                        bssf = i;
                    } else if(numI == numB) {
                        for (int j = this.length - 1; j > 0; j--) {
                            if (sampleB.charAt(j) != guess && sampleI.charAt(j) == guess) {
                                bssf = i;
                            }
                        }


                    }


                }
            }

        }

        if(theSets[exponent] != null && theSets[bssf].size() == theSets[exponent].size()){
            bssf = exponent;
        }

        Iterator iteratorB = theSets[bssf].iterator();
        String sampleB = (String) iteratorB.next();
        StringBuilder sb = new StringBuilder(this.word);
        for(int i = 0; i < length; i++){
            if(sampleB.charAt(i) == guess){
                sb.delete(i,i+1);
                sb.insert(i,guess);
            }
        }
        this.word = sb.toString();



        if(theSets[bssf] != null)
            this.theDictionary = theSets[bssf];

        return this.theDictionary;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessed;
    }
}

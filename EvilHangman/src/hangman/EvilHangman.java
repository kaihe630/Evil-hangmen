package hangman;

import java.io.File;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        File theFile = new File(args[0]);
        EvilHangmanGame theGame = new EvilHangmanGame();
        int size = Integer.decode(args[1]);
        int guessesLeft = Integer.decode(args[2]);
        try{
            theGame.startGame(theFile,size);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        boolean won = false;
        boolean inError = false;
        while(guessesLeft > 0 && !won){
            if(! inError) {
                if(guessesLeft > 1)
                    System.out.println("You have " + guessesLeft + " guesses left");
                else
                    System.out.println("You have 1 guess left");
                System.out.println("Used Letters: " + theGame.guessed.toString());
                System.out.println("Word: " + theGame.word);
            }
            inError = false;
            System.out.print("Enter guess: ");
            String currentWord = theGame.word;
            try{
                Scanner keyboard = new Scanner(System.in);
                String input = keyboard.next();
                input = input.toLowerCase(Locale.ROOT);
                while(input.length() > 1 || input.charAt(0) < 'A' || input.charAt(0) > 'z'){
                    System.out.print("Invalid input. Enter guess: ");
                    input = keyboard.next();
                }

                theGame.makeGuess(input.charAt(0));
                if(currentWord.equals(theGame.word)){
                    guessesLeft--;
                    System.out.println("Sorry, there are no " + input + "\'s\n");
                }
                else{
                    int numberNew = 0;
                    for(int i = 0; i < theGame.length; i++){
                        if(theGame.word.charAt(i) == input.charAt(0))
                            numberNew++;
                    }
                    System.out.println("Yes, there are " + numberNew + input.charAt(0) + "\'s\n");
                }
                if(theGame.theDictionary.size() == 1){
                    Iterator iterator = theGame.theDictionary.iterator();
                    String theWord = (String) iterator.next();
                    if(theWord.equals(theGame.word))
                        won = true;
                }

            }
            catch(GuessAlreadyMadeException e){
                System.out.print("Guess already made! ");
                inError = true;
            }

        }
        if(won){
            System.out.println("You win! The word was " + theGame.word);
        }
        else{
            Iterator iterator = theGame.theDictionary.iterator();
            String theWord = (String) iterator.next();
            System.out.println("You lose! The word was " + theWord);
        }


    }

}

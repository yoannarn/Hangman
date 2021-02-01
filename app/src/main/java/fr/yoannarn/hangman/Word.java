package fr.yoannarn.hangman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Word {
    private String word;

    private List<Character> letterToFound = new ArrayList<>();

    public Word(String word){
        this.word = word;
        char[] wordToChar = word.toCharArray();

        for(char letter: wordToChar){
            if(!letterToFound.contains(letter))
                letterToFound.add(letter);
        }
    }

    public String getWord() {
        return word;
    }

    public String getWordWithDiscoverLetters(){
        String hiddenWord = new String();
        for(char letter : word.toCharArray()){
            if(letterToFound.contains(letter)){
                hiddenWord += "_ ";
            }else{
                hiddenWord+=letter+ " ";
            }
        }
        return hiddenWord;
    }

    public List<Character> getLetterToFound() {
        return letterToFound;
    }
}

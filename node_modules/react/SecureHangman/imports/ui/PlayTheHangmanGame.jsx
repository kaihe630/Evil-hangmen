import React, { useState, useEffect } from "react";
import dictionaryWords from "./dictionary";

export default function PlayTheHangmanGame() {
  const [numChars, setNumChars] = useState("");
  const [numGuesses, setNumGuesses] = useState("");
  const [word, setWord] = useState("");
  const [guessesLeft, setGuessesLeft] = useState("");
  const [gameOver, setGameOver] = useState(false);
  const [guessedChars, setGuessedChars] = useState(new Set());
  const [displayedWord, setDisplayedWord] = useState("");

  const handleNumCharsChange = (event) => {
    setNumChars(event.target.value);
  };

  const handleNumGuessesChange = (event) => {
    setNumGuesses(event.target.value);
  };

  const handleStartGame = () => {
    const words = dictionaryWords.split("\n");
    const filteredWords = words.filter((word) => word.length === Number(numChars));
    const randomIndex = Math.floor(Math.random() * filteredWords.length);
    const randomWord = filteredWords.length > 0 ? filteredWords[randomIndex] : "";
    setWord(randomWord);
    setGuessesLeft(numGuesses);
    setGameOver(false);
  };

  const handleGuess = (event) => {

    const char = event.target.value.toLowerCase();
    if (!/^[a-z]$/.test(char) || guessedChars.has(char) || gameOver) {
      return;
    }
    const newGuessedChars = new Set(guessedChars);
    newGuessedChars.add(char);
    setGuessedChars(newGuessedChars);


    if (word.includes(char)) {
      const newDisplayedWord = word.replace(new RegExp(`[^${Array.from(newGuessedChars).join("")}]`, "g"), "_");
      setDisplayedWord(newDisplayedWord);
    } else {
      setGuessesLeft(guessesLeft - 1);
    }

    if (guessesLeft === 1 || !displayedWord.includes("_")) {
      setGameOver(true);
    }
  };


  useEffect(() => {
    if (numChars && numGuesses) {
      setWord("");
      setDisplayedWord("");
      setGuessesLeft("");
      setGuessedChars(new Set());
      setGameOver(false);
    }
  }, [numChars, numGuesses]);

  useEffect(() => {
    setDisplayedWord(word.replace(/[a-z]/g, "_"));
  }, [word]);

  return (
    <div>
      {!gameOver ? (
        <div>
          {!word ? (
            <div>
              <label>
                Number of characters in word:
                <input type="number" value={numChars} onChange={handleNumCharsChange} />
              </label>
              <br />
              <label>
                Number of guesses allowed:
                <input type="number" value={numGuesses} onChange={handleNumGuessesChange} />
              </label>
              <br />
              <button onClick={handleStartGame}>Start game</button>
            </div>
          ) : (
            <div>
              <p>Word: {displayedWord}</p>
              <p>Guesses left: {guessesLeft}</p>
              <p>Guessed characters: {Array.from(guessedChars).join(", ")}</p>
              <br />
              <label>
                Guess a character:
                <input type="text" value="" maxLength="1" pattern="[a-zA-Z]" onChange={handleGuess} />
              </label>
            </div>
          )}
        </div>
      ) : (
        <div>
          <p>{word}</p>
          {guessesLeft === 0 ? (
            <p>You lose!</p>
          ) : (
            <p>You win!</p>
          )}
          <button onClick={() => window.location.reload()}>Play again</button>
        </div>
      )}
    </div>
  );
}


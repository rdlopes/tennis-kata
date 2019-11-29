package com.nexity.kata.steps;

import com.nexity.kata.TennisGame;
import com.nexity.kata.TennisGame.Players;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.nexity.kata.TennisGame.Players.PLAYER1;
import static com.nexity.kata.TennisGame.Players.PLAYER2;
import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoringSteps {

    private TennisGame game;

    @Given("a new game")
    public void aNewGame() throws Exception {
        this.game = new TennisGame();
    }

    @When("(\\w+) wins")
    public void playerWins(Players player) {
        this.game.playerWon(player);
    }

    @Then("set is (\\w+) with score (\\d+)\\/(\\d+)")
    public void setIsInStateWithScore(TennisGame.States state, int firstPlayerScore, int secondPlayerScore) {
        assertThat(this.game.getState()).isEqualTo(state);
        assertThat(this.game.getScore(PLAYER1)).isEqualTo(firstPlayerScore);
        assertThat(this.game.getScore(PLAYER2)).isEqualTo(secondPlayerScore);
    }

    @When("both players reach score 40")
    public void bothPlayersReachScore40() {
        this.game.playerWon(PLAYER1);
        this.game.playerWon(PLAYER1);
        this.game.playerWon(PLAYER1);

        this.game.playerWon(PLAYER2);
        this.game.playerWon(PLAYER2);
        this.game.playerWon(PLAYER2);
    }
}

#order: 1
Feature: Manage game score

  As a tennis referee
  I want to manage the score of a set of a tennis match between 2 players with simple game rules
  In order to display the current game score of each player.

  * Set starts with a score of 0 point for each player
  * Each time a player wins a point, the set score changes as follow: 0 -> 15 -> 30 -> 40 -> win set

  Scenario: Game start

    Given a new game
    Then set is PLAYING with score 0/0


  Scenario: Player 1 wins game

    Given a new game

    When PLAYER1 wins
    Then set is PLAYING with score 15/0

    When PLAYER1 wins
    Then set is PLAYING with score 30/0

    When PLAYER1 wins
    Then set is PLAYING with score 40/0

    When PLAYER1 wins
    Then set is SET_PLAYER1 with score 40/0


  Scenario: Player 2 wins game

    Given a new game

    When PLAYER2 wins
    Then set is PLAYING with score 0/15

    When PLAYER2 wins
    Then set is PLAYING with score 0/30

    When PLAYER2 wins
    Then set is PLAYING with score 0/40

    When PLAYER2 wins
    Then set is SET_PLAYER2 with score 0/40


#order: 2
Feature: Manage rule DEUCE

  As a tennis referee
  I want to manage the specifics of the DEUCE rule at the end of the game
  In order to display the current game score of each player.

  * If the 2 players reach the score 40, the DEUCE rule is activated.
  * If the score is DEUCE, the player who wins the point takes the ADVANTAGE.
  * If the player who has the ADVANTAGE wins the point, he wins the game.
  * If the player who has the ADVANTAGE loses the point, he score is DEUCE.

  Scenario: 2 players reach the score 40

    Given a new game
    When both players reach score 40
    Then set is DEUCE with score 40/40

  Scenario: Player 1 wins advantage

    Given a new game
    When both players reach score 40
    And PLAYER1 wins
    Then set is ADVANTAGE_PLAYER1 with score 40/40

  Scenario: Player 1 wins game after ADVANTAGE

    Given a new game
    When both players reach score 40
    And PLAYER1 wins
    And PLAYER1 wins
    Then set is SET_PLAYER1 with score 40/40

  Scenario: Player 1 loses advantage

    Given a new game
    When both players reach score 40
    And PLAYER1 wins
    And PLAYER2 wins
    Then set is DEUCE with score 40/40

  Scenario: Player 2 wins advantage

    Given a new game
    When both players reach score 40
    And PLAYER2 wins
    Then set is ADVANTAGE_PLAYER2 with score 40/40

  Scenario: Player 2 wins game after ADVANTAGE

    Given a new game
    When both players reach score 40
    And PLAYER2 wins
    And PLAYER2 wins
    Then set is SET_PLAYER2 with score 40/40

  Scenario: Player 2 loses advantage

    Given a new game
    When both players reach score 40
    And PLAYER2 wins
    And PLAYER1 wins
    Then set is DEUCE with score 40/40



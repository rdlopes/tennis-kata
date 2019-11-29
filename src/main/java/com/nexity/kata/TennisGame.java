package com.nexity.kata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static com.nexity.kata.TennisGame.Players.PLAYER1;
import static com.nexity.kata.TennisGame.Players.PLAYER2;
import static com.nexity.kata.TennisGame.States.*;
import static com.nexity.kata.TennisGame.States.ADVANTAGE_PLAYER1;

@Slf4j
public class TennisGame {

    private static final List<Integer> SCORE_MAP = Arrays.asList(0, 15, 30, 40);

    public enum States {PLAYING, DEUCE, ADVANTAGE_PLAYER1, ADVANTAGE_PLAYER2, SET_PLAYER1, SET_PLAYER2}

    public enum Players {
        PLAYER1(ADVANTAGE_PLAYER1, SET_PLAYER1),
        PLAYER2(ADVANTAGE_PLAYER2, SET_PLAYER2);
        public final States advantageState;
        public final States setState;

        Players(States advantageState, States setState) {
            this.advantageState = advantageState;
            this.setState = setState;
        }
    }

    private final StateMachine<States, Players> stateMachine;

    public TennisGame() throws Exception {
        this.stateMachine = buildStateMachine();
    }

    private StateMachine<States, Players> buildStateMachine() throws Exception {
        Builder<States, Players> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .autoStartup(true);
        builder.configureStates()
                .withStates()
                .initial(PLAYING, stateContext -> {
                    stateContext.getExtendedState().getVariables().put(PLAYER1, 0);
                    stateContext.getExtendedState().getVariables().put(PLAYER2, 0);
                })
                .states(EnumSet.allOf(States.class));
        builder.configureTransitions()
                // Winning
                .withExternal()
                .source(PLAYING).target(SET_PLAYER1)
                .event(PLAYER1)
                .guard(context -> {
                    return context.getExtendedState().get(PLAYER1, Integer.class) == SCORE_MAP.size();
                })

                .and()
                .withExternal()
                .source(PLAYING).target(SET_PLAYER2)
                .event(PLAYER2)
                .guard(context -> {
                    return context.getExtendedState().get(PLAYER2, Integer.class) == SCORE_MAP.size();
                })

                // DEUCE rule
                .and()
                .withExternal()
                .source(PLAYING).target(DEUCE)
                .event(PLAYER1)
                .guard(context -> {
                    return context.getExtendedState().get(PLAYER1, Integer.class) == SCORE_MAP.size() - 1 &&
                            context.getExtendedState().get(PLAYER2, Integer.class) == SCORE_MAP.size() - 1;
                })

                .and()
                .withExternal()
                .source(PLAYING).target(DEUCE)
                .event(PLAYER2)
                .guard(context -> {
                    return context.getExtendedState().get(PLAYER1, Integer.class) == SCORE_MAP.size() - 1 &&
                            context.getExtendedState().get(PLAYER2, Integer.class) == SCORE_MAP.size() - 1;
                })

                // ADVANTAGE player 1
                .and()
                .withExternal()
                .source(DEUCE).target(ADVANTAGE_PLAYER1)
                .event(PLAYER1)

                .and()
                .withExternal()
                .source(ADVANTAGE_PLAYER1).target(DEUCE)
                .event(PLAYER2)

                .and()
                .withExternal()
                .source(ADVANTAGE_PLAYER1).target(SET_PLAYER1)
                .event(PLAYER1)

                // ADVANTAGE player 2
                .and()
                .withExternal()
                .source(DEUCE).target(ADVANTAGE_PLAYER2)
                .event(PLAYER2)

                .and()
                .withExternal()
                .source(ADVANTAGE_PLAYER2).target(DEUCE)
                .event(PLAYER1)

                .and()
                .withExternal()
                .source(ADVANTAGE_PLAYER2).target(SET_PLAYER2)
                .event(PLAYER2);

        return builder.build();
    }

    public int getScore(Players player) {
        int points = stateMachine.getExtendedState().get(player, Integer.class);
        return SCORE_MAP.get(Math.min(SCORE_MAP.size() - 1, points));
    }

    public States getState() {
        return stateMachine.getState().getId();
    }

    public void playerWon(Players player) {
        int playerPoints = stateMachine.getExtendedState().get(player, Integer.class);
        stateMachine.getExtendedState().getVariables().put(player, playerPoints + 1);
        stateMachine.sendEvent(player);
    }

}

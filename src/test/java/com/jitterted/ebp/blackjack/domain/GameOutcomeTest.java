package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeTest {

  @Test
  public void playerBeatsDealerThenOutcomeIsPlayerWins() throws Exception {
    Deck stubDeck = new StubDeck(Rank.TEN, Rank.EIGHT, Rank.QUEEN, Rank.NINE);
    Game game = new Game(stubDeck);

    game.initialDeal();
    game.playerStands();
    game.dealerTurn();

    assertThat(game.determineOutcome())
        .isEqualTo("You beat the Dealer! \uD83D\uDCB5");
  }

  @Test
  public void playerGoesBustAndLoses() throws Exception {
    Deck stubDeck = new StubDeck(Rank.TEN, Rank.EIGHT, Rank.QUEEN, Rank.SIX, Rank.THREE);
    Game game = new Game(stubDeck);

    game.initialDeal();
    game.playerHits();

    assertThat(game.determineOutcome())
        .isEqualTo("You Busted, so you lose.  💸");
  }

}
package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

  @Test
  public void startGameThenInitialCardsAreDealt() throws Exception {
    Game game = new Game();
    BlackjackController blackjackController = new BlackjackController(game);

    blackjackController.startGame();

    assertThat(game.playerHand().cards())
        .hasSize(2);
  }

  @Test
  public void gameViewPopulatesViewModel() throws Exception {
    Deck deck = new StubDeck(List.of(new Card(Suit.DIAMONDS, Rank.TEN),
                                     new Card(Suit.HEARTS, Rank.TWO),
                                     new Card(Suit.DIAMONDS, Rank.KING),
                                     new Card(Suit.CLUBS, Rank.THREE)));
    Game game = new Game(deck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.viewGame(model);

    GameView gameView = (GameView) model.getAttribute("gameView");

    assertThat(gameView.getDealerCards())
        .containsExactly("2♥", "3♣");
    assertThat(gameView.getPlayerCards())
        .containsExactly("10♦", "K♦");
  }

  @Test
  public void hitCommandDealsAdditionalCardToPlayer() throws Exception {
    Game game = new Game();
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    blackjackController.hitCommand();

    assertThat(game.playerHand().cards())
        .hasSize(3);
  }

  @Test
  public void whenPlayerIsDoneAfterBustingRedirectToDonePage() throws Exception {
    Deck stubDeck = new StubDeck(List.of(new Card(Suit.DIAMONDS, Rank.TEN),
                                         new Card(Suit.HEARTS, Rank.TWO),
                                         new Card(Suit.DIAMONDS, Rank.KING),
                                         new Card(Suit.CLUBS, Rank.THREE),
                                         new Card(Suit.DIAMONDS, Rank.SEVEN)));

    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String view = blackjackController.hitCommand();

    assertThat(view)
        .isEqualTo("redirect:/done");
  }

  @Test
  public void donePageShowsFinalGameViewWithOutcome() throws Exception {
    Game game = new Game();
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.gameDone(model);

    assertThat(model.getAttribute("gameView"))
        .isInstanceOf(GameView.class);

    String outcome = (String) model.getAttribute("outcome");

    assertThat(outcome)
        .isNotBlank();
  }

  @Test
  public void standResultsInGamePlayerIsDone() throws Exception {
    Game game = new Game();
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String redirect = blackjackController.standCommand();

    assertThat(redirect)
        .isEqualTo("redirect:/done");

    assertThat(game.isPlayerDone())
        .isTrue();
  }

  @Test
  public void standResultsInPlayerBeatsDealerWhoDrewAdditionalCard() throws Exception {
    Deck stubDeck = new StubDeck(List.of(new Card(Suit.DIAMONDS, Rank.TEN),
                                         new Card(Suit.HEARTS, Rank.QUEEN),
                                         new Card(Suit.DIAMONDS, Rank.KING),
                                         new Card(Suit.CLUBS, Rank.THREE),
                                         new Card(Suit.DIAMONDS, Rank.SIX)));
    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    blackjackController.standCommand();

    assertThat(game.dealerHand().cards())
        .hasSize(3);

    assertThat(game.determineOutcome())
        .isEqualTo("You beat the Dealer! 💵");
  }

}
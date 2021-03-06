package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.domain.port.GameMonitor;

public class Game {

  private final Deck deck;

  private final Hand dealerHand = new Hand();
  private final Hand playerHand = new Hand();
  private final GameMonitor gameMonitor;

  private boolean playerDone = false;

  public Game() {
    this(new Deck());
  }

  public Game(Deck deck) {
    this.deck = deck;
    this.gameMonitor = game -> {};
  }

  public Game(Deck deck, GameMonitor gameMonitor) {
    this.deck = deck;
    this.gameMonitor = gameMonitor;
  }

  public Game(GameMonitor gameMonitor) {
    this.deck = new Deck();
    this.gameMonitor = gameMonitor;
  }

  public void initialDeal() {
    dealRoundOfCards();
    dealRoundOfCards();
  }

  private void dealRoundOfCards() {
    // why: players first because this is the rule
    playerHand.drawFrom(deck);
    dealerHand.drawFrom(deck);
  }

  public String determineOutcome() {
    if (playerHand.isBlackjack()) {
      return "You won Blackjack, congratulations!!";
    } else if (playerHand.isBusted()) {
      return("You Busted, so you lose.  💸");
    } else if (dealerHand.isBusted()) {
      return("Dealer went BUST, Player wins! Yay for you!! 💵");
    } else if (playerHand.beats(dealerHand)) {
      return("You beat the Dealer! 💵");
    } else if (playerHand.pushes(dealerHand)) {
      return("Push: The house wins, you Lose. 💸");
    } else {
      return("You lost to the Dealer. 💸");
    }
  }

  public void dealerTurn() {
    // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
    if (!playerHand.isBusted()) {
      while (dealerHand.dealerMustDrawCard()) {
        dealerHand.drawFrom(deck);
      }
    }
  }

  public Hand dealerHand() {
    return dealerHand;
  }

  public Hand playerHand() {
    return playerHand;
  }

  public void playerHits() {
    // if isPlayerDone, throw IllegalStateException
    playerHand.drawFrom(deck);
    playerDone = playerHand.isBusted();
    if (playerDone) {
      roundCompleted();
    }
  }

  public void playerStands() {
    // if isPlayerDone, throw IllegalStateException
    playerDone = true;
    dealerTurn();
    roundCompleted();
  }

  private void roundCompleted() {
    gameMonitor.roundCompleted(this);
  }

  public boolean isPlayerDone() {
    return playerDone;
  }
}

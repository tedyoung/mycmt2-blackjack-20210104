package com.jitterted.ebp.blackjack.adapter.out.gamemonitor;

import com.jitterted.ebp.blackjack.domain.Game;

public class GameResultDto {
  /*
  "playerName": "Joe",
    "outcome": "You busted",
    "playerHandValue": "23",
    "dealerHandValue": "17"
   */
  private final String playerName = "TED";
  private final String outcome;
  private final String playerHandValue;
  private final String dealerHandValue;

  public GameResultDto(String outcome, String playerHandValue, String dealerHandValue) {
    this.outcome = outcome;
    this.playerHandValue = playerHandValue;
    this.dealerHandValue = dealerHandValue;
  }

  public static GameResultDto of(Game game) {
    GameResultDto gameResultDto = new GameResultDto(game.determineOutcome(),
                                                    game.playerHand().displayValue(),
                                                    game.dealerHand().displayValue());
    return gameResultDto;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getOutcome() {
    return outcome;
  }

  public String getPlayerHandValue() {
    return playerHandValue;
  }

  public String getDealerHandValue() {
    return dealerHandValue;
  }
}

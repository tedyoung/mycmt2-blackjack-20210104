package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

  private final Game game; // caveat: this violates layering constraint

  @Autowired
  public BlackjackController(Game game) {
    this.game = game;
  }

  @PostMapping("/start-game")
  public String startGame() {
    game.initialDeal();
    return "redirect:/game";
  }

  @GetMapping("/game")
  public String viewGame(Model model) {
    model.addAttribute("gameView", GameView.of(game));
    return "blackjack";
  }

  @PostMapping("/hit")
  public String hitCommand() {
    game.playerHits();

    if (game.isPlayerDone()) {
      return "redirect:/done";
    }

    return "redirect:/game";
  }

  @PostMapping("/stand")
  public String standCommand() {
    game.playerStands();
    return "redirect:/done";
  }

  @GetMapping("/done")
  public String gameDone(Model model) {
    GameView gameView = GameView.of(game);
    model.addAttribute("gameView", gameView);

    model.addAttribute("outcome", game.determineOutcome());

    return "done";
  }

}

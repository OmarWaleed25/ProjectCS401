package game.engine.cards;

import game.engine.monsters.*;
import game.engine.*;

public class ConfusionCard extends Card {
	private int duration;
	
	public ConfusionCard(String name, String description, int rarity, int duration) {
		super(name, description, rarity, false);
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void performAction(Monster player, Monster opponent){
			Role tmp = player.getRole();
			player.setRole(opponent.getRole());
			opponent.setRole(tmp);
			player.setConfusionTurns(duration);
			opponent.setConfusionTurns(duration);
	}

}

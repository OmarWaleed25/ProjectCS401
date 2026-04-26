package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class Dynamo extends Monster {
	
	public Dynamo(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		opponentMonster.setFrozen(true);
	}
	@Override
	public void setEnergy(int energy) {
		super.setEnergy(this.getEnergy()+2*(energy-this.getEnergy()));
	}
	
}

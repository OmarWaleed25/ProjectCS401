package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell{

	public ContaminationSock(String name, int effect) {
		super(name, effect);
	}
	public void transport(Monster landingMonster) {
        int newPosition = landingMonster.getPosition() + this.getEffect();
        landingMonster.setPosition(newPosition);
        landingMonster.alterEnergy(Constants.SLIP_PENALTY);
    }
}




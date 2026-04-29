package game.engine.cells;
import game.engine.Constants;
import game.engine.monsters.Monster;

public class ConveyorBelt extends TransportCell {

	public ConveyorBelt(String name, int effect) {
		super(name, effect);
	}
	public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
        landingMonster.setPosition(landingMonster.getPosition()+this.getEffect());
        landingMonster.setEnergy(landingMonster.getEnergy()+Constants.SLIP_PENALTY);
	}
	

}

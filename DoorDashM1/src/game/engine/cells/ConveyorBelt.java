package game.engine.cells;
import game.engine.monsters.Monster;

public class ConveyorBelt extends TransportCell {

	public ConveyorBelt(String name, int effect) {
		super(name, effect);
	}
	public void transport(Monster landingMonster) {
        landingMonster.setPosition(landingMonster.getPosition()+this.getEffect());
       
	}
	

}

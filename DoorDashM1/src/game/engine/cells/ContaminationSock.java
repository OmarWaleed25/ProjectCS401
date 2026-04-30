package game.engine.cells;

import game.engine.Constants;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class ContaminationSock extends TransportCell implements CanisterModifier {

    public ContaminationSock(String name, int effect) {
        super(name, effect);
    }

    public void transport(Monster landingMonster) {
        int newPosition = landingMonster.getPosition() + this.getEffect();
        landingMonster.setPosition(newPosition);
        modifyCanisterEnergy(landingMonster, - Constants.SLIP_PENALTY);
    }

    @Override
    public void modifyCanisterEnergy(Monster monster, int canisterValue) {
        monster.alterEnergy(canisterValue);
    }
}


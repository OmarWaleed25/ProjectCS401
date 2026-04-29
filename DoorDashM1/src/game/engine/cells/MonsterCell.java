package game.engine.cells;

import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}
	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
	    super.onLand(landingMonster, opponentMonster);
	    Monster stationedMonster = this.getCellMonster();

	    if (stationedMonster == null) {
	        return;
	    }

	    if (landingMonster.getRole() == stationedMonster.getRole()) {
	        landingMonster.executePowerupEffect(opponentMonster);
	    } else {
	        if (landingMonster.getEnergy() > stationedMonster.getEnergy()) {
	            int landingOldEnergy = landingMonster.getEnergy();
	            int stationedOldEnergy = stationedMonster.getEnergy();

	            stationedMonster.setEnergy(landingOldEnergy);

	            if (landingMonster.isShielded()) {
	                landingMonster.setShielded(false);
	            } else {
	                landingMonster.setEnergy(stationedOldEnergy);
	            }
	        }
	    }
	}

}

package game.engine.cells;

import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;
	
	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
	}
	
	public Role getRole() {
		return role;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}
	public void modifyCanisterEnergy(Monster monster, int canisterValue)
	{
		if(monster.getRole()==this.getRole())
			monster.alterEnergy(canisterValue);
		else
			monster.alterEnergy(-canisterValue);
	}
	public void onLand(Monster landingMonster, Monster opponentMonster) {
	    super.onLand(landingMonster, opponentMonster);

	    if (this.isActivated()) return;
	    
	    boolean energyChanged = false;
	    Role targetRole = landingMonster.getRole();
	    int effectValue = (landingMonster.getRole() == this.getRole()) ? this.energy : -this.energy;

	    if (effectValue > 0 || !landingMonster.isShielded()) {
	        int oldEnergy = landingMonster.getEnergy();
	        modifyCanisterEnergy(landingMonster, this.energy);
	        if (landingMonster.getEnergy() != oldEnergy) energyChanged = true;
	    }
	    for (Monster m : Board.getStationedMonsters()) {
	        if (m != landingMonster && m != opponentMonster && m.getRole() == targetRole) {
	            if (effectValue > 0 || !m.isShielded()) {
	                int oldEnergy = m.getEnergy();
	                modifyCanisterEnergy(m, this.energy);
	                if (m.getEnergy() != oldEnergy) energyChanged = true;
	            }
	        }
	    }
	    if (energyChanged) {
	        this.setActivated(true);
	    }
	}
	}



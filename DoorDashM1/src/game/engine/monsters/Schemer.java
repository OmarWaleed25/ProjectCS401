package game.engine.monsters;

import java.util.ArrayList;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	
	private int stealEnergyFrom(Monster target){
		int AmountToSteal ;
		if(target.getEnergy() < Constants.SCHEMER_STEAL)
			AmountToSteal=target.getEnergy();
		else 
			AmountToSteal= Constants.SCHEMER_STEAL ;
		target.setEnergy(target.getEnergy() - AmountToSteal);
		return AmountToSteal ;
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		int stolen = 0;
		if(opponentMonster != null){
			stolen += stealEnergyFrom(opponentMonster);
		}
		ArrayList<Monster> stationed = Board.getStationedMonsters();
        if (stationed != null) {
            for (Monster stationedMonster : stationed) {
            	if (stationedMonster != this)
                stolen += stealEnergyFrom(stationedMonster);
            }
        }
        this.setEnergy(this.getEnergy() + stolen);
		
	}
	@Override
    public void setEnergy(int energy) {
        int finalValue = energy + 10; 
        if (finalValue < 0) {
            finalValue = 0;
        }
        super.setEnergy(finalValue);
    }
	
}

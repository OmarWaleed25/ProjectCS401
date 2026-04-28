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
	public void onLand(Monster landingMonster, Monster opponentMonster) {
        super.onLand(landingMonster, opponentMonster);
	    Monster stationdMonster = this.getMonster();
	    if (stationdMonster==null)
	    	return;
	    else if(landingMonster.getRole()==stationdMonster.getRole())
	    {
	    	landingMonster.executePowerupEffect(opponentMonster);
	    }
	    else
	    {
	    	if(landingMonster.getEnergy()>stationdMonster.getEnergy())
	    	{
	    		int temp=landingMonster.getEnergy();
	    		if(!landingMonster.isShielded())
	    	    {
	    			landingMonster.setEnergy(stationdMonster.getEnergy());
	    			
	    		}
	    	stationdMonster.setEnergy(temp);
	        }
	    }
	
	
	
	}

}

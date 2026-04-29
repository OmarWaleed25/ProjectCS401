package game.engine.cards;
import game.engine.monsters.*;
import game.engine.interfaces.CanisterModifier;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void performAction(Monster player, Monster opponent){
		if(!opponent.isShielded()){
			if(energy <= opponent.getEnergy()){
				this.modifyCanisterEnergy(opponent,opponent.getEnergy()-energy);
				player.setEnergy(player.getEnergy()+energy);
			}else{
				this.modifyCanisterEnergy(player,player.getEnergy()+opponent.getEnergy());
				opponent.setEnergy(0);
			}
		}

		else{
			opponent.setShielded(false);
		}

	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(canisterValue);
	}
}

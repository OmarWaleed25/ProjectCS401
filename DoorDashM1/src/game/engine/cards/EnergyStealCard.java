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
		if(!player.isShielded()){
			if(energy <= opponent.getEnergy()){
				opponent.setEnergy(opponent.getEnergy()-energy);
				player.setEnergy(player.getEnergy()+energy);
			}else{
				player.setEnergy(player.getEnergy()+opponent.getEnergy());
				opponent.setEnergy(0);
			}
		}
	}
}

package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import game.engine.exceptions.*;
import game.engine.dataloader.DataLoader;
import game.engine.monsters.*;
import game.engine.cells.*;
public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
		
		this.board = new Board(DataLoader.readCards());
		this.allMonsters = DataLoader.readMonsters();
		this.player = selectRandomMonsterByRole(playerRole);
		this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.current = player;
		
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	
	private Monster getCurrentOpponent(){
		if(getCurrent()==getPlayer())
			return getOpponent();
		else
			return getPlayer();
	}
	
	private int rollDice(){
		Random random = new Random();
		int x = random.nextInt(6) + 1;
		return x;
	}
	
	public void usePowerup() throws OutOfEnergyException{
		if ( this.getCurrent().getEnergy() >= Constants.POWERUP_COST ){
			this.getCurrent().executePowerupEffect( this.getCurrentOpponent() );
		}else{
			throw new OutOfEnergyException ("Not Enough Energy for Power Up");
		}
	}
	
	public void playTurn() throws InvalidMoveException{
		if(getCurrent().isFrozen()){
			getCurrent().setFrozen(false);
			switchTurn();
		}else{
			int move = rollDice();
			board.moveMonster(player, move, opponent);
			switchTurn();
		}
	}
	
	private void switchTurn(){
		if(getCurrent() == getPlayer()){
			setCurrent(getOpponent());
		}else{
			setCurrent(getPlayer());
		}
	}
	
	private boolean checkWinCondition(Monster monster){
		if(monster.getEnergy() >= 1000 && monster.getPosition() == 99)
			return true;
		else
			return false;
	}
	
	public Monster getWinner(){
		if(checkWinCondition(getCurrent()) && !checkWinCondition(getOpponent())){
			return getCurrent();
		}
		else if(checkWinCondition(getOpponent()) && !checkWinCondition(getCurrent()) ){
			return getOpponent();
		}
		else{
			return null;
		}
	}
	
	public void setStationedMonsters(ArrayList<Monster> stationedMonsters){
		board.setStationedMonsters(stationedMonsters);
	}
	
	public void initializeBoard(ArrayList<Cell> specialCells){
		board.initializeBoard(specialCells);
	}
	 
}
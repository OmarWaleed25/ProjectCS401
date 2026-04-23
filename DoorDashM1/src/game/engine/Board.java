package game.engine;

import java.util.ArrayList;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.monsters.Monster;
import game.engine.Constants;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
	}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}
	
	private int[] indexToRowCol(int index){
		int[] sol = new int[2];
		sol[0] = index/10;
		if(sol[0]%2==0)
			sol[1] = index%10;
		else
			sol[1] = 9 - index%10;
		return sol;
	}
	
	private Cell getCell(int index){
		int [] ind = indexToRowCol(index);
		return boardCells[ind[0]][ind[1]];
	}
	
	private void setCell(int index, Cell cell){
		int [] ind = indexToRowCol(index);
		boardCells[ind[0]][ind[1]] = cell;
	}
	
	
	void initializeBoard(ArrayList<Cell> specialCells){
		ArrayList<DoorCell> doors = new ArrayList<>();
		ArrayList<ConveyorBelt> belts = new ArrayList<>();
		ArrayList<ContaminationSock> socks = new ArrayList<>();
		
		for(int i=0;i<specialCells.size();i++){
			if(specialCells.get(i) instanceof DoorCell) doors.add((DoorCell) specialCells.get(i));
			
			else if(specialCells.get(i) instanceof ConveyorBelt) belts.add((ConveyorBelt) specialCells.get(i));
			
			else socks.add((ContaminationSock) specialCells.get(i));
		}
		int d =0; // index for the doors list
		int b =0; // index for the belts list
		int s = 0; // index for the socks list
		int bc =0; // index for the list of constant positions of 
		int sc =0; // index for the list of constant positions of 
		
		for(int i = 0;i < 100;i++){
			if(i%2==1){ 
				setCell(i,doors.get(d));
				d++;
			}
			else{
				if(i==Constants.CONVEYOR_CELL_INDICES[bc]){


				}
			}
			
		
		
		}
	
	
	}
	
	
	
	
	
	
	
	
}

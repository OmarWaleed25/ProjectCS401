package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cells.*;
import game.engine.exceptions.InvalidMoveException;
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
		this.setCardsByRarity();
		cards = new ArrayList<Card>();
		this.reloadCards();
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
	
	
	public void initializeBoard(ArrayList<Cell> specialCells){
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
		int s = 0;// index for the socks list
		int m = 0; //index for the monsters list




		for(int i =0;i<Constants.CONVEYOR_CELL_INDICES.length;i++){
			if(belts==null || b>=belts.size()) break;
			setCell(Constants.CONVEYOR_CELL_INDICES[i],belts.get(b));
			b++;
		}
		for(int i =0;i<Constants.SOCK_CELL_INDICES.length;i++){
			if(socks==null || s>=socks.size()) break;
			setCell(Constants.SOCK_CELL_INDICES[i],socks.get(s));
			s++;
		}
		for(int i=0;i<Constants.CARD_CELL_INDICES.length;i++){
			setCell(Constants.CARD_CELL_INDICES[i],new CardCell("Card Cell"));
		}

		for(int i = 0;i < Constants.MONSTER_CELL_INDICES.length;i++){
			if(stationedMonsters==null || m>=stationedMonsters.size()) break;
			setCell(Constants.MONSTER_CELL_INDICES[i],new MonsterCell(stationedMonsters.get(m).getName(),stationedMonsters.get(m)));
			m++;
		}

		for(int i = 0;i < 100;i++){
			if(doors==null || d>=doors.size()) break;
			if(i%2==1){
				setCell(i,doors.get(d));
				d++;
				continue;
			}
			int[] r_c =indexToRowCol(i);
			if(boardCells[r_c[0]][r_c[1]]==null) setCell(i,new Cell("Normal Cell"));
		}
	}


	private	void setCardsByRarity(){
		ArrayList<Card> new_cards = new ArrayList<>();

		for(int i=0;i<originalCards.size();i++){
			Card c = originalCards.get(i);
			int r = c.getRarity();
			for(int j=0;j<r;j++) new_cards.add(c);
		}

		originalCards = new_cards;
	}

	public static void reloadCards(){
		cards = new ArrayList<>(originalCards);
		Collections.shuffle(cards);
	}

	public static Card drawCard(){
		if(cards.size()==0) reloadCards();
		Card a = cards.get(0);
		cards.remove(0);
		return a;
	}

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int temp = currentMonster.getPosition();
        boolean dont_decrement = false;
        currentMonster.move(roll);

        if(getCell(currentMonster.getPosition()) instanceof CardCell && cards.get(0) instanceof ConfusionCard) dont_decrement = true;

        if(currentMonster.getPosition() == opponentMonster.getPosition()){
            currentMonster.setPosition(temp);
            throw new InvalidMoveException("Invalid Move Attempted");
        }

        this.getCell(currentMonster.getPosition()).onLand(currentMonster,opponentMonster); // --------> perform the action of the cell

        if(currentMonster.getPosition() == opponentMonster.getPosition()){
            currentMonster.setPosition(temp);
            throw new InvalidMoveException("Invalid Move Attempted");
        }

        if(!dont_decrement && currentMonster.getConfusionTurns()>0 && opponentMonster.getConfusionTurns()>0){
            currentMonster.setConfusionTurns(currentMonster.getConfusionTurns()-1);
            opponentMonster.setConfusionTurns(opponentMonster.getConfusionTurns()-1);
        }

        updateMonsterPositions(currentMonster, opponentMonster);
    }


    private void updateMonsterPositions(Monster player, Monster opponent){
    	
    }





	
	
	
	
	
	
	
	
}

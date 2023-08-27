package hw3;

import static api.Direction.*;
import static api.Orientation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

import api.Cell;
import api.CellType;
import api.Direction;
import api.Move;

/**
 * Represents a board in the Block Slider game. A board contains a 2D grid of
 * cells and a list of blocks that slide over the cells.
 */
public class Board {

	private Cell grabbedCell;
	private Block grabbedBlock;
	private boolean isGameOver;
	private int moveCount = 0;

	/**
	 * 2D array of cells, the indexes signify (row, column) with (0, 0) representing
	 * the upper-left cornner of the board.
	 */
	private Cell[][] grid;

	/**
	 * A list of blocks that are positioned on the board.
	 */
	private ArrayList<Block> blocks;

	/**
	 * A list of moves that have been made in order to get to the current position
	 * of blocks on the board.
	 */
	private ArrayList<Move> moveHistory;

	/**
	 * Constructs a new board from a given 2D array of cells and list of blocks. The
	 * cells of the grid should be updated to indicate which cells have blocks
	 * placed over them (i.e., setBlock() method of Cell). The move history should
	 * be initialized as empty.
	 * 
	 * @param grid   a 2D array of cells which is expected to be a rectangular shape
	 * @param blocks list of blocks already containing row-column position which
	 *               should be placed on the board
	 */
	public Board(Cell[][] grid, ArrayList<Block> blocks) {
		this.grid = grid;
		this.blocks = blocks;
		moveHistory = new ArrayList<>();

		setBlockOnGrid();
		reset();

	}

	/**
	 * sets up the blocks on grid
	 */
	private void setBlockOnGrid(){
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[row].length; col++){
				if(getCell(row, col).isFloor()){
					for(int i = 0; i < blocks.size(); i++){
						Block b = blocks.get(i);
						if(row == b.getFirstRow() && col == b.getFirstCol()){
							grid[row][col].setBlock(b);
							if(b.getOrientation() == HORIZONTAL){
								int len = b.getLength();
								for(int j = col; j <= (col + (len-1)); j++){
									grid[row][j].setBlock(b);
								}
							} else {
								int len = b.getLength();
								for(int j = row; j <= (row + (len-1)); j++){
									grid[j][col].setBlock(b);
								}
							}
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Constructs a new board from a given 2D array of String descriptions.
	 * <p>
	 * DO NOT MODIFY THIS CONSTRUCTOR
	 * 
	 * @param desc 2D array of descriptions
	 */
	public Board(String[][] desc) {
		this(GridUtil.createGrid(desc), GridUtil.findBlocks(desc));
	}

	/**
	 * Models the user grabbing a block over the given row and column. The purpose
	 * of grabbing a block is for the user to be able to drag the block to a new
	 * position, which is performed by calling moveGrabbedBlock(). This method
	 * records two things: the block that has been grabbed and the cell at which it
	 * was grabbed.
	 * 
	 * @param row row to grab the block from
	 * @param col column to grab the block from
	 */
	public void grabBlockAtCell(int row, int col) {
		 grabbedCell = grid[row][col];
		 grabbedBlock = grabbedCell.getBlock();
	}

	/**
	 * Set the currently grabbed block to null.
	 */
	public void releaseBlock() {
		grabbedBlock = null;
	}

	/**
	 * Returns the currently grabbed block.
	 * 
	 * @return the current block
	 */
	public Block getGrabbedBlock() {
		return grabbedBlock;
	}

	/**
	 * Returns the currently grabbed cell.
	 * 
	 * @return the current cell
	 */
	public Cell getGrabbedCell() {
		return grabbedCell;
	}

	/**
	 * Returns true if the cell at the given row and column is available for a block
	 * to be placed over it. Blocks can only be placed over floors and exits. A
	 * block cannot be placed over a cell that is occupied by another block.
	 * 
	 * @param row row location of the cell
	 * @param col column location of the cell
	 * @return true if the cell is available for a block, otherwise false
	 */
	public boolean canPlaceBlock(int row, int col) {
		Cell target = grid[row][col];
		if(row >= grid.length || col >= grid[0].length){
			return false;
		} else if(target.hasBlock()){
			return false;
		} else if (target.isFloor() || target.isExit()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the number of moves made so far in the game.
	 * 
	 * @return the number of moves
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * Returns the number of rows of the board.
	 * 
	 * @return number of rows
	 */
	public int getRowSize() {
		return grid.length;
	}

	/**
	 * Returns the number of columns of the board.
	 * 
	 * @return number of columns
	 */
	public int getColSize() {
		return grid[0].length;
	}

	/**
	 * Returns the cell located at a given row and column.
	 * 
	 * @param row the given row
	 * @param col the given column
	 * @return the cell at the specified location
	 */
	public Cell getCell(int row, int col) {
		return grid[row][col];
	}

	/**
	 * Returns a list of all blocks on the board.
	 * 
	 * @return a list of all blocks
	 */
	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	/**
	 * Returns true if the player has completed the puzzle by positioning a block
	 * over an exit, false otherwise.
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * Moves the currently grabbed block by one cell in the given direction. A
	 * horizontal block is only allowed to move right and left and a vertical block
	 * is only allowed to move up and down. A block can only move over a cell that
	 * is a floor or exit and is not already occupied by another block. The method
	 * does nothing under any of the following conditions:
	 * <ul>
	 * <li>The game is over.</li>
	 * <li>No block is currently grabbed by the user.</li>
	 * <li>A block is currently grabbed by the user, but the block is not allowed to
	 * move in the given direction.</li>
	 * </ul>
	 * If none of the above conditions are meet, the method does the following:
	 * <ul>
	 * <li>Moves the block object by calling its move method.</li>
	 * <li>Sets the block for the grid cell that the block is being moved into.</li>
	 * <li>For the grid cell that the block is being moved out of, sets the block to
	 * null.</li>
	 * <li>Moves the currently grabbed cell by one cell in the same moved direction.
	 * The purpose of this is to make the currently grabbed cell move with the block
	 * as it is being dragged by the user.</li>
	 * <li>Adds the move to the end of the moveHistory list.</li>
	 * <li>Increment the count of total moves made in the game.</li>
	 * </ul>
	 * 
	 * @param dir the direction to move
	 */
	public void moveGrabbedBlock(Direction dir) {
		Cell target = getTargetCell(dir);
		if(!isGameOver() && grabbedBlock != null
				&& (((dir == UP || dir == DOWN) && grabbedBlock.getOrientation() == VERTICAL) || ((dir == LEFT || dir == RIGHT) && grabbedBlock.getOrientation() == HORIZONTAL))
		        && canPlaceBlock(target.getRow(), target.getCol())){
			//Moves the block object by calling its move method
			grabbedBlock.move(dir);
			//Sets the block for the target cell that ir is being moved into
			target.setBlock(grabbedBlock);
			Cell toClear = getCellToEmpty(dir, target);
			toClear.setBlock(null);
			moveCount++;
			Move mv = new Move(grabbedBlock, dir);
			moveHistory.add(mv);
			if(target.isExit()){
				isGameOver = true;
			}
			if(dir == LEFT){
				grabbedCell = grid[grabbedCell.getRow()][grabbedCell.getCol() - 1];
			} else if (dir == RIGHT) {
				grabbedCell = grid[grabbedCell.getRow()][grabbedCell.getCol() + 1];
			} else if (dir == UP) {
				grabbedCell = grid[grabbedCell.getRow() - 1][grabbedCell.getCol()];
			} else if (dir == DOWN) {
				grabbedCell = grid[grabbedCell.getRow() + 1][grabbedCell.getCol()];
			}

		}
	}

	/**
	 *
	 * @param dir
	 * @param target
	 * @return Makes the cell empty
	 */

	private Cell getCellToEmpty(Direction dir, Cell target){
//		right	cell whose col and row matched blocks firstrow and col
//		left	block in the position of target cells col + len
//		up	targets position row + length
//		down	target position row - length

		Cell toClear = null;
		if(dir == RIGHT){
			int col = target.getCol() - grabbedBlock.getLength();
			toClear = grid[grabbedBlock.getFirstRow()][col];
		} else if(dir == LEFT){
			int col = target.getCol() + grabbedBlock.getLength();
			toClear = grid[grabbedBlock.getFirstRow()][col];
		} else if (dir == UP){
			int row = target.getRow() + grabbedBlock.getLength();
			toClear = grid[row][grabbedBlock.getFirstCol()];
		} else {
			int row = target.getRow() - grabbedBlock.getLength();
			toClear = grid[row][getGrabbedBlock().getFirstCol()];
		}
		return toClear;
	}

	/**
	 *
	 * @param dir
	 * @return
	 * gets the cell where the block is supposed to move to
	 */
	private Cell getTargetCell(Direction dir){
//		right	cell whose col and row matched blocks firstrow and col is empty
//		left	block in the position of target cells col + len will be empty
//		up	targets position row + length will be empty
//		down	target position row - length will be emepty
		//if dir == left && canplaceblock(grabbedblock.getfirstrow(), grabbedblock.getfirstcol()-1)
		Cell target = null;
		if(dir == RIGHT){
			int col = grabbedBlock.getFirstCol() + grabbedBlock.getLength();
			target = grid[grabbedBlock.getFirstRow()][col];
		} else if(dir == LEFT){
			int col = grabbedBlock.getFirstCol() - 1;
			target = grid[grabbedBlock.getFirstRow()][col];
		} else if (dir == UP){
			int row = grabbedBlock.getFirstRow() - 1;
			target = grid[row][grabbedBlock.getFirstCol()];
		} else if (dir == DOWN){
			int row = grabbedBlock.getFirstRow() + grabbedBlock.getLength();
			target = grid[row][grabbedBlock.getFirstCol()];
		}
		return target;
	}

	/**
	 * Resets the state of the game back to the start, which includes the move
	 * count, the move history, and whether the game is over. The method calls the
	 * reset method of each block object. It also updates each grid cells by calling
	 * their setBlock method to either set a block if one is located over the cell
	 * or set null if no block is located over the cell.
	 */
	public void reset() {
		// TODO
		// set the move count to 0
		// block has reset function call that for each block
		// move history.deleteAll
		moveCount = 0;
		moveHistory.clear();
		for(int i = 0; i < blocks.size(); i++){
			Block block = blocks.get(i);
			block.reset();
		}
		cleanGrid();
		isGameOver = false;
		setBlockOnGrid();
	}

	/**
	 * cleans the grid
	 */
	private void cleanGrid(){
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				grid[row][col].setBlock(null);
			}
		}
	}

	/**
	 * Returns a list of all legal moves that can be made by any block on the
	 * current board. If the game is over there are no legal moves.
	 * 
	 * @return a list of legal moves
	 */
	public ArrayList<Move> getAllPossibleMoves() {
		ArrayList<Move> possibleMove = new ArrayList<>();
		if(!isGameOver){
			for(int i = 0; i < getBlocks().size(); i++){
				Block b = blocks.get(i);
				if(b.getOrientation() == HORIZONTAL){
					if (canPlaceBlock(b.getFirstRow(), b.getFirstCol() - 1)){
						Move move = new Move(b, LEFT);
						possibleMove.add(move);
					}
					if (canPlaceBlock(b.getFirstRow(), b.getFirstCol() + b.getLength())) {
						Move move = new Move(b, RIGHT);
						possibleMove.add(move);
					}
				} else if (b.getOrientation() == VERTICAL) {
					if(canPlaceBlock(b.getFirstRow() - 1, b.getFirstCol())){
						Move move = new Move(b, UP);
						possibleMove.add(move);
					}
					if(canPlaceBlock(b.getFirstRow() + b.getLength(), b.getFirstCol())){
						Move move = new Move(b, DOWN);
						possibleMove.add(move);
					}
				}
			}
		}
		return possibleMove;
	}

	/**
	 * Gets the list of all moves performed to get to the current position on the
	 * board.
	 * 
	 * @return a list of moves performed to get to the current position
	 */
	public ArrayList<Move> getMoveHistory() {
		return moveHistory;
	}

	/**
	 * EXTRA CREDIT 5 POINTS
	 * <p>
	 * This method is only used by the Solver.
	 * <p>
	 * Undo the previous move. The method gets the last move on the moveHistory list
	 * and performs the opposite actions of that move, which are the following:
	 * <ul>
	 * <li>grabs the moved block and calls moveGrabbedBlock passing the opposite
	 * direction</li>
	 * <li>decreases the total move count by two to undo the effect of calling
	 * moveGrabbedBlock twice</li>
	 * <li>if required, sets is game over to false</li>
	 * <li>removes the move from the moveHistory list</li>
	 * </ul>
	 * If the moveHistory list is empty this method does nothing.
	 */
	public void undoMove() {
		if(moveHistory.size() > 0) {
			isGameOver = false;
			Move move = moveHistory.get(moveHistory.size()-1);
			Block b = move.getBlock();
			moveHistory.remove(moveHistory.size()-1);
			if(move.getDirection() == UP){
				b.move(DOWN);
			} else if (move.getDirection() == DOWN){
				b.move(UP);
			} else if (move.getDirection() == RIGHT){
				b.move(LEFT);
			} else {
				b.move(RIGHT);
			}
			moveHistory.remove(moveHistory.size()-1);
			moveCount -= 2;
		}
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		for (Cell row[] : grid) {
			if (!first) {
				buff.append("\n");
			} else {
				first = false;
			}
			for (Cell cell : row) {
				buff.append(cell.toString());
				buff.append(" ");
			}
		}
		return buff.toString();
	}
}

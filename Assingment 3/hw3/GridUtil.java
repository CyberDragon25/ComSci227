package hw3;

import static api.Orientation.*;
import static api.CellType.*;

import java.util.ArrayList;

import api.Cell;
import api.CellType;
import api.Orientation;

/**
 * Utilities for parsing string descriptions of a grid.
 */
public class GridUtil {
	/**
	 * Constructs a 2D grid of Cell objects given a 2D array of cell descriptions.
	 * String descriptions are a single character and have the following meaning.
	 * <ul>
	 * <li>"*" represents a wall.</li>
	 * <li>"e" represents an exit.</li>
	 * <li>"." represents a floor.</li>
	 * <li>"[", "]", "^", "v", or "#" represent a part of a block. A block is not a
	 * type of cell, it is something placed on a cell floor. For these descriptions
	 * a cell is created with CellType of FLOOR. This method does not create any
	 * blocks or set blocks on cells.</li>
	 * </ul>
	 * The method only creates cells and not blocks. See the other utility method
	 * findBlocks which is used to create the blocks.
	 * 
	 * @param desc a 2D array of strings describing the grid
	 * @return a 2D array of cells the represent the grid without any blocks present
	 */
	public static Cell[][] createGrid(String[][] desc) {
		Cell[][] arr = new Cell[desc.length][desc[0].length];
		ArrayList<Block> blocks = findBlocks(desc);
		for(int row = 0; row < desc.length; row++){
			for(int col = 0; col < desc[row].length; col++){
				CellType type = getType(desc[row][col]);
				Cell cell = new Cell(type, row, col);
				arr[row][col] = cell;
			}
		}

		return arr;
	}

	/**
	 *
	 * @param type
	 * @return gets the type of block like floor, exit etc
	 */
	private static api.CellType getType(String type){
		CellType ct = null;
		if (type.equals("*")){
			ct = WALL;
		} else if (type.equals("e")){
			ct = EXIT;
		} else {
			ct = FLOOR;
		}
		return ct;
	}

	/**
	 * Returns a list of blocks that are constructed from a given 2D array of cell
	 * descriptions. String descriptions are a single character and have the
	 * following meanings.
	 * <ul>
	 * <li>"[" the start (left most column) of a horizontal block</li>
	 * <li>"]" the end (right most column) of a horizontal block</li>
	 * <li>"^" the start (top most row) of a vertical block</li>
	 * <li>"v" the end (bottom most column) of a vertical block</li>
	 * <li>"#" inner segments of a block, these are always placed between the start
	 * and end of the block</li>
	 * <li>"*", ".", and "e" symbols that describe cell types, meaning there is not
	 * block currently over the cell</li>
	 * </ul>
	 * 
	 * @param desc a 2D array of strings describing the grid
	 * @return a list of blocks found in the given grid description
	 */
	public static ArrayList<Block> findBlocks(String[][] desc) {
		// TODO
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Block> h = getHorizontal(desc);
		blocks.addAll(h);

		ArrayList<Block> v = getVertical(desc);
		blocks.addAll(v);


		return blocks;
	}

	/**
	 *
	 * @param desc
	 * @return
	 * for the horizontal blocks
	 */
	private static ArrayList<Block> getHorizontal(String[][] desc){
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int row = 0; row < desc.length; row++){
			for(int col = 0; col < desc[row].length; col++){
				if(desc[row][col].equals("[")){
					Orientation o = HORIZONTAL;
					int firstRow = row;
					int firstCol = col;
					for(int i = col; i < desc[row].length; i++){
						if(desc[row][i].equals("]")){
							int len = (i - col) + 1;
							Block b = new Block(firstRow, firstCol, len, o);
							blocks.add(b);
							break;
						}
					}
				}
			}
		}
		return blocks;
	}

	/**
	 *
	 * @param desc
	 * @return
	 * for the vertical blocks
	 */
	private static ArrayList<Block> getVertical(String[][] desc){
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int col = 0; col < desc[0].length; col++){
			for(int row = 0; row < desc.length; row++){
				if(desc[row][col].equals("^")){
					Orientation o = VERTICAL;
					int firstRow = row;
					int firstCol = col;
					for(int i = row; i < desc.length; i++){
						if(desc[i][col].equals("v")){
							int len = (i - row) + 1;
							Block b = new Block(firstRow, firstCol, len, o);
							blocks.add(b);
							break;
						}
					}
				}
			}
		}
		return blocks;
	}
}

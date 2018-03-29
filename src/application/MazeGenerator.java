package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
	private final int rows;
	private final int cols;
	private Cell[][] grid;
	private Cell currentCell;
	
	public boolean finish = false;

	private Stack<Cell> stack = new Stack<>();

	public MazeGenerator(Cell[][] grid, int startRow, int startCol) {
		this.grid = grid;
		rows = grid.length;
		cols = grid[0].length;
		currentCell = grid[startRow][startCol];
	}

	public void generateMaze() {
		currentCell.visited = true;
		currentCell.setFloorColor(MyColor.visitedColor);
		
		if(currentCell.popped)
			currentCell.setFloorColor(MyColor.backtrackColor);
		
		// WIKI: Maze generation algorithm
		Cell nextCell = checkNeighbors(currentCell);
		if(nextCell != null) {
			// has neighbor
			stack.push(currentCell);
			
			removeWalls(currentCell, nextCell);
			
			currentCell = nextCell;
			currentCell.setFloorColor(MyColor.currnetColor);
		} else if(!stack.isEmpty()) {
			// current cell is dead end 
			currentCell.setFloorColor(MyColor.backtrackColor);
			
			// backtrack to the previous cell
			currentCell = stack.pop();
			currentCell.popped = true;
			currentCell.setFloorColor(MyColor.currnetColor);
		} else if(stack.isEmpty()) {
			finish = true;
			for(Cell[] cArr : grid) {
				for(Cell c : cArr) {
					c.setFloorColor(MyColor.floorColor);
				}
			}
		}
	}
	
	private void removeWalls(Cell a, Cell b) {
		int i = a.getPos().row - b.getPos().row;
		if(i == 1) {
			a.hasWall[DirectionType.TOP] = false;
			b.hasWall[DirectionType.BOTTOM] = false;
		} else if(i == -1) {
			b.hasWall[DirectionType.TOP] = false;
			a.hasWall[DirectionType.BOTTOM] = false;
		}
		
		int j = a.getPos().col - b.getPos().col;
		if(j == 1) {
			a.hasWall[DirectionType.LEFT] = false;
			b.hasWall[DirectionType.RIGHT] = false;
		} else if(j == -1) {
			b.hasWall[DirectionType.LEFT] = false;
			a.hasWall[DirectionType.RIGHT] = false;
		}

		a.buildWalls();
		b.buildWalls();
	}
	
	private Cell checkNeighbors(Cell cell) {
		int i = cell.getPos().row;
		int j = cell.getPos().col;

		Cell top = isInsideTheGrid(i - 1, j) ? grid[i - 1][j] : null;
		Cell right = isInsideTheGrid(i, j + 1) ? grid[i][j + 1] : null;
		Cell bottom = isInsideTheGrid(i + 1, j) ? grid[i + 1][j] : null;
		Cell left = isInsideTheGrid(i, j - 1) ? grid[i][j - 1] : null;
		
		ArrayList<Cell> cellList = new ArrayList<>(); 
		
		if(top != null && !top.visited)
			cellList.add(top);
		if(right != null && !right.visited)
			cellList.add(right);
		if(bottom != null && !bottom.visited) 
			cellList.add(bottom);
		if(left != null && !left.visited) 
			cellList.add(left);
		
		if(cellList.size() > 0) {
			return cellList.get(new Random().nextInt(cellList.size()));
		}
		return null;
	}
	
	private boolean isInsideTheGrid(int row, int col) {
		if(0 <= row && row < rows && 0 <= col && col < cols)
			return true;
		return false;
	}
}
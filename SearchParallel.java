/* M. Kuttel 2023
 * Searcher class that lands somewhere random on the surfaces and 
 * then moves downhill, stopping at the local minimum.
 */

import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class SearchParallel extends RecursiveTask<Integer> {	
	private int id; // Searcher identifier
	private int steps; // number of steps to end of search
	private boolean stopped; // Did the search hit a previous trail?
	private int numSearches;
	private int pos_row, pos_col;
	
	private static int nextID = 1;

	private boolean DEBUG = false;

	private TerrainArea terrain;

	protected static int SEQUENTIAL_CUTOFF = 100;

	public SearchParallel(TerrainArea terrain, int numSearches, boolean DEBUG) {
		this.terrain = terrain;
		this.stopped = false;
		this.numSearches = numSearches;
		this.DEBUG = DEBUG;
	}

	public SearchParallel(TerrainArea terrain, int row, int col, int id) {
		this.id = id;
		this.terrain = terrain;
		this.stopped = false;
		this.pos_row = row;
		this.pos_col = col;
	}

	protected Integer compute() {

		if (numSearches <= SEQUENTIAL_CUTOFF) {
			int min = Integer.MAX_VALUE;
			int local_min = Integer.MAX_VALUE;
			int finder = -1;

			for (int i = 0; i < numSearches; i++) {
				Random rand = new Random();
				int random_row = rand.nextInt(terrain.rows);
				int random_col = rand.nextInt(terrain.columns);

				SearchParallel search = new SearchParallel(terrain, random_row, random_col, nextID);
				nextID++;

				local_min = search.find_valleys();
				if ((!search.isStopped()) && (local_min < min)) { // don't look at those who stopped because hit									// exisiting path
					min = local_min;
					finder = i; // keep track of who found it
					
					// System.out.println(nextID);
					// if(DEBUG) System.out.println("Search "+search.getID()+" finished at  "+local_min + " in " +search.getSteps());
				}
			}

			return min;
		}
		else{
			SearchParallel left = new SearchParallel(terrain, numSearches/2, DEBUG);
			SearchParallel right = new SearchParallel(terrain, numSearches/2, DEBUG);

			left.fork();
			int rightMin = right.compute();
			int leftMin = left.join();
			return Math.min(rightMin, leftMin);
		}
	}

	public int find_valleys() {
		int height = Integer.MAX_VALUE;
		TerrainArea.Direction next = TerrainArea.Direction.STAY_HERE;
		while (terrain.visited(pos_row, pos_col) == 0) { // stop when hit existing path
			height = terrain.get_height(pos_row, pos_col);
			terrain.mark_visited(pos_row, pos_col, id); // mark current position as visited
			steps++;
			next = terrain.next_step(pos_row, pos_col);
			switch (next) {
				case STAY_HERE:
					return height; // found local valley
				case LEFT:
					pos_row--;
					break;
				case RIGHT:
					pos_row = pos_row + 1;
					break;
				case UP:
					pos_col = pos_col - 1;
					break;
				case DOWN:
					pos_col = pos_col + 1;
					break;
			}
		}
		stopped = true;
		return height;
	}

	public int getID() {
		return id;
	}

	public int getPos_row() {
		return 1;
	}

	public int getPos_col() {
		return 1;
	}

	public int getSteps() {
		return steps;
	}

	public boolean isStopped() {
		return stopped;
	}

}

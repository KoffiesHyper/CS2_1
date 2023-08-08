/* M. Kuttel 2023
 * Searcher class that lands somewhere random on the surfaces and 
 * then moves downhill, stopping at the local minimum.
 */

import java.util.concurrent.RecursiveTask;

public class SearchParallel extends RecursiveTask<Integer> {
	private int id;
	private int steps;
	private boolean stopped;

	private int pos_row, pos_col;
	private TerrainArea terrain;
	private int startIndex, endIndex;

	private static boolean DEBUG = false;

	protected static int SEQUENTIAL_CUTOFF = 10000;

	private SearchParallel[] searches;

	int min = Integer.MAX_VALUE;
	int local_min = Integer.MAX_VALUE;
	int finder = -1;

	public SearchParallel(SearchParallel[] searches, int startIndex, int endIndex) {
		this.searches = searches;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public SearchParallel(int id, int row, int col, TerrainArea terrain) {
		this.id = id;
		this.pos_row = row;
		this.pos_col = col;
		this.stopped = false;
		this.terrain = terrain;
	}

	protected Integer compute() {
		if (endIndex - startIndex <= SEQUENTIAL_CUTOFF) {
			for (int i = startIndex; i < endIndex; i++) {
				local_min = searches[i].find_valleys();
				// if ((!searches[i].isStopped()) && (local_min < min)) {
				// 	min = local_min;
				// 	finder = i;
				// }

				// if (DEBUG)
				// 		System.out.println("Search " + searches[i].getID() + " finished at  " + local_min + " in "
				// 				+ searches[i].getSteps());
			}

			return 1;
		} else {

			SearchParallel left = new SearchParallel(searches, startIndex, (startIndex + endIndex) / 2);
			SearchParallel right = new SearchParallel(searches, (startIndex + endIndex) / 2, endIndex);

			left.fork();
			int rightMin = right.compute();
			int leftMin = left.join();
			return Math.min(rightMin, leftMin);
		}
	}

	public int find_valleys() {
		return 1;
		// int height = Integer.MAX_VALUE;
		// TerrainArea.Direction next = TerrainArea.Direction.STAY_HERE;
		// while (terrain.visited(pos_row, pos_col) == 0) { // stop when hit existing path
		// 	height = terrain.get_height(pos_row, pos_col);
		// 	terrain.mark_visited(pos_row, pos_col, id); // mark current position as visited
		// 	steps++;
		// 	next = terrain.next_step(pos_row, pos_col);
		// 	switch (next) {
		// 		case STAY_HERE:
		// 			return height; // found local valley
		// 		case LEFT:
		// 			pos_row--;
		// 			break;
		// 		case RIGHT:
		// 			pos_row = pos_row + 1;
		// 			break;
		// 		case UP:
		// 			pos_col = pos_col - 1;
		// 			break;
		// 		case DOWN:
		// 			pos_col = pos_col + 1;
		// 			break;
		// 	}
		// }
		// stopped = true;
		// return height;
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

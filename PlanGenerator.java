import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * @author Team1
 * Generate a Plan based on a specific case
 *
 */
public class PlanGenerator implements Runnable {

	// SyncQ SA -> PG
	public SynchronousQueue<Integer> inQ;
	public SynchronousQueue<ArrayList<Integer>> outQ; // disable this and change type eventually

	PlanGenerator(SynchronousQueue<Integer> inQ, SynchronousQueue<ArrayList<Integer>> outQ) {
		this.inQ = inQ;
		this.outQ = outQ;
	}

	// This method can be called in the mainapp
	@Override
	public void run() {

		// keep checing the inQ to see if there are any asseded situations we can
		// process
		Integer caseFromSA = -1;
		while (true) {
			if (inQ != null) {
				caseFromSA = inQ.poll();
			}

			// this means that that q is currently empty, just set it back a useful val
			if (caseFromSA != null) {
				/**
				 * NOTE:
				 * outQ.poll() will poll the outQ to see if there an element for us to use,
				 * and it will block until there is one.
				 * 
				 * We want to do the opposite: Push elements into the Q.
				 * 
				 * Checkout the sendDataToPlanExecutor function, it calls outQ.push().
				 * 
				 * Additionally, we don't need seperate arrays/lists for x/y values.
				 * We have just one outQ so we want to send only one piece of data.
				 * 
				 * We accomplish this by storing both values in teh same array.
				 * 
				 * The length array is two.
				 * 
				 * array[0] should be the x coordinate offset
				 * array[1] should be the y coordiante offset.
				 * 
				 * The PE will poll this Q, and will receive an array/arraylist off of the Q
				 * whenever we push one on.
				 * 
				 * I've changed the first one for you as an example :-)
				 */

				ArrayList<Integer> coordinateOffsetArray = new ArrayList<Integer>(); // let's use this one instead.

				// evaluate the cases we received and send the proper information to PE
				if (caseFromSA == 0) {
					System.out.println("CSO:PG: case " + caseFromSA + ": offset = (-1,0)");
					coordinateOffsetArray.add(-1);
					coordinateOffsetArray.add(0); // now coordinateOffsetArray looks like [-1, 0]
					this.sendDataToPlanExecutor(coordinateOffsetArray);

				}

				else if (caseFromSA == 1) {
					// (1, 0)
					System.out.println("CSO:PG: case " + caseFromSA + ": offset = (1,0)");
					coordinateOffsetArray.add(1);
					coordinateOffsetArray.add(0);
					this.sendDataToPlanExecutor(coordinateOffsetArray);
				}

				else if (caseFromSA == 2) {
					// (0, 1)
					System.out.println("CSO:PG: case " + caseFromSA + ": offset = (0,1)");
					coordinateOffsetArray.add(0);
					coordinateOffsetArray.add(1);
					this.sendDataToPlanExecutor(coordinateOffsetArray);
				}

				else if (caseFromSA == 3) {
					// (0, -1)
					System.out.println("CSO:PG: case " + caseFromSA + ": offset = (0,-1)");
					coordinateOffsetArray.add(0);
					coordinateOffsetArray.add(-1);
					this.sendDataToPlanExecutor(coordinateOffsetArray);
				}

				// the walk is in bounds so perform no correction
				else if (caseFromSA == 4) {
					// (0, 0)
					System.out.println("CSO:PG: case " + caseFromSA + ": offset = (0,0)");
					coordinateOffsetArray.add(0);
					coordinateOffsetArray.add(0);
					this.sendDataToPlanExecutor(coordinateOffsetArray);
				}

			}
		}

	}

	/**
	 * sendDataToPlanExecutor Place a message in the outQ. This will be received by
	 * the PlanExecutor.
	 * 
	 * This will *block* this thread until the message is lifted out of the Q.
	 * 
	 * @param message - type TBD
	 */

	public void sendDataToPlanExecutor(ArrayList<Integer> message) {
		try {
			this.outQ.put(
					message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// this method just returns for now, there is nothing receiving
		// messages on the other side! so this method will block.
		return;
	}

}

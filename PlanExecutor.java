import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * @author Team1
 * Exectue the computed plan by sending it back to the physical process
 */
public class PlanExecutor implements Runnable {

	// SyncQ PG -> PE
	public SynchronousQueue<ArrayList<Integer>> inQ;
	public SynchronousQueue<Plan> outQ; // disable this and change type eventually

	PlanExecutor(SynchronousQueue<ArrayList<Integer>> inQ, SynchronousQueue<Plan> outQ) {
		this.inQ = inQ;
		this.outQ = outQ;
	}

	@Override
	public void run() {
		//Check if it meets the requirements
		ArrayList<Integer> messageFromPG = null;

		while(true){

			// while(messageFromPG == null){
			// 	messageFromPG = inQ.poll();
			// }
			if (inQ != null) {
				messageFromPG = inQ.poll();
				if (messageFromPG != null) {
					Plan planToExecute = new Plan(messageFromPG.get(0), messageFromPG.get(1));
					System.out.println("This is PE");
					System.out.println("CSO:PE: Offset = (" + planToExecute.getX() + "," + planToExecute.getY() + ")");
					sendDataToRandomWalk(planToExecute);
				}
			}
		}
	}

	/**
	 * sendDataToRandomWalk Place a message in the outQ. This will be received by
	 * the PlanExecutor.
	 * 
	 * This will *block* this thread until the message is lifted out of the Q.
	 * 
	 * @param message - type TBD
	 */

	public void sendDataToRandomWalk(Plan message) {
		try {
			System.out.println("CSO:PE: Sending a message");
			this.outQ.put(
				message
			);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// this method just returns for now, there is nothing receiving
		// messages on the other side! so this method will block.
		return;
	}
}

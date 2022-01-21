
import java.util.concurrent.SynchronousQueue;
/** SituationAssesor
 * @author Team1
 * 
 * Assess a situation from the physical process model. 
 * Pass the message to plan generator.
 */
public class SituationAssesor implements Runnable {

	public SynchronousQueue<State> inQ; // get states from the process model
	public SynchronousQueue<Integer> outQ; // eventually will be a different type
	
	// Constructor
	SituationAssesor(
		SynchronousQueue<State> inQ, 
		SynchronousQueue<Integer> outQ
	){
		this.inQ = inQ;
		this.outQ = outQ;
	}

	// This method can be called in the mainapp
	@Override
	public void run() {
		
		/** SAS main loop
		 * This is the primary loop driving the Situation Assesor.  
		 * 
		 * Using a SynchronousQueue it is able to receive State objects from the Process Model.
		 * 
		 * We compare the current (x,y) location of the process to the dimensions. 
		 * 
		 * If there is a violation, we will send the appropriate message to PlanGenerator.
		 */
		// while(true) {
		// 	State state = null;
        //     System.out.println("before SAS inq");
		// 	while (inQ != null && state == null) {
		// 		state = inQ.poll();
		// 	}
		// 	if (state != null) {
		// 		System.out.println("CSO:SAS: I received a new state with step # " + state.step + ", iteration " + state.iteration + ".  Walk positions is " + '(' + state.x + ',' + state.y + ')');
			
		// 		// case 0: goes off top x < 0
		// 		if(state.x < 0){
		// 			System.out.println("CSO:SAS: going off the top!");
		// 			this.sendDataToPlanGenerator(0);
		// 		}

		// 		// case 1: goes off the bottom x >= dimensions
		// 		else if(state.x >= state.dimensions) {
		// 			System.out.println("CSO:SAS: going off the bottom!");
		// 			this.sendDataToPlanGenerator(1);
		// 		}

		// 		// case 2: goes off the left y < 0
		// 		else if(state.y < 0){
		// 			System.out.println("CSO:SAS:going off the left!");
		// 			this.sendDataToPlanGenerator(2);
		// 		}

		// 		// case 3: goes off the right y >= dimensions
		// 		else if(state.y >= state.dimensions){
		// 			System.out.println("CSO:SAS:going off the right!");
		// 			this.sendDataToPlanGenerator(3);
		// 		}

		// 		// case 4: everything is fine.  We don't need alter the course of the process model. 
		// 		else {
		// 			System.out.println("CSO:SAS:everything looks good :)");
		// 			this.sendDataToPlanGenerator(4);
		// 		}
		// 	}
		// }
		while(true) {
			if (inQ != null) {
				State state = this.inQ.poll();

					if (state != null) {
						System.out.println("CSO:SAS: I received a new state with step # " + state.step + ", iteration " + state.iteration + ".  Walk positions is " + '(' + state.x + ',' + state.y + ')');
							
						// case 0: goes off top x < 0
						if(state.x < 0){
							System.out.println("CSO:SAS: going off the top!");
							this.sendDataToPlanGenerator(0);
						}
				
						// case 1: goes off the bottom x >= dimensions
						else if(state.x >= state.dimensions) {
							System.out.println("CSO:SAS: going off the bottom!");
							this.sendDataToPlanGenerator(1);
						}
				
						// case 2: goes off the left y < 0
						else if(state.y < 0){
							System.out.println("CSO:SAS:going off the left!");
							this.sendDataToPlanGenerator(2);
						}
				
						// case 3: goes off the right y >= dimensions
						else if(state.y >= state.dimensions){
							System.out.println("CSO:SAS:going off the right!");
							this.sendDataToPlanGenerator(3);
						}
				
						// case 4: everything is fine.  We don't need alter the course of the process model. 
						else {
							System.out.println("CSO:SAS:everything looks good :)");
							this.sendDataToPlanGenerator(4);
						}
					}
			}
		}

	}
	
	/** sendDataToPlanGenerator
	 * Place a message in the outQ.  This will be received by the PlanGenerator.
	 * 
	 * This will *block* this thread until the message is lifted out of the Q.  
	 * 
	 * @param message - type TBD
	 */
	public void sendDataToPlanGenerator(int message){
        try {
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

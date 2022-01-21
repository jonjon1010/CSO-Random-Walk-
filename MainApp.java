import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * MainApp
 * 
 * This is the main driver code for the program!!
 * 
 * It builds a CSO and an randomwalk.  
 * It also establishes IO between the CSO and the randomwalk.
 */

public class MainApp implements Runnable{
    // class fields
    private final int poolSize = 10;
    private final ExecutorService pool;


    /** MainApp() Constructor
     * @author: James Kroner 
     * @return a new MainApp object.
     */
    public MainApp() throws IOException {
        pool = Executors.newFixedThreadPool(this.poolSize);
    }

    public void run() {
        SynchronousQueue<Plan> inQ = new SynchronousQueue<Plan>();
        SynchronousQueue<State> outQ = new SynchronousQueue<State>();
        // init and launch the CSO
       CSO cso = new CSO(outQ, inQ); // this CSO gets pointed at the outQ of the model.
        
        // execute the random walk
        this.pool.execute(new RandomWalk(inQ, outQ));

        // execute the cso
        this.pool.execute(cso);
    }

    public void shutDownNow(){
        this.pool.shutdownNow();
    }


    // main method to implement
    public static void main(String[] args) {
        System.out.println("MainApp.java is running....");

        try {
            MainApp app = new MainApp();
            app.run();
            // app.shutDownNow();
        } catch (IOException e) {
            System.out.println("IOException caught, returning from main");
            return;
        }
    }
}

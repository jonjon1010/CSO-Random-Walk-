import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.ArrayList;

/**
 * CSO
 * 
 * A Cyberspatial System Object class
 */
public class CSO implements Runnable {
    // thread pool 
    private final int poolSize = 10;
    private final ExecutorService pool;

    // CSO runnable sub-systems
    public SituationAssesor SAS;
    public PlanGenerator PG;
    public PlanExecutor PE;

    public SynchronousQueue<State> modelToSASq; // main INPUT q to CSO
    public SynchronousQueue<Integer> SAStoPGq; // internal q SAS <-> PG
    public SynchronousQueue<ArrayList<Integer>> PGtoPEq; // array q PG <-> PE
    public SynchronousQueue<Plan> PEtoModelq; // main OUTPUT q from CSO to model

   
    /** CSO constructor
     * Construct a new CSO object.  
     * 
     * Takes four params: 
     * @param modelToSASq - primary INPUT q from the process model
     * @param PEtoModelq - primary OUTPUT q from plan executor to process model 
     */
    public CSO (
        SynchronousQueue<State> modelToSASq, // IN to the CSO, OUT from the MODEL
        SynchronousQueue<Plan> PEtoModelq // OUT of the cso, INTO the MODEL
    ) {
        // initialize the executor service 
        this.pool = Executors.newFixedThreadPool(this.poolSize);
        this.modelToSASq = modelToSASq;
        this.PEtoModelq = PEtoModelq;

        // construct the situation assessor
        this.SAStoPGq = new SynchronousQueue<Integer>(); // init the SAS -> PG q
        this.SAS = new SituationAssesor(
            modelToSASq, this.SAStoPGq
        );
        this.SAStoPGq = this.SAS.outQ;

       // construct the plan generator
        this.PGtoPEq = new SynchronousQueue<ArrayList<Integer>>(); // init the PG -> PE q
        this.PG = new PlanGenerator(
           this.SAStoPGq, this.PGtoPEq
        );
        this.PGtoPEq = this.PG.outQ;
        
       // construct the PlanExecutor
        this.PEtoModelq = new SynchronousQueue<Plan>(); // init the PE -> model q
        this.PE = new PlanExecutor(
           this.PGtoPEq, PEtoModelq
        );
    }


    /** run()
     * run() will case the SAS, PG, and PE to spin up.  
     */
    @Override
    public void run() {
        this.pool.execute(this.SAS);
        this.pool.execute(this.PG); 
        this.pool.execute(this.PE); 
    }


}

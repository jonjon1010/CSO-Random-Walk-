import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * RandomWalk
 * @author team 1
 * This is the RandomWalk example from class with a few modification.  
 * - it implements Runnable: it can be executed from your favorite executor object or service
 * - It contains synchronous IO.  These can be used by an external CSO thread to make sure the RandomWalk stays in bounds.
 */
public class RandomWalk implements Runnable{

    // IO streams
    public SynchronousQueue<Plan> inQ; // get plans from the CSO
    public SynchronousQueue<State> outQ; // send states to the CSO

     // Constructor
    RandomWalk(
        SynchronousQueue<Plan> inQ,
        SynchronousQueue<State> outQ
    ) {
        this.inQ = inQ;
        this.outQ = outQ;
    }

//     public void run() {
//         int dimensions = 20;
//         int[] x = new int[dimensions]; // x[i] = x position on step i
//         int[] y = new int[dimensions]; // y[i] = y position on step i
//         int cellsToVist = dimensions * dimensions; // cells lefts to visit
//         int steps = 0; // number of steps taken
//         double r;
//         boolean[][] visited = new boolean[dimensions][dimensions]; // track if [i][j] visited

//         // // a logger for tracking out walk
//         // Logger logger = Logger.getLogger("processLog");
//         // FileHandler fh;

//         // setup the logger
//         // try {
//         //     fh = new FileHandler("./processLog.log");
//         //     logger.addHandler(fh);
//         //     SimpleFormatter formatter = new SimpleFormatter();
//         //     fh.setFormatter(formatter);
//         // } catch (SecurityException e) {
//         //     e.printStackTrace();
//         // } catch (IOException e) {
//         //     e.printStackTrace();
//         // }

//         // logger.info("logger started");

//        // start at center
//        // logger.info("initialized x[] and y[]");
//         for (int i = 0; i < dimensions; ++i){
//             x[i] = dimensions / 2;
//             y[i] = dimensions / 2;
//         }
//         visited[dimensions / 2][dimensions / 2] = true;
//         --cellsToVist;

//        // repeat until all cells have been visited
//         while(cellsToVist > 0){
//             ++steps;

//            // move the random walker
//             for(int i = 0; i < dimensions; ++i){
//                 System.out.println("RandomWalk:Processing step " + steps + ", iteration " + i);
//                 r = Math.random();
//                 if(r <= 0.25) ++x[i];
//                 else if (r <= 0.50) --x[i];
//                 else if (r <= 0.75) ++y[i];
//                 else if (r <= 1.00) --y[i];

//                 // logger.info("step " + 0 + "x[i] = " + x[i] + " y[i] = " + y[i]);
//                 // sending data to the CSO model! this will block until another thread fetches it. 
//                 try {
//                     this.outQ.put(
//                         new State(steps, i, dimensions, x[i], y[i])
//                     );
//                 } catch (InterruptedException e) {
//                     System.out.println("RandomWalk was interupted while waiting for the outQ");
//                     e.printStackTrace();
//                 } 

//                 // receiving data back from the CSO model!  The PE in the CSO is blocked until we fetch this. 
//                 // this is not implemented in the backend yet
//                 // Plan p = null;
//                 // while (p == null) p = inQ.poll();

//                 // // apply the plan.  x and y may be zero if no correction is needed!
//                 // // commented out for now
//                 // x[i] = x[i] + p.x;
//                 // y[i] = y[i] + p.y;

//                 // check if [i][j] has been visited and in bounds
//                 // we will eventually remove the bounds checking
//                 // because the CSO will handle those cases.
//                 if(
//                     x[i] < dimensions 
//                     && y[i] < dimensions
//                     && x[i] >= 0
//                     && y[i] >= 0
//                     && !visited[x[i]][y[i]]
//                 ) {
//                     --cellsToVist;
//                     visited[x[i]][y[i]] = true;
                    
//                     // pass the current coordinates to the next round
//                     // x[i + 1] = x[i];
//                     // y[i + 1] = y[i];
//                 }
//            }
//        }


//        System.out.println("completed in " + steps);
//        return;
//    }
    /**
    * reimplementation of random walk
    */
    public void run() {
        System.out.println("launching new random walk");
        int dimensions = 40;
        ArrayList<Integer> current = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> history = new ArrayList<ArrayList<Integer>>();


        int cellsToVisit = dimensions * dimensions;

        int steps = 0;
        double r;
        boolean[][] visited = new boolean[dimensions][dimensions];

        int curX = dimensions / 2;
        int curY = dimensions / 2;

        visited[curX][curY] = true;

        current.add(curX);
        current.add(curY);
        history.add(current);
        current.clear();

        // setup stdDraw

        StdDraw.setScale(0, dimensions);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering();

        // start the random walk
        while (cellsToVisit > 0) {
            System.out.println("RandomWalk:Processing step " + steps + "currx,y = " + "(" + curX + "," + curY + ")");
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.filledSquare(curX, curY, 0.45);

            // get random value
            r = Math.random();

            // increment position 
            if(r <= 0.25) ++curX;
            else if (r <= 0.50) --curX;
            else if (r <= 0.75) ++curY;
            else if (r <= 1.00) --curY;


            // send the data to the CSO model! this will block until another thread fetches it
            try {
                System.out.println("RandomWalk: adding new state to outQ");

                System.out.println("before randomwalk inq");
                this.outQ.put(
                    new State(steps, 0, dimensions, curX, curY)
                );
                System.out.println("after randomwalk in q");
            } catch (InterruptedException e) {
                System.out.println("RandomWalk was interupted while waiting for the outQ");
                e.printStackTrace();
            } 

            // ~~( await results )~~

            // poll for data 
            System.out.println("trying to grab something out of the q...");
            Plan p = null;
            while (p == null){
                p = inQ.poll();
            } 

            System.out.println("RandomWalk received from the CSO");

            // apply the plan
            curX = curX + p.x;
            curY = curY + p.y;

            if(
                curX < dimensions 
                && curY < dimensions
                && curX >= 0
                && curY >= 0
                && !visited[curX][curY]
            ) {
                --cellsToVisit;
                visited[curX][curY] = true;

                current.add(curX);
                current.add(curY);
                history.add(current);
                current.clear();
            } 

            current.add(curX);
            current.add(curY);
            history.add(current);
            current.clear();

            ++steps;

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.filledSquare(curX, curY, 0.45);
            StdDraw.show();
            StdDraw.pause(240);

        }

        System.out.println("~~~ completed in " + steps + " steps");




    }

}

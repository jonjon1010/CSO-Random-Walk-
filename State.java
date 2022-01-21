public class State {
    
    // Define the parameters
    public int step;
    public int iteration;
    public int dimensions;
    public int x;
    public int y;

    // Constructor
    State(int step, int iteration, int dimensions, int x, int y){
        this.step = step;
        this.iteration = iteration;
        this.dimensions = dimensions;
        this.x = x;
        this.y = y;
    }
}

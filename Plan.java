/** Plan
 * wrapper object used to send plan offset information back to the physical process 
 */
public class Plan {
    public int x;
    public int y;

    Plan(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}

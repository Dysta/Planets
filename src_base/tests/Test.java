package tests;

/**
 * Custom test model
 *
 * @author Adri
 */
abstract public class Test {
    
    protected String name;
    
    public Test(String name) {
        this.name = name;
    }
    
    abstract public boolean test();
    
    public boolean run() {
        boolean result = test();
        print(result);
        return result;
    }
    
    public void print(boolean success) {
        String state = (success) ? "SUCCESS" : "FAILURE";
        System.out.println(this.name+" : "+state);
    }
}

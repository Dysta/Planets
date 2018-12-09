/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

/**
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

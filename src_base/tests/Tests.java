package tests;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import planets.Game;
import planets.ResourcesManager;

public class Tests extends Application  {
    public final static double WIDTH = 1280;
    public final static double HEIGHT = 720;
    
    public final static boolean SUCCESS = true;
    public final static boolean FAILURE = false;
    
    private static ArrayList<Test> tests;
    private static int successes;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Tests.successes = 0;
        Tests.tests = new ArrayList<>();
        
        Game game = new Game();
        ResourcesManager.initGameAssets(WIDTH, HEIGHT);
        
        // Add all tests
        Tests.tests.add(new NoPlanetCollidingTest("NoPlanetCollidingTest"));
        
        System.out.println("--- Running tests ---");
        // Run all tests
        for(Test t : Tests.tests) {
            try {
                if(t.run()) {
                    Tests.successes++;
                }
            } catch(Exception e) {
                System.out.println(t.name+" : CRITICAL FAILURE "+e);
            }
        }
        System.out.println("--- "+Tests.successes+"/"+Tests.tests.size()+" passed ---");
    }
} 

package com.gamingroom.gameauth;

import com.gamingroom.gameauth.auth.GameAuthenticator;
import com.gamingroom.gameauth.auth.GameAuthorizer;
import com.gamingroom.gameauth.examples.BasicAuthExample;
import com.gamingroom.gameauth.examples.AdvancedAuthExample;

/**
 * Main application class for GameAuth.
 * This class demonstrates the usage of the GameAuth authentication system.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
public class GameAuthApplication {
    
    public static void main(String[] args) {
        System.out.println("=== GameAuth Application ===");
        System.out.println("Game Authentication & Authorization System");
        System.out.println("Version: 1.0.0");
        System.out.println();
        
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "basic":
                    System.out.println("Running Basic Authentication Example...");
                    BasicAuthExample.main(new String[0]);
                    break;
                case "advanced":
                    System.out.println("Running Advanced Authentication Example...");
                    AdvancedAuthExample.main(new String[0]);
                    break;
                case "help":
                    printHelp();
                    break;
                default:
                    System.out.println("Unknown command: " + args[0]);
                    printHelp();
                    break;
            }
        } else {
            // Run both examples by default
            System.out.println("Running Basic Authentication Example...");
            BasicAuthExample.main(new String[0]);
            
            System.out.println("\n" + "=".repeat(50) + "\n");
            
            System.out.println("Running Advanced Authentication Example...");
            AdvancedAuthExample.main(new String[0]);
        }
        
        System.out.println("\n=== Application completed ===");
    }
    
    private static void printHelp() {
        System.out.println("Usage: java -jar gameauth.jar [command]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  basic     - Run basic authentication example");
        System.out.println("  advanced  - Run advanced authentication example");
        System.out.println("  help      - Show this help message");
        System.out.println();
        System.out.println("If no command is provided, both examples will run.");
    }
}

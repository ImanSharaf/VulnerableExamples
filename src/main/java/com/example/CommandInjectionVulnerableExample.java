package com.example;

import java.io.*;
import java.util.*;

public class CommandInjectionVulnerableExample {
    public static void main(String[] args) throws Exception {
        String userInput = args.length > 0 ? args[0] : "HelloWorld";
        // VULNERABLE: Concatenating userInput into a command string.
        String command = "echo " + userInput;
        // Force shell interpretation
        Process proc = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
        proc.waitFor();

        try (InputStream is = proc.getInputStream();
                Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            String output = scanner.hasNext() ? scanner.next() : "";
            System.out.println("Command output: " + output.trim());
        }
    }
}
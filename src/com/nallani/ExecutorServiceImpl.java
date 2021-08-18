package com.nallani;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceImpl {

    public void execute() {
        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(1000);
        // Runnable, return void, nothing, submit and run the task async
        executor.submit(() -> System.out.println("Starting thread..."));

        try {
            FileSearch fileSearch = new FileSearch();
            //try different directory and filename :)
            fileSearch.searchDirectory(new File("S:/Aws"), "jwt-handbook-v0_14_1.pdf");

            int count = fileSearch.getResult().size();
            if(count ==0){
                System.out.println("No result found!");
            }else{
                System.out.println("Found " + count + " result!");
                for (String matched : fileSearch.getResult()){
                    System.out.println("Found : " + matched);
                }
            }
        } finally {
            // shut down the executor manually
            executor.shutdown();
            long end = System.currentTimeMillis();
            float msec = end - start;
            float sec= msec/1000F;
            float minutes=sec/60F;
            System.out.println("Total seconds to find file " + sec);
        }
    }
}
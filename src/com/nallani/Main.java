package com.nallani;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to search service... Happy Searching!!!!");
        System.out.println("please enter the disk letter to search");
        String diskName = input.next();

        System.out.println("please enter the file full name to search");
        String fileName = input.next();

        long start = System.currentTimeMillis();
        //File f = Search.findFile("jwt-handbook-v0_14_1.pdf", "N");
        Search search = new Search();
        ExecutorService executorService = Executors.newCachedThreadPool();
        File f = search.findFile(executorService, fileName, diskName);
        System.out.println("Total time taken is " + (System.currentTimeMillis() - start)/1000F);
        executorService.shutdownNow();
    }
}

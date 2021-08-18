package com.nallani;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class HardDiskSearcher {
    private static boolean fileFound = false;

    private static String searchTerm = "jwt-handbook-v0_14_1.pdf";

    public static void findFile() throws InterruptedException, ExecutionException {
        fileFound = false;
        System.out.println("Search started.. ");
        File[] systemRoots = File.listRoots();
        System.out.println("The disks are  " + Arrays.toString(systemRoots));
        FileSystemView fsv = FileSystemView.getFileSystemView();

        /*int length = systemRoots.length;
        for (int i = 0;i<length; i++){
            System.out.println(i);
            File root = systemRoots[i];*/
            for (File root: systemRoots) {
            if (fsv.getSystemTypeDescription(root).equals("Local Disk")) {
                File[] filesFromRoot = root.listFiles();
                System.out.println("The files from root " + root + " are " + Arrays.toString(filesFromRoot));

                ExecutorService executorService = Executors.newFixedThreadPool(1000);
                Future<?> future1 = executorService.submit(() -> {
                    System.out.println(String.format("starting expensive task thread %s", Thread.currentThread().getName()));
                    recursiveSearch(searchTerm, filesFromRoot);
                });
            }
        }
        System.out.println("File you searched for was found?  : " + fileFound);
    }

    private static void recursiveSearch(String searchTerm, File... files) {

        for (File file : files) {
            if (file.isDirectory()) {
                File[] filesInFolder = file.listFiles();
               // System.out.println("The files in folder are " + Arrays.toString(filesInFolder));
                if (filesInFolder != null) {
                    for (File f : filesInFolder) {
                        if (f.isFile()) {
                            if (isTheSearchedFile(f, searchTerm)) {
                                fileFound = true;
                            }
                        }
                    }

                    for (File f : filesInFolder) {
                        if (f.isDirectory()) {
                            recursiveSearch(searchTerm, f);
                        }
                    }
                }
            } else if (isTheSearchedFile(file, searchTerm)) {
                fileFound = true;
                AtomicInteger count = new AtomicInteger();
                System.out.println("No of files searched is " + count.incrementAndGet());
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    private static boolean isTheSearchedFile(File file, String searchTerm) {
        if (file.isFile() && (searchTerm.equals(file.getName()))) {
            System.out.println("The file you searched for has been found! " +
                    "It was found at: " + file.getAbsolutePath());
            return true;
        }
        return false;
    }
}

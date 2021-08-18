package com.nallani;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Search {

    public File findFile(ExecutorService executorService, String toSearch, String disk) throws Exception {
        boolean isDiskFound = false;
        File whichDisk = null;
        File root = null;
        File[] systemRoots = File.listRoots();
        System.out.println("The directories are  " + Arrays.toString(systemRoots));
        FileSystemView fsv = FileSystemView.getFileSystemView();

        int length = systemRoots.length;
        for (File systemRoot : systemRoots) {
            root = systemRoot;
            String diskName = root.getCanonicalPath().replace("\\", "").replace(":", "");
            if (disk.equalsIgnoreCase(diskName)) {
                isDiskFound = true;
                whichDisk = systemRoot;
            }
        }
        if (!isDiskFound) {
            System.out.println("The entered disk " +disk+ " is not found");
            System.exit(0);
        }
                File[] filesFromRoot = whichDisk.listFiles();
                System.out.println("The folders from root " + whichDisk + " is " + Arrays.toString(filesFromRoot));

                Future<File> future1 = executorService.submit(() -> {
                    //System.out.println(String.format("starting expensive task thread %s", Thread.currentThread().getName()));
                    return recursiveSearch(toSearch, filesFromRoot);
                });

                while (!(future1.isDone())) {
                    System.out.println(String.format("searching%s", future1.isDone() ? "done" : "...."));
                    Thread.sleep(300);
                }
                return future1.get();
            }

    private File recursiveSearch(String searchTerm, File... files) {
        File output = null;
        for (File file : files) {
            if (file.isDirectory()) {
                File[] filesInFolder = file.listFiles();
                // System.out.println("The files in folder are " + Arrays.toString(filesInFolder));
                if (filesInFolder != null) {
                    for (File f : filesInFolder) {
                        if (f.isFile()) {
                            if (isTheSearchedFile(f, searchTerm)) {
                                output = f.getAbsoluteFile();
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
                output = file.getAbsoluteFile();
            }
        }
        return output;
    }

    private boolean isTheSearchedFile(File file, String searchTerm) {
        if (file.isFile() && (searchTerm.equals(file.getName()))) {
            System.out.println("The file you searched for has been found! " +
                    "It was found at: " + file.getAbsolutePath());
            return true;
        }
        return false;
    }
}

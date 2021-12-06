package ems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmsTest {

    private static final List<Integer> TEST_FILE_SIZES = Arrays.asList(1024, 1024 * 1024, 1024 * 1024 * 10,
            1024 * 1024 * 100);

    public static void main(String[] args) throws Exception {
        int bufferSize = 1024 * 700;
        for (int inputFileSize : TEST_FILE_SIZES) {
            File inFile = genFile(inputFileSize, "data/inputFile.txt");
            final File outFile = new File("data/sorted-" + inFile.getName());
            final File partitionFolder = new File("data/tmp-partx/");
            long time = System.currentTimeMillis();
            System.out.println("Sorting file... with " + getSize(bufferSize) + " memory");
            ExternalMSort externalMSort = new ExternalMSort(bufferSize);
            externalMSort.extSort(inFile, outFile, partitionFolder);
            long timeTaken = System.currentTimeMillis() - time;
            System.out.println("Verifying output file...");
            if (new ExternalMSort().isSorted(outFile)) {
                System.out.println("Sorted");
            } else {
                System.err.println("File not sorted");
            }
            System.out.println("Execution time : " + timeTaken);
        }
    }

    static File genFile(int bytes, String path) throws IOException {
        System.out.println("Generating input file...");
        File inFile = new File(path);
        FileWriter fw = new FileWriter(inFile);
        BufferedWriter bw = new BufferedWriter(fw);
        int bytesWritten = 0;
        Random random = new Random();
        while (true) {
            bw.write(String.valueOf(random.nextInt(99999)));
            bw.write(",");
            bytesWritten += 5;
            if (bytesWritten >= bytes) {
                break;
            }
        }
        bw.close();
        System.out.println("Input file generated for size : " + getSize(inFile.length()));
        return inFile;
    }

    public static String getSize(long size) {
        DecimalFormat df = new DecimalFormat("0.00");
        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;
        if (size < sizeMb)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";
        return "";
    }

}
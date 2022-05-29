import java.io.*;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class btsearch {

    public static void main(String[] args) throws IOException, ParseException {

        final String heapFileName = args[0];
        final String indexFileName = args[1];
        String line;


        Date startdate = new SimpleDateFormat("yyyyMMdd").parse(args[2]);
        Date enddate = new SimpleDateFormat("yyyyMMdd").parse(args[3]);

        BufferedReader br = new BufferedReader(new FileReader(indexFileName));

        HashMap<Date, Integer> indexMap = new HashMap();

        // Load all index values to hashmap
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                String[] testArray = line.split("\0");
                Date date = new Date(new BigInteger(testArray[0]).longValue());
                int lineNum = Integer.valueOf(testArray[1]);
                indexMap.put(date, lineNum);
            }
        }
        br.close();

        ArrayList<Integer> lineNums = new ArrayList<>();

        long startTime1 = System.currentTimeMillis();

        // Get all hashmap values whose keys are in range
        for (int i = 0; i < indexMap.size(); i++) {
            Date targetDate = (Date)indexMap.keySet().toArray()[i];
            if (targetDate.equals(startdate) || targetDate.equals(enddate) || (targetDate.before(enddate) && targetDate.after(startdate))) {
                lineNums.add(indexMap.get(indexMap.keySet().toArray()[i]));
            }
        }

        Collections.sort(lineNums);

        long startTime2 = System.currentTimeMillis();

        // Get all records with the associated line nums
        br = new BufferedReader(new FileReader(heapFileName));
        int arrayIndex = 0;
        for (int i = 0; i < lineNums.get(lineNums.size() - 1); i++) {
            if (i == lineNums.get(arrayIndex)) {
                System.out.println(br.readLine());
                arrayIndex++;
            } else {
                br.readLine();
            }
        }
        if (lineNums.get(lineNums.size() - 1) == lineNums.size()) {
            System.out.println(br.readLine());
        }

        long endTime = System.currentTimeMillis();

        System.out.println("------------------------------------------");
        System.out.println("Time Taken to Perform Search (Including Copying values from Hashmap that match the Search): " + (endTime - startTime1) + "ms");
        System.out.println("Time Taken to Perform Search (Read from arraylist and heapfile only): " + (endTime - startTime2) + "ms");
        System.out.println("------------------------------------------");

    }

}

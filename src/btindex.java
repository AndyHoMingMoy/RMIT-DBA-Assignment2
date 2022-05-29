import java.io.*;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class btindex {

    public static void main(String[] args) throws IOException, ParseException {

        final String fileName = args[2];

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line;
        int pageSize = Integer.valueOf(args[1]);
        int totalBytesReadForPage = 0;

        HashMap<String, Integer> recordList = new HashMap<>();
        BTree bT = new BTree(pageSize);

        /*
         * Build B+ Tree
        */

        int recordNum = 0;
        Matcher matcher;
        while((line = bufferedReader.readLine()) != null) {
            if (!line.isEmpty()) {
                if (totalBytesReadForPage + line.getBytes().length <= pageSize) {
                    if (Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(line).find()) {
                        recordList.put(line, recordNum);
                    }
                    totalBytesReadForPage += line.getBytes().length;
                } else {
                    for (int i = 0; i < recordList.size(); i++) {
                        matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(recordList.keySet().toArray()[i].toString());
                        if (matcher.find()) {
                            bT.insert(new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group()), recordList.get(recordList.keySet().toArray()[i]));
                        }
                    }
                    totalBytesReadForPage = 0;
                    recordList.clear();
                    if (Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(line).find()) {
                        recordList.put(line, recordNum);
                    }
                    totalBytesReadForPage += line.getBytes().length;
                }
                recordNum++;
            }
        }
        for (int i = 0; i < recordList.size(); i++) {
            matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(recordList.keySet().toArray()[i].toString());
            if (matcher.find()) {
                bT.insert(new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group()), recordList.get(recordList.keySet().toArray()[i]));
            }
        }

        /*
         * Build Index File
         */

        long startTime = System.currentTimeMillis();

        HashMap<Date, Integer> index = bT.getIndex();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream("index." + pageSize);
        for (int i = 0; i < index.size(); i++) {
            // Get date to bytes
            long dateToLong = ((Date) index.keySet().toArray()[i]).getTime();

            // Get value to bytes
            int value = index.get(index.keySet().toArray()[i]);

            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(dateToLong);

            bos.write(Long.toString(dateToLong).getBytes());
            bos.write('\0');
            bos.write(Integer.toString(value).getBytes());
            bos.write('\n');
            fos.write(bos.toByteArray());
            bos.reset();
        }
        fos.close();

        long endTime = System.currentTimeMillis();

        System.out.println("------------------------------------------");
        System.out.println("Height of Tree: " + bT.getHeight());
        System.out.println("Time taken to Build Index File: " + (endTime - startTime) + "ms");
        System.out.println("------------------------------------------");
    }


}

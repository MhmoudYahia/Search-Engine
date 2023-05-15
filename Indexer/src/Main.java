import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.*;
import java.util.*;

public class Main {
    public static int lastFileOpened =0;
    public static HashMap< String, DataBaseObject> words_DBMap = new HashMap< String, DataBaseObject>();

    public static HashMap< String, String> Link_Score_Map = new HashMap< String, String>();

   public static void main(String[] args) throws InterruptedException, IOException {
        //start from last file opened
       try {
            File current_file = new File("currentFile.txt");
            try (Scanner myScanner = new Scanner(current_file)) {
                if (myScanner.hasNext()) {
                    lastFileOpened = myScanner.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
       //set Data base
       indexer.setDB();
       loadScores();

        System.out.println("start at file#"+lastFileOpened);
        Thread[] thrds = new Thread[Constants.NUM_THREADS];
        indexer indexer = new indexer();
        for (int i = 0; i < Constants.NUM_THREADS; i++) {
            thrds[i] = new Thread(indexer);
            thrds[i].setName(String.valueOf(i));
            thrds[i].start();
        }
        for (int i = 0; i < Constants.NUM_THREADS; i++) {
            thrds[i].join();
        }
        //record last file opened
       BufferedWriter writer = null;
       try {
           writer = new BufferedWriter(new FileWriter("currentFile.txt", true));
           writer.write(String.valueOf(indexer.fileCnt - 1));
           writer.close();
       } catch (IOException e1) {
           e1.printStackTrace();
       }
       System.out.println("end at file#"+ (indexer.fileCnt - 1) );

        System.out.println("start Adding (our DBMap) to the data base.");
        System.out.println(" indexer.documentCount "+ indexer.documentCount);
        List<DBObject> DBlist = new ArrayList<>();
        for (Map.Entry<String, DataBaseObject> entry : words_DBMap.entrySet()) {
            DBObject doc = entry.getValue().convertToDocument();
            DBlist.add(doc);
        }

        //insert in DB

        if(lastFileOpened == 0){
            indexer.webpagesCollection.insert(new BasicDBObject("info","Doc_meta_data" ).append("Doc_Cnt",indexer.fileCnt));
        }else{
            BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject("Doc_Cnt", indexer.fileCnt));
            indexer.webpagesCollection.update(new BasicDBObject("info","Doc_meta_data" ), update);
        }
       indexer.webpagesCollection.insert(DBlist);
        System.out.println("list addedd" +DBlist.size());
        System.out.println("addedd dirictly "+indexer.tempcnt);
        System.out.println("Finished Adding to the data base.");
    }

    public static void loadScores(){
        try {
            File Scores = new File("Crawler/Seed/Scores.txt");
            try (Scanner myScanner = new Scanner(Scores)) {
                while (myScanner.hasNextLine()) {
                   String link = myScanner.nextLine();
                    String score = "";
                   if(myScanner.hasNextLine()) {
                       score = myScanner.nextLine();

                   }
                   Link_Score_Map.put( link, score);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
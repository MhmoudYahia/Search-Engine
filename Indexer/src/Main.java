import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class Main {
   public static void main(String[] args) throws InterruptedException, IOException {
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
        System.out.println("start Adding (our DBMap) to the data base.");
        System.out.println(" indexer.documentCount "+ indexer.documentCount);
        List<DBObject> DBlist = new ArrayList<>();
        for (Map.Entry<String, DataBaseObject> entry : indexer.words_DBMap.entrySet()) {
            entry.getValue().CalculateIDF(indexer.documentCount);
            DBObject doc = entry.getValue().convertToDocument();
            DBlist.add(doc);
        }

        //set Data base
        indexer.setDB();
        indexer.webpagesCollection.insert(DBlist);
        indexer.webpagesCollection.insert(new BasicDBObject("docCnt", indexer.documentCount));
        System.out.println("Finished Adding to the data base.");

    }






}
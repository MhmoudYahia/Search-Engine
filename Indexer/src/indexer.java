
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tartarus.snowball.ext.porterStemmer;
import com.mongodb.DBCollection;
import com.mongodb.DB;
/*
 * @author mahmoud
 */
public class indexer implements Runnable {

    public void run() {
        indexHandeler();
    }
    /*
     * class variables
     */
    public static DB database;
    public static DBCollection webpagesCollection;
    public static MongoClient mongoClient;
    public static List<String> page_links = new ArrayList<String>();
    public static int documentCount = 0;
    public static boolean addToDataBasePhase = false;
    public static boolean updateOldDataBasePhase = false;

    public static HashMap< String, DataBaseObject> words_DBMap = new HashMap< String, DataBaseObject>();

    //    public static void main(String[] args) throws Exception {
//
//        Thread thrds[] = new Thread[Constants.NUM_THREADS];
//        indexer indxer = new indexer();
//        for (int i = 0; i < Constants.NUM_THREADS; i++) {
//            thrds[i] = new Thread(indxer);
//            thrds[i].setName(String.valueOf(i));
//            thrds[i].start();
//        }
//        for (int i = 0; i < Constants.NUM_THREADS; i++) {
//            thrds[i].join();
//        }
//        System.out.println("start Adding (our DBMap) to the data base.");
//        List<DBObject> DBlist = new ArrayList<>();
//        for (Map.Entry<String, DataBaseObject> entry : words_DBMap.entrySet()) {
//            entry.getValue().CalculateIDF(documentCount);
//            DBObject doc = entry.getValue().convertToDocument();
//            DBlist.add(doc);
//        }
//
//        //set Data base
//        indexer.setDB();
//        webpagesCollection.insertMany(DBlist);
//        webpagesCollection.insertOne(new BasicDBObject("docCnt", documentCount));
//        System.out.println("Finished Adding to the data base.");
//    }
    public static void setDB() {
        System.out.println("starting data base.");
        mongoClient = new MongoClient(Constants.DATABASE_HOST_ADDRESS, Constants.DATABASE_PORT_NUMBER);
        mongoClient.dropDatabase(Constants.DATABASE_NAME);
        database = mongoClient.getDB(Constants.DATABASE_NAME);

        //IndexOptions indexOPtions =new IndexOptions();
        webpagesCollection = database.getCollection(Constants.WEB_PAGES_COLLECTION);
        //collection.createIndex(Indexes.ascending(Constants.F_URL),indexOPtions);
        System.out.println("Connecting to DB successfully.");

    }

    public indexer() {

    }

    public void indexHandeler() {
        System.out.println("start indexing");
        HashMap< String, IndexedWebPage> wordsHashMap = new HashMap<>();
        HashMap<String, String> stemmedToNonStemmedMap = new HashMap<>();

        int th_id = Integer.valueOf(Thread.currentThread().getName());
        int current_Index = th_id;

//        // current index file read & write
//        BufferedWriter writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter("currentindex/" + th_id + ".txt", true));
//            writer.close();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }

        // get current_index
//        try {
//            File currentIndex = new File("currentindex/" + th_id + ".txt");
//            try (Scanner myScanner = new Scanner(currentIndex)) {
//                if (myScanner.hasNext()) {
//                    current_Index = myScanner.nextInt();
//                }
//            }
//            currentIndex.delete();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

        // load stop words
        String stopWords = loadStopWords();

        try {
            int fileCnt = 0;
            int fileTotCnt = 0;

            try (Stream<Path> files = Files.list(Paths.get("Crawler/Files"))) {
                fileCnt = (int)files.count() - 1;
            }
            fileTotCnt =fileCnt;
            while(fileCnt != 0 ) {
                if(current_Index < fileTotCnt) {
                    File htmlFile = new File("Crawler/Files/"+current_Index+"/"+current_Index+".html");
                    String ParsedStr = Jsoup.parse(htmlFile, null).text();

                    String StemmedStr = processStringWithStemming(ParsedStr, stopWords);
                    String nonStemmedStr = processStringWithoutStemming(ParsedStr, stopWords);
                    String[] nonStmdWords = nonStemmedStr.split(" ");

                    int words_i = 0;
                    String linkURL = "";
// to do
                    try {
                    File linkFile = new File("Crawler/Files/"+current_Index+"/"+"link.txt");

                    try (Scanner myScanner = new Scanner(linkFile)) {
                        while (myScanner.hasNext()) linkURL += myScanner.nextLine();
                    }
                                linkFile.delete();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    System.out.println("URL#"+current_Index +" : "+linkURL);
                    String title = "";
                    String desc = "";
                    Document doc = null;

                    for (String str : StemmedStr.split(" ")) {
                        if (wordsHashMap.containsKey(str)) {
                            wordsHashMap.get(str).addPosition(words_i);
                        } else {
                            stemmedToNonStemmedMap.put(str, nonStmdWords[words_i]);
                            wordsHashMap.put(str, new IndexedWebPage(linkURL, words_i));
                        }
                        words_i++;
                    }

                    //parse title & headings
                    doc = Jsoup.parse(htmlFile, "UTF-8");
                    title = doc.title();
                    String[] heads = new String[3];
                    for (int i = 0; i < 3; i++) {
                        Elements h = doc.getElementsByTag("h" + String.valueOf(i + 1));
                        heads[i] = Jsoup.parse(h.toString()).text();
                    }

                    //parse desc
                    for (Map.Entry<String, IndexedWebPage> entry : wordsHashMap.entrySet()) {
                        if (entry.getKey() == null || "".equals(entry.getKey())) {
                            break;
                        }
                        // Find the <meta> tag with the name attribute set to "description"
                        if(doc.select("meta[name=description]").size() > 0) {
                            Element meta = doc.select("meta[name=description]").first();

                            // Extract the value of the "content" attribute from the <meta> tag
                            if (meta.hasAttr("content")) {
                                desc = meta.attr("content");
                            }
                        }else{
                            Elements elements= doc.getElementsContainingOwnText(stemmedToNonStemmedMap.get(entry.getKey()));
                        if(elements.size() > 0) {
                            Element descr = doc.getElementsContainingOwnText(stemmedToNonStemmedMap.get(entry.getKey())).get(0).clearAttributes();
                            desc = Jsoup.parse(descr.toString()).text();
                        }
                        }
                        //set headings
                        // check on this entry word, in which headings?
                        for (int i = 0; i < 3; i++) {
                            heads[i] = processStringWithStemming(heads[i], stopWords);
                            for (String word : heads[i].split(" ")) {
                                if (word.equals(entry.getKey())) {
                                    entry.getValue().setHeadings(i);
                                    break;
                                }
                            }
                        }

//                // for test
//                System.out.println("title " +title);
//                System.out.println("desc " +desc);
//                System.out.println("entry.getValue() " +entry.getValue().WordPositions);


                        //set to indexed word
                        entry.getValue().setTitle(title);
                        entry.getValue().setDesc(desc);
                        entry.getValue().normalize(StemmedStr.length());

                        //add to DB
                        synchronized (indexer.class) {
                            if (words_DBMap.containsKey(entry.getKey())) {
                                words_DBMap.get(entry.getKey()).addToPageData(entry.getValue());

                            } else {
                                words_DBMap.put(entry.getKey(), new DataBaseObject(entry.getKey(), entry.getValue()));
                            }
//                    System.out.println("entry.getValue() "+entry.getValue().TF);
                        }
                    }
                    synchronized (indexer.class) {
                        documentCount++;
                    }
                    System.out.println("Thread : " + th_id + " finished link num :" + current_Index);
                    current_Index += Constants.NUM_THREADS;
                }
                fileCnt--;
            }
//             writer = new BufferedWriter(new FileWriter("currentindex/" + th_id + ".txt", true));
//                writer.write(String.valueOf(current_Index));


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Thread#"+th_id+ " finished indexing");
        System.out.println("finished Docs untill now: "+ documentCount);
    }

    //return a clean, preprocessed and stemmed string
    public static String processStringWithStemming(String txt, String stopWords) {

        String processd_txt = "";
        txt = processStringWithoutStemming(txt, stopWords);
        porterStemmer stemmer = new porterStemmer();

        for (String iterator : txt.split(" ")) {
            stemmer.setCurrent(iterator);
            stemmer.stem();
            processd_txt += stemmer.getCurrent() + " ";
        }
        return processd_txt;
    }

    public static String processStringWithoutStemming(String txt, String stopWords) {

        String processd_txt = "";
        txt = txt.replaceAll("[^a-zA-Z0-9_ ]", "");
        txt = txt.replaceAll("\\s+", " ");
        txt = txt.toLowerCase();                                    //
        processd_txt = txt.replaceAll("\\b(" + stopWords + ")\\b\\s?", "");  // this wrapper for (word1 | word2 | ....)
        return processd_txt;
    }

    //return a string with the stop words
    public static String loadStopWords() {
        String stopWords = "";
        //try catch to check if the stop words file is opened correctly
        try {
            File stopWordsFile = new File("stopwords.txt");
            //Reading the first stop word
            try (Scanner sc = new Scanner(stopWordsFile)) {
                //Reading the first stop word
                if (sc.hasNext()) {
                    stopWords += sc.nextLine();
                }
                //Reading the remaining stop words
                while (sc.hasNext()) {
                    stopWords += "|" + sc.nextLine();
                }
            }
        } catch (FileNotFoundException e) {
            //Print an error message if an exception is thrown
            System.out.println("An error occurred.");
        }
        return stopWords;
    }
}

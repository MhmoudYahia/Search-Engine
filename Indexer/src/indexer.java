
import com.mongodb.*;
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

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tartarus.snowball.ext.porterStemmer;

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
    public static int tempcnt=0;
    public static DB database;
    public static DBCollection webpagesCollection;
    public static MongoClient mongoClient;
    public static List<String> page_links = new ArrayList<String>();
    public static int documentCount = 0;
    public static boolean addToDataBasePhase = false;
    public static boolean updateOldDataBasePhase = false;
    public static int fileCnt = 0;


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

        mongoClient = new MongoClient(Constants.DATABASE_HOST_ADDRESS, Constants.DATABASE_PORT_NUMBER);
        database = mongoClient.getDB(Constants.DATABASE_NAME);
        if(Main.lastFileOpened == 1) {
            System.out.println("starting new data base.");
            mongoClient.dropDatabase(Constants.DATABASE_NAME);
        }
        else{
            System.out.println("append to data base");
        }

        webpagesCollection = database.getCollection(Constants.WEB_PAGES_COLLECTION);

        // create a word index to make the search in O(1)
        DBObject index = new BasicDBObject("Word", 1);
        DBObject options = new BasicDBObject("unique", false).append("name", "word_index");
        webpagesCollection.createIndex(index, options);

        System.out.println("Connecting to DB successfully.");

    }

    public indexer() {
        try (Stream<Path> files = Files.list(Paths.get("Crawler/Files"))) {
            fileCnt = (int)files.count() - 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void indexHandeler() {
        System.out.println("start indexing");


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


           int curr_file = Main.lastFileOpened;
           current_Index+=Main.lastFileOpened;
            while(curr_file <= fileCnt ) {
                if(current_Index < fileCnt) {
                    HashMap< String, IndexedWebPage> wordsHashMap = new HashMap<>();
//                    HashMap<String, String> stemmedToNonStemmedMap = new HashMap<>();
                    File htmlFile = new File("Crawler/Files/"+current_Index+"/"+current_Index+".html");
                    String ParsedStr = Jsoup.parse(htmlFile, null).text();

                    String StemmedStr = processStringWithStemming(ParsedStr, stopWords);
 //                   String nonStemmedStr = processStringWithoutStemming(ParsedStr, stopWords);
 //                   String[] nonStmdWords = nonStemmedStr.split(" ");

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
//                    System.out.println("URL#"+current_Index +" : "+linkURL);
                    String title = "";
                    String desc = "";
                    int score = 0;
                    Document doc = null;
                    // word => page
                    for (String str : StemmedStr.split(" ")) {
                        if (wordsHashMap.containsKey(str)) {
                            wordsHashMap.get(str).addPosition(words_i);
                        } else {
//                            stemmedToNonStemmedMap.put(str, nonStmdWords[words_i]);
                            wordsHashMap.put(str, new IndexedWebPage(linkURL, words_i));
                        }
                        words_i++;
                    }

                    //parse title & headings
                    doc = Jsoup.parse(htmlFile, "UTF-8");
                    title = doc.title();
//                    String[] heads = new String[3];
//                    for (int i = 0; i < 3; i++) {
//                        Elements h = doc.getElementsByTag("h" + String.valueOf(i + 1));
//                        heads[i] = Jsoup.parse(h.toString()).text();
//                    }
                    // Find the <meta> tag with the name attribute set to "description"
                    if(doc.select("meta[name=description]").size() > 0) {
                        Element meta = doc.select("meta[name=description]").first();

                        // Extract the value of the "content" attribute from the <meta> tag
                        if (meta.hasAttr("content")) {
                            desc = meta.attr("content");
                        }
                    }
//                    else{
//                        Elements elements= doc.getElementsContainingOwnText(stemmedToNonStemmedMap.get(entry.getKey()));
//                        if(elements.size() > 0) {
//                            Element descr = doc.getElementsContainingOwnText(stemmedToNonStemmedMap.get(entry.getKey())).get(0).clearAttributes();
//                            desc = Jsoup.parse(descr.toString()).text();
//                        }
//                    }
                    //headings
                    Elements elements = doc.select("h1, h2, h3, h4 , h5, h6, body,p");
                    String h1Text = "";
                    String h2Text = "";
                    String h3Text = "";
                    String h4Text = "";
                    String h5Text = "";
                    String h6Text = "";
                    String bodyText = "";
                    String PText = "";


                    for (Element element : elements) {
                        String tagName = element.tagName();
                        String text = element.ownText();
                        if (tagName.equals("h1")) {
                            h1Text=text;
                        } else if (tagName.equals("h2")) {
                            h2Text=text;
                        } else if (tagName.equals("h3")) {
                            h3Text=text;
                        }
                        else if (tagName.equals("h4")) {
                            h4Text=text;
                        }
                        else if (tagName.equals("h5")) {
                            h5Text=text;
                        }
                        else if (tagName.equals("h6")) {
                            h6Text=text;
                        }
                      else if (tagName.equals("body")) {
                            bodyText=text;
                        }
                    }

                    //loop on all words of this link
                    for (Map.Entry<String, IndexedWebPage> entry : wordsHashMap.entrySet()) {
                        if (entry.getKey() == null || "".equals(entry.getKey())) {
                            break;
                        }
                        if( isNumeric(entry.getKey()) || entry.getKey().length() == 1){
                            continue;
                        }
                        //get p



//                         decide score

                        if(h1Text.contains(entry.getKey())){
                            score = Constants.h1Score;
                        } else if(h2Text.contains( entry.getKey())){
                            score = Constants.h2Score;
                        }else if(h3Text.contains( entry.getKey())){
                            score = Constants.h3Score;
                        }else if(h4Text.contains( entry.getKey())){
                            score = Constants.h4Score;
                        }else if(h5Text.contains( entry.getKey())){
                            score = Constants.h5Score;
                        }else if(h6Text.contains( entry.getKey())){
                            score = Constants.h6Score;
                        }else if(bodyText.contains( entry.getKey())){
                            score = Constants.bodyScore;
                        }

                        //set p
                        Elements p_elements = doc.select("p");
                        //System.out.println(p_elements);
                        for (Element element : elements) {
                            String text = element.ownText();
                            if(text.contains(entry.getKey())){
                                entry.getValue().setParagraph(text);
                                score = Constants.PScore;
                                break;
                            }
                        }
                        //set to indexed word
                        entry.getValue().setTitle(title);
                        entry.getValue().setDesc(desc);
                        entry.getValue().normalize(StemmedStr.length());
                        entry.getValue().setScore(score);
                        //add to DB
                        synchronized (indexer.class) {

                            if (Main.words_DBMap.containsKey(entry.getKey())) {
//                                System.out.println(entry.getKey());
                                Main.words_DBMap.get(entry.getKey()).addPage(entry.getValue());

                            } else {
//                                if(Main.lastFileOpened != 0) {
//                                    BasicDBObject update = new BasicDBObject("$addToSet", new BasicDBObject("Pages_Containing_This_Word", IndexedWebPage.toDocument(entry.getValue())))
//                                            .append("$inc", new BasicDBObject("Total_Apperance_in_All_Pages", 1));;
//                                    DBObject document = webpagesCollection.findAndModify(new BasicDBObject("Word", entry.getKey()), update);
//                                    if (document == null) {
//                                        Main.words_DBMap.put(entry.getKey(), new DataBaseObject(entry.getKey(), entry.getValue()));
//                                    }else{
////                                        System.out.println("word: "+entry.getKey()+" is added diriictly to DB");
//                                        tempcnt++;
//                                    }
//                                }else{
                                    Main.words_DBMap.put(entry.getKey(), new DataBaseObject(entry.getKey(), entry.getValue()));

//                                }
                            }
                        }
                    }
                    synchronized (indexer.class) {
                        documentCount++;
                    }
                    System.out.println("Thread : " + th_id + " finished link num :" + current_Index);
                    current_Index += Constants.NUM_THREADS;

                }
                curr_file++;
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
//        System.out.println("non stemmed: "+txt);
        porterStemmer stemmer = new porterStemmer();

        for (String iterator : txt.split(" ")) {
            stemmer.setCurrent(iterator);
            stemmer.stem();
            String stemed= stemmer.getCurrent();
            processd_txt += stemed + " ";
//            System.out.println("stemmed=> "+stemed+"   non stemmed=> "+iterator);
        }
//        System.out.println("stemmed: "+ processd_txt);
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
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

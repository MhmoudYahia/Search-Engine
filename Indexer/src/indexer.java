
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

                    String OriginalStr = processStringWithoutStemming(ParsedStr, stopWords);
                    String StemmedStr = processStringWithStemming(ParsedStr, stopWords);

                    int words_i = 0;
                    String linkURL = "";
// to do
                    try {
                    File linkFile = new File("Crawler/Files/"+current_Index+"/"+"link.txt");

                    try (Scanner myScanner = new Scanner(linkFile)) {
                        while (myScanner.hasNext()) linkURL += myScanner.nextLine();
                    }
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }

                    String title = "";
                    String desc = "";
                    int score = 0;
                    Document doc = null;
                    // word => page
                    String[] stemmedWords = StemmedStr.split(" ");
                    String[] originalWords = OriginalStr.split(" ");
                    if(stemmedWords.length != originalWords.length)
                    {
                        int index = 0;
                        for (String str:
                             originalWords) {
                            System.out.print(originalWords[index]);
                            System.out.print(" ");
                            System.out.print(stemmedWords[index] + "\n");
                            index++;
                        }
                    }

                    for (String str : originalWords) {
                        if (wordsHashMap.containsKey(str)) {
                            wordsHashMap.get(str).addPosition(words_i);
                        } else {
                            wordsHashMap.put(str, new IndexedWebPage(linkURL, words_i, stemmedWords[words_i]));
                        }
                        words_i++;
                    }

                    //parse title & headings
                    doc = Jsoup.parse(htmlFile, "UTF-8");
                    title = doc.title();

                    // Find the <meta> tag with the name attribute set to "description"
                    if(doc.select("meta[name=description]").size() > 0) {
                        Element meta = doc.select("meta[name=description]").first();

                        // Extract the value of the "content" attribute from the <meta> tag
                        if (meta.hasAttr("content")) {
                            desc = meta.attr("content");
                        }
                    }
                    // get elements
                    Elements elements = doc.select("h1, h2, h3, h4, h5, h6,a,button,meta");
                    String h1Text = "";
                    String h2Text = "";
                    String h3Text = "";
                    String h4Text = "";
                    String h5Text = "";
                    String h6Text = "";

                    String aText ="";
                    String buttonText ="";
                    String metaText ="";



                    for (Element element : elements) {
                        String tagName = element.tagName();
                        String text = element.text()+" ";
                        if (tagName.equals("h1")) {
                            h1Text+=text;
                        }
                        else if (tagName.equals("h2")) {
                            h2Text+=text;
                        }
                        else if (tagName.equals("h3")) {
                            h3Text += text;
                        }
                        else if (tagName.equals("h4")) {
                            h4Text += text;
                        }
                        else if (tagName.equals("h5")) {
                            h5Text += text;
                        }
                        else if (tagName.equals("h6")) {
                            h6Text += text;
                        }
                        else if (tagName.equals("a")) {
                            aText += text;
                        }
                        else if (tagName.equals("button")) {
                            buttonText += text;
                        }
                        else if (tagName.equals("meta")) {
                            metaText += text;
                        }
                    }

                    h1Text = processStringWithoutStemming(h1Text,stopWords);
                    h2Text = processStringWithoutStemming(h2Text,stopWords);
                    h3Text = processStringWithoutStemming(h3Text,stopWords);
                    h4Text = processStringWithoutStemming(h4Text,stopWords);
                    h5Text = processStringWithoutStemming(h5Text,stopWords);
                    h6Text = processStringWithoutStemming(h6Text,stopWords);
                    aText = processStringWithStemming(aText,stopWords);
                    metaText= processStringWithStemming(metaText,stopWords);
                    buttonText = processStringWithStemming(buttonText,stopWords);



                    String titleText = processStringWithoutStemming(title, stopWords);

                    //loop on all words of this link
                    for (Map.Entry<String, IndexedWebPage> entry : wordsHashMap.entrySet()) {
                        score = 0;
                        if (entry.getKey() == null || "".equals(entry.getKey())) {
                            break;
                        }
                        if(entry.getKey().length() <= 2 && !isNumeric(entry.getKey())){
                            continue;
                        }

                        if(isContain( aText,entry.getKey())||isContain( metaText,entry.getKey())||isContain( buttonText,entry.getKey())){

                            continue;
                        }

//                         decide score
                        while(isContain(h1Text,entry.getKey())){
                            score += Constants.h1Score;
                            h1Text = h1Text.replaceFirst(entry.getKey(),"");
                        }
                        while(isContain(h2Text,entry.getKey())){
                            score += Constants.h2Score;
                            h2Text = h2Text.replaceFirst(entry.getKey(),"");
                        }
                        while(isContain(h3Text,entry.getKey())){
                            score += Constants.h3Score;
                            h3Text = h3Text.replaceFirst(entry.getKey(),"");
                        }
                        while(isContain(h4Text,entry.getKey())){
                            score += Constants.h4Score;
                            h4Text = h4Text.replaceFirst(entry.getKey(),"");
                        }
                        while(isContain(h5Text,entry.getKey())){
                            score += Constants.h5Score;
                            h5Text = h5Text.replaceFirst(entry.getKey(),"");
                        }
                        while(isContain(h6Text,entry.getKey())){
                            score += Constants.h6Score;
                            h6Text = h6Text.replaceFirst(entry.getKey(),"");
                        }
                        while(isContain(titleText,entry.getKey())){
                            score += Constants.TScore;
                            titleText = titleText.replaceFirst(entry.getKey(),"");
                        }

                        //set the paragraph
                        Elements p_elements = doc.select("p");
                        for (Element element : p_elements) {
                            String _text = element.text();
                            String text = _text.toLowerCase();
                            if(isContain(text,entry.getKey())){
                                entry.getValue().setParagraph(text);
                                break;
                            }
                        }
                        //set to indexed word
                        entry.getValue().setTitle(title);
                        entry.getValue().setDesc(desc);
                        entry.getValue().normalize(OriginalStr.length());
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
                                    documentCount++;
//                                }
                            }
                        }
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
        System.out.println("finished Docs until now: "+ documentCount);
    }

    //return a clean, preprocessed and stemmed string
    public static String processStringWithStemming(String txt, String stopWords) {

        StringBuilder processd_txt = new StringBuilder();
        txt = processStringWithoutStemming(txt, stopWords);

        Analyzer analyzer = new EnglishAnalyzer();

        try (TokenStream stream = analyzer.tokenStream(null, new StringReader(txt))) {
            stream.reset();
            CharTermAttribute termAttribute = stream.addAttribute(CharTermAttribute.class);

            while (stream.incrementToken()) {
                String stemed = termAttribute.toString();
                processd_txt.append(stemed).append(" ");
            }

            stream.end();
        } catch (IOException e) {
            System.out.println("An error occurred in processStringWithStemming.");
        }

        return processd_txt.toString();
    }

    public static String processStringWithoutStemming(String txt, String stopWords) {
        if(txt.isBlank())
            return "";
        String processd_txt = "";
        txt = txt.replaceAll("[^a-zA-Z0-9 ]", "");
        txt = txt.replaceAll("\\b(?=\\w*\\d)(?=\\w*[a-zA-Z])\\w+\\b", "");
        //txt = txt.replaceAll("\\b(?=.*\\d)(?=.*[a-zA-Z])[\\w\\d]+\\b", "");
        txt = txt.replaceAll("\\s+", " ");
        if(txt.isBlank())
            return "";
        if(txt.charAt(0) == ' ')
            txt = txt.substring(1);
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

    public static boolean containsCharsAndNumbers(String str) {
        boolean hasChars = false;
        boolean hasNumbers = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                hasChars = true;
            } else if (Character.isDigit(c)) {
                hasNumbers = true;
            }

            if (hasChars && hasNumbers) {
                return true;
            }
        }

        return false;
    }

    private static boolean isContain(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }
}

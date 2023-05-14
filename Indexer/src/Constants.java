import java.util.HashMap;

public class Constants {
    //=================================== Indexer ==================================
    public static final int NUM_THREADS = 10;

    //score
    public static final int h1Score = 12;
    public static final int h2Score = 10;
    public static final int h3Score = 8;
    public static final int h4Score = 6;
    public static final int h5Score = 4;
    public static final int h6Score = 2;
    public static final int TScore = 20;


    /**
     * Database constants
     */
    public static final String DATABASE_NAME = "search_engine";
    public static final String DATABASE_HOST_ADDRESS = "localhost";
    public static final String DATABASE_URI = "mongodb+srv://YousefRabia:Goat10@cluster0.wy22fj9.mongodb.net/?retryWrites=true&w=majority";
    public static final int DATABASE_PORT_NUMBER = 27017;

    /**
     * Collection constants
     */
    public static final String WEB_PAGES_COLLECTION = "web_pages";
}

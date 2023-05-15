import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.MutablePair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class CrawlerIMP implements Runnable {
    static private HashSet<String> Vis;
    static private Map<String,Double> Rank;
    static private Queue<String>Seed;
    private RobotParser RP;
    PrintWriter pwQ = null;
    PrintWriter pwT = null;


    AtomicInteger idx;

    public CrawlerIMP(String path) {
        Vis=new HashSet<>();
        Seed=new LinkedList<>();
        Rank=new HashMap<>();
        idx=new AtomicInteger(0);
        RP=new RobotParser();
        BufferedReader bf=null;
        BufferedReader bfQ=null;
        BufferedReader bfS=null;

        int stopIdx;

        try {
            pwQ = new PrintWriter(new BufferedWriter(new FileWriter("./Crawler/Seed/queue.txt", true)));
            bf=new BufferedReader(new FileReader("./Crawler/Seed/num.txt"));
            bfQ=new BufferedReader(new FileReader("./Crawler/Seed/queue.txt"));
            File f=new File("./Crawler/Seed/Scores.txt");
            if(f.exists())
                bfS=new BufferedReader(new FileReader("./Crawler/Seed/Scores.txt"));

            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            String str=null;
            if((str=bf.readLine())!=null){
                stopIdx=Integer.parseInt(str);
                idx.set(stopIdx+1);
                int temp=0;
                while((str= bfQ.readLine())!=null){
                        if (temp > stopIdx)
                            Seed.add(str);
                        Vis.add(str);
                        temp++;
                }
                String li=null;
                double score=0;
                if(bfS!=null){
                    while((str=bfS.readLine())!=null){
                        if(pattern.matcher(str).matches()){
                            score=Double.parseDouble(str);
                            Rank.put(li,score);
                        }else{
                            li=str;
                        }
                    }
                }
            }
            else{
                BufferedWriter bw=new BufferedWriter(new FileWriter("./currentFile.txt",false));
                PrintWriter fw=new PrintWriter(bw);
                fw.println(0);
                fw.close();
                readSeed(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void StartCrawelling() {
        String curLink;
        Document doc=null;
        HashSet<String>childLinks=new HashSet<>();
        while(true) {
            synchronized(this) {
                while(Seed.isEmpty()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                curLink=Seed.poll();
            }
            if(!RobotParser.isSafe(curLink)) {
                System.out.println("NOT Safe!!! "+curLink);
                continue;
            }

            try {
                Connection con=Jsoup.connect(curLink).ignoreHttpErrors(true);
                doc = con.maxBodySize(Integer.MAX_VALUE).get();
                if(con.response().statusCode()==200 && Vis.size()<=6000 )
                {
                    childLinks=getLinks(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized(this) {
                DownloadPage(doc);
                if(childLinks!=null){
                    double baseRank=Rank.get(curLink).doubleValue();
                    System.out.println(baseRank);
                    for(String li:childLinks) {
                        if(Vis.add(li)) {
                            Rank.put(li,0.2*baseRank);
                            if(Seed.add(li));
                            {
                                pwQ.println(li);
                                pwQ.flush();
                            }
                        }else{
                            Rank.put(li,Rank.get(li)+0.2*baseRank);
                        }
                    }
                }
                notifyAll();
            }
        }
    }

    private HashSet<String> getLinks(Document doc) {
        HashSet<String> links=new HashSet<>();
        Elements linksInside=doc.select("a[href]");
        for(Element element: linksInside) {
            String childLink=element.attr("abs:href");
            links.add(childLink);
        }
        return links;
    }
    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
    private void DownloadPage(Document doc) {
        try {
            int id=idx.getAndSet(idx.intValue()+1);
            pwT = new PrintWriter(new BufferedWriter(new FileWriter("./Crawler/Seed/num.txt", false)));
            pwT.print(id);
            pwT.flush();
            File dir=new File("Crawler/Files/"+id);
            while(!dir.mkdir()){
                deleteDir(dir);
            }
            BufferedWriter buff=new BufferedWriter(new FileWriter("Crawler/Files/"+id+"/"+id+".html"));
            BufferedWriter linkbuff=new BufferedWriter(new FileWriter("Crawler/Files/"+id+"/"+"link.txt"));
            System.out.println(doc.baseUri());
            buff.write(doc.html());
            buff.close();
            linkbuff.write(doc.baseUri());
            linkbuff.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void readSeed(String path) {
        File file=new File(path);

        try {
            Scanner sc=new Scanner(file);
            String link;

            while (sc.hasNextLine()) {
                link=sc.nextLine();
                Seed.add(link);
                Vis.add(link);
                Rank.put(link,200.0);
                pwQ.println(link);
                pwQ.flush();
            }

            sc.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    static public void handleSTP(){
        try {
            PrintWriter ff=new PrintWriter(new BufferedWriter(new FileWriter("./Crawler/Seed/Scores.txt")));
            for (Map.Entry<String,Double> e:
                 Rank.entrySet()) {
                ff.println(e.getKey());
                ff.println(e.getValue());
            }
            ff.flush();
            ff.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        StartCrawelling();
    }
}

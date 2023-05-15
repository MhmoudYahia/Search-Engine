import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import crawlercommons.robots.*;
import org.apache.commons.io.IOUtils;

public class RobotParser {

    public static boolean isSafe(String url2)  {
        String USER_AGENT = "*";
        URL url= null;
        try {
            url = new URL(url2);
        } catch (MalformedURLException e) {
            return false;
        }
        URL urlObj = url;
        String hostId = urlObj.getProtocol() + "://" + urlObj.getHost()
                + (urlObj.getPort() > -1 ? ":" + urlObj.getPort() : "");
        String RobotFile="";
        String str;
        URL urll= null;
        URLConnection urlCon= null;
        try {
            urll = new URL(hostId+"/robots.txt");
            urlCon = urll.openConnection();
            InputStreamReader in=new InputStreamReader((InputStream) urlCon.getContent());
            BufferedReader bf=new BufferedReader(in);
            while ((str=bf.readLine())!=null){
                RobotFile+=str;
                RobotFile+="\n";
            }
            bf.close();
            in.close();
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return true;
        }


        SimpleRobotRulesParser RP=new SimpleRobotRulesParser();
        BaseRobotRules rules= RP.parseContent(hostId,IOUtils.toByteArray(RobotFile),"text/plain",USER_AGENT);
        boolean urlAllowed = rules.isAllowed(url.toString());
        return urlAllowed;
    }
}

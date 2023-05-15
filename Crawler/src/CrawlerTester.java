import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.Scanner;

public class CrawlerTester {
    public static int num_threads=4;
    public static void main(String[] args) {
        CrawlerIMP CR=new CrawlerIMP("Crawler/Seed/Seed.txt");
        Scanner scan=new Scanner(System.in);
        System.out.print("Please, Enter the number of threads for Crawler: ");
        String temp;
        do{
            temp=scan.nextLine();
            if(temp.matches("\\d")){
                break;
            }else{
                System.out.print("Invalid!!! Please, Enter the number of threads for Crawler: ");
            }
        }while(true);
        num_threads=Integer.parseInt(temp);
        System.out.println("The Current Number of threads: "+num_threads);

        Signal sigInt = new Signal("INT");
        final SignalHandler oldHandler = Signal.handle(sigInt, SignalHandler.SIG_DFL );

        Signal.handle(new Signal("INT"), signal -> {
            CrawlerIMP.handleSTP();
            oldHandler.handle(signal);
        });
        Thread[] threads=new Thread[num_threads];
        for(int i=0; i<num_threads;i++)
        {
            Thread t=new Thread(CR);
            threads[i]=t;
            threads[i].start();
        }

        for(int i=0; i<num_threads;i++)
        {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
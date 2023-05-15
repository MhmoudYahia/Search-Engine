import sun.misc.Signal;
import sun.misc.SignalHandler;

public class CrawlerTester {
    final static int num_threads=6;
    public static void main(String[] args) {

        CrawlerIMP CR=new CrawlerIMP("./Crawler/Seed/Seed.txt");

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
public class CrawlerTester {
    final static int num_threads=6;
    public static void main(String[] args) {

        CrawlerIMP CR=new CrawlerIMP("Crawler/Seed/Seed.txt");
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
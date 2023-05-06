public class CrawlerTester {
    public static void main(String[] args) {

        CrawlerIMP CR=new CrawlerIMP("Crawler/Seed/Seed.txt");
        Thread[] threads=new Thread[5];
        for(int i=0; i<5;i++)
        {
            Thread t=new Thread(CR);
            threads[i]=t;
            threads[i].start();
        }
        for(int i=0; i<5;i++)
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
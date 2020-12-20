
public class CrawlerTask implements Runnable{

    private URLDepthPair urlDepthPair;

    public CrawlerTask (URLDepthPair urlDepthPair){
        this.urlDepthPair = urlDepthPair;
    }

    // [Runnable] interface
    @Override
    public void run() {
        Crawl.crawlThroughURL(urlDepthPair);
        Crawl.activeThreadsDec();
    }

}

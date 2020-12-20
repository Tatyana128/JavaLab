public class URLDepthPair {

    public static final String URL_PREFIX = "http://";//для определения нужного формата
    public static final String URL_PREFIX_S = "https://";//для определения нужного формата

    private String URLAddress;//адрес
    private int depth;//глубина поиска

    URLDepthPair (String URLAddress, int depth){
        this.URLAddress = URLAddress;
        this.depth = depth;
    }


    public String getStringFormat (){
        return "URL = " + URLAddress + ", depth = " + depth;
    }


    public void setURLAddress(String URLAddress) {
        this.URLAddress = URLAddress;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public int getDepth() {
        return depth;
    }
    public String getURLAddress() {
        return URLAddress;
    }
}

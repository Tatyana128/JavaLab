import jdk.jfr.internal.tool.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class Crawl {

    public static final String BEFORE_URL = "a href=";
    public static final int MAXDepth = 3;//глубина поиска
    public static final int MAXThreads = 16;// почему 16

    private static int activeThreads = 0;

    private static LinkedList<URLDepthPair> CheckedURL = new LinkedList<>();
    private static LinkedList<URLDepthPair> UncheckedURL = new LinkedList<>();


    private static final Main m = new Main();

    public static void main(String[] args) throws IOException {

        URLDepthPair firstURL = new URLDepthPair("https://slashdot.org/", 0);

        UncheckedURL.add(firstURL);//первый адрес из методички

        synchronized (UncheckedURL) {
            synchronized (CheckedURL) {
        while (!UncheckedURL.isEmpty() || activeThreads != 0) { // работает пока не закончатся непроверенные ссылки и все потоки

            // Если потоков меньше MAXThreads -> крутим цикл (иначе переводим поток в состояние ожидания).
            try {
                synchronized (m) {
                    while (activeThreads >= MAXThreads) {
                        System.out.println("waiting");
                        m.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();//показывает где ошибка
            }


            if (!UncheckedURL.isEmpty()) {// Если UncheckedURL не пуст
                if (UncheckedURL.get(0).getDepth() < MAXDepth) {//если глубина поиска не превышена

                    //добавляем UncheckedURL.get(0) к CheckedURL и удаляем из UncheckedURL
                    URLDepthPair urlDepthPair = UncheckedURL.get(0);
                    UncheckedURL.remove(urlDepthPair);
                    CheckedURL.add(urlDepthPair);

                    System.out.println(urlDepthPair.getStringFormat());

                    //открытие нового потока и чтение в нем
                    activeThreads++;
                    startThread(urlDepthPair);

                } else {
                    CheckedURL.add(UncheckedURL.get(0));
                    UncheckedURL.remove(UncheckedURL.get(0));
                }
            }
        }
            }
        }

        //вывод прочитанных и непрочитанных адресов
        System.out.println("UNCHECKED");
        printAll(UncheckedURL);
        System.out.println("CHECKED");
        printAll(CheckedURL);
    }

    public static void startThread (URLDepthPair urlDepthPair){//для создания нового потока
        new Thread(){
            @Override
            public void run() {
                super.run();
                crawlThroughURL(urlDepthPair);//читает новый url в потоке
                activeThreads--;
                synchronized (m) {
                    if (activeThreads <= MAXThreads) {
                        m.notify();//продолжает работу потока
                        System.out.println("notifying");
                    }
                }
            }
        }.start();
    }

    private static void crawlThroughURL (URLDepthPair urlDepthPair){
        try {
            // установка соединения
            URLConnection urlConnection = new URL(urlDepthPair.getURLAddress()).openConnection();
            urlConnection.setConnectTimeout(10_1000);

            // Reader для чтения
            BufferedReader in = null;
            InputStreamReader inputStreamReader = null;
            try {
                 inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                 in = new BufferedReader(inputStreamReader);
            } catch (ConnectException connectException) {
                // connectException.printStackTrace();
            }

            // читаем сайт
            String s;
            if (in != null) {
                while ((s = in.readLine()) != null) {

                    if (s.contains(Crawl.BEFORE_URL + "\"" + URLDepthPair.URL_PREFIX) && urlDepthPair.getDepth() < Crawl.MAXDepth) { // содержит a href="http://
                        try {
                            //вырезаем адрес
                            String url = s.substring(s.indexOf(Crawl.BEFORE_URL + "\"" + URLDepthPair.URL_PREFIX) + Crawl.BEFORE_URL.length() + 1); // обрезаем url адресс от лишнего слева
                            url = url.substring(0, url.indexOf("\"")); //обрезаем url адресс от лишнего справа


                            URLDepthPair foundURL = new URLDepthPair(url, urlDepthPair.getDepth() + 1);
                            //добавление нового адреса в список непроверенных
                            if (!listContains(UncheckedURL, foundURL) && !listContains(CheckedURL, foundURL)) {
                                UncheckedURL.add(foundURL);
                            }
                        } catch (StringIndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                    if (s.contains(Crawl.BEFORE_URL + "\"" + URLDepthPair.URL_PREFIX_S) && urlDepthPair.getDepth() < Crawl.MAXDepth) {
                        try {

                            String url = s.substring(s.indexOf(Crawl.BEFORE_URL + "\"" + URLDepthPair.URL_PREFIX_S) + Crawl.BEFORE_URL.length() + 1); // обрезаем url адресс от лишнего слева
                            url = url.substring(0, url.indexOf("\"")); //обрезаем url адресс от лишнего справа


                            URLDepthPair foundURL = new URLDepthPair(url, urlDepthPair.getDepth() + 1);
                            if (!listContains(UncheckedURL, foundURL) && !listContains(CheckedURL, foundURL)) {
                                UncheckedURL.add(foundURL);
                            }
                        } catch (StringIndexOutOfBoundsException e){
                            //e.printStackTrace();
                        }
                    }

                    if (s.contains("301 Moved Permanently") && !urlDepthPair.getURLAddress().contains(URLDepthPair.URL_PREFIX_S)){
                        crawlThroughURL(new URLDepthPair(urlDepthPair.getURLAddress().replace(URLDepthPair.URL_PREFIX, URLDepthPair.URL_PREFIX_S), urlDepthPair.getDepth()));
                        break;
                    }

                }

                in.close();
                inputStreamReader.close();
                urlConnection.getInputStream().close();
            }
        } catch (IOException ioException) {
            //ioException.printStackTrace();
        }
    }

    //для определения содержет ли список адресов конкретный адрес
    private static boolean listContains (LinkedList<URLDepthPair> linkedList, URLDepthPair urlDepthPair) {

        if (linkedList.isEmpty()) return false;
        LinkedList<URLDepthPair> newLinkedList =(LinkedList<URLDepthPair>) linkedList.clone(); // чтобы избежать ConcurrentModificationException
        for (URLDepthPair ur: newLinkedList) {
            if (ur.getURLAddress().equals(urlDepthPair.getURLAddress())) return true;
        }
        return false;
    }



    private static void printAll (LinkedList<URLDepthPair> linkedList){//для вывода результата
        for (URLDepthPair urlDepthPair : linkedList) {
            System.out.println(urlDepthPair.getStringFormat());
        }
    }





}

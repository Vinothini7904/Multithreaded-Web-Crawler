import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class WebCrawler {

    // Maintain a list of visited URLs to avoid revisiting them
    private static Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Initialize a semaphore for synchronization between threads
    private static final Semaphore semaphore = new Semaphore(1);

    // Function to download a page given its URL and save it to a file
    private static void downloadPage(String pageUrl, String fileName) {
        try {
            URL url = new URL(pageUrl);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                 BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
                String line;
                while ((line = in.readLine()) != null) {
                    out.write(line);
                    out.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to extract links from a file containing HTML code
    private static List<String> extractLinksFromFile(String fileName) {
        List<String> links = new ArrayList<>();
        Pattern pattern = Pattern.compile("<a href=\"(.*?)\">", Pattern.CASE_INSENSITIVE);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String link = matcher.group(1);
                    if (link.startsWith("http") || link.startsWith("https")) {
                        links.add(link.split(" ")[0].replace("\"", ""));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

    // Function to check if a URL is already present in the list of visited URLs
    private static boolean contains(Set<String> set, String url) {
        return set.contains(url);
    }

    // Function to crawl a URL
    private static void crawl(String url, String fileName, int depth, int id) {
        if (depth <= 0 || contains(visited, url)) {
            return;
        }

        visited.add(url);
        try {
            semaphore.acquire();
            System.out.println("\t\tThread ID : " + id);
            System.out.println("Extracted Link : " + url);

            // Download the page and extract links from it
            downloadPage(url, fileName);
            List<String> links = extractLinksFromFile(fileName);

            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Crawl all the extracted links recursively
        for (String link : links) {
            try {
                Thread.sleep(10);  // Simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!contains(visited, link)) {
                crawl(link, fileName, depth - 1, id);
            }
        }
    }

    public static void main(String[] args) {
        // Create a thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Start crawling with three different URLs
        executor.execute(() -> crawl("https://www.geeksforgeeks.org/mutex-vs-semaphore/", "Links.txt", 3, 1));
        executor.execute(() -> crawl("https://stackoverflow.com/questions/5620235/cpp-regular-expression-to-validate-url", "Links.txt", 3, 2));
        executor.execute(() -> crawl("https://stackoverflow.com/questions/7432100/how-to-get-integer-thread-id-in-c11", "Links.txt", 3, 3));

        // Shutdown the executor and wait for tasks to finish
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}

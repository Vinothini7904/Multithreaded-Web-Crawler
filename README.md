# **Multi-threaded Web Crawler**

## **Project Overview**
This project is a multi-threaded web crawler implemented in Java that downloads web pages and extracts links from HTML files. It supports concurrent crawling with multiple threads, synchronizing the process using semaphores. Additionally, the project includes **link priority evaluation** and **web page classification techniques** to optimize the crawling strategy.

## **Features**
- **Multi-threaded Crawling**: Leverages Java's `ExecutorService` to create a pool of threads that can download and process multiple web pages concurrently.
- **Link Extraction**: Extracts all valid links (`<a href="...">`) from downloaded HTML pages.
- **Link Priority Evaluation**: Prioritizes links based on custom rules (e.g., domain relevance, URL pattern).
- **Web Page Classification**: Classifies crawled pages using basic content-based techniques (e.g., based on keywords or metadata).

## **Installation**

### **Dependencies**
This project requires **Java** (JDK 8 or higher). No additional libraries are needed.

### **Steps to Compile and Run**
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/multithreaded-webcrawler.git
    ```
2. Compile the Java code:
    ```bash
    javac WebCrawler.java
    ```
3. Run the crawler:
    ```bash
    java WebCrawler
    ```

## **Link Priority Evaluation**
The crawler evaluates links before deciding which to crawl based on the following criteria:
1. **Relevance**: Links from the same domain or subdomains are prioritized over external links.
2. **URL Patterns**: Links matching certain patterns (e.g., `/blog/`, `/tutorials/`) can be given higher priority.
3. **Page Depth**: Links appearing at a higher depth level are given lower priority.

### **How Link Priority Works**
```java
 //Function to prioritize links based on predefined rules
private static int evaluateLinkPriority(String link) {
    if (link.contains("blog") || link.contains("tutorials")) {
        return 1;  // High priority
    }
    return 0;  // Default priority
}
```
The priority score is assigned during the extraction phase, and higher priority links are crawled first.

## **Web Page Classification Techniques**
After downloading a web page, the content is analyzed to classify the page. This project uses a simple keyword-based classification to categorize pages (e.g., **Tech**, **News**, or **Education**). You can extend this functionality with more complex classifiers such as **TF-IDF** or machine learning models.

### **How Classification Works**
```java
// Simple keyword-based page classification
private static String classifyPageContent(String content) {
    if (content.contains("programming") || content.contains("tutorial")) {
        return "Tech";
    } else if (content.contains("news")) {
        return "News";
    } else if (content.contains("education") || content.contains("university")) {
        return "Education";
    }
    return "Other";
}
```
The crawler assigns a category to each downloaded page based on its content and stores this information for further analysis.

## **Usage**
- **Input**: List of URLs to start crawling from.
- **Output**: A file `Links.txt` that contains all the extracted links from the crawled web pages.
- **Multithreading**: By default, the crawler uses 3 threads. You can adjust this in the `ExecutorService`.

### **Sample Output**
```txt
Thread ID: 1
Extracted Link: https://www.geeksforgeeks.org/mutex-vs-semaphore/
Classified as: Tech

Thread ID: 2
Extracted Link: https://stackoverflow.com/questions/5620235/cpp-regular-expression-to-validate-url
Classified as: Tech
```

## **How to Extend**

### **1. Priority-based Crawling**
Modify the `evaluateLinkPriority` method to implement more complex rules for link prioritization. You can prioritize certain domains, URL structures, or keywords in the links.

### **2. Advanced Classification**
Integrate machine learning models for better accuracy in classifying web pages based on their content. For example, you can use models like **TF-IDF** or **BERT** to classify pages more accurately than with basic keyword matching.

### **3. Custom Crawling Rules**
Add custom conditions to the `crawl` method to target specific content or focus on certain types of pages. For instance, you can crawl pages based on specific keywords in the HTML body or metadata (e.g., `<meta>` tags).

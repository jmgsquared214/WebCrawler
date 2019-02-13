
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The ExtractSave Class
 * @author Jamie Gammel
 */

public class ExtractSave {


        private HashSet<String> links;
        private List<List<String>> articles;

        public ExtractSave() {
            links = new HashSet<>();
            articles = new ArrayList<>();
        }

        //Find all URLs that start with "http://www.thinkful.com/page/" and add them to the HashSet
        public void getPageLinks(String URL) {
            if (!links.contains(URL)) {
                try {
                    Document document = Jsoup.connect(URL).get();
                    Elements otherLinks = document.select("a[href^=\"http://www.thinkful.com/page/\"]");

                    for (Element page : otherLinks) {
                        if (links.add(URL)) {
                            //Remove the comment from the line below if you want to see it running on your editor
                            System.out.println(URL);
                        }
                        getPageLinks(page.attr("abs:href"));
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        //Connect to each link saved in the article and find all the articles in the page
        public void getArticles() {
            links.forEach(x -> {
                Document document;
                try {
                    document = Jsoup.connect(x).get();
                    Elements articleLinks = document.select("h2 a[href^=\"http://www.thinkful.com/\"]");
                    for (Element article : articleLinks) {
                        //Only retrieve the titles of the articles that contain Java 8
                        if (article.text().matches("^.*?(Mentor |mentor |MENTOR).*$")) {


                            //Remove the comment from the line below if you want to see it running on your editor,
                            //or wait for the File at the end of the execution
                            //System.out.println(article.attr("abs:href"));

                            ArrayList<String> temporary = new ArrayList<>();
                            temporary.add(article.text()); //The title of the article
                            temporary.add(article.attr("abs:href")); //The URL of the article
                            articles.add(temporary);
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        }

        public void writeToFile(String filename) {
            FileWriter writer;
            try {
                writer = new FileWriter(filename);
                articles.forEach(a -> {
                    try {
                        String temp = "- Title: " + a.get(0) + " (link: " + a.get(1) + ")\n";
                        //display to console
                        System.out.println(temp);
                        //save to file
                        writer.write(temp);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
                writer.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void main(String[] args) {
            ExtractSave bwc = new ExtractSave();
            bwc.getPageLinks("http://www.thinkful.com");
            bwc.getArticles();
            bwc.writeToFile("Mentor");
        }
    }


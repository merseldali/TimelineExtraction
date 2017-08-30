package fr.tse.pri.reader;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import fr.tse.pri.workers.Extractor;

/**
 * Load data from the XML dump and send its to a queue
 *
 * @author julien
 *
 */
public class Main {

    private static final String POISON = new String("poison");

    /**
     * The queue into which the page will be pushed, in order to extract the
     * timelines
     */
    static final LinkedBlockingQueue<Object> PAGES_TO_PARSE = new LinkedBlockingQueue<Object>(50000);
    

    /**
     *
     * @param args
     *            0 contains the path of the input file
     *
     *            1 contains the path of the output file
     */
    public static void main(String[] args) {
        File f = new File(args[0]);
        if (!f.exists()) {
            throw new RuntimeException("File not found" + args[0]);
        }
        // Start the reading thread that pushes the wikitext into the queue
        Thread reading = new Thread(new ReadingThread(PAGES_TO_PARSE, args[0], 1));
        reading.setName("Reading");
        reading.start();
        
        Thread extractor = new Thread(new Extractor(PAGES_TO_PARSE));
        extractor.setName("Extracting");
        extractor.start();
        
        // Wait for the reading thread to finish
        try {
            reading.join();
            extractor.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static class ReadingThread implements Runnable {

        private BlockingQueue<Object> inputPage;
        String fileInput;
        private int cores;

        public ReadingThread(BlockingQueue<Object> inputPage, String fileInput, int cores) {
            super();
            this.inputPage = inputPage;
            this.fileInput = fileInput;
            this.cores = cores;
        }


        public void run() {
            WikiXMLParser wxsp;
            try {
                wxsp = WikiXMLParserFactory.getSAXParser(new File(fileInput).toURL());
                wxsp.setPageCallback(new PageCallbackHandler() {

                    public void process(WikiPage page) {
                        try {
                            //System.out.println(page.getTitle());
                            inputPage.put(page);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
                wxsp.parse();
                
                for (int i = 0; i < cores; i++) {
                    inputPage.put(POISON);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
package fr.tse.pri.test;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.jhu.nlp.wikipedia.WikiPage;
import fr.tse.pri.workers.Extractor;

public class TestTimeLineType {
	
	private BlockingQueue<String[]> result;
	
	@Before
	public void setUp() throws Exception {
		
		Extractor extractor = new Extractor(null);
		WikiPage page = new WikiPage();
		page.setWikiText(Extractor.readFile("testTimeLineType.txt"));
		page.setTitle("Testing page");
		extractor.Extract(page);
		result = extractor.getListTimeline();
		System.out.println(result.size());
		for(String[] elements : result){
			System.out.println(elements[2]);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSize() {
		assertTrue(result.size() == 3);
	}
	
	@Test
	public void testContentEasyTimeLine() {
		int nbEasyTimeLine = 0;
		for (String[] strings : result) {
			if (strings[2].equalsIgnoreCase("EasyTimeLine")){
				nbEasyTimeLine++;
			}
		}
		assertTrue(nbEasyTimeLine == 1);
	}
	@Test
	public void testContentGraphicalTimeLine() {
		int nbGraphicalTimeLine = 0;
		for (String[] strings : result) {
			if (strings[2].equalsIgnoreCase("GraphicalTimeLine")){
				nbGraphicalTimeLine++;
			}
		}
		assertTrue(nbGraphicalTimeLine == 2);
	}

}

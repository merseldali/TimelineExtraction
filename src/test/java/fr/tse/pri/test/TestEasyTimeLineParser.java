package fr.tse.pri.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.tse.pri.info.InfoDate;
import fr.tse.pri.parsers.easytimeline.EasyTimeLine;
import fr.tse.pri.workers.Extractor;

public class TestEasyTimeLineParser {

	private InputStream is; 
	private EasyTimeLine parser;
	private BlockingQueue<Object> list;
	@Before
	public void setUp() throws Exception {
		list = new LinkedBlockingQueue<Object>();
		is = new ByteArrayInputStream(Extractor.readFile("testEasytimeline.txt").getBytes(StandardCharsets.UTF_8));
		parser = new EasyTimeLine(is);
		parser.setOutQueue(list);
		parser.Start();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParser() {
		InfoDate infoExisting = new InfoDate();
		infoExisting.setLink("Maurice Garin");
		infoExisting.setType(1);
		infoExisting.setbeginDate("1903");
		infoExisting.setendDate("1904");
		boolean existing = false; 
		for (Object infoObject : list) {
			if (infoObject instanceof InfoDate){
				InfoDate infoDate = (InfoDate) infoObject;
			if (infoDate.getLink().equalsIgnoreCase(infoExisting.getLink())){
				if (infoDate.getbeginDate().equals(infoExisting.getbeginDate())){
					if (infoDate.getendDate().equals(infoExisting.getendDate())){
						if (infoDate.getType() == infoExisting.getType()){
							existing = true;
							break;
						}
					}
				}
			}
		}

	}
	assertTrue(existing);
}

}

package fr.tse.pri.workers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import fr.tse.pri.info.InfoDate;
import fr.tse.pri.parsers.Parser;
import fr.tse.pri.parsers.easytimeline.EasyTimeLine;
import fr.tse.pri.parsers.easytimeline.ParseException;
import fr.tse.pri.parsers.easytimeline.TokenMgrError;
import fr.tse.pri.parsers.graphicaltimeline.GraphicalTimeLine;
import fr.tse.pri.parsers.wikitable.WikiTableParser;

public class ParserCaller implements Runnable{

	private BlockingQueue<String[]> listTimeline;
	private BlockingQueue<Object> listInfo;
	private ArrayList<Object> parsers;
	private static final boolean WIKITABLEACTIVATED = false;
	private static final String POISON = new String("poison");
	private static int countTimelines = 0;
	private static int countEasyTimeLines = 0;
	private static int countGraphicalTimeLines = 0;
	private static int countWikiTable = 0;
	
	public ParserCaller(BlockingQueue<String[]> listTimeline, BlockingQueue<Object> listInfo){
		this.listTimeline = listTimeline;
		this.listInfo = listInfo;
		this.parsers = new ArrayList<Object>();
		this.parsers.add(new EasyTimeLine(new ByteArrayInputStream("ok".getBytes())));
		this.parsers.add(new GraphicalTimeLine(new ByteArrayInputStream("ok".getBytes())));
		this.parsers.add(new WikiTableParser(new ByteArrayInputStream("ok".getBytes()),null,""));
	}

	public void run() {
		boolean continueLoop = true;

		while(continueLoop){
			boolean parsed = true;
			String[] currentData = null;
			try {
				currentData = this.listTimeline.poll(5,TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(!currentData[0].equals(POISON)) {
				if(currentData != null){
					InputStream is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
					if (currentData[2] != null && !currentData[2].equalsIgnoreCase(""))
					{
						System.out.println(">>>>>>>>>>>>>>>>>" +currentData[2]);
						if(currentData[2].equalsIgnoreCase("EasyTimeLine")){
							System.out.println();
							System.out.println("===========Processing an Easy Timeline==========");
							System.out.println();
							EasyTimeLine parser = new EasyTimeLine(is);
							parser.setOutQueue(this.listInfo);
							parser.setPageTitle(currentData[0]);
							try {
								parser.Start();
								System.out.println();
								System.out.println("===========Finished processing an Easy Timeline==========");
								System.out.println();
								countTimelines++;
								countEasyTimeLines++;
							} catch (ParseException e) {
								//This is not an easytimeline
								System.out.println("Estimation Error");
								parsed = false;
								is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
							}
						}
						else if (currentData[2].equalsIgnoreCase("GraphicalTimeLine")){
							System.out.println();
							System.out.println("===========Processing a Graphical Timeline==========");
							System.out.println();
							GraphicalTimeLine gparser = new GraphicalTimeLine(is);
							gparser.setOutQueue(this.listInfo);
							gparser.setPageTitle(currentData[0]);
							try {
								gparser.Start();
								System.out.println();
								System.out.println("===========Finished processing a Graphical Timeline==========");
								System.out.println();
								countTimelines++;
								countGraphicalTimeLines++;
							} catch (fr.tse.pri.parsers.graphicaltimeline.ParseException e) {
								//This is not a graphicalTimeLine
								System.out.println("Estimation Error");
								parsed = false;
								is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
							}
						}
						else if (currentData[2].equalsIgnoreCase("WikiTable")&& (WIKITABLEACTIVATED)){
							System.out.println();
							System.out.println("===========Processing a WikiTable==========");
							System.out.println();
							WikiTableParser tparser = new WikiTableParser(is, this.listInfo, currentData[0]);
							try{
								tparser.Start();
								countTimelines++;
								countWikiTable++;
							}catch(Exception e){
								System.err.println("Probem in wikitable parsing.\n"+e);
								parsed = false;
								is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
							}
							//gparser.getResult();
							System.out.println();
							System.out.println("===========Finished processing a WikiTable==========");
							System.out.println();
							
						}
					} else {
						for (Object parser : this.parsers){
							if (parser instanceof EasyTimeLine){
								System.out.println();
								System.out.println("===========Processing an Easy Timeline==========");
								System.out.println();
								parser = new EasyTimeLine(is);
								((EasyTimeLine) parser).setOutQueue(this.listInfo);
								((EasyTimeLine) parser).setPageTitle(currentData[0]);
								try {
									((EasyTimeLine) parser).Start();
									System.out.println();
									System.out.println("===========Finished processing an Easy Timeline==========");
									System.out.println();
									countTimelines++;
									countEasyTimeLines++;
									break;
								} catch (ParseException e) {
									//This is not an EasyTimeLine
									parsed = false;
									is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
								}
							}
							else if (parser instanceof GraphicalTimeLine) {
								System.out.println();
								System.out.println("===========Processing a Graphical Timeline==========");
								System.out.println();
								parser = new GraphicalTimeLine(is);
								((GraphicalTimeLine) parser).setOutQueue(this.listInfo);
								((GraphicalTimeLine) parser).setPageTitle(currentData[0]);
								try {
									((GraphicalTimeLine) parser).Start();
									System.out.println();
									System.out.println("===========Finished processing a Graphical Timeline==========");
									System.out.println();
									countTimelines++;
									countGraphicalTimeLines++;
									break;
								} catch (fr.tse.pri.parsers.graphicaltimeline.ParseException e) {
									//This is not a GraphicalTimeline
									parsed = false;
									is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
								}
							}
							else if (parser instanceof WikiTableParser && (WIKITABLEACTIVATED)) {
								System.out.println();
								System.out.println("===========Processing a WikiTable==========");
								System.out.println();
								parser = new WikiTableParser(is, this.listInfo, currentData[0]);;
								try{
									((WikiTableParser)parser).Start();
									countTimelines++;
									countWikiTable++;
								}catch(Exception e){
									System.err.println("Probem in wikitable parsing.\n"+e);
									parsed = false;
									is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
								}
									System.out.println();
									System.out.println("===========Finished processing a WikiTable==========");
									System.out.println();
									
									break;
							}
						}
					}

					//Checks if the timeline has been parsed
					if (!parsed){
						for (Object parser : this.parsers){
							if (parser instanceof EasyTimeLine){
								System.out.println();
								System.out.println("===========Processing an Easy Timeline==========");
								System.out.println();
								parser = new EasyTimeLine(is);
								((EasyTimeLine) parser).setOutQueue(this.listInfo);
								((EasyTimeLine) parser).setPageTitle(currentData[0]);
								try {
									((EasyTimeLine) parser).Start();
									System.out.println();
									System.out.println("===========Finished processing an Easy Timeline==========");
									System.out.println();
									countTimelines++;
									countEasyTimeLines++;
									break;
								} catch (ParseException e) {
									//This is not an EasyTimeLine
									parsed = false;
									is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
								}
							}
							else if (parser instanceof GraphicalTimeLine) {
								System.out.println();
								System.out.println("===========Processing a Graphical Timeline==========");
								System.out.println();
								parser = new GraphicalTimeLine(is);
								((GraphicalTimeLine) parser).setOutQueue(this.listInfo);
								((GraphicalTimeLine) parser).setPageTitle(currentData[0]);
								try {
									((GraphicalTimeLine) parser).Start();
									System.out.println();
									System.out.println("===========Finished processing a Graphical Timeline==========");
									System.out.println();
									countTimelines++;
									countGraphicalTimeLines++;
									break;
								} catch (fr.tse.pri.parsers.graphicaltimeline.ParseException e) {
									//This is not a GraphicalTimeline
									parsed = false;
									is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
								}
							}
							else if (parser instanceof WikiTableParser && (WIKITABLEACTIVATED)) {
								System.out.println();
								System.out.println("===========Processing a WikiTable==========");
								System.out.println();
								parser = new WikiTableParser(is, this.listInfo, currentData[0]);;
								try{
									((WikiTableParser) parser).Start();
									countTimelines++;
									countWikiTable++;
								}catch(Exception e){
									System.err.println("Probem in wikitable parsing.\n"+e);
									parsed = false;
									is = new ByteArrayInputStream(currentData[1].getBytes(StandardCharsets.UTF_8));
								}
									System.out.println();
									System.out.println("===========Finished processing a WikiTable==========");
									System.out.println();
									
									break;
							}
						}
					}
				}
			} else {
				System.out.println("ParserCaller : Poison detected!");
				System.out.println("Nb timelines : " + countTimelines);
				System.out.println("Nb EasyTimeLines : " + countEasyTimeLines);
				System.out.println("Nb GraphicalTimeLines : " + countGraphicalTimeLines);
				System.out.println("Nb WikiTable : " + countWikiTable);
				continueLoop = false;
				try {
					this.listInfo.put(POISON);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println("Thread finished!");
		}
	}
}
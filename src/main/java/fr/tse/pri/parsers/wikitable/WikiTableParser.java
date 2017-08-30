package fr.tse.pri.parsers.wikitable;
import com.google.common.collect.*;

import fr.tse.pri.info.InfoDate;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.model.WikiModel;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by naoufalel on 16/01/2017.
 */
public class WikiTableParser {
	static BlockingQueue<Object> outqueue = new LinkedBlockingQueue<Object>();
	static String titlePage = "";
	static InputStream inputStream;

	public WikiTableParser(InputStream stream, BlockingQueue<Object> infoDates, String titlePage) {
		this.inputStream = stream;
		this.outqueue = infoDates;
		this.titlePage = titlePage;
	}

	public static enum DatePossibleNames {
		Period,
		Year,
		Start_of_service,
		End_of_service,
		Month,
		Career,
		Season
	}

	public static enum TitleNames {
		Title,
		Player,
		League,
		School
	}



	private void ListMapToInfoDates(List<LinkedHashMap<String, String>> a) {
		List<DatePossibleNames> datePossibleNames = Arrays.asList(DatePossibleNames.values());
		List<TitleNames> titleNames = Arrays.asList(TitleNames.values());

		// Work on finalTable to create InfoDates
		for (LinkedHashMap<String, String> e : a) {
			InfoDate infoDate = new InfoDate();
			for (Map.Entry en : e.entrySet()) {
				//Date :
				for (DatePossibleNames date : datePossibleNames) {
					if ((!en.getKey().toString().contains("Start")) || (!en.getKey().toString().contains("End"))) {
						infoDate.setType(2);
						if (en.getKey().toString().equalsIgnoreCase(date.toString())) {
							infoDate.setatDate(en.getValue().toString());
						}
					} else {
						infoDate.setType(1);
						if (en.getKey().toString().contains("Start"))
							infoDate.setbeginDate(en.getValue().toString());
						if (en.getKey().toString().contains("End"))
							infoDate.setendDate(en.getValue().toString());
					}
				}
				//Title :
				for (TitleNames title : titleNames) {
					if (en.getKey().toString().contains(title.toString())) {
						infoDate.setLink(en.getValue().toString());
					}

				}
				infoDate.setDateFormat("--");
				infoDate.settitlePage("--");

			}
			if (!infoDate.getLink().isEmpty() && (infoDate.getLink() != null)){
				infoDate.settitlePage(titlePage);
				outqueue.add(infoDate);
			}

		}
	}

	public void Start() {
		WikiModel wikiModel = new WikiModel("https://en.wikipedia.org/wiki/${image}", "https://en.wikipedia.org/wiki/${title}");

		try {
			String theString = IOUtils.toString(inputStream, "UTF-8");
			Document doc = Jsoup.parse(wikiModel.render(new HTMLConverter(), theString ));

			Elements table = doc.select("table.wikitable");
			List<String> headerArray;
			List<String> elementsArray;
			List<LinkedHashMap<String, String>> finalTable = new ArrayList<>();
			for (Element tableCount : table) {

				headerArray = new ArrayList<>();
				elementsArray = new ArrayList<>();
				Elements ths = tableCount.select("th");
				for (Element th : ths) {
					if ((th.hasText())) {
						headerArray.add(th.text());
					}
				}

				Elements tds = tableCount.select("td");
				for (Element td : tds) {
					if ((td.hasText()) || (!td.hasAttr("style"))) {
						elementsArray.add(td.text());
					}
				}

				List<List<String>> elements = Lists.partition(elementsArray, headerArray.size());

				for (List a : elements) {
					LinkedHashMap<String, String> map = new LinkedHashMap<>();
					Iterator<String> aIter = a.iterator();
					Iterator<String> headerIter = headerArray.iterator();

					while (aIter.hasNext() && headerIter.hasNext()) {
						map.put(headerIter.next(), aIter.next());
					}
					finalTable.add(map);
				}
			}

			ListMapToInfoDates(finalTable);
		} catch (Exception e) {
			System.err.println("There was a problem" + e);
		}

	}

}
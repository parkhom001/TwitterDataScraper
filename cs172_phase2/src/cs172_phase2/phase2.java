package cs172_phase2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class phase2 {
	static JSONParser parser = new JSONParser();

	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
       	
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = FSDirectory.open(Paths.get("./test"));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory,config);

		int fileNumber = 1;
		File file = new File("./tweets" + fileNumber + ".txt");

		while(file.exists()) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); 
			for(String line; (line = bufferedReader.readLine()) != null; ) {
				JSONObject json = (JSONObject) parser.parse(line);
				Document doc = new Document();
				doc.add(new Field("USERNAME", json.get("USERNAME").toString(), TextField.TYPE_STORED));
				doc.add(new Field("TIMESTAMP", json.get("TIMESTAMP").toString(), TextField.TYPE_STORED));
				doc.add(new Field("TWEET", json.get("TWEET").toString(), TextField.TYPE_STORED));
				
				JSONArray hashtagsJson = (JSONArray) json.get("HASHTAGS");
				Iterator<String> iterator = hashtagsJson.iterator();
				String hashtagsTemp = "";
				while (iterator.hasNext()) {
					hashtagsTemp += iterator.next() + " ";
				}
				
				JSONArray urltitlesJson = (JSONArray) json.get("URLSTITLE");
				iterator = urltitlesJson.iterator();
				String urltitlesTemp = "";
				while (iterator.hasNext()) {
					urltitlesTemp += iterator.next() + " ";
				}
				
				doc.add(new Field("HASHTAGS", hashtagsTemp, TextField.TYPE_STORED));
				doc.add(new Field("URLSTITLE", urltitlesTemp, TextField.TYPE_STORED));
				
				if (json.get("LONGITUDE") != null) {
					doc.add(new Field("LONGITUDE", json.get("LONGITUDE").toString(), TextField.TYPE_STORED));
				}
				else {
					doc.add(new Field("LONGITUDE", "null", TextField.TYPE_STORED));
				}
				
				if (json.get("LONGITUDE") != null) {
					doc.add(new Field("LATITUDE", json.get("LATITUDE").toString(), TextField.TYPE_STORED));
				}
				else {
					doc.add(new Field("LATITUDE", "null", TextField.TYPE_STORED));
				}
				
				
				
									
				indexWriter.addDocument(doc);
			}
			bufferedReader.close();
			System.out.println("file " + fileNumber);
			fileNumber += 1;
			file = new File("./tweets" + fileNumber + ".txt");
		}

		indexWriter.close();
		
		
		DirectoryReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		String[] fields = {"USERNAME", "TWEET", "HASHTAGS", "URLSTITLES"};
		Map<String, Float> boosts = new HashMap<>();
		boosts.put(fields[0], 0.2f);
		boosts.put(fields[1], 0.2f);
		boosts.put(fields[2], 0.3f);
		boosts.put(fields[3], 0.3f);
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
		Query query = parser.parse("test");
		System.out.println(query.toString());
		int topHitCount = 100;
		ScoreDoc[] hits = indexSearcher.search(query, topHitCount).scoreDocs;
		
		for (int rank = 0; rank < hits.length; ++rank) {
			Document hitDoc = indexSearcher.doc(hits[rank].doc);
			System.out.println((rank + 1) + " (score:" + hits[rank].score + ") --> " + 
								"TIMESTAMP: " + hitDoc.get("TIMESTAMP") + " USERNAME: " +
								hitDoc.get("USERNAME") + " TWEET: " + hitDoc.get("TWEET") + " HASHTAGS: " +
								hitDoc.get("HASHTAGS") + " URLSTITLE: " + hitDoc.get("URLSTITLE") + " LONGITUDE: " +
								hitDoc.get("LONGITUDE") + " LATITUDE: " + hitDoc.get("LATITUDE"));
		}
	}
}

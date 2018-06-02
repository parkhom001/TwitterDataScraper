package edu.ucr.cs.nle020.lucenesearcher;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ArticleController {
    static List<Article> articles;
 
    @GetMapping("/articles")
    public List<Article> searchArticles(
            @RequestParam(required=false, defaultValue="") String query) throws IOException, ParseException {
        if (query.isEmpty())
            return articles;

        List<Article> matches = new ArrayList<>();
        
        Analyzer analyzer = new StandardAnalyzer();
		Directory directory = FSDirectory.open(Paths.get("./test"));
        DirectoryReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		String[] fields = {"USERNAME", "TWEET", "HASHTAGS", "URLSTITLES"};
		Map<String, Float> boosts = new HashMap<>();
		boosts.put(fields[0], 0.2f);
		boosts.put(fields[1], 0.2f);
		boosts.put(fields[2], 0.3f);
		boosts.put(fields[3], 0.3f);
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
		Query queryLucene = parser.parse(query);
		System.out.println(queryLucene.toString());
		int topHitCount = 100;
		ScoreDoc[] hits = indexSearcher.search(queryLucene, topHitCount).scoreDocs;
		
		for (int rank = 0; rank < hits.length; ++rank) {
			Document hitDoc = indexSearcher.doc(hits[rank].doc);
			matches.add(new Article(hitDoc.get("USERNAME"), hitDoc.get("TIMESTAMP"), hitDoc.get("TWEET"), hitDoc.get("HASHTAGS"), hitDoc.get("URLSTITLE"), hitDoc.get("LONGITUDE"), hitDoc.get("LATITUDE")));
		}
		return matches;
    }
}

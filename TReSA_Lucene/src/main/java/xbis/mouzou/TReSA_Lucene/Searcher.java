package xbis.mouzou.TReSA_Lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;

public class Searcher {
   
   IndexSearcher indexSearcher;
   Directory indexDirectory;
   IndexReader indexReader;
   QueryParser queryParser;

   Query query;
   
   public Searcher(String indexDirectoryPath) throws IOException {
      Path indexPath = Paths.get(indexDirectoryPath);
      indexDirectory = FSDirectory.open(indexPath);
      indexReader = DirectoryReader.open(indexDirectory);
      indexSearcher = new IndexSearcher(indexReader);
      //queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
   }
   
   public TopDocs search(String searchQuery, int resultNum, boolean advSearch) throws IOException, ParseException {
      //query = queryParser.parse(searchQuery);

	  if(advSearch == true) {
		  
		  String[] queryStr = searchQuery.split("&", -1);
		  
		  String placesStr = queryStr[0];
		  String peopleStr = queryStr[1];
		  String titleStr = queryStr[2];
		  String bodyStr = queryStr[3];
		  		  
		  BooleanQuery.Builder chainQuery = new BooleanQuery.Builder();

	      Query placesQuery = new TermQuery(new Term(LuceneConstants.PLACES, placesStr));
	      Query peopleQuery = new TermQuery(new Term(LuceneConstants.PEOPLE, peopleStr));
	      Query titleQuery = new TermQuery(new Term(LuceneConstants.TITLE, titleStr));
	      Query bodyQuery = new TermQuery(new Term(LuceneConstants.BODY, bodyStr));
	      
	      if(placesStr.isBlank()) {
	    	  chainQuery.add(placesQuery, BooleanClause.Occur.SHOULD);
	      }
	      else {
	    	  chainQuery.add(placesQuery, BooleanClause.Occur.MUST);
	      }
	      
	      if(peopleStr.isBlank()) {
	    	  chainQuery.add(peopleQuery, BooleanClause.Occur.SHOULD);
	      }
	      else {
	    	  chainQuery.add(peopleQuery, BooleanClause.Occur.MUST);
	      }

	      if(titleStr.isBlank()) {
	    	  chainQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
	      }
	      else {
	    	  chainQuery.add(titleQuery, BooleanClause.Occur.MUST);
	      }
			
	      if(bodyStr.isBlank()) {
	    	  chainQuery.add(bodyQuery, BooleanClause.Occur.SHOULD); 
	      }
	      else {
	    	  chainQuery.add(bodyQuery, BooleanClause.Occur.MUST);  
	      }
	      
	      BooleanQuery booleanQuery = chainQuery.build();

	      //
	      System.out.println("query: " + booleanQuery.toString());
	      return indexSearcher.search(booleanQuery, resultNum);
	  }
	  else {
		  //check if query contains more than one word
		  //deal accordingly
		  
//		  String[] queryStr = searchQuery.split("[ ,.;:]+");
//		  
//		  if(queryStr.length > 1) {
//			  //split query
//		  }
//		  else {
//			  
//		  }
//		  
	      queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
		  query = queryParser.parse(searchQuery);
		  System.out.println("query: " + query.toString());
		  return indexSearcher.search(query, resultNum);
	  }
	  
      
   }
   
   public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
      return indexSearcher.doc(scoreDoc.doc);
   }

   public void close() throws IOException {
      indexReader.close();
      indexDirectory.close();
   }

}

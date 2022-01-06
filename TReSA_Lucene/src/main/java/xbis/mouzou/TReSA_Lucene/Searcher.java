package xbis.mouzou.TReSA_Lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;

public class Searcher {
   
   IndexSearcher indexSearcher;
   Directory indexDirectory;
   IndexReader indexReader;
   QueryParser queryParser;

   BooleanQuery booleanQuery;
   Query query;
   
   public Searcher(String indexDirectoryPath) throws IOException {
      Path indexPath = Paths.get(indexDirectoryPath);
      indexDirectory = FSDirectory.open(indexPath);
      indexReader = DirectoryReader.open(indexDirectory);
      indexSearcher = new IndexSearcher(indexReader);
      //queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
   }
   
   public TopDocs search(String searchQuery, int resultNum, boolean advSearch, List<Boolean> queryNotExistList) throws IOException, ParseException {
	   //when more than one word search in each field
	   //no results

	   
	  //we cannot only search for documents that dont have a word
	  //we must specify and a word that exists
	  //otherwise score cant be calculated and there will be no results
	   
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
	      
	      //places boolean
	      if(placesStr.isBlank()) {
	    	  chainQuery.add(placesQuery, BooleanClause.Occur.SHOULD);
	      }
	      else {
	    	  if(queryNotExistList.get(0) == true) {
	    		  chainQuery.add(placesQuery, BooleanClause.Occur.MUST_NOT);
	    	  }
	    	  else {
	    		  chainQuery.add(placesQuery, BooleanClause.Occur.MUST);
	    	  }
	      }
	      
	      //people boolean
	      if(peopleStr.isBlank()) {
	    	  chainQuery.add(peopleQuery, BooleanClause.Occur.SHOULD);
	      }
	      else {
	    	  if(queryNotExistList.get(1) == true) {
	    		  chainQuery.add(peopleQuery, BooleanClause.Occur.MUST_NOT);	    	 
	    	  }
	    	  else {
	    		  chainQuery.add(peopleQuery, BooleanClause.Occur.MUST);	    	
	    	  }    
	  	  }

	      //title boolean
	      if(titleStr.isBlank()) {
	    	  chainQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
	      }
	      else {
	    	  if(queryNotExistList.get(2) == true) {
	    		  chainQuery.add(titleQuery, BooleanClause.Occur.MUST_NOT);	    	  
	    	  }
	    	  else {
	    		  chainQuery.add(titleQuery, BooleanClause.Occur.MUST);	    	 
	    	  }
	      }
			
	      //body boolean
	      if(bodyStr.isBlank()) {
	    	  chainQuery.add(bodyQuery, BooleanClause.Occur.SHOULD); 
	      }
	      else {
	    	  if(queryNotExistList.get(3) == true) {
	    		  chainQuery.add(bodyQuery, BooleanClause.Occur.MUST_NOT);	    	  
	    	  }
	    	  else {
	    		  chainQuery.add(bodyQuery, BooleanClause.Occur.MUST);	    	 
	    	  }  
	      }
	      
	      booleanQuery = chainQuery.build();

	      //
	      System.out.println("query: " + booleanQuery.toString());
	      return indexSearcher.search(booleanQuery, resultNum);
	  }
	  else {

	      queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
		  query = queryParser.parse(searchQuery);
		  System.out.println("query: " + query.toString());
		  return indexSearcher.search(query, resultNum);
	  }
	  
      
   }
   
   public String[] getHighlightedFrag(boolean advSearch, ScoreDoc scoreDoc) throws IOException, ParseException, InvalidTokenOffsetsException {
	   
	   Formatter formatter = new SimpleHTMLFormatter();
		   
	   QueryScorer scorer;
		   
	   if(advSearch == true) {
		   scorer = new QueryScorer(booleanQuery);
	   }
	   else {
		   scorer = new QueryScorer(query, LuceneConstants.CONTENTS);
	   }

	   Highlighter highlighter = new Highlighter(formatter, scorer);
	   
	   Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 50);
		   
	   highlighter.setTextFragmenter(fragmenter);
		   	   
	   Document document = getDocument(scoreDoc);
	   String text = document.get(LuceneConstants.CONTENTS);
		
	   @SuppressWarnings("deprecation")
	   TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader, scoreDoc.doc, LuceneConstants.CONTENTS, new StandardAnalyzer());
	   String[] fragment = highlighter.getBestFragments(tokenStream, text, 10);
	   return fragment;   
   }
   
   public TopDocs moreLikeThis(int docId, int resultNum) throws IOException, ParseException {
	   MoreLikeThis mlt = new MoreLikeThis(indexReader);
	   mlt.setAnalyzer(new StandardAnalyzer());
	   mlt.setFieldNames(new String[] {LuceneConstants.PLACES, LuceneConstants.PEOPLE, LuceneConstants.TITLE, LuceneConstants.BODY, LuceneConstants.CONTENTS});
	   
	   Query query = mlt.like(docId);
	   
	   return indexSearcher.search(query, resultNum);

   }
   
   public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
      return indexSearcher.doc(scoreDoc.doc);
   }

   public void close() throws IOException {
      indexReader.close();
      indexDirectory.close();
   }

}

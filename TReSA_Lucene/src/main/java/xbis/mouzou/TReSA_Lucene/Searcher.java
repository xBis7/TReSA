package xbis.mouzou.TReSA_Lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
   
   public TopDocs search(String searchQuery) throws IOException, ParseException {
      //query = queryParser.parse(searchQuery);

      //
      BooleanQuery.Builder chainQuery = new BooleanQuery.Builder();

      Query placesQuery = new TermQuery(new Term(LuceneConstants.PLACES, searchQuery));
      Query peopleQuery = new TermQuery(new Term(LuceneConstants.PEOPLE, searchQuery));
      Query titleQuery = new TermQuery(new Term(LuceneConstants.TITLE, searchQuery));
      Query bodyQuery = new TermQuery(new Term(LuceneConstants.BODY, searchQuery));

      chainQuery.add(placesQuery, BooleanClause.Occur.SHOULD);
      chainQuery.add(peopleQuery, BooleanClause.Occur.SHOULD);
      chainQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
      chainQuery.add(bodyQuery, BooleanClause.Occur.SHOULD);
      
      BooleanQuery booleanQuery = chainQuery.build();

      //
      System.out.println("query: " + booleanQuery.toString());
      return indexSearcher.search(booleanQuery, LuceneConstants.MAX_SEARCH);
   }
   
   public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
      return indexSearcher.doc(scoreDoc.doc);
   }

   public void close() throws IOException {
      indexReader.close();
      indexDirectory.close();
   }

}

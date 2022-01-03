package xbis.mouzou.TReSA_Lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {
	
	File currentDir;
	File parentDir;
	
	File index;
	
	String indexDir;
	
	File data; 
	
	String dataDir;
	
	Indexer indexer;
	Searcher searcher;
   
	public LuceneTester() {
		currentDir = new File(".");
		parentDir = currentDir.getParentFile();
 
		index = new File(parentDir, "../Index/");

		//path to Index dir 
		indexDir = index.getAbsolutePath();
	
		data = new File(parentDir, "../TestData/");
	   
		//path to Data dir
		dataDir = data.getAbsolutePath();
	}

	public void tester(String query, int resultNum, boolean advSearch) {
      LuceneTester tester;
      try {
         tester = new LuceneTester();
         
         //delete existing index files
         tester.cleanIndexFiles();
         tester.createIndex();
         tester.search(query, resultNum, advSearch);
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   private void createIndex() throws IOException {
      indexer = new Indexer(indexDir);
      long startTime = System.currentTimeMillis();
      long endTime = System.currentTimeMillis();
      int numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      indexer.close();
      System.out.println(numIndexed + " File(s) indexed, time taken: " + (endTime - startTime)+" ms");
   }
   
   private void search(String searchQuery, int resultNum, boolean advSearch) throws IOException, ParseException {
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      long endTime = System.currentTimeMillis();

      TopDocs hits = searcher.search(searchQuery, resultNum, advSearch);
      
      System.out.println(hits.totalHits +" documents found. Time :" + (endTime - startTime));

      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
         System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH) + "\tScore: " + scoreDoc.score);
      }

      searcher.close();
   }
   
   private void cleanIndexFiles() throws IOException{
	   FileUtils.cleanDirectory(new File(indexDir));
   }

}
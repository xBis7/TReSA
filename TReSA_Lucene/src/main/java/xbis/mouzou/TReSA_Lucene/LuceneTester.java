package xbis.mouzou.TReSA_Lucene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

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
	
		data = new File(parentDir, "../Reuters_articles/");
	   
		//path to Data dir
		dataDir = data.getAbsolutePath();
	}

	public List<Result> results(String query, int resultNum, boolean advSearch, List<Boolean> queryNotExistList) {
      LuceneTester tester;
      List<Result> resultsList = new ArrayList<>();
      
      try { 
         tester = new LuceneTester();
         resultsList = tester.search(query, resultNum, advSearch, queryNotExistList);
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      } catch (InvalidTokenOffsetsException e) {
    	  e.printStackTrace();
      }
      
      return resultsList;
      
   }

   public void createIndex() throws IOException {
	  LuceneTester tester = new LuceneTester();
       
      if(index.exists()) {
    	  //delete existing index files
    	  tester.cleanIndexFiles();
      }
      
      indexer = new Indexer(indexDir);
      long startTime = System.currentTimeMillis();
      long endTime = System.currentTimeMillis();
      int numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      indexer.close();
      
      //print in terminal for debugging
      System.out.println(numIndexed + " File(s) indexed, time taken: " + (endTime - startTime)+" ms");
   }
   
   private List<Result> search(String searchQuery, int resultNum, boolean advSearch, List<Boolean> queryNotExistList) throws IOException, ParseException, InvalidTokenOffsetsException {
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      long endTime = System.currentTimeMillis();

      TopDocs hits = searcher.search(searchQuery, resultNum, advSearch, queryNotExistList);
      
      //print in terminal for debugging
      System.out.println(hits.totalHits +" documents found. Time :" + (endTime - startTime));
      
      List<Result> resultsList = new ArrayList<>();
      
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
    	 String fragment = "";
         Document doc = searcher.getDocument(scoreDoc);
         
         //print in terminal for debugging
         System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH) + "\tScore: " + scoreDoc.score);
      
         String[] frag = searcher.getHighlightedFrag(advSearch, scoreDoc);
         for(int j=0; j<frag.length ; j++) {
        	 
        	 //print in terminal for debugging
        	 System.out.println(frag[j]);
        	 fragment += frag[j];
         }
         
         Result result = new Result(scoreDoc.doc, doc.get(LuceneConstants.FILE_PATH), scoreDoc.score, fragment);
         resultsList.add(result);
      }

      searcher.close();
      
      return resultsList;
   }
   
   public List<Result> moreLikeThis(int docId, int resultNum) throws IOException, ParseException, InvalidTokenOffsetsException {
	   searcher = new Searcher(indexDir);

	   TopDocs hits = searcher.moreLikeThis(docId, resultNum);
	      
	   List<Result> resultsList = new ArrayList<>();
	      
	   for(ScoreDoc scoreDoc : hits.scoreDocs) {
		   String fragment = "";
	       Document doc = searcher.getDocument(scoreDoc);
	         
	       //print in terminal for debugging
	       System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH) + "\tScore: " + scoreDoc.score);
	       String[] frag = searcher.getHighlightedFrag(false, scoreDoc);
	       for(int j=0; j<frag.length ; j++) {
	        	 
	    	   //print in terminal for debugging
	    	   System.out.println(frag[j]);
	    	   fragment += frag[j];
	       }
	         
	       Result result = new Result(scoreDoc.doc, doc.get(LuceneConstants.FILE_PATH), scoreDoc.score, fragment);
	       resultsList.add(result);
	   }

	   searcher.close();
	      
	   return resultsList;
   }
   
   public void addFile(String places, String people, String title, String body, String fileName) {
	   
	   File file = new File(dataDir + "\\" + fileName + ".txt");
	   
	   	try {
	   		file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	   
	   Path filePath = Paths.get(dataDir, fileName + ".txt");
	   
	   try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
		   writer.write("<PLACES>" + places + "</PLACES>");
		   writer.newLine();
		   writer.write("<PEOPLE>" + people + "</PEOPLE>");
		   writer.newLine();
		   writer.write("<TITLE>" + title + "</TITLE>");
		   writer.newLine();
		   writer.write("<BODY>" + body + "\n</BODY>");
		   
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
   
	   try {
		   createIndex();
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   
   }
   
   public void cleanIndexFiles() throws IOException{
	   FileUtils.cleanDirectory(new File(indexDir));
   }

}
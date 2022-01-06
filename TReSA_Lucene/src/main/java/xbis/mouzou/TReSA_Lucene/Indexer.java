package xbis.mouzou.TReSA_Lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

   private IndexWriter writer;
   
   public Indexer(String indexDirectoryPath) throws IOException {
      //this directory will contain the indexes
      Path indexPath = Paths.get(indexDirectoryPath);
      if(!Files.exists(indexPath)) {
         Files.createDirectory(indexPath);
      }
      //Path indexPath = Files.createTempDirectory(indexDirectoryPath);
      Directory indexDirectory = FSDirectory.open(indexPath);
      //create the indexer
      IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
      writer = new IndexWriter(indexDirectory, config);
   }

   public void close() throws CorruptIndexException, IOException {
      writer.close();
   }
   
   private Document getDocument(File file) throws IOException {

      Document document = new Document();
      
      //term vectors are not used
	  FieldType fieldType = new FieldType();        
	  fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
	  fieldType.setStored(true);
	  fieldType.setStoreTermVectors(true);
	  fieldType.setStoreTermVectorPositions(true);
	  fieldType.setStoreTermVectorOffsets(true);
	  fieldType.setTokenized(true);
	  fieldType.stored();
      
      String allLines;

      allLines = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

      //index places
      int placesStart = allLines.indexOf("<PLACES>") + "<PLACES>".length();
      int placesEnd = allLines.indexOf("</PLACES>", placesStart);
      String places = allLines.substring(placesStart, placesEnd);

      //places are not splited with whitespaces or any other delimiter
      //we use StringField to store them as an entire string 
      //and delimit them later
      Field placesField = new Field(LuceneConstants.PLACES, places, fieldType);
      
	
      
      //index people
      int peopleStart = allLines.indexOf("<PEOPLE>") + "<PEOPLE>".length();
      int peopleEnd = allLines.indexOf("</PEOPLE>", peopleStart);
      String people = allLines.substring(peopleStart, peopleEnd);

      Field peopleField = new Field(LuceneConstants.PEOPLE, people, fieldType);

      //index title
      int titleStart = allLines.indexOf("<TITLE>") + "<TITLE>".length();
      int titleEnd = allLines.indexOf("</TITLE>", titleStart);
      String title = allLines.substring(titleStart, titleEnd);

      Field titleField = new Field(LuceneConstants.TITLE, title, fieldType);

      //index body
      int bodyStart = allLines.indexOf("<BODY>") + "<BODY>".length();
      int bodyEnd = allLines.indexOf("</BODY>", bodyStart);
      String body = allLines.substring(bodyStart, bodyEnd);

      Field bodyField = new Field(LuceneConstants.BODY, body, fieldType);

      //System.out.println("places:----- " + placesField + "\npeople:----- " + peopleField + "\ntitle:----- " + titleField + "\nbody:----- " + bodyField);
      
      //index all file contents at once for simple search
      String contents = places + "\n" + people + "\n" + title + "\n" + body;
      Field contentsField = new Field(LuceneConstants.CONTENTS, contents, fieldType);
      
      //index file name
      Field fileNameField = new Field(LuceneConstants.FILE_NAME, file.getName(), fieldType);

      //index file path
      Field filePathField = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(), fieldType);

      document.add(placesField);
      document.add(peopleField);
      document.add(titleField);
      document.add(bodyField);
      document.add(contentsField);
      document.add(fileNameField);
      document.add(filePathField);

      return document;
   } 

   private void indexFile(File file) throws IOException {

      //System.out.println("Indexing " + file.getCanonicalPath());
      Document document = getDocument(file);
      writer.addDocument(document);
   
   }

   public int createIndex(String dataDirPath, FileFilter filter) throws IOException {
      
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();
      for (File file : files) {
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()
            && filter.accept(file)
         ){
            indexFile(file);
         }
      }

      //return number of documents currently buffered in RAM
      return writer.numRamDocs();
   }
}
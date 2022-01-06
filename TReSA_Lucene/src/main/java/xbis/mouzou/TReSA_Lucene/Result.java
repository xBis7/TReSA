package xbis.mouzou.TReSA_Lucene;

public class Result {
	
	private int docId;
	private String path;
	private float score;
	private String fragments;
	
	public Result(int docId, String path, float score, String fragments) {
		this.docId = docId;
		this.path = path;
		this.score = score;
		this.fragments = fragments;
	}
	
	public int getDocId() {
		return docId;
	}
	
	public String getPath() {
		return path;
	}
	
	public float getScore() {
		return score;
	}
	
	public String getFragments() {
		return fragments;
	}

}

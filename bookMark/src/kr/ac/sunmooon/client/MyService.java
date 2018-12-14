package kr.ac.sunmooon.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import kr.ac.sunmooon.shared.bookMarkData;
import kr.ac.sunmooon.shared.oldBookmarkData;
import kr.ac.sunmooon.shared.recommendBookmark;


@RemoteServiceRelativePath("myservice")
public interface MyService extends RemoteService{
	public Vector<bookMarkData> getSql(int time);
	public void setSql(String time, String id);
	public Vector<recommendBookmark> popRecommendSql(String name);
	public Vector<oldBookmarkData> getOldBookMark(String time);
}

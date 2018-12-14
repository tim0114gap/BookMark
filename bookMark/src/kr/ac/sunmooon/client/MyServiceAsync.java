package kr.ac.sunmooon.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import kr.ac.sunmooon.shared.bookMarkData;
import kr.ac.sunmooon.shared.oldBookmarkData;
import kr.ac.sunmooon.shared.recommendBookmark;

public interface MyServiceAsync {

	void getSql(int time, AsyncCallback<Vector<bookMarkData>> callback);

	void setSql(String time, String id, AsyncCallback<Void> callback);

	void popRecommendSql(String name, AsyncCallback<Vector<recommendBookmark>> callback);

	void getOldBookMark(String time, AsyncCallback<Vector<oldBookmarkData>> callback);

}

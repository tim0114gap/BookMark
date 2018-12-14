package kr.ac.sunmooon.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class recommendBookmark implements IsSerializable{
	String name;
	String url;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}

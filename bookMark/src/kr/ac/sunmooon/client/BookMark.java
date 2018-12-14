package kr.ac.sunmooon.client;


import java.util.Date;
import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java_cup.internal_error;
import kr.ac.sunmooon.shared.bookMarkData;
import kr.ac.sunmooon.shared.oldBookmarkData;
import kr.ac.sunmooon.shared.recommendBookmark;



public class BookMark implements EntryPoint {
	
	private static Vector<bookMarkData> list = null;
	private static Vector<recommendBookmark> recomendationBookmarks = null;
	private static Vector<oldBookmarkData> oldBookmarkDatalist = new Vector<oldBookmarkData>();
	private static boolean flag = true;
	private static boolean isClicked = true;
	private static boolean isOldClicked = true;
	FocusPanel forOldFoc1;

	public void onModuleLoad() {

		final VerticalPanel vpMain = new VerticalPanel();
		vpMain.setWidth("1024px");
		vpMain.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		vpMain.getElement().getStyle().setMarginRight(50, Unit.PX);
		RootPanel.get("main").add(vpMain);
		
		final VerticalPanel topVer = new VerticalPanel();				//top vertical panel
		topVer.setStyleName("topVer");

		final HorizontalPanel topHor = new HorizontalPanel();			//top horizontal panel
		topHor.getElement().getStyle().setMarginLeft(10, Unit.PX);
		topHor.getElement().getStyle().setMarginRight(10, Unit.PX);
		topHor.setStyleName("header");
		
		
		Date date = new Date();											//prepare for Today's year - 1years 
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
		String s = dtf.format(date, TimeZone.createTimeZone(0));
		int year1 = Integer.parseInt(s) - 1;
		
		//get data from sql
		list = new Vector<bookMarkData>();
		MyServiceAsync service = GWT.create(MyService.class);
		service.getSql(year1, new AsyncCallback<Vector<bookMarkData>>() {
			
			@Override
			public void onSuccess(Vector<bookMarkData> result) {
				// TODO Auto-generated method stub
				for (int i = 0; i < result.size(); i++) {
					list.add(result.get(i));
				}
				
 				for (int i = 0; i < 10; i++) {
 					final VerticalPanel btver = new VerticalPanel();			//add recommendation button under the base button
 					final FocusPanel wapper = new FocusPanel();					//(for image and <a> tag makes horizontal) == base button
 					final HorizontalPanel bthor = new HorizontalPanel();
 					wapper.addStyleName("bt");
 					wapper.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
 					wapper.getElement().getStyle().setBorderColor("#FFFFFF");
 					wapper.getElement().getStyle().setPadding(5, Unit.PX);
 					wapper.getElement().getStyle().setMarginRight(10, Unit.PX);
					
					final Image img = new Image();
					img.setUrl("image/file_icon.png");
					img.getElement().getStyle().setWidth(15, Unit.PX);
					img.getElement().getStyle().setHeight(15, Unit.PX);
					img.getElement().getStyle().setMarginRight(10, Unit.PX);
					bthor.add(img);

					final HTML html = new HTML("<a href=\""+ list.get(i).getUrl() +"\">"+list.get(i).getName() +"</a>");
					html.addStyleName("name");
					html.setStyleName(list.get(i).getName());
					html.getElement().getStyle().setMarginRight(10, Unit.PX);
					
					
					//---------------------------------------------click handler--------------------------------------------
					//when html was clicked access to sql database and add plus 1 to userbookmark/times column 
					html.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							bookMarkData data = null;
							for (int j = 0; j < list.size(); j++) {
								if((list.get(j).getName()) == html.getStyleName())
								{
									data = list.get(j);
								}
							}
							MyServiceAsync service = GWT.create(MyService.class);
							service.setSql(Integer.toString(data.getTimes()+1), Integer.toString(data.getId()), new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(Void result) {
									// TODO Auto-generated method stub

								}
							});
						}
					});
					
					
					//---------------------------------------------mouse click handler------------------------------------------
					//when a mouse click the book marks , get new recommandation's book marks as same catalog from mysql
					//and set inner handler for recommendation book marks
					wapper.addClickHandler(new ClickHandler() {
						

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							final FocusPanel EaddVer;
							final VerticalPanel addVer;
							EaddVer = new FocusPanel();
							addVer = new VerticalPanel();    //add inner vertical panel for recommendation buttons
							EaddVer.addStyleName("bt");
							EaddVer.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
							EaddVer.getElement().getStyle().setBorderColor("#FFFFFF");
							EaddVer.getElement().getStyle().setPadding(5, Unit.PX);
							EaddVer.getElement().getStyle().setMarginRight(10, Unit.PX);
							//EaddVer.setVisible(false);
							EaddVer.addBlurHandler(new BlurHandler() {
								
								@Override
								public void onBlur(BlurEvent event) {
									// TODO Auto-generated method stub
									EaddVer.setVisible(false);
								}
							});
							
							String getName = html.getStyleName();
							recomendationBookmarks = new Vector<recommendBookmark>();
							MyServiceAsync service = GWT.create(MyService.class);
							service.popRecommendSql(getName, new AsyncCallback<Vector<recommendBookmark>>() {
		
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}
		
								
								
								@Override
								public void onSuccess(Vector<recommendBookmark> result) {
									// TODO Auto-generated method stub
									for (int j = 0; j < result.size(); j++) {
										recomendationBookmarks.add(result.get(j));
									}
									
									if(isClicked)
									{
										for (int j = 0; j < recomendationBookmarks.size(); j++) {
											final FocusPanel inwapper = new FocusPanel();					//(for image and <a> tag makes horizontal) == base button 
											final HorizontalPanel addInHor = new HorizontalPanel();			//add inner horizontal panel for recommendation image and <a> tags to sort horizon
											inwapper.getElement().getStyle().setMarginBottom(10, Unit.PX);
											inwapper.addStyleName("bt");
											inwapper.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
											inwapper.getElement().getStyle().setBorderColor("#FFFFFF");
											inwapper.getElement().getStyle().setPadding(5, Unit.PX);
											inwapper.getElement().getStyle().setMarginRight(10, Unit.PX);
											
											final Image img = new Image();
											img.setUrl("image/file_icon.png");
											img.getElement().getStyle().setWidth(15, Unit.PX);
											img.getElement().getStyle().setHeight(15, Unit.PX);
											img.getElement().getStyle().setMarginRight(10, Unit.PX);
											addInHor.add(img);
											
											final HTML html = new HTML("<a href=\""+ recomendationBookmarks.get(j).getUrl() +"\">"+recomendationBookmarks.get(j).getName() +"</a>");
											html.setStyleName(recomendationBookmarks.get(j).getName());
											html.getElement().getStyle().setMarginRight(10, Unit.PX);
											addInHor.add(html);
											inwapper.addMouseOverHandler(new MouseOverHandler() {
												
												@Override
												public void onMouseOver(MouseOverEvent event) {
													// TODO Auto-generated method stub
													inwapper.getElement().getStyle().setBackgroundColor("#E1E1E1");
												}
											});
											inwapper.addMouseOutHandler(new MouseOutHandler() {
												
												@Override
												public void onMouseOut(MouseOutEvent event) {
													// TODO Auto-generated method stub
													inwapper.getElement().getStyle().setBackgroundColor("#FFFFFF");
												}
											});
											inwapper.add(addInHor);
											addVer.add(inwapper);
											EaddVer.add(addVer);
											btver.add(EaddVer);
											isClicked = false;
										}
										isClicked = false;
									}
									else
									{
										btver.remove(EaddVer);
										isClicked = true;
									}
						
								}
							});
							
						}
					});
					
					
					//---------------------------------------------mouse over handler------------------------------------------
					//when a mouse over the book marks , background color will be changed to gray
					wapper.addMouseOverHandler(new MouseOverHandler() {
						
						@Override
						public void onMouseOver(MouseOverEvent event) {
							// TODO Auto-generated method stub
							wapper.getElement().getStyle().setBackgroundColor("#E1E1E1");
						}
					});
					
					
					//---------------------------------------------mouse out handler------------------------------------------
					//when a mouse out the book marks , background color will be changed to white
					wapper.addMouseOutHandler(new MouseOutHandler() {
						
						@Override
						public void onMouseOut(MouseOutEvent event) {
							// TODO Auto-generated method stub
							wapper.getElement().getStyle().setBackgroundColor("#FFFFFF");
						}
					});
					
					
					
					//---------------------------------------------blur handler------------------------------------------
					//when user clicked none focus place , recommendation book marks will be disappeared 
					wapper.addBlurHandler(new BlurHandler() {
						
						@Override
						public void onBlur(BlurEvent event) {
							// TODO Auto-generated method stub
							Timer timer = new Timer() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									btver.remove(1);
									isClicked = true;
								}
							};
							timer.schedule(200);
						}
					});
					
					
					bthor.add(html);
					wapper.add(bthor);
					btver.add(wapper);
					topHor.add(btver);

 				}
 				
 				
				//---------------------------------------------The rest book marks------------------------------------------
 				final FocusPanel focExtraVer = new FocusPanel();				//view for more than 10 bookmarks panel
 				final VerticalPanel extraVer = new VerticalPanel();				//view for more than 10 bookmarks panel
 				
 				final FocusPanel extraWapper = new FocusPanel();
 				extraWapper.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
				extraWapper.addStyleName("bt");
				extraWapper.getElement().getStyle().setBorderColor("#FFFFFF");
				extraWapper.getElement().getStyle().setPadding(5, Unit.PX);
				extraWapper.getElement().getStyle().setPaddingLeft(15, Unit.PX);
				extraWapper.getElement().getStyle().setPaddingRight(15, Unit.PX);
				extraWapper.getElement().getStyle().setMarginRight(10, Unit.PX);
				extraWapper.getElement().getStyle().setMarginBottom(10, Unit.PX);
 				final VerticalPanel inExtraVer1 = new VerticalPanel();
 				final Label extraSqlLab = new Label();
				extraSqlLab.setText(">>");
				inExtraVer1.add(extraSqlLab);
				extraWapper.add(inExtraVer1);
				extraVer.add(extraWapper);
				
				
				final VerticalPanel inExtraVer2 = new VerticalPanel();
				
				
				//---------------------------------------------mouse over handler------------------------------------------
				//when a mouse on the book marks , background color will be changed to gray
				extraWapper.addMouseOverHandler(new MouseOverHandler() {
					
					@Override
					public void onMouseOver(MouseOverEvent event) {
						// TODO Auto-generated method stub
						extraWapper.getElement().getStyle().setBackgroundColor("#E1E1E1");
					}
				});
				
				
				//---------------------------------------------mouse out handler------------------------------------------
				//when a mouse on the book marks , background color will be changed to white
				extraWapper.addMouseOutHandler(new MouseOutHandler() {
					
					@Override
					public void onMouseOut(MouseOutEvent event) {
						// TODO Auto-generated method stub
						extraWapper.getElement().getStyle().setBackgroundColor("#FFFFFF");
					}
				});
				
				
				//---------------------------------------------click handler------------------------------------------
				//when user pressed label , show the rest book marks
				extraWapper.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						bookMarkData addData = null;
						HTML html = null;
						if(flag)
						{
							if(inExtraVer2.getWidgetCount() == 0)
							{
								
								HorizontalPanel additionalSqlHorizon = null;
								for (int i = 10; i < list.size(); i++) {
									final FocusPanel focAdditionalSqlHor = new FocusPanel();
									additionalSqlHorizon = new HorizontalPanel();
									focAdditionalSqlHor.getElement().getStyle().setMarginBottom(10, Unit.PX);
									focAdditionalSqlHor.addStyleName("bt");
									focAdditionalSqlHor.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
									focAdditionalSqlHor.getElement().getStyle().setBorderColor("#FFFFFF");
									focAdditionalSqlHor.getElement().getStyle().setPadding(5, Unit.PX);
									focAdditionalSqlHor.getElement().getStyle().setPaddingLeft(15, Unit.PX);
									focAdditionalSqlHor.getElement().getStyle().setPaddingRight(15, Unit.PX);
									focAdditionalSqlHor.getElement().getStyle().setMarginTop(10, Unit.PX);
									focAdditionalSqlHor.addMouseOverHandler(new MouseOverHandler() {
										
										@Override
										public void onMouseOver(MouseOverEvent event) {
											// TODO Auto-generated method stub
											focAdditionalSqlHor.getElement().getStyle().setBackgroundColor("#E1E1E1");
										}
									});
									focAdditionalSqlHor.addMouseOutHandler(new MouseOutHandler() {
										
										@Override
										public void onMouseOut(MouseOutEvent event) {
											// TODO Auto-generated method stub
											focAdditionalSqlHor.getElement().getStyle().setBackgroundColor("#FFFFFF");
										}
									});
									
									html = new HTML("<a href=\""+ list.get(i).getUrl() +"\">"+list.get(i).getName() +"</a>");
									html.setStyleName(list.get(i).getName());
									additionalSqlHorizon.add(html);
									focAdditionalSqlHor.add(additionalSqlHorizon);
									inExtraVer2.add(focAdditionalSqlHor);
								}
								
								for (int i = 10; i < list.size(); i++) {
									if((list.get(i).getName()) == html.getStyleName())
									{
										addData = list.get(i);
									}
								}
							}
							
							inExtraVer2.setVisible(true);
								
						
							flag = false;
						}
						else
						{
							inExtraVer2.setVisible(false);
							flag = true;
						}
						
						MyServiceAsync service = GWT.create(MyService.class);
						service.setSql(Integer.toString(addData.getTimes()+1), Integer.toString(addData.getId()), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});
				
				
				
				
				//---------------------------------------------blur handler------------------------------------------
				//when user clicked none focus place , the rest book marks will be disappeared 
				extraWapper.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						// TODO Auto-generated method stub
						Timer timer = new Timer() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								inExtraVer2.setVisible(false);
								flag = true;
							}
						};
						timer.schedule(200);
					}
				});
				

				extraVer.add(inExtraVer2);
				focExtraVer.add(extraVer);
				topHor.add(extraVer);
				
				final VerticalPanel forOldBookmarkVer = new VerticalPanel();
				final FocusPanel clear = new FocusPanel();
				clear.getElement().getStyle().setMarginBottom(10, Unit.PX);
				clear.addStyleName("btclear");
				clear.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
				clear.getElement().getStyle().setBorderColor("#FFFFFF");
				clear.getElement().getStyle().setBackgroundColor("#89f7fe");
				clear.getElement().getStyle().setPadding(10, Unit.PX);
				clear.getElement().getStyle().setMarginTop(5, Unit.PX);
				//clear.getElement().getStyle().setPosition(Position.ABSOLUTE);
				forOldBookmarkVer.add(clear);
				clear.addMouseOverHandler(new MouseOverHandler() {
					
					@Override
					public void onMouseOver(MouseOverEvent event) {
						// TODO Auto-generated method stub
						clear.getElement().getStyle().setBackgroundColor("#66a6ff");
					}
				});
				clear.addMouseOutHandler(new MouseOutHandler() {
					
					@Override
					public void onMouseOut(MouseOutEvent event) {
						// TODO Auto-generated method stub
						clear.getElement().getStyle().setBackgroundColor("#89f7fe");
					}
				});
				clear.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						// TODO Auto-generated method stub
						Timer timer = new Timer() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								forOldFoc1.clear();
								isOldClicked = true;
							}
						};
						timer.schedule(200);
					}
				});
				clear.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						if(isOldClicked)
						{
							Date date = new Date();											//prepare for Today's year - 1years 
							DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
							String s = dtf.format(date, TimeZone.createTimeZone(0));
							int year1 = Integer.parseInt(s) - 1;
							MyServiceAsync service = GWT.create(MyService.class);
							service.getOldBookMark(Integer.toString(year1), new AsyncCallback<Vector<oldBookmarkData>>() {
								
								@Override
								public void onSuccess(Vector<oldBookmarkData> result) {
									// TODO Auto-generated method stub
									forOldFoc1 = new FocusPanel();
									Vector<oldBookmarkData> old = new Vector<oldBookmarkData>();
									for (int i = 0; i < result.size(); i++) {
										old.add(result.get(i));
									}
									final VerticalPanel forInOldVer = new VerticalPanel();
									for (int i = 0; i < old.size(); i++) {
										final FocusPanel forOldFoc2 = new FocusPanel();
										forOldFoc2.addStyleName("bt");
										forOldFoc2.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
										forOldFoc2.getElement().getStyle().setBorderColor("#FFFFFF");
										forOldFoc2.getElement().getStyle().setBackgroundColor("#FFFFFF");
										forOldFoc2.getElement().getStyle().setMarginRight(50, Unit.PX);
										forOldFoc2.getElement().getStyle().setPadding(5, Unit.PX);
										forOldFoc2.getElement().getStyle().setPaddingLeft(15, Unit.PX);
										forOldFoc2.getElement().getStyle().setPaddingRight(15, Unit.PX);
										//forOldFoc2.getElement().getStyle().setMarginTop(45, Unit.PX);
										forOldFoc2.addMouseOverHandler(new MouseOverHandler() {
											
											@Override
											public void onMouseOver(MouseOverEvent event) {
												// TODO Auto-generated method stub
												forOldFoc2.getElement().getStyle().setBackgroundColor("#E1E1E1E1");
											}
										});
										forOldFoc2.addMouseOutHandler(new MouseOutHandler() {
											
											@Override
											public void onMouseOut(MouseOutEvent event) {
												// TODO Auto-generated method stub
												forOldFoc2.getElement().getStyle().setBackgroundColor("#FFFFFF");
											}
										});
										final HTML html = new HTML("<a href=\""+ old.get(i).getUrl() +"\">"+old.get(i).getName() +"</a>");
										html.setStyleName(old.get(i).getName());
										final oldBookmarkData oldbook = old.get(i);
										html.addClickHandler(new ClickHandler() {
											
											@Override
											public void onClick(ClickEvent event) {
												// TODO Auto-generated method stub
												MyServiceAsync service = GWT.create(MyService.class);
												service.setSql(Integer.toString(oldbook.getTimes() + 1), Integer.toString(oldbook.getId()), new AsyncCallback<Void>() {

													@Override
													public void onFailure(Throwable caught) {
														// TODO Auto-generated method stub
														
													}

													@Override
													public void onSuccess(Void result) {
														// TODO Auto-generated method stub
														Window.alert("OK");
													}
												});
											}
										});

										forOldFoc2.add(html);
										forInOldVer.add(forOldFoc2);
										forOldFoc1.add(forInOldVer);
										forOldBookmarkVer.add(forOldFoc1);
									}
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}
							});
							isOldClicked = false;
						}
						else
						{
							isOldClicked = true;
						}
					}
				});
				

				
				topHor.add(forOldBookmarkVer);
				
 				topVer.add(topHor);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		vpMain.add(topVer);
		HTML htmlLine = new HTML();
		htmlLine.setStyleName("htmlLine");
		htmlLine.getElement().getStyle().setPaddingTop(45, Unit.PX);
		RootPanel.get("line").add(htmlLine);
		
		
	}
}

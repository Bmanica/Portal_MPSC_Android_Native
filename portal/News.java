package com.snapcrap.bmanica.portal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.snapcrap.bmanica.portal.PostData;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class News extends AppCompatActivity {
    private enum RSSXMLTag {
        //rss tag
        TITLE, DATE, LINK, CONTENT, GUID, DESCRIPTION,
        //customized tag
        IGNORETAG, FEATURED_IMAGE;
    }
    public static ArrayList <PostData> listData;
    private PostItemAdapter postAdapter;
    private ListView listView;
    protected final String link = "http://www.mpsc.mp.br";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        overridePendingTransition(R.animator.fadein, R.animator.fadeout);
        StrictMode.ThreadPolicy policy =  new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        overridePendingTransition(R.animator.fadein, R.animator.fadeout);

        new RssDataController().execute("http://g1.globo.com/dynamo/carros/rss2.xml");
        listData = new ArrayList<PostData>();
        listView = (ListView) this.findViewById(R.id.postListView);
        postAdapter = new PostItemAdapter(this, R.layout.postitem, listData);
        listView.setAdapter(postAdapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                PostData data = listData.get(arg2);
                Bundle postInfo = new Bundle();
                postInfo.putString("content", data.postLink);
                Intent postviewIntent = new Intent(News.this, News_View.class);
                postviewIntent.putExtras(postInfo);
                startActivity(postviewIntent);
            }
        });
    }

    public class RssDataController extends AsyncTask<String, Integer, ArrayList<PostData>> {
        private RSSXMLTag currentTag;

        @Override
        protected ArrayList<PostData> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String urlStr = params[0];
            InputStream is = null;
            ArrayList<PostData> postDataList = new ArrayList<PostData>();
            URL url;
            try {
                url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("debug", "The response is: " + response);
                is = connection.getInputStream();

                // parse xml after getting the data
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, null);
                int eventType = xpp.getEventType();
                PostData pdData = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss Z", Locale.US);
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    } else if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("item")) {
                            pdData = new PostData();
                            currentTag = RSSXMLTag.IGNORETAG;
                        } else if (xpp.getName().equals("title")) {
                            currentTag = RSSXMLTag.TITLE;
                        } else if (xpp.getName().equals("link")) {
                            currentTag = RSSXMLTag.LINK;
                        } else if (xpp.getName().equals("pubDate")) {
                            currentTag = RSSXMLTag.DATE;
                        } else if (xpp.getName().equals("description")){
                            currentTag = RSSXMLTag.FEATURED_IMAGE;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equals("item")) {
                            // format the data here, otherwise format data in
                            // Adapter
                            Date postDate = dateFormat.parse(pdData.postDate);
                            pdData.postDate = dateFormat.format(postDate);
                            postDataList.add(pdData);
                        } else {
                            currentTag = RSSXMLTag.IGNORETAG;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        String content = xpp.getText();
                        content = content.trim();
                        Log.d("debug", content);
                        if (pdData != null) {
                            switch (currentTag) {
                                case TITLE:
                                    if (content.length() != 0) {
                                        if (pdData.postTitle != null) {
                                            pdData.postTitle += content;
                                        } else {
                                            pdData.postTitle = content;
                                        }
                                    }
                                    break;
                                case LINK:
                                    if (content.length() != 0) {
                                        if (pdData.postLink != null) {
                                            pdData.postLink += content;
                                        } else {
                                            pdData.postLink = content;
                                        }
                                    }
                                    break;
                                case DATE:
                                    if (content.length() != 0) {
                                        if (pdData.postDate != null) {
                                            pdData.postDate += content;
                                        } else {
                                            pdData.postDate = content;
                                        }
                                    }
                                    break;
								case FEATURED_IMAGE:
								if (content.length() != 0) {
									if (pdData.postThumbUrl != null) {
										pdData.postThumbUrl += content;
									} else {
										pdData.postThumbUrl = content;
									}
								}
                                default:
                                    break;
                            }
                        }
                    }
                    eventType = xpp.next();
                }
                Log.v("tst", String.valueOf((postDataList.size())));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return postDataList;
        }
        @Override
        protected void onPostExecute(ArrayList<PostData> result) {
            for (int i = 0; i < result.size(); i++) {
                listData.add(result.get(i));
            }

            postAdapter.notifyDataSetChanged();
        }
    }
}

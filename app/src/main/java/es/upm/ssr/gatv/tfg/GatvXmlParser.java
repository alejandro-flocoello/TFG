package es.upm.ssr.gatv.tfg;


import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GatvXmlParser {

    static final String KEY_ENTRY = "entry";
    static final String KEY_TITLE = "title";
    static final String KEY_LINK = "link";
    static final String KEY_SUMMARY = "summary";
    static final String KEY_AUDIO = "audio";
    static final String KEY_IMAGE_URL = "image";
    static final String KEY_IMAGE_MSG_URL = "imagemsg";

    public static List<Entry> getStackSitesFromFile(Context ctx) {

        // List of StackSites that we will return
        List<Entry> entrySites;
        entrySites = new ArrayList<Entry>();

        // temp holder for current StackSite while parsing
        Entry curStackSite = null;
        // temp holder for current text value while parsing
        String curText = "";

        try {
            // Get our factory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("Mensajes.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // point the parser to our file.
            xpp.setInput(reader);

            // get initial eventType
            int eventType = xpp.getEventType();

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(KEY_ENTRY)) {
                            // If we are starting a new <site> block we need
                            //a new StackSite object to represent it
                            curStackSite = new Entry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //grab the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_ENTRY)) {
                            // if </site> then we are done with current Site
                            // add it to the list.
                            entrySites.add(curStackSite);
                        } else if (tagname.equalsIgnoreCase(KEY_TITLE)) {
                            // if </name> use setName() on curSite
                            curStackSite.setTitle(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LINK)) {
                            // if </link> use setLink() on curSite
                            curStackSite.setLink(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_SUMMARY)) {
                            // if </about> use setAbout() on curSite
                            curStackSite.setSummary(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_IMAGE_URL)) {
                            // if </image> use setImgUrl() on curSite
                            curStackSite.setImgUrl(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_IMAGE_MSG_URL)) {
                            // if </image> use setImgUrl() on curSite
                            curStackSite.setImgMsgUrl(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_AUDIO)) {
                           // if </audio> use setImgUrl() on curSite
                            curStackSite.setAudioUrl(curText);

                }
                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the populated list.

        return entrySites;
    }
}

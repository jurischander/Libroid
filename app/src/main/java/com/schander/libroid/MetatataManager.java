package com.schander.libroid;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class MetatataManager {
	// We don't use namespaces
    private static final String ns = null;
   
    public MetaData parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readMetaData(parser);
        } finally {
            in.close();
        }
    }

	private MetaData readMetaData(XmlPullParser parser)  throws XmlPullParserException, IOException {

	    parser.require(XmlPullParser.START_TAG, ns, "package");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("metadata")) {
	        	return readEntry(parser);
	            //entries.add(readEntry(parser));
	        } else {
	            skip(parser);
	        }
	    }  
	    return null;	
	}
	
	public static class MetaData {
	    public final String title;
	    public final String creator;
	    public final String description;

	    private MetaData(String title, String description, String creator) {
	        this.title = title;
	        this.description = description;
	        this.creator = creator;
	    }
	}
	  
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private MetaData readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "metadata");
	    String title = null;
	    String description = null;
	    String creator = null;
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("dc:title")) {
	            title = readTitle(parser);
	        } else if (name.equals("dc:description")) {
	        	description = readDescription(parser);
	        } else if (name.equals("dc:creator")) {
	        	creator = readCreator(parser);
	        } else {
	            skip(parser);
	        }
	    }
	    return new MetaData(title, description, creator);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "dc:title");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "dc:title");
	    return title;
	}
	  
	// Processes link tags in the feed.
	private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
	    String creator = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "dc:creator");
	    return creator;
	}

	// Processes summary tags in the feed.
	private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "dc:description");
	    String description = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "dc:description");
	    return formatDescription(description);
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	}
	
	private String formatDescription(String ds){
		String startTag = "<p class=\"description\">";
		String endTag = "</p>";
		int indexOfStartTag = ds.indexOf(startTag);
		int indexOfEnd = ds.indexOf(endTag);
		return ds.substring(indexOfStartTag + startTag.length(), indexOfEnd).replaceAll("<br>", "\n");
	}
	
	
}

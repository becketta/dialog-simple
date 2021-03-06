package edu.msu.becketta.dialog_simple;

import android.graphics.Path;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Aaron Beckett on 1/10/2016.
 */
public class diaLog {

    private String name;
    private String imageUri = null;
    private ArrayList<Annotation> annotations = new ArrayList<>();

    public void addAnnotation(Annotation newAnnot) {
        annotations.add(newAnnot);
    }

    public void saveToXml(XmlSerializer xml) throws IOException {
        xml.startTag(null, "diaLog");

        xml.attribute(null, "name", name);
        xml.attribute(null, "uri", imageUri != null ? imageUri : "");
        for (Annotation annot : annotations) {
            annot.saveAnnotationsXml(xml);
        }

        xml.endTag(null, "diaLog");
    }

    public void loadFromXml(XmlPullParser xml) throws IOException, XmlPullParserException {
        name = xml.getAttributeValue(null, "name");
        imageUri = xml.getAttributeValue(null, "uri");

        annotations = new ArrayList<>();

        xml.nextTag();
        while (xml.getName().equals("annotation")) {
            Annotation newAnnot = new Annotation();
            newAnnot.loadAnnotationsXml(xml);
            annotations.add(newAnnot);

            xml.nextTag();
        }
    }

    /********************** GETTERS AND SETTERS **********************/

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImageUri(String newUri) {
        imageUri = newUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public ArrayList<Path> getPaths() {
        ArrayList<Path> paths = new ArrayList<>();
        for (Annotation a : annotations) {
            paths.add(a.getPath());
        }
        return paths;
    }
}

package control;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;

public class Utils {
    public static  String valuesColorsXml = "values/colors.xml";
    public static  String localProps = "local.properties";
    public static  String platformsRes = "%s/platforms/%s/data/res/values";
    public static  String drawableDir = "drawable";
    public static  String nsUri = "http://www.w3.org/2000/xmlns/";
    public static  String androidUri = "http://schemas.android.com/apk/res/android";
    public static  String INDENT_SPACE = "{http://xml.apache.org/xslt}indent-amount";
    public static  String[][] countryList =
                    {{"Circle", "/image/circle20.png"},
                    {"Rectangle", "/image/rectanglee35.png"},
                    {"Elipze", "/image/elipze35.png"},
                    {"Triangle", "/image/triangle35.png"}};


    public static void CreateShape(VirtualFile dir,
                                   String filename,
                                    String color,
                                    String pressed) throws Exception {
        VirtualFile child = dir.findChild(drawableDir);
        if (child == null) {
            child = dir.createChildDirectory(null, drawableDir);
        }

        VirtualFile newXmlFile = child.findChild(filename);
        if (newXmlFile != null && newXmlFile.exists()) {
            newXmlFile.delete(null);
        }
        newXmlFile = child.createChildData(null, filename);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();
        Element root = doc.createElement("layer-list");
        root.setAttributeNS(nsUri, "xmlns:android", androidUri);
        doc.appendChild(root);

        Element item = doc.createElement("item");
        item.setAttribute("android:state_pressed", "false");
        item.setAttribute("android:state_focused", "false");
        Element rte  = doc.createElement("rotate");
        rte.setAttribute("android:fromDegrees","45");
        rte.setAttribute("android:pivotX","-40%");
        rte.setAttribute("android:pivotY","87%");
        rte.setAttribute("android:toDegrees","45");


        Element shp  = doc.createElement("shape");
        shp.setAttribute("android:shape","rectangle");


        Element sld  = doc.createElement("solid");
        sld.setAttribute("android:color",color);

        shp.appendChild(sld);
        rte.appendChild(shp);
        item.appendChild(rte);
        root.appendChild(item);


        OutputStream os = newXmlFile.getOutputStream(null);
        PrintWriter out = new PrintWriter(os);

        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(INDENT_SPACE, "4");
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        out.println(writer.getBuffer().toString());
        out.close();
    }


    public static void CreateXmlBackground(VirtualFile dir,
                                           Project project,
                                           String filename,
                                           String color,
                                           String pressed,String sdp) throws Exception {
        VirtualFile child = dir.findChild(drawableDir);
        if (child == null) {
            child = dir.createChildDirectory(null, drawableDir);
        }

        VirtualFile newXmlFile = child.findChild(filename);
        if (newXmlFile != null && newXmlFile.exists()) {
            newXmlFile.delete(null);
        }
        newXmlFile = child.createChildData(null, filename);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();
        Element root = doc.createElement("layer-list");
        root.setAttributeNS(nsUri, "xmlns:android", androidUri);
        doc.appendChild(root);

        Element item = doc.createElement("item");
        item.setAttribute("android:state_pressed", "false");
        item.setAttribute("android:state_focused", "false");
        Element shp  = doc.createElement("shape");
        Element gnt = doc.createElement("gradient");
        gnt.setAttribute("android:startColor",color);
        gnt.setAttribute("android:centerColor",color);
        gnt.setAttribute("android:endColor",color);
        gnt.setAttribute("android:angle","270");

        Element stk = doc.createElement("stroke");
        stk.setAttribute("android:width","0.5dp");
        stk.setAttribute("android:color",color);

        Element cnr = doc.createElement("corners");
        cnr.setAttribute("android:radius",sdp);


        shp.appendChild(gnt);
        shp.appendChild(stk);
        shp.appendChild(cnr);
        item.appendChild(shp);
        root.appendChild(item);


        OutputStream os = newXmlFile.getOutputStream(null);
        PrintWriter out = new PrintWriter(os);

        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(INDENT_SPACE, "4");
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        out.println(writer.getBuffer().toString());
        out.close();
    }


    public static boolean valid(String filename, String color,
                          String pressed) {
        if (filename.isEmpty() || ".xml".equals(filename))
            return false;

        String regex = "^@color/.+";
        return color.matches(regex) ||
                pressed.matches(regex);
    }


    public static void showMessageDialog(Project project,String title, String message) {
        Messages.showMessageDialog(
                project, message, title, Messages.getErrorIcon());
    }

    public static boolean exists(VirtualFile dir,String filename) {
        String[] dirs = new String[]{drawableDir};
        for (String d : dirs) {
            VirtualFile f = dir.findChild(d);
            if (f != null && f.isDirectory()) {
                VirtualFile dest = f.findChild(filename);
                if (dest != null && dest.exists()) {
                    return true;
                }
            }
        }

        return false;
    }


    @NotNull
    public static HashMap<String, String> parseColorsXml(VirtualFile colorsXml) {
        HashMap<String, String> map = new LinkedHashMap<String, String>();
        try {
            NodeList colors = getColorNodes(colorsXml.getInputStream());
            makeColorMap(colors, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }



    public static void makeColorMap(NodeList colors, HashMap<String, String> map) {
        for (int i = 0; i < colors.getLength(); i++) {
            Element node = (Element) colors.item(i);
            String nodeName = node.getNodeName();
            if ("color".equals(nodeName) || "item".equals(nodeName)) {
                String name = node.getAttribute("name");
                String color = node.getTextContent();
                if (name != null && color != null && !map.containsKey(name)) {
                    map.put(name, color);
                }
            }
        }
    }
    public static NodeList getColorNodes(InputStream stream) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "//item[@type=\"color\"]|//color";
        XPathExpression compile = xPath.compile(expression);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(stream);
        return (NodeList) compile.evaluate(doc, XPathConstants.NODESET);
    }



    @NotNull
    public static HashMap<String, String> parseAndroidColorsXml(Project project) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (project == null) return map;
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile prop = baseDir.findFileByRelativePath(localProps);
        if (prop == null) return map;

        Properties properties = new Properties();
        try {
            properties.load(prop.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sdkDir = properties.getProperty("sdk.dir");
        File file = new File(sdkDir + File.separator + "platforms");
        if (!file.isDirectory()) return map;

        ArrayList<String> platforms = new ArrayList<String>();
        Collections.addAll(platforms, file.list());
        Collections.reverse(platforms);
        for (int i = 0, size = platforms.size(); i < size; i++) {
            String platform = platforms.get(i);
            if (platform.matches("^android-\\d+$")) continue;
            if (i > 3) break;

            String path = String.format(platformsRes, sdkDir, platform);
            File[] files = new File(path).listFiles();
            if (files == null) continue;
            for (File f : files) {
                if (f.getName().matches("colors.*\\.xml")) {
                    try {
                        FileInputStream stream = new FileInputStream(f);
                        NodeList colors = getColorNodes(stream);
                        makeColorMap(colors, map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return map;
    }


    public static String getColorName(JComboBox<Object> combo) {
        Object colorItem = combo.getSelectedItem();
        try {
            if (colorItem instanceof Object[]) {
                return "@color/" + ((Object[]) (colorItem))[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}


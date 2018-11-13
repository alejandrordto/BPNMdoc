
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 2112107
 */
public class analizador {

    public Element process;
    public List<Element> actors = new ArrayList<Element>();
    public List<Element> businessRules = new ArrayList<Element>();
    public List<Element> events = new ArrayList<Element>();
    public List<Element> taskes = new ArrayList<Element>();

    public void read(String filename) {
        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList collaboration = doc.getElementsByTagName("model:collaboration");
            Node collaborators = collaboration.item(0);
            NodeList participants = collaborators.getChildNodes();
            NodeList process = doc.getElementsByTagName("model:process");
            Node components = process.item(0);
            NodeList all = components.getChildNodes();
            for (int i = 0; i < participants.getLength(); i++) {
                Node nNode = participants.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    if (element.hasAttribute("processRef")) {
                        this.process = element;
                    } else {
                        actors.add(element);
                    }
                }
            }
            for (int i = 0; i < all.getLength(); i++) {
                Node nNode = all.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    if (element.getNodeName().contains("Task") || element.getNodeName().equals("model:callActivity")) {
                        taskes.add(element);
                    }
                    if (element.getNodeName().contains("Event")) {
                        events.add(element);
                    }
                    if (element.getNodeName().contains("exclusiveGateway")) {
                        businessRules.add(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generate() {
        String html;
        String flag = "";
        String doc = "";
        html = "<html> <head> <title> BPMNDoc </title> </head> <body>	<h1 align=\"center\"> Process:";
        html += process.getAttribute("name") + "</h1>";
        doc = getDoc(process);
        if (!doc.equals("")) {
            html += "<p>" + doc + "</p>";
        }
        html += "<h2 align=\"center\"> Actors </h2> <TABLE BORDER align=\"center\"> <TR align=\"center\"> <TH>ID</TH> <TH>Actor name</TH> <TH>Description</TH> </TR>";
        flag = "";
        for (int i = 0; i < actors.size(); ++i) {
            flag += "<TR > <TD> Actor " + (i + 1) + "</TD>";
            flag += "<TD>" + actors.get(i).getAttribute("name") + "</TD>";
            doc = getDoc(actors.get(i));
            if (!doc.equals("")) {
                flag += "<TD>" + doc + "</TD> </TR>";
            } else {
                flag += "<TD> </TD> </TR>";
            }
        }
         html += flag + "</TABLE>  <h2 align=\"center\">  Business rule </h2> <TABLE BORDER align=\"center\"> <TR> <TH>ID</TH> <TH>Rule name</TH> <TH>Description</TH>  </TR>";
        flag = "";
        for (int i = 0; i < businessRules.size(); ++i) {
            flag += "<TR> <TD> Business Rule " + (i + 1) + "</TD>";
            flag += "<TD>" + businessRules.get(i).getAttribute("name") + "</TD>";
            doc = getDoc(businessRules.get(i));
            if (!doc.equals("")) {
                flag += "<TD>" + doc + "</TD>  ";
            } else {
                flag += "<TD> </TD> </TR>";
            }
        }
        html += flag + "</TABLE>  <h2 align=\"center\"> Events  </h2> <TABLE BORDER align=\"center\"> <TR> <TH>ID</TH> <TH>Event name</TH> <TH>Description</TH> <TH>Type</TH> </TR>";
        flag = "";
        for (int i = 0; i < events.size(); ++i) {
            flag += "<TR> <TD> Event " + (i + 1) + "</TD>";
            flag += "<TD>" + events.get(i).getAttribute("name") + "</TD>";
            doc = getDoc(events.get(i));
            if (!doc.equals("")) {
                flag += "<TD>" + doc + "</TD> ";
            } else {
                flag += "<TD> </TD>";
            }
            flag += "<TD>" + events.get(i).getNodeName().replace("model:", "") + "</TD> </TR>";
        }
        html += flag + "</TABLE>  <h2 align=\"center\"> Task </h2> <TABLE BORDER align=\"center\"> <TR> <TH>ID</TH> <TH>Task name</TH> <TH>Description</TH> <TH>Type</TH> </TR>";
        flag = "";
        for (int i = 0; i < taskes.size(); ++i) {
            flag += "<TR> <TD> Task-" + (i + 1) + "</TD>";
            flag += "<TD>" + taskes.get(i).getAttribute("name") + "</TD>";
            doc = getDoc(taskes.get(i));
            if (!doc.equals("")) {
                flag += "<TD>" + doc + "</TD>";
            } else {
                flag += "<TD> </TD>";
            }
            flag += "<TD>" + taskes.get(i).getNodeName().replace("model:", "") + "</TD> </TR>";
        }
        html += flag + "</TABLE> </body> </html>";
        return html;
    }

    public String getDoc(Element e) {
        NodeList doc = e.getElementsByTagName("model:documentation");
        if (doc.getLength() > 0) {
            return doc.item(0).getTextContent();
        }
        return "";
    }
}

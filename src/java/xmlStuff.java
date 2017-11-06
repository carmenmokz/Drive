
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author carme
 */
public class xmlStuff {
    
    //CREA UN XML CON EL NOMBRE DEL DIRECTORIO E IDENTIFICADOR
    public static void creatingXML(String Root, String id) throws TransformerException, ParserConfigurationException{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
        Document document = documentBuilder.newDocument();
        
        Element element = document.createElement(Root);
        document.appendChild(element);
        
        Attr attr = document.createAttribute("ID");
        attr.setValue(id);
        element.setAttributeNode(attr);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        
        String xmlfile = "C:\\Users\\carme\\Desktop\\"+Root+".xml";
        StreamResult streamResult = new StreamResult(new File(xmlfile));
        
        transformer.transform(source, streamResult);
        
    }
    
    //AGREGA ELEMENTO AL XML
    public static void addToXML(String Root, String fileName, String content) throws TransformerException, ParserConfigurationException, SAXException, IOException{
        String filepath = "C:\\Users\\carme\\Desktop\\"+Root+".xml";
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	Document doc = docBuilder.parse(filepath);
        
        Node staff = doc.getElementsByTagName(Root).item(0);
        
        Element age = doc.createElement(fileName);
	age.appendChild(doc.createTextNode(content));
	staff.appendChild(age);
                
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        
        String xmlfile = "C:\\Users\\carme\\Desktop\\"+Root+".xml";
        StreamResult streamResult = new StreamResult(new File(xmlfile));
        
        transformer.transform(source, streamResult);
    }
    
    
    //MAIN
    public static void main(String[] args) throws Exception{
        xmlStuff xml = new xmlStuff();
        //xml.creatingXML("Directorio","1");
        xml.addToXML("Directorio","file2","esto es un archivo");
        
    }
}

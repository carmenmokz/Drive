
import java.io.File;
import java.io.FileInputStream;
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
    public static void addToXML(String Root, String fileName, String content) throws TransformerException, ParserConfigurationException{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
        Document document = documentBuilder.newDocument();
        
        Element element = document.createElement(Root);
        document.appendChild(element);
        
        //Attr attr = document.createAttribute("ID");
        //attr.setValue(id);
        //element.setAttributeNode(attr);
        
        Element file = document.createElement(fileName);
        file.appendChild(document.createTextNode(content));
        element.appendChild(file);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        
        String xmlfile = "C:\\Users\\carme\\Desktop\\"+Root+".xml";
        StreamResult streamResult = new StreamResult(new File(xmlfile));
        
        transformer.transform(source, streamResult);
    }
    
    //MAIN
    public static void main(String[] args) throws Exception{
        xmlStuff xml = new xmlStuff();
        xml.creatingXML("Directorio","1");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
        Document document = documentBuilder.newDocument();
        Element firstName = document.createElement("FirstName");
        Element lastName = document.createElement("LastName");

        firstName.appendChild(document.createTextNode("First Name"));
        lastName.appendChild(document.createTextNode("Last Name"));

        // create contact element
        Element contact = document.createElement("contact");

        // create attribute
        Attr genderAttribute = document.createAttribute("gender");
        genderAttribute.setValue("F");

        // append attribute to contact element
        contact.setAttributeNode(genderAttribute);
        contact.appendChild(firstName);
        contact.appendChild(lastName);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        
        String xmlfile = "C:\\Users\\carme\\Desktop\\"+"Directorio"+".xml";
        StreamResult streamResult = new StreamResult(new File(xmlfile));
        
        transformer.transform(source, streamResult);
        /**DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
        Document document = documentBuilder.newDocument();
        
        Element element = document.createElement("Directorio");
        document.appendChild(element);
        
        Element file = document.createElement("file");
        file.appendChild(document.createTextNode("content"));
        element.appendChild(file);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        
        String xmlfile = "C:\\Users\\carme\\Desktop\\"+"Directorio"+".xml";
        StreamResult streamResult = new StreamResult(new File(xmlfile));
        
        transformer.transform(source, streamResult);**/
    }
}

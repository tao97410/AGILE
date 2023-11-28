package xml;

import model.Map;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class Main {

    /**
	 * @param args the arguments 
	 */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
        Map map = new Map(null);
        XMLdeserializer.load(map);
        System.out.println(map);
    }    
    
}

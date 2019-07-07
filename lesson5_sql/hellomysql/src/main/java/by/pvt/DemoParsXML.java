package by.pvt;

import by.pvt.entity.Word;
import by.pvt.service.WordService;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class DemoParsXML {
    static WordService wordService = new WordService();

    public static void main(String[] args) {
        System.out.println("перевод с английского");
        while (true) {
            System.out.println("напишите слово на английском: ");
            Scanner scanner = new Scanner(System.in);
            String scannerWordEn = scanner.nextLine();
            if("end".equals(scannerWordEn)) {
                break;
            }
            Word word = wordService.select(scannerWordEn);
            if (word == null) {
                System.out.println("нет такого слова в базе данных");
                System.out.println("напишите перевод на русском: ");
                Scanner scanner1 = new Scanner(System.in);
                String scannerWordRus = scanner1.nextLine();
                wordService.insert(scannerWordEn, scannerWordRus);
                try {
                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = documentBuilder.parse("F:\\projects\\JD2\\lesson5_sql\\hellomysql\\src\\main\\resources\\changeLog.xml");
                    addNewWord(document, scannerWordEn, scannerWordRus);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("слово добавлено в словарь");
                System.out.println("английский: " + scannerWordEn + "\t\tрусский: " + scannerWordRus);
            } else {
                System.out.println("английский: " + word.getEn() + "\t\tрусский: " + word.getRus());
            }
        }


        /*try {

         *//*Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/translation", "root", "Skatina964");
            PreparedStatement ps = connection.prepareStatement(" insert into `translation`.`translation` (`EN`, `RUS`) values ('hello', 'привет')");
            ps.executeUpdate();*//*


            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse("F:\\projects\\JD2\\lesson5_sql\\hellomysql\\src\\main\\resources\\changeLog.xml");
            addNewWord(document, "hello", "привет");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }


    private static void addNewWord(Document document, String wordEN, String wordRUS) {
        String id = null;
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = nodeList.getLength() - 1; i >= 0; i--) {
            if ("changeSet".equals(nodeList.item(i).getNodeName())) {
                NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
                id = namedNodeMap.getNamedItem("id").getNodeValue();
                Integer changeSetId = 1 + Integer.parseInt(id);
                id = String.valueOf(changeSetId);
                break;
            }
        }

        Element changeSet = document.createElement("changeSet");
        changeSet.setAttribute("id", id);
        changeSet.setAttribute("author", "lipski");
        Element sql = document.createElement("sql");
        sql.setTextContent("insert into `dictionary`.`translation` (`EN`, `RUS`) values ('" + wordEN + "', '" + wordRUS + "');");
        changeSet.appendChild(sql);
        root.appendChild(changeSet);
        writeDocument(document);
    }

    private static void writeDocument(Document document) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("F:\\projects\\JD2\\lesson5_sql\\hellomysql\\src\\main\\resources\\changeLog.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        } catch (TransformerException | IOException e) {
            e.printStackTrace(System.out);
        }
    }
}

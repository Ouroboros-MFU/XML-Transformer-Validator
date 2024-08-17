import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XsltTransformer {
    public static void main(String[] args) {
        try {
            // Загрузка XML-файла
            File xmlFile = new File("src\\input.xml");
            StreamSource xmlSource = new StreamSource(xmlFile);

            // Загрузка XSLT-файла
            File xsltFile = new File("src\\transform_1.xsl");
            StreamSource xsltSource = new StreamSource(xsltFile);

            // Создание трансформатора
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xsltSource);

            // Преобразование XML с помощью XSLT
            File outputFile = new File("src\\output.xml");
            StreamResult result = new StreamResult(new FileOutputStream(outputFile));
            transformer.transform(xmlSource, result);

            System.out.println("Transformation completed. Output file: " + outputFile.getAbsolutePath());
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}

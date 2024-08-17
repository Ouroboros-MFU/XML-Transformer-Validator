import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.time.LocalDate;
import java.time.Period;

public class XmlValidator {
    public static final Integer[] MULT_N1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    public static final Integer[] MULT_N2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    public static final Integer[] MULT_N =  {2, 4, 10, 3, 5, 9, 4, 6, 8};

    public static boolean checkInn(String innStr) {
        Boolean valid;
        Integer[] inn = stringToIntArray(innStr);

        Integer innSize = inn.length;

        switch (innSize) {
            case 12:
                Integer N1 = getChecksum(inn,MULT_N1);
                Integer N2 = getChecksum(inn,MULT_N2);

                valid = (inn[inn.length-1].equals(N2) && inn[inn.length-2].equals(N1));
                break;
            case 10:
                Integer N = getChecksum(inn,MULT_N);
                valid = (inn[inn.length-1].equals(N));
                break;
            default:
                valid = false;
                break;
        }
        return valid;
    }

    public static Integer[] stringToIntArray(String src) {
        char[] chars = src.toCharArray();
        ArrayList<Integer> digits = new ArrayList<Integer>();
        for (char aChar : chars) {
            digits.add(Character.getNumericValue(aChar));
        }
        return digits.toArray(new Integer[digits.size()]);
    }

    public static Integer getChecksum(Integer[] digits, Integer[] multipliers) {
        int checksum = 0;
        for (int i=0; i<multipliers.length; i++) {
            checksum+=(digits[i]*multipliers[i]);
        }
        return (checksum % 11) % 10;
    }

    private static boolean validateAge(String birthDate) {
        LocalDate birthDateLocal = LocalDate.parse(birthDate);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDateLocal, currentDate);
        return period.getYears() <= 85;
    }

    private static String extractBirthDate(Document doc) {
        Element applicantDetailsElement = (Element) doc.getElementsByTagName("ApplicantDetails").item(0);
        return applicantDetailsElement.getElementsByTagName("DateOfBirth").item(0).getTextContent();
    }
    private static String extractINN(Document doc) {
    Element applicantDetailsElement = (Element) doc.getElementsByTagName("ApplicantDetails").item(0);
    return applicantDetailsElement.getElementsByTagName("INN").item(0).getTextContent();
    }

    private static boolean validateLoanAmount(int loanAmount, int MinAmount, int MaxAmount) {
        return loanAmount >= MinAmount && loanAmount <= MaxAmount;
    }

    private static boolean validateLoanTerm(int loanTerm, int MinMonths, int MaxMonths) {
        return loanTerm >= MinMonths && loanTerm <= MaxMonths;
    }

    private static int extractLoanTerm(Document doc) {
        Element rootElement = doc.getDocumentElement();

        NodeList applicationDataNodeList = rootElement.getElementsByTagName("ApplicationData");
        if (applicationDataNodeList.getLength() > 0) {
            Element applicationDataElement = (Element) applicationDataNodeList.item(0);
            NodeList ApplicationInformationDataNodeList = applicationDataElement.getElementsByTagName("ApplicationInformation");
            if (ApplicationInformationDataNodeList.getLength() > 0) {
                Element ApplicationInformationDataElement = (Element) ApplicationInformationDataNodeList.item(0);
                return Integer.parseInt(ApplicationInformationDataElement.getAttribute("LoanTerm"));
            }
        }
        return -1;
    }

    private static int extractLoanAmount(Document doc) {
        Element rootElement = doc.getDocumentElement();

        NodeList applicationDataNodeList = rootElement.getElementsByTagName("ApplicationData");
        if (applicationDataNodeList.getLength() > 0) {
            Element applicationDataElement = (Element) applicationDataNodeList.item(0);
            NodeList ApplicationInformationDataNodeList = applicationDataElement.getElementsByTagName("ApplicationInformation");
            if (ApplicationInformationDataNodeList.getLength() > 0) {
                Element ApplicationInformationDataElement = (Element) ApplicationInformationDataNodeList.item(0);
                return Integer.parseInt(ApplicationInformationDataElement.getAttribute("LoanAmount"));
            }
        }
        return -1;
    }


    private static LoanProduct extractLoanProduct(Document doc) {
    Element rootElement = doc.getDocumentElement();

    NodeList systemDataNodeList = rootElement.getElementsByTagName("SystemData");
    if (systemDataNodeList.getLength() > 0) {
        Element applicationDataElement = (Element) systemDataNodeList.item(0);

        NodeList loanProductsDataNodeList = applicationDataElement.getElementsByTagName("LoanProducts");
        if (loanProductsDataNodeList.getLength() > 0) {
            Element ApplicationInformationDataElement = (Element) loanProductsDataNodeList.item(0);
            NodeList ProductsDataNodeList = ApplicationInformationDataElement.getElementsByTagName("Product");
            if (ProductsDataNodeList.getLength() > 0) {
                for (int i=0; i<= ProductsDataNodeList.getLength(); i++){
                    Element ProductDataElement = (Element) ProductsDataNodeList.item(i);
                    String selected = ProductDataElement.getAttribute("IsSelected");
                    if (Objects.equals(selected, "1")){
                        NodeList ParamsDataNodeList = ProductDataElement.getElementsByTagName("Params");
                        if (ParamsDataNodeList.getLength() > 0) {
                            Element ParamsDataElement = (Element) ParamsDataNodeList.item(0);
                            return new LoanProduct(Integer.parseInt(ParamsDataElement.getAttribute("MinAmount")),
                                    Integer.parseInt(ParamsDataElement.getAttribute("MaxAmount")),
                                    Integer.parseInt(ParamsDataElement.getAttribute("MinMonths")),
                                    Integer.parseInt(ParamsDataElement.getAttribute("MaxMonths")));
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String xmlFilePath = "src/input.xml";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(xmlFilePath));

        LoanProduct LP = extractLoanProduct(doc);
        System.out.println("Валидация данных:");
        System.out.println("ИНН: " + (checkInn(extractINN(doc))? "валиден" : "не валиден"));
        System.out.println("Возраст: " + (validateAge(extractBirthDate(doc)) ? "валиден" : "не валиден"));
        System.out.println("Сумма кредита: " + (validateLoanAmount(extractLoanAmount(doc), LP.getMinAmount(), LP.getMaxAmount()) ? "валидна" : "не валидна"));
        System.out.println("Срок кредита: " + (validateLoanTerm(extractLoanTerm(doc), LP.getMinMonths(), LP.getMaxMonths()) ? "валиден" : "не валиден"));
    }
}


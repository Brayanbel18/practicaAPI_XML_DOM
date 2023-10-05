import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Cargar el archivo XML existente
            File archivo = new File("src\\sales.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);

            String departamento;
            double porcentaje;

            boolean departamentoValido = false;

            do {
                departamento = obtenerDepartment(scanner);

                // Obtener la lista de elementos sale_record
                doc.getDocumentElement().normalize();
                NodeList saleRecords = doc.getElementsByTagName("sale_record");

                for (int i = 0; i < saleRecords.getLength(); i++) {
                    Node saleRecord = saleRecords.item(i);

                    if (saleRecord.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) saleRecord;

                        String departmentElement = element.getElementsByTagName("department").item(0).getTextContent();

                        // Verificar si el departamento coincide con el ingresado por el usuario
                        if (departmentElement.equalsIgnoreCase(departamento)) {
                            departamentoValido = true;
                            break; // Rompe el bucle cuando se encuentra un departamento válido
                        }
                    }
                }

                if (!departamentoValido) {
                    System.out.println("El departamento ingresado no existe. Intente nuevamente.\n");
                }
            } while (!departamentoValido);

            // Solicitar el porcentaje después de validar el departamento
            porcentaje = obtenerPorcentajeValido(scanner);

            // Obtener la lista de elementos sale_record nuevamente después de validar el departamento
            doc.getDocumentElement().normalize();
            NodeList saleRecords = doc.getElementsByTagName("sale_record");

            for (int i = 0; i < saleRecords.getLength(); i++) {
                Node saleRecord = saleRecords.item(i);

                if (saleRecord.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) saleRecord;

                    String departmentElement = element.getElementsByTagName("department").item(0).getTextContent();

                    // Verificar si el departamento coincide con el ingresado por el usuario
                    if (departmentElement.equalsIgnoreCase(departamento)) {
                        // Obtener el valor de ventas actual y aplicar el aumento
                        double ventasActual = Double.parseDouble(element.getElementsByTagName("sales").item(0).getTextContent());
                        double ventasAumentadas = ventasActual * (1 + (porcentaje / 100));

                        // Actualizar el valor de ventas en el documento
                        element.getElementsByTagName("sales").item(0).setTextContent(String.format("%.2f", ventasAumentadas));
                    }
                }
            }

            // Guardar el nuevo documento XML
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            File nuevoArchivo = new File("new_sales.xml");
            StreamResult result = new StreamResult(new FileOutputStream(nuevoArchivo));
            transformer.transform(source, result);

            System.out.println("El archivo new_sales.xml se ha generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static double obtenerPorcentajeValido(Scanner scanner) {
        double porcentaje;

        while (true) {
            System.out.print("Ingrese el porcentaje de aumento (entre 5% y 15%): ");
            String input = scanner.next();

            try {
                porcentaje = Double.parseDouble(input);

                if (porcentaje >= 5 && porcentaje <= 15) {
                    break;
                } else {
                    System.out.println("El porcentaje debe estar entre 5% y 15%.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.\n");
            }
        }

        return porcentaje;
    }

    private static String obtenerDepartment(Scanner scanner) {
        System.out.print("Ingrese el departamento al que desea aplicar el aumento: ");
        return scanner.nextLine();
    }
}
//Beltran Calvo Brayan #222217084

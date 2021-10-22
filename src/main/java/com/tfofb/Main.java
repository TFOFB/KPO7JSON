package com.tfofb;

import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class Main {

    public static void main (String[] args) throws IOException {

        //Читаем файл XML и записываем его в строку
        String XMLpatientsDataString = readUsingBufferedReader("modelPatients.xml");

        File patientsSchemaFile = new File("modelPatientsSchema.json");
        File testsSchemaFile = new File("modelTestsSchema.json");

        JSONTokener patientsSchemaData = new JSONTokener(new FileInputStream(patientsSchemaFile));
        JSONTokener testSchemaData = new JSONTokener(new FileInputStream(testsSchemaFile));

        JSONObject jsonPatientSchema = new JSONObject(patientsSchemaData);
        JSONObject jsonTestsSchema = new JSONObject(testSchemaData);

        File jsonTestsData = new File("modelTests.json");

        JSONTokener jsonTestsDataFile = new JSONTokener(new FileInputStream(jsonTestsData));

        //Получаем объект JSON из строки
        JSONObject jsonPatientObject = XML.toJSONObject(XMLpatientsDataString);
        jsonPatientObject = (JSONObject) jsonPatientObject.get("Patient");
        JSONObject jsonTestsObject = new JSONObject(jsonTestsDataFile);

        //Выполняем проверку по заданной схеме
        Schema schemaPatientsValidator = SchemaLoader.load(jsonPatientSchema);
        schemaPatientsValidator.validate(jsonPatientObject);

        Schema schemaTestsValidator = SchemaLoader.load(jsonTestsSchema);
        schemaTestsValidator.validate(jsonTestsObject);

        //Выполняем вывод данных из json
        System.out.println("----Данные из файла modelPatients.json-----------------" + "\n"
                         + "ID: " + jsonPatientObject.getInt("ID") + "\n"
                         + "Name: " + jsonPatientObject.getString("Name") + "\n"
                         + "Surname: " + jsonPatientObject.getString("Surname") + "\n"
                         + "Patronymic: " + jsonPatientObject.getString("Patronymic") + "\n"
                         + "DateOfBirth: " + jsonPatientObject.getString("DateOfBirth") + "\n"
                         + "PolicyNumber: " + jsonPatientObject.getString("PolicyNumber") + "\n");

        System.out.println("----Данные из файла modelTest.json---------------------" + "\n"
                + "ID: " + jsonTestsObject.getString("ID") + "\n"
                + "P_id: " + jsonTestsObject.getString("P_id") + "\n"
                + "DateOfTest: " + jsonTestsObject.getString("DateOfTest") + "\n"
                + "TestType: " + jsonTestsObject.getString("TestType") + "\n"
                + "LabName: " + jsonTestsObject.getString("LabName") + "\n"
                + "TestResult: " + jsonTestsObject.getString("TestResult") + "\n");
    }

    private static String readUsingBufferedReader(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (fileName));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}

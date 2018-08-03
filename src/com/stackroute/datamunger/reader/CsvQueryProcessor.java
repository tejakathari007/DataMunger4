package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {
    private String fileName;
    BufferedReader br = null;

    // Parameterized constructor to initialize filename
    public CsvQueryProcessor(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        br = new BufferedReader(new FileReader(fileName));

    }

    /*
     * implementation of getHeader() method. We will have to extract the headers
     * from the first line of the file.
     */
    @Override
    public Header getHeader() throws IOException {
        // read the first line
        String headerRow = "";
        br = new BufferedReader(new FileReader(fileName));
        String str = br.readLine();
        if (str != null) {
            headerRow = str;
        }
        String[] HeaderArgs = headerRow.split(",");
        br.close();

        // populate the header object with the String array containing the header names
        Header hd = new Header(HeaderArgs);
        return hd;
    }

    /**
     * This method will be used in the upcoming assignments
     */
    @Override
    public void getDataRow() {

    }

    /*
     * implementation of getColumnType() method. To find out the data types, we will
     * read the first line from the file and extract the field values from it. In
     * the previous assignment, we have tried to convert a specific field value to
     * Integer or Double. However, in this assignment, we are going to use Regular
     * Expression to find the appropriate data type of a field. Integers: should
     * contain only digits without decimal point Double: should contain digits as
     * well as decimal point Date: Dates can be written in many formats in the CSV
     * file. However, in this assignment,we will test for the following date
     * formats('dd/mm/yyyy',
     * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm
     * -dd')
     */
    @Override
    public DataTypeDefinitions getColumnType() throws IOException {
		Object obj;
        int i = 0;
        String type = "";
        br = new BufferedReader(new FileReader(fileName));
        String headerRow = br.readLine();
        String headerArgs[] = headerRow.split(",");
        String secondRow = br.readLine();
        secondRow += " ,";
        String[] dataElements = secondRow.split(",");
        String[] dataTypes = new String[headerArgs.length];
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");

           for (i = 0; i < dataElements.length; i++) {
               if (dataElements[i] != null) {
                   if (dataElements[i].matches("\\d+")) {
                       Integer in = Integer.parseInt(dataElements[i]);
                       dataTypes[i] = in.getClass().getName();
                   }
                   // checking for date format
                   else if (dataElements[i].matches("\\d{4}-\\d{2}-\\d{2}")) {
                       try {
                           Date dateType = df.parse(dataElements[i]);
                           dataTypes[i] = dateType.getClass().getName();
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   } // check for last element
                   else if (dataElements[i].matches(" ")) {
                       dataTypes[i] = dataElements[i].getClass().getSuperclass().getName();
                   } else {
                       dataTypes[i] = dataElements[i].getClass().getName();
                   }
               } else {
                   dataTypes[i] = dataElements[i].getClass().getName();
               }
           }

        br.close();
        DataTypeDefinitions types = new DataTypeDefinitions(dataTypes);
        return types;

}



}

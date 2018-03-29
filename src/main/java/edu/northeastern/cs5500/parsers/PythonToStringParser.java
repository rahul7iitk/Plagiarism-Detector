package edu.northeastern.cs5500.parsers;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Praveen Singh
 */
@Component
public class PythonToStringParser {
    private static final Logger LOGGER = LogManager.getLogger(PythonToStringParser.class);

    /**
     * Method to read file and return content as a string for comparison.
     * @param filePath Path of File in string.
     * @return Returns the file content as a String.
     */
    public String readFile(String filePath){
    	/**
    	 * Replacing all %20 by spaces for compatibility with Windows file paths
    	 */
    	filePath = filePath.replaceAll("%20", " ");
        LOGGER.info("Reading file {}", filePath);
        StringBuilder contentBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().length() > 0)
                    contentBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            LOGGER.error("Exception in reading files : {}", ex.getMessage());
        }
        return contentBuilder.toString();
    }

    /**
     * Method to read zip file and return list of strings each with each .py file's content for comparison.
     * @param filePath Path of Zip File in string.
     * @return Returns the List of files' contents as a String List.
     */
    public List<String> parseFiles(String filePath) {
    	/**
    	 * Replacing all %20 by spaces for compatibility with Windows file paths
    	 */
    	filePath = filePath.replaceAll("%20", " ");
        LOGGER.info("Reading file {}", filePath);
        List<String> stringFiles = new ArrayList<String>();
        try (ZipFile fis = new ZipFile(filePath)) {
            for (Enumeration e = fis.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                InputStream in = fis.getInputStream(entry);
                String str = IOUtils.toString(in).trim();
                stringFiles.add(str);
            }
        }
        catch (IOException ex) {
            LOGGER.error("Exception in reading files : {}", ex.getMessage());
        }
        return stringFiles;
    }

}

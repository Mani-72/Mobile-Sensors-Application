package com.example.mobilesensorapplication.OperationalClasses;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class OpenCSVWriter {

    public static final char CSV_SEPARATOR = ';';



    public static <T> void writeFromListOfObjects(List<T> ObjectList, String OBJECT_PATH_SAMPLE, Context context) throws IOException,
            CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {

        String prePath = context.getExternalFilesDir(null).getAbsolutePath()+"/Exercise/edata/data/SensorApplicationDownloadResults";

        OBJECT_PATH_SAMPLE= OBJECT_PATH_SAMPLE.replaceAll("[\\\\/:*?\"<>|]", "");
        OBJECT_PATH_SAMPLE= OBJECT_PATH_SAMPLE.replaceAll("\\s+", "");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            checkForExternalDirectry(new File(prePath));

            try (
                    Writer writer = Files.newBufferedWriter(Paths.get(prePath + "/" + OBJECT_PATH_SAMPLE));
            ) {
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withSeparator(CSV_SEPARATOR)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
                        .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                        .build();

                beanToCsv.write(ObjectList);

            //    Toast.makeText(context, "Data Saved Successfully to /Android/edata/data/SensorApplicationDownloadResults Folder!!!", Toast.LENGTH_SHORT).show();


            } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error in Saving Data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*20200713: Make new Directry of it does not exist in android memory*/
    private static void checkForExternalDirectry(File file) {

        File folder = file;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }

    }




}

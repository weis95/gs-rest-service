package com.example.restservice;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PointController {

    @CrossOrigin
    @GetMapping("point")
    public List<Point> convertCSV() throws IOException, CsvValidationException {
        List<Point> points = new ArrayList<>();
        File resource = new ClassPathResource("./properties_f.csv").getFile();

        try (CSVReader csvReader = new CSVReader( new FileReader(resource.getAbsolutePath())) ) {
            String[] values = null;

            while (( values = csvReader.readNext() ) != null) {
                Point point = new Point();
                // Regex works, but isn't best practice nor pretty.
                String pointIndex = values[0].split(";")[0];
                if (pointIndex.contains("POINT")) {
                    String price = values[0].split(";")[1];
                    String type = values[0].split(";")[2];
                    String parking = null;
                    if (values[0].split(";").length > 3){
                        parking = values[0].split(";")[3];
                    }

                    // Extremely beautiful conversion of the point() "cough". Worst practice for sure.
                    String coords[] = pointIndex.substring(pointIndex.indexOf("(")+1, pointIndex.indexOf(")")).split(" ");
                    String latitude = coords[0];
                    Double lat = Double.parseDouble(latitude);
                    String longitude = coords[1];
                    Double lng = Double.parseDouble(longitude);

                    point.setLat(lat);
                    point.setLng(lng);
                    point.setPrice(Double.parseDouble(price));
                    point.setType(type);
                    point.setParking(parking);

                    points.add(point);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }
}

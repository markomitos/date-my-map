package com.ftn.sbnz.kjar.generator;

import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RuleGenerator {
//    public static void main(String[] args) {
//        generateRules();
//    }

    public static void generateRules(){
        InputStream templateStream = RuleGenerator.class.getResourceAsStream("/rules/template/template.drt");
        InputStream dataStream = RuleGenerator.class.getResourceAsStream("/data/data.csv");

        List<String[]> dataRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(dataStream))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                dataRows.add(line.split(","));
            }
        } catch (IOException e) {

        }

        DataProvider dataProvider = new ArrayDataProvider(dataRows.toArray(new String[0][]));
        DataProviderCompiler compiler = new DataProviderCompiler();
        String generatedDrl = compiler.compile(dataProvider, templateStream);

        URL resource = RuleGenerator.class.getResource("/");
        if (resource == null) {
            throw new RuntimeException("Cannot find classpath root.");
        }
        File classesDir = null;
        try {
            classesDir = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Path projectDir = classesDir.toPath().getParent().getParent();

        Path outputPath = projectDir.resolve(Paths.get("src", "main", "resources", "rules", "generated-rules.drl"));

        File outputFile = outputPath.toFile();
        outputFile.getParentFile().mkdirs();

        try (Writer writer = new FileWriter(outputFile)) {
            writer.write(generatedDrl);
        }catch (IOException e){}
        System.out.println("Successfully generated rules DRL file.");
    }
}
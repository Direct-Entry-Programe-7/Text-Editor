/*
 *  Copyright (c) Nethmina Senarathne. All rights reserved.
 *
 *   Licensed under the MIT License. See License.txt in the project root for license
 *   information.
 *
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.nashorn.internal.ir.CallNode;

import java.io.*;
import java.util.Properties;

public class AppInitializer extends Application {

    private Properties prop = new Properties();
    private File propFile = new File("application.properties");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/EditorForm.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text Editor");
        loadProperties();

        double xPos;
        double yPos;
        try {
            xPos = Double.parseDouble(prop.getProperty("xPos","-1"));
            yPos = Double.parseDouble(prop.getProperty("yPos","-1"));
        } catch (NumberFormatException e) {
            xPos = -1;
            yPos =-1;
        }

        double width;
        double height;
        try {
            width = Double.parseDouble(prop.getProperty("width","-1"));
            height = Double.parseDouble(prop.getProperty("height","-1"));
        } catch (NumberFormatException e) {
            width = -1;
            height = -1;
        }


        if (width == -1 && height == -1){
            primaryStage.centerOnScreen();
        }else{
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
        }

        if (xPos == -1 && yPos == -1){
            primaryStage.centerOnScreen();
        }else{
            primaryStage.setX(xPos);
            primaryStage.setX(yPos);
        }
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            prop.put("xPos", primaryStage.getX() + "");
            prop.put("yPos", primaryStage.getY() + "");

            if(!primaryStage.isMaximized()) {
                prop.put("width", primaryStage.getWidth() + "");
                prop.put("height", primaryStage.getHeight() + "");
            }else{
                prop.put("width", "-1");
                prop.put("height", "-1");
            }

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(propFile))) {
                prop.store(bos,null);
            }catch (IOException e){
                e.printStackTrace();
            }


        });
    }

    private void loadProperties(){


        if (!propFile.exists()) return;

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propFile))){
            prop.load(bis);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

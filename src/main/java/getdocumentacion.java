
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author AsusPC
 */
public class getdocumentacion {

    public static void main(String[] args) {
        String file=args[0];
        //String file = "\\Users\\AsusPC\\Downloads\\requerimiento.bpmn";
        analizador app = new analizador();
        app.read(file);
        String body = app.generate();
        FileOutputStream archivo;
        PrintStream p;
        try {
            archivo = new FileOutputStream("processdoc.html");
            p = new PrintStream(archivo);
            p.println(body);
            p.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

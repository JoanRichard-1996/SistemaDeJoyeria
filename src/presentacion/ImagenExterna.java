package presentacion;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class ImagenExterna extends javax.swing.JPanel {
// static ArrayList<String> ruta = new ArrayList<String>();
    String ruta;

    public ImagenExterna(int x, int y, String ruta) {
        this.setSize(x, y); //se selecciona el tamaño del panel
        this.ruta=ruta;
       ruta="";
    }

//Se crea un método cuyo parámetro debe ser un objeto Graphics
    public void paint(Graphics grafico) {
        Dimension height = getSize();
        System.out.println(ruta);
        
//Se selecciona la imagen que tenemos en el paquete de la //ruta del programa
        Image imagenExterna = new ImageIcon(ruta).getImage();

//se dibuja la imagen que tenemos en el paquete Images //dentro de un panel
        grafico.drawImage(imagenExterna, 0, 0, height.width, height.height, null);

        setOpaque(false);
        super.paintComponent(grafico);
       
       ruta="";
       
        
    }
}

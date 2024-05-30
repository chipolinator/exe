import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

import com.jogamp.opengl.awt.GLCanvas;

import javax.imageio.ImageIO;

public class Main implements Runnable, KeyListener {

    public static ByteBuffer pixels; // "массив" пикселей
    public static int widthTexture; // ширина текстуры
    public static int heightTexture; // высота текстуры
    private static final double ROTATE_SPEED = 0.9;
    private static Thread displayT = new Thread(new Main());
    private static float ROTATION_SPEED = 0.01f;
    private static float ZOOM_SPEED = 1.05f;
    private static boolean bQuit = false;
    public static float rotationX = 0f;
    public static float rotationY = 0f;
    public static float rotationZ = 0f;
    public static float zoomChange = 1.0f;
    public static boolean wireFrame = false;
    ///////////////////////////////////////////////////////////////
    public static Vector3 POS = new Vector3(0, 0, 5);
    public static Vector3 DIR = new Vector3(0, 0, -5);
    public static Vector3 At = new Vector3(0, 0, 0);
    public static Vector3 UP = new Vector3(0, 1, 0);
    public static Vector3 POSp = new Vector3(1, 1, 0);
    public static Vector3 DIRp = new Vector3(5, 0, 0);
    public static Vector3 Atp = new Vector3(0, 0, 0);
    public static Vector3 UPp = new Vector3(0, 0, 1);

    public double speed = 0.01;
    public static int xM = 0;
    public static int yM = 0;
    public static double zM = 0;

    private static BufferedImage image;

    //public static double[][] zet;
    public static double[][] H;
    ///////////////////////////////////////////////////////////////


    public static void main(String[] args) throws FileNotFoundException {

        Scanner f = new Scanner(new File("C:\\Users\\hp\\Downloads\\H (1).txt"));
        String s = f.nextLine();
        int wH = Integer.parseInt(s);
        s = f.nextLine();
        int hH = Integer.parseInt(s);
        H = new double[wH][hH];
        for (int y = 0; y < hH; y++) {
            for (int x = 0; x < wH; x++) {
                s = f.nextLine();
                H[x][y] = Double.parseDouble(s);
                //System.out.println(y + " " + x + " " + H[x][y]);
            }
        }
        f.close();

//        zet = new double[129][129];
//        try {
//            image = ImageIO.read(new File("C:\\1\\map.bmp"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        WritableRaster rr = image.getRaster();
//
//        double[] pixel = new double[3];
//        for (int x = 0; x < image.getWidth(); x++) {
//            for (int y = 0; y < image.getHeight(); y++) {
//                rr.getPixel(x, y, pixel);
//                zet[x][y] = pixel[0] / 10.;//????????? ?????? ? 10 ???
//            }
//        }



        displayT.start();

        try {
            image = ImageIO.read(new File("C:\\Users\\hp\\Downloads\\map3.bmp"));
            widthTexture = image.getWidth(); // ширина текстуры
            heightTexture = image.getHeight(); // высота текстуры
            // извлечение пикселей из считанного изображения
            DataBufferByte dataBufferByte =
                    (DataBufferByte) image.getData().getDataBuffer();
            // приведение их к подходящему внутреннему виду
            pixels = ByteBuffer.wrap(dataBufferByte.getData());
            byte r,b; // временные переменные – яркость синего и красного
            // перебираем все пиксели изображения
            for (int i = 0 ; i < heightTexture * widthTexture ; i++) {
                // меняем местами синюю и красную компоненты
                b = pixels.get(3 * i);
                r = pixels.get(3 * i + 2);
                pixels.put(3 * i, r);
                pixels.put(3 * i + 2, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Frame frame = new Frame("Jogl 3D Shape/Rotation");
        frame.setLocation(0, 0);
        GLCanvas canvas = new GLCanvas();
        int size = frame.getExtendedState();

        canvas.addGLEventListener(new JavaRenderer());
        frame.add(canvas);
        frame.setUndecorated(true);
        size |= Frame.MAXIMIZED_BOTH;
        frame.setExtendedState(size);
        canvas.addKeyListener(this);
        frame.pack();
        //frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                bQuit = true;
                System.exit(0);
            }
        });

        frame.setVisible(true);
        canvas.requestFocus();
        while (!bQuit) {
            canvas.display();
        }
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            displayT = null;
            bQuit = true;
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            POS = POS.plus(DIR.x(speed).x(4));
            At = POS.plus(DIR);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            POS = POS.minus(DIR.x(speed).x(4));
            At = POS.plus(DIR);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            DIR = Matrix3x3.mRot(UP, -0.01).x(DIR.x(speed).norm());
            At = POS.plus(DIR);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            DIR = Matrix3x3.mRot(UP, 0.01).x(DIR.x(speed)).norm();
            At = POS.plus(DIR);
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            UP = Matrix3x3.mRot(DIR, 0.01).x(UP).x(speed).norm();
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            UP = Matrix3x3.mRot(DIR, -0.01).x(UP).x(speed).norm();
        }
        if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            Matrix3x3 m = Matrix3x3.mRot(DIR.x(UP), 0.01);
            UP = m.x(UP).norm();
            DIR = m.x(DIR).norm();
            At = POS.plus(DIR);
        }
        if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
            Matrix3x3 m = Matrix3x3.mRot(DIR.x(UP), -0.01);
            UP = m.x(UP).norm();
            DIR = m.x(DIR).norm();
            At = POS.plus(DIR);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            wireFrame = !wireFrame;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
//            yM += 1;//0.1 ????????*?????(??????) static ???? 0.1
//            zM = zet[xM][yM];//getZet+?????? 1 ??? ???????? ???????
            POSp = POSp.plus(DIRp.x(speed).x(4));
            Atp = POSp.plus(DIRp);
            zM = getZ(POSp.x, POSp.y);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//            yM -= 1;
//            if (yM<0)
//                yM=0;
//            zM = zet[xM][yM];
            POSp = POSp.minus(DIRp.x(speed).x(4));
            Atp = POSp.plus(DIRp);
            zM = getZ(POSp.x, POSp.y);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//            xM += 1;
//            zM = zet[xM][yM];
            DIRp = Matrix3x3.mRot(UPp, 0.2).x(DIRp.x(speed).norm());
            Atp = POSp.plus(DIRp);
            zM = getZ(POSp.x, POSp.y);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//            xM -= 1;
//            if (xM<0)
//                xM=0;
//            zM = zet[xM][yM];
            DIRp = Matrix3x3.mRot(UPp, -0.2).x(DIRp.x(speed).norm());
            Atp = POSp.plus(DIRp);
            zM = getZ(POSp.x, POSp.y);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
            rotationX = 0f;
        }
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
            rotationY = 0f;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_E) {
            rotationZ = 0f;
        }
        if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            zoomChange = 1.0f;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public static double getZ(double xZ, double yZ) {

        int x = (int) Math.floor(xZ);
        int y = (int) Math.floor(yZ);
        double manX = xZ - x;
        double manY = yZ - y;
        double z1 = H[x][y];
        Vector3 a = new Vector3(x, y, z1);
        Vector3 b = null;
        Vector3 c = null;

        if ((x % 2 == 0) && (y % 2) == 0) {
            a = new Vector3(x + 1, y + 1, H[x + 1][y + 1]);
            if (manX > manY) {
                b = new Vector3(x, y, H[x][y]);
                c = new Vector3(x, y + 1, H[x][y + 1]);
            } else {
                b = new Vector3(x + 1, y, H[x + 1][y]);
                c = new Vector3(x, y, H[x][y]);
            }
        }
        if ((x % 2 == 1) && (y % 2) == 1) {
            a = new Vector3(x, y, H[x][y]);
            if (manX > manY) {
                b = new Vector3(x + 1, y + 1, H[x + 1][y + 1]);
                c = new Vector3(x + 1, y, H[x + 1][y]);
            } else {
                b = new Vector3(x, y + 1, H[x][y + 1]);
                c = new Vector3(x + 1, y + 1, H[x + 1][y + 1]);
            }
        }
        if ((x % 2 == 0) && (y % 2) == 1) {
            a = new Vector3(x + 1, y, H[x + 1][y]);
            if (manY > (1 - manX)) {
                b = new Vector3(x, y + 1, H[x][y + 1]);
                c = new Vector3(x + 1, y + 1, H[x + 1][y + 1]);

            } else {
                b = new Vector3(x, y, H[x][y]);
                c = new Vector3(x, y + 1, H[x][y + 1]);
            }
        }
        if ((x % 2 == 1) && (y % 2) == 0) {
            a = new Vector3(x, y + 1, H[x][y + 1]);
            if (manY > (1 - manX)) {
                b = new Vector3(x + 1, y + 1, H[x + 1][y + 1]);
                c = new Vector3(x + 1, y, H[x + 1][y]);
            } else {
                b = new Vector3(x + 1, y, H[x + 1][y]);
                c = new Vector3(x, y, H[x][y]);
            }
        }

        Vector3 v12 = b.minus(a);
        Vector3 v13 = c.minus(a);

        Vector3 n = v12.x(v13);

        double d = -1 * (n.dot(a));

        double z = -1 * ((n.x * xZ + n.y * yZ + d) / n.z);

        return z;
    }


}
/*
//import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class Main {
    public static void main(String args[]) {
        // ????? ?????.
        JFrame frame = new JFrame("MyWindow");
        // ??????? ????.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ????? ?? ?????????.
        frame.add(new GLCanvas());
        // ????????? GLCanvas ?? ????.

        frame.setSize(800, 600);
        // ????????????? ?????? ????.
        frame.setVisible(true);
        // ???????? ? ?????? ??????? ????.
    }
}
*/
/*public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
    }
}*/
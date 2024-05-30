import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class JavaRenderer implements GLEventListener {
    private BufferedImage image;
    private float angleX = 0.0f;
    private float angleY = 0.0f;
    private float angleZ = 0.0f;
    private float zoom = 1.0f;
    private static final GLU glu = new GLU();


    public void sfera(GL2 gl) {
        double radius = 0.2;
        int slices = 20;
        int stacks = 10;

        for (int i = -4; i <= stacks / 2; i++) {
            double corner1 = 18.0 * (i - 1);
            double z0 = Math.sin(Math.toRadians(corner1));
            double zr0 = Math.cos(Math.toRadians(corner1));

            double corner2 = 18.0 * i;
            double z1 = Math.sin(Math.toRadians(corner2));
            double zr1 = Math.cos(Math.toRadians(corner2));

            gl.glBegin(GL2.GL_QUAD_STRIP);

            for (int j = 0; j <= slices; j++) {
                double corner = 18 * j;
                double x = Math.cos(Math.toRadians(corner));
                double y = Math.sin(Math.toRadians(corner));

                //gl.glColor3d(0, 0, 1);

                gl.glVertex3d(radius * x * zr0 + Main.POSp.x, radius * y * zr0 + Main.POSp.y, radius * z0 + Main.zM + radius);
                gl.glVertex3d(radius * x * zr1 + Main.POSp.x, radius * y * zr1 + Main.POSp.y, radius * z1 + Main.zM + radius);
                //System.out.println(Main.zM);
            }
            gl.glEnd();
        }
    }


    public void landscape(GL2  gl) {

        double dx = 0.03125;
        double dy = dx;

//        for (int x = 1; x < 129; x += 2) {
//            //System.out.println(dy);
//            for (int y = 1; y < 129; y += 2) {
//                gl.glBegin(GL.GL_TRIANGLE_FAN);
//                //gl.glColor3d(0, 0, 1);
//                gl.glTexCoord2d(0.5,0.5); gl.glVertex3d(x, y, Main.H[x][y]);
//
//                //gl.glColor3d(0,1,1);
//                gl.glTexCoord2d(0,0); gl.glVertex3d(x - 1, y - 1, Main.H[x - 1][y - 1]);
//
//                gl.glTexCoord2d(0.5,0); gl.glVertex3d(x, y - 1, Main.H[x][y - 1]);
//
//                gl.glTexCoord2d(1,0); gl.glVertex3d(x + 1, y - 1, Main.H[x + 1][y - 1]);
//                gl.glTexCoord2d(1,0.5); gl.glVertex3d(x + 1, y, Main.H[x + 1][y]);
//
//                gl.glTexCoord2d(1,1);gl.glVertex3d(x + 1, y + 1, Main.H[x + 1][y + 1]);
//                gl.glTexCoord2d(0.5,1); gl.glVertex3d(x, y + 1, Main.H[x][y + 1]);
//
//                gl.glTexCoord2d(0,1); gl.glVertex3d(x - 1, y + 1, Main.H[x - 1][y + 1]);
//                gl.glTexCoord2d(0,0.5); gl.glVertex3d(x - 1, y, Main.H[x - 1][y]);
//                gl.glTexCoord2d(0,0); gl.glVertex3d(x - 1, y - 1, Main.H[x - 1][y - 1]);
//
//                gl.glEnd();
//            }
//        }

        for (int x = 1; x < 129; x += 2) {
            for (int y = 1; y < 129; y += 2) {
                gl.glBegin(GL.GL_TRIANGLE_FAN);
                //gl.glColor3d(1, 1, 1);
                //System.out.println("de="+dx);
                //System.out.println(dy * y % 32);
                gl.glTexCoord2d(dx * x % 32,dy * y % 32); gl.glVertex3d(x, y, Main.H[x][y]);

                //gl.glColor3d(0,1,1);
                gl.glTexCoord2d(dx * (x-1) % 32,dy * (y-1) % 32); gl.glVertex3d(x - 1, y - 1, Main.H[x - 1][y - 1]);

                gl.glTexCoord2d(dx * x % 32,dy * (y-1) % 32); gl.glVertex3d(x, y - 1, Main.H[x][y - 1]);

                gl.glTexCoord2d(dx * (x+1) % 32,dy * (y-1) % 32); gl.glVertex3d(x + 1, y - 1, Main.H[x + 1][y - 1]);
                gl.glTexCoord2d(dx * (x+1) % 32,dy * y % 32); gl.glVertex3d(x + 1, y, Main.H[x + 1][y]);

                gl.glTexCoord2d(dx * (x+1) % 32,dy * (y+1) % 32);gl.glVertex3d(x + 1, y + 1, Main.H[x + 1][y + 1]);
                gl.glTexCoord2d(dx * x % 32,dy * (y+1) % 32); gl.glVertex3d(x, y + 1, Main.H[x][y + 1]);

                gl.glTexCoord2d(dx * (x-1) % 32,dy * (y+1) % 32); gl.glVertex3d(x - 1, y + 1, Main.H[x - 1][y + 1]);
                gl.glTexCoord2d(dx * (x-1) % 32,dy * y % 32); gl.glVertex3d(x - 1, y, Main.H[x - 1][y]);
                gl.glTexCoord2d(dx * (x-1) % 32,dy * (y-1) % 32); gl.glVertex3d(x - 1, y - 1, Main.H[x - 1][y - 1]);

                gl.glEnd();
            }
        }

//        for (int x = 0; x < 128; x++) {
//            for (int y = 0; y < 128; y++) {
//                gl.glBegin(GL.GL_TRIANGLE_FAN);
//
//                gl.glTexCoord2d(x/128.0, y/128.0);
//                gl.glVertex3d(x, y, Main.zet[x][y]);
//
//                gl.glTexCoord2d((x+1)/128.0, y/128.0);
//                gl.glVertex3d(x+1, y, Main.zet[x+1][y]);
//
//                gl.glTexCoord2d((x+1)/128.0, (y+1)/128.0);
//                gl.glVertex3d(x+1, y+1, Main.zet[x+1][y+1]);
//
//                gl.glTexCoord2d(x/128.0, (y+1)/128.0);
//                gl.glVertex3d(x, y+1, Main.zet[x][y+1]);
//
//                gl.glEnd();
//            }
//        }
//        for (int x = 0; x < 128; x += 1) {
//            for (int y = 0; y < 128; y += 1) {
//                gl.glBegin(GL.GL_TRIANGLE_FAN);
//                //gl.glColor3d(1, 1, 1);
//                gl.glTexCoord2d(0,0); gl.glVertex3d(x, y, zet[x][y]);
//                gl.glTexCoord2d(1,0); gl.glVertex3d(x+1, y, zet[x+1][y]);
//                gl.glTexCoord2d(1,1); gl.glVertex3d(x+1, y+1, zet[x+1][y+1]);
//                gl.glTexCoord2d(0,1); gl.glVertex3d(x, y+1, zet[x][y+1]);
//
//                gl.glEnd();
//            }
//        }
//        gl.glBegin(GL.GL_TRIANGLE_FAN);
//        //gl.glColor3d(1, 1, 1);
//        gl.glTexCoord2d(0,0); gl.glVertex3d(0, 0, zet[0][0]);
//        gl.glTexCoord2d(1,0); gl.glVertex3d(128, 0, zet[128][0]);
//        gl.glTexCoord2d(1,1); gl.glVertex3d(128, 128, zet[128][128]);
//        gl.glTexCoord2d(0,1); gl.glVertex3d(0, 128, zet[0][128]);
//
//        gl.glEnd();
    }
//
//    public void line(GL2 gl) {
//        gl.glBegin(GL2.GL_LINES);
//        gl.glColor3d(0, 0, 1);
//        gl.glVertex3d(1, 1, Main.getZ(1,1));
//        gl.glVertex3d(11.3,20.2, Main.getZ(11.3, 20.2));
//
//        gl.glEnd();
//    }


    public void display(GLAutoDrawable gLDrawable) {

        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        // gl.glTranslatef(-64, 0, -5);

        if (Main.wireFrame)
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
        else
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);

        angleX += Main.rotationX;
        angleY += Main.rotationY;
        angleZ += Main.rotationZ;
        zoom *= Main.zoomChange;
        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f);
        gl.glScalef(zoom, zoom, zoom);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        glu.gluLookAt(Main.POS.x, Main.POS.y, Main.POS.z, Main.At.x, Main.At.y, Main.At.z, Main.UP.x, Main.UP.y, Main.UP.z);
        //line(gl);
        landscape(gl);

        sfera(gl);


    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void init(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

        int[] textureId = new int[1]; // создаем массив для хранения номера текстуры
        gl.glGenTextures(1, textureId, 0); // Получаем свободный ID текстуры
        gl.glEnable(GL.GL_TEXTURE_2D); // Разрешаем текстурирование
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]); // выбираем ID текстуры
// устанавливаем параметры выбора пискеля при увеличении/уменьшении текстуры
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
// загружаем пиксели текстуры в текущий выбранный ID текстуры
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, Main.widthTexture,
                Main.heightTexture, 0,GL.GL_RGB, GL.GL_UNSIGNED_BYTE, Main.pixels);

//        gl.glDisable(GL.GL_TEXTURE_2D);
//        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    public void reshape(GLAutoDrawable gLDrawable, int x,
                        int y, int width, int height) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 1.0, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        //gl.glLoadIdentity();
    }

    public void dispose(GLAutoDrawable arg0) {

    }
}

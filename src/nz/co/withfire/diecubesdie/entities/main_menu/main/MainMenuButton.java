/****************************\
| A button on the main menu. |
|                            |
| @author David Saxon       |
\****************************/

package nz.co.withfire.diecubesdie.entities.main_menu.main;

import android.opengl.Matrix;
import nz.co.withfire.diecubesdie.entities.Drawable;
import nz.co.withfire.diecubesdie.entities.Entity;
import nz.co.withfire.diecubesdie.renderer.shapes.Shape;
import nz.co.withfire.diecubesdie.utilities.vectors.Vector2d;

public class MainMenuButton extends Drawable implements Entity {

    //VARIABLES
    //the shape of the button
    private Shape button;
    
    //the position of the button
    private Vector2d pos = new Vector2d();
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the transformation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    /**Creates a new main menu button
    @param button the shape of the button
    @param pos the position of the button*/
    public MainMenuButton(Shape button, Vector2d pos) {
        
        this.button = button;
        this.pos.copy(pos);
    }
    
    //PUBLIC METHODS
    @Override
    public void update() {

        
    }

    @Override
    public void draw(float viewMatrix[], float projectionMatrix[]) {
        
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, pos.getX(), pos.getY(), 0);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, tMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        //draw the cube
        button.draw(mvpMatrix);
    }
}

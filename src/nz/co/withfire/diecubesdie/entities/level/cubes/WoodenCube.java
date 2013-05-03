/*********************************************************\
| A wooden cube, the most basic and weakest type if cube. |
|                                                         |
| @author David Saxon                                    |
\*********************************************************/

package nz.co.withfire.diecubesdie.entities.level.cubes;

import android.opengl.Matrix;
import nz.co.withfire.diecubesdie.renderer.shapes.Shape;
import nz.co.withfire.diecubesdie.utilities.vectors.Vector2d;

public class WoodenCube extends Cube {

    //VARIABLES
    
    //TESTING
    private Vector2d pos = new Vector2d();
    private float rot = 0.0f;
    private float initRot = 0.0f;
    
    //the shape for the cube
    private Shape cube;
    
    //Matrix
    //the model view projection matrix
    private float[] mvpMatrix = new float[16];
    //the translation matrix
    private float[] tMatrix = new float[16];
    
    //CONSTRUCTOR
    /**Creates a new wooden cube
    @param cube the shape for the cube*/
    public WoodenCube(Shape cube) {
        
        this.cube = cube;
    }
    
    //PUBLIC MEMBER FUNCTIONS
    @Override
    public void update() {
        
        //TESTING
        //pos.setX(pos.getX() - 0.01f);
        
        if (rot < 89.5) {
        
            rot += 2.0f;
        }
        else {
            
            rot = 0;
            pos.setX(pos.getX() - 1);
            initRot += 90;
        }
        
        //TODO:
    }
    
    @Override
    public void draw(float viewMatrix[], float projectionMatrix[]) {
        
        //shift to position
        Matrix.setIdentityM(tMatrix, 0);
        Matrix.translateM(tMatrix, 0, 2 * pos.getX(), 0, 2 * pos.getY());
        Matrix.translateM(tMatrix, 0, -1, -1, 0);
        Matrix.rotateM(tMatrix, 0, rot, 0, 0, 1);
        Matrix.translateM(tMatrix, 0, 1, 1, 0);
        Matrix.rotateM(tMatrix, 0, initRot, 0, 0, 1);
        
        //Matrix.rotateM(tMatrix, 0, rot, 0, 0, 1);
        
        //multiply the matrix
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, tMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);
        
        //draw the cube
        cube.draw(mvpMatrix);
    }
}
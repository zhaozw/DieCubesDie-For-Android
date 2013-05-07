package nz.co.withfire.diecubesdie.engine.level;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.Matrix;

import nz.co.withfire.diecubesdie.engine.Engine;
import nz.co.withfire.diecubesdie.entities.Drawable;
import nz.co.withfire.diecubesdie.entities.Entity;
import nz.co.withfire.diecubesdie.entities.level.cubes.WoodenCube;
import nz.co.withfire.diecubesdie.entities.level.terrian.Ground;
import nz.co.withfire.diecubesdie.fps_manager.FpsManager;
import nz.co.withfire.diecubesdie.resources.ResourceManager;
import nz.co.withfire.diecubesdie.resources.ResourceManager.ResourceGroup;
import nz.co.withfire.diecubesdie.utilities.vectors.Vector2d;

public class LevelEngine implements Engine {

    //VARIABLES
    //the android context
    private final Context context;
    
    //the resource manager
    private ResourceManager resources;
    
    //is true once the level is complete
    private boolean complete = false;
    
    //The list of all entities
    private List<Entity> entities = new ArrayList<Entity>();
    //subset of entites that contains the drawables
    private List<Drawable> drawables = new ArrayList<Drawable>();
    
    //TESTING
    private float followCam = 0.0f;
    
    //CONSTRUCTOR
    /**!Constructs a new Level engine
    @param context the android context
    @param resources the resource manager to use*/
    public LevelEngine(Context context, ResourceManager resources) {
        
        //set the variables
        this.context = context;
        this.resources = resources;
    }
    
    //PUBLIC METHODS
    @Override
    public void init() {

        resources.loadTexturesFromGroup(ResourceGroup.LEVEL);
        resources.loadShapesFromGroup(ResourceGroup.LEVEL);
        
        //TESTING
        //add a wooden cube
        WoodenCube testWoodenCube = new WoodenCube(
            resources.getShape("wooden_cube"));
        entities.add(testWoodenCube);
        drawables.add(testWoodenCube);
        
        //add ground
        for (int i = -20; i < 4; ++i) {
            for (int j = -1; j < 4; ++j) {
                
                Vector2d gPos = new Vector2d(i, j);
                Ground g = new Ground(gPos,
                    resources.getShape("plains_grass_tile"));
                //entities.add(g);
                drawables.add(g);
            }
        }
    }

    @Override
    public boolean execute() {
        
        //TESTING
        followCam += (0.0445f * FpsManager.getTimeScale());
        
        //update the entities
        for (Entity e : entities) {
            
            e.update();
        }

        return complete;
    }
    
    @Override
    public void applyCamera(float[] viewMatrix) {

        //TESTING
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(viewMatrix, 0, 0.0f, 0.0f, 6.0f);
        Matrix.rotateM(viewMatrix, 0, 70, -1.0f, 0, 0.0f);
        Matrix.rotateM(viewMatrix, 0, 0, 0, 1.0f, 0.0f);
        
       // Matrix.translateM(viewMatrix, 0, 0, -20.0f, 20.0f);
        Matrix.translateM(viewMatrix, 0, followCam, 0.0f, 0.0f);
    }   

    @Override
    public List<Drawable> getDrawables() {

        return drawables;
    }

    @Override
    public Engine nextState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean shouldExit() {
        // TODO Auto-generated method stub
        return false;
    }

}

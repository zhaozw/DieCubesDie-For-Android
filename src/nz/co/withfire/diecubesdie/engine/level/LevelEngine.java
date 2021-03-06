package nz.co.withfire.diecubesdie.engine.level;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import nz.co.withfire.diecubesdie.engine.Engine;
import nz.co.withfire.diecubesdie.entities.Entity;
import nz.co.withfire.diecubesdie.entities.gui.Overlay;
import nz.co.withfire.diecubesdie.entities.level.cubes.PaperCube;
import nz.co.withfire.diecubesdie.entities.level.terrian.Ground;
import nz.co.withfire.diecubesdie.entity_list.EntityList;
import nz.co.withfire.diecubesdie.fps_manager.FpsManager;
import nz.co.withfire.diecubesdie.gesture.GestureWatcher;
import nz.co.withfire.diecubesdie.gesture.gestures.Gesture;
import nz.co.withfire.diecubesdie.gesture.gestures.Pinch;
import nz.co.withfire.diecubesdie.gesture.gestures.Swipe;
import nz.co.withfire.diecubesdie.gesture.gestures.Tap;
import nz.co.withfire.diecubesdie.renderer.GLRenderer;
import nz.co.withfire.diecubesdie.resources.ResourceManager;
import nz.co.withfire.diecubesdie.resources.ResourceManager.ResourceGroup;
import nz.co.withfire.diecubesdie.utilities.ValuesUtil;
import nz.co.withfire.diecubesdie.utilities.vectors.Vector2d;
import nz.co.withfire.diecubesdie.utilities.vectors.Vector3d;
import nz.co.withfire.diecubesdie.utilities.vectors.Vector4d;

public class LevelEngine implements Engine {

    //VARIABLES
    //the android context
    private final Context context;
    
    //the resource manager
    private ResourceManager resources;
    
    //The list of all entities
    private EntityList entities = new EntityList();
    
    //the gesture watcher
    private GestureWatcher gestureWatcher = new GestureWatcher();
    
    //the map of terrain entities
    private Entity entityMap[][][];
    
    //is true once the level is complete
    private boolean complete = false;
    
    //gesture
    //multiplies the distance moved by swiping
    private final float CAM_MOVE_MULTIPLY = 2.0f;
    //the last swipe position
    private Vector2d lastSwipe = null;
    //if there was a pinch last frame
    private boolean pinchLast = false;
    //multiplies the zoom
    private final float CAM_ZOOM_MULTIPLY = 5.0f;
    //the pinch distance
    private float pinchDis = 0.0f;
    //the pinch angle
    private float pinchAngle = 0.0f;
    
    //the camera position
    private Vector3d camPos = new Vector3d(0.0f, 0.0f, 0.0f);
    //the camera rotation
    private Vector2d camRot = new Vector2d(65.0f, 0.0f);
    
    //CONSTRUCTOR
    /**!Constructs a new Level engine
    @param context the android context
    @param resources the resource manager to use
    @param entityMap the entityMap for the terrain
    @param ground the ground*/
    public LevelEngine(Context context, ResourceManager resources,
        Entity entityMap[][][], Ground ground) {
        
        //set the variables
        this.context = context;
        this.resources = resources;
        this.entityMap = entityMap;
        
        //add the terrain entities
        addTerrain();
        
        //add the ground
        entities.add(ground);
    }
    
    //PUBLIC METHODS
    @Override
    public void init() {

        //set the clear colour of the renderer
        GLRenderer.setClearColour(new Vector4d(0, 0, 0, 1));
        
        //TODO: do this in the loader
        resources.loadGroup(ResourceGroup.LEVEL);
        
        //TESTING
        entities.add(new PaperCube(resources.getShape("paper_cube"),
            new Vector3d(4.0f, 0.0f, 0.0f), PaperCube.Side.RIGHT, entityMap));
        
        //the fade in overlay
        entities.add(new Overlay(resources.getShape("fade_overlay"),
            new Vector2d(), null, true));
    }

    @Override
    public boolean execute() {
        
        //update the entities
        entities.update();
        
        //process any touch input
        processTouch();

        return complete;
    }
    
    @Override
    public void applyCamera(float[] viewMatrix) {
        
        
        //translate to the camera position
        Matrix.translateM(viewMatrix, 0, camPos.getX(),
            camPos.getY(), camPos.getZ());
        
        //camera rotations
        
        
        Matrix.rotateM(viewMatrix, 0, camRot.getX(), -1.0f, 0, 0.0f);
        Matrix.rotateM(viewMatrix, 0, camRot.getY(), 0.0f, 1.0f, 0.0f);

        
    }
    
    @Override
    public void touchEvent(int event, int index, Vector2d touchPos) {
        
        gestureWatcher.inputEvent(event, index, touchPos);
    }


    @Override
    public EntityList getEntities() {

        return entities;
    }

    @Override
    public Engine nextState() {
        // TODO Auto-generated method stub
        return null;
    }
    
    //PRIVATE METHODS
    /**Process a touch point
    @param viewMatrix the view Matrix*/
    void processTouch() {
        
        Gesture gesture = gestureWatcher.getGesture();
        
        //swipe store
        Vector2d lastSwipeStore = lastSwipe;
        lastSwipe = null;
        
        //pinch store
        boolean pinchLastStore = pinchLast;
        pinchLast = false;
        
        //check to see what kind of gesture this is
        if (gesture instanceof Tap) {
            
            //do nothing for now
        }
        else if (gesture instanceof Swipe) {
            
            Swipe swipe = (Swipe) gesture;
            
            //the camera movement
            Vector2d camMove = new Vector2d();
            
            //calculate the camera movement
            if (lastSwipeStore != null) {
                
                camMove.setX(swipe.getPos().getX() -
                        lastSwipeStore.getX());
                    camMove.setY(swipe.getPos().getY() -
                            lastSwipeStore.getY());
            }
            
            camPos.setX(camPos.getX() +
                (CAM_MOVE_MULTIPLY * camMove.getX()));
            camPos.setY(camPos.getY() +
                (CAM_MOVE_MULTIPLY * camMove.getY()));
            
            lastSwipe = swipe.getPos();
        }
        else if (gesture instanceof Pinch) {
            
            Pinch pinch = (Pinch) gesture;
            
            if (pinchLastStore) {
                
                //zoom based on the distance change
                float thisPinchDis = pinch.getPos1().distance(pinch.getPos2());
                float zoom = (pinchDis - thisPinchDis) * CAM_ZOOM_MULTIPLY;
                pinchDis = thisPinchDis;
                camPos.setZ(camPos.getZ() + zoom);
                
                //rotate based on the angle change
                float thisAngle =
                    pinch.getCentrePos().angleBetween(pinch.getPos1()) *
                    ValuesUtil.RADIANS_TO_DEGREES;
                float rot = pinchAngle - thisAngle;
                pinchAngle = thisAngle;
                camRot.setY(camRot.getY() + rot);
                
                Log.v(ValuesUtil.TAG, "rot: " + rot);
                
                pinchLast = true;
            }
            else {
                
                //store the pinch details
                pinchLast = true;
                pinchDis = pinch.getPos1().distance(pinch.getPos2());
                pinchAngle =
                    pinch.getCentrePos().angleBetween(pinch.getPos1()) *
                    ValuesUtil.RADIANS_TO_DEGREES;
            }
        }
    }
    
    /**Adds the terrain from the entity map to the entity list*/
    private void addTerrain() {
        
        for (int z = 0; z < entityMap.length; ++z) {
            for (int y = 0; y < entityMap[0].length; ++y) {
                for (int x = 0; x < entityMap[0][0].length; ++x) {
                    
                    if (entityMap[z][y][x] != null) {
                        
                        entities.add(entityMap[z][y][x]);
                    }
                }
            }
        }
    }
}

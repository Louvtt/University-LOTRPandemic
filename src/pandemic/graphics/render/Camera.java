package pandemic.graphics.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Represent a 3D Camera
 */
public class Camera {
    /** Projection matrix */
    protected Matrix4f projMatrix;
    /** View matrix */
    protected Matrix4f viewMatrix;

    /** Position */
    protected Vector3f position;
    /** Forward vector */
    protected Vector3f forward;
    /** Up vector */
    protected Vector3f up;

    /** Field of view */
    protected float fov;
    /** Screen aspect ratio */
    protected float aspectRatio;
    /** Near plane distance */
    protected final static float NEAR_PLANE = .01f;
    /** Far plane distance */
    protected final static float FAR_PLANE = 1000f;

    /**
     * Create a camera
     * @param width width
     * @param height height
     * @param fov field of view (degrees)
     */
    public Camera(int width, int height, float fov) {
        this.viewMatrix = new Matrix4f();
        this.projMatrix = new Matrix4f();
        this.fov = fov;

        this.position = new Vector3f(0f, 0f, 6f);
        this.forward  = new Vector3f(0f, -1f, 0f);
        this.up       = new Vector3f(0f, 1f, 0f);

        this.aspectRatio = (float)width / height;

        updateMatrices();
    }

    /**
     * Update the camera matrices (proj and view)
     */
    public void updateMatrices() {
        projMatrix.setPerspective((float) Math.toRadians(fov), aspectRatio, NEAR_PLANE, FAR_PLANE);

        Vector3f target = new Vector3f(position).add(this.forward);
        viewMatrix.setLookAt(this.position, target, up);  
    }

    /**
     * Set the camera position
     * @param newPosition new position
     * @return this
     */
    public Camera setPosition(Vector3f newPosition) {
        this.position = newPosition;

        updateMatrices();
        return this;
    }

    /**
     * Set the camera target position
     * @param targetPosition target position
     * @return this
     */
    public Camera lookAt(Vector3f targetPosition) {
        this.forward = new Vector3f(targetPosition).sub(this.position).normalize();

        updateMatrices();
        return this;
    } 

    /**
     * Move the camera by a certain amount
     * @param translation translation
     */
    public void move(Vector3f translation) {
        this.position.x += translation.x;
        this.position.y += translation.y;
        this.position.z += translation.z;

        updateMatrices();
    }

    /**
     * Resize the camera
     * @param width new width
     * @param height new height
     */
    public void resize(int width, int height) {
        this.aspectRatio = (float) width / height;
        
        updateMatrices();
    }

    /**
     * Returns the projection matrix
     * @return the projection matrix
     */
    public Matrix4f getProjectionMat() {
        return this.projMatrix;
    }

    /**
     * Returns the view matrix
     * @return the view matrix
     */
    public Matrix4f getViewMat() {
        return this.viewMatrix;
    }   

    /**
     * Returns the camera position
     * @return the camera position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Update the camera
     * @param delta frame delta time (in seconds)
     */
    public void update(float delta) { }
}
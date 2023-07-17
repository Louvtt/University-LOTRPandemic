package pandemic.graphics.core;

import pandemic.graphics.exception.LWJGLException;
import pandemic.graphics.render.*;
import pandemic.graphics.scene.*;
import pandemic.game.*;

import org.joml.Vector2i;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Represent the main graphical pipeline
 */
public class Pipeline {
    /** Attached window */
    protected Window window;
    /** Attached camera */
    protected Camera camera;
    /** Quad where we render to screen */
    protected Mesh screenQuad;
    /** List of render passes (world) */
    protected List<RenderPass> renderPasses;
    /** List of post process render passes (screen) */
    protected List<RenderPass> postProcessPasses;
    /** Scene to render */
    protected Scene scene;
    /** Time data */
    protected Time timeData;
    /** Last frame time (in seconds) */
    protected float lastFrameTime;
    /** Bound textures count */
    protected int boundTextures;

    /**
     * Create the default pipeline
     * @param windowName name of the window
     */
    public Pipeline(String windowName) {
        this.window = new Window(windowName);
        this.renderPasses = new ArrayList<RenderPass>();
        this.postProcessPasses = new ArrayList<RenderPass>();

        this.timeData = new Time();
        this.lastFrameTime = 0f;

        this.scene = new Scene("default");
    }

    /**
     * Setup the pipeline (camera + shaders + render passes + geometry + scene)
     */
    public void setup() {
        // Camera
        Vector2i size = window.getSize();
        this.camera = new FlyCamera(size.x(), size.y(), 45f);
        this.camera.setPosition(new Vector3f(0f, 5f, 6f)).lookAt(new Vector3f());
        window.addCallback(new ResizeCallback(){
            public void call(int w, int h) {
                try {
                    for(RenderPass rp : Pipeline.this.renderPasses) {
                        rp.getFramebuffer().resize(new Vector2i(w,h));
                    }
                    for(RenderPass rp : Pipeline.this.postProcessPasses) {
                        rp.getFramebuffer().resize(new Vector2i(w,h));
                    }
                } catch(Exception e) { Log.Get().error(e.getMessage()); }
                Pipeline.this.updateCamera();
                ShaderLibrary.Get().setUniform("uScreenSize", new Vector2f(w,h));
            }
        });

        try {
            this.screenQuad = Mesh.Quad(2f, 2f);
        } catch(Exception e) { Log.Get().error(e.getMessage()); return; }

        this.setupDefaultsShaders();
        this.setupShaders();
        this.setupRenderPasses();

        this.updateCamera();
    }

    /**
     * Setup the default pipeline shaders
     */
    private void setupDefaultsShaders() {
        Vector2i size = window.getSize();
        Shader textShader = ShaderLibrary.loadShader("screenUI", "text");
        textShader.setUniform("uScreenSize", new Vector2f(size));

        Shader screenUIShader = ShaderLibrary.loadShader("screenUI", "screenUI");
        screenUIShader.setUniform("uScreenSize", new Vector2f(size));
        
        Shader spriteShader = ShaderLibrary.loadShader("screenUI", "sprite");
        spriteShader.setUniform("uScreenSize", new Vector2f(size));

        ShaderLibrary.loadShader("simple", "mainShader");
        ShaderLibrary.loadShader("simple", "depth");

        Shader screenShader = ShaderLibrary.loadShader("screen", "screen");
        screenShader.setUniform("screenTexture", 0);

        Shader boxBlurShader = ShaderLibrary.loadShader("screen", "boxBlur");
        boxBlurShader.setUniform("parameters", new Vector2f(3f, .5f));

        Shader dofShader = ShaderLibrary.loadShader("screen", "depthOfField");
        dofShader.setUniform("mouseFocusPoint", new Vector2f(.5f, .5f));

    }

    /**
     * Setup the default pipeline shaders
     */
    protected void setupShaders() {}

    /**
     * Setup render passes and post process render passes
     */
    protected void setupRenderPasses() {
        // FRAMEBUFFERS
        try {
            // main render
            {
                renderPasses.add(new DepthRenderPass());

                List<AttachementType> attachementTypes = new ArrayList<>();
                attachementTypes.add(AttachementType.TEXTURE_COLOR);
                attachementTypes.add(AttachementType.TEXTURE_POSITION);
                attachementTypes.add(AttachementType.TEXTURE_NORMAL);
                attachementTypes.add(AttachementType.RENDERBUFFER_DEPTH_STENCIL);
                renderPasses.add(new RenderPass("main", ShaderLibrary.Get().getShader("mainShader"), attachementTypes));
            }
            // post process
            postProcessPasses.add(new RenderPass("blur", ShaderLibrary.Get().getShader("boxBlur"), false));
            postProcessPasses.add(new RenderPass("dof", ShaderLibrary.Get().getShader("depthOfField"), false));
        } catch (LWJGLException e) { Log.Get().error(e.getMessage()); return; }
    }

    /**
     * Returns true if the pipeline is running, false otherwise
     * @return true if the pipeline is running, false otherwise
     */
    public boolean isRunning() {
        return !window.shouldBeClosed();
    }

    /**
     * Begin a frame
     */
    public void beginFrame() {
        this.timeData.time = (float)glfwGetTime();
        this.timeData.delta = this.timeData.time - this.lastFrameTime;
        this.lastFrameTime = this.timeData.time;
    }

    /**
     * Update the pipeline
     */
    public void update() {
        this.scene.update(this.timeData.delta);
    } 

    /**
     * Render the pipeline
     */
    public void render() {
        this.updateRenderPasses();
        this.renderToScreen();
    
        this.unbindAllRenderPassTextures();
    }

    /**
     * End the frame
     */
    public void endFrame() {
        this.camera.update(this.timeData.delta);
        updateCamera();

        this.window.endFrame();
    }

    /**
     * Update the world render passes
     */
    protected void updateRenderPasses() {
        for (RenderPass rPass : this.renderPasses) {
            rPass.begin();
            scene.render(rPass.getShader());
            rPass.end();
        }

        // bind textures from world framebuffer render
        boundTextures = 0;
        for (RenderPass rPass : this.renderPasses) {
            Map<String, Texture> rPassTextures = rPass.getFramebuffer().getTextures();
            for(String texType : rPassTextures.keySet()) {
                String texName = rPass.getName() + '_' + texType + "Texture";
                ShaderLibrary.Get().setUniform(texName, boundTextures);
                rPassTextures.get(texType).bind(boundTextures);
                boundTextures++;
            }
        }
    }

    /**
     * Apply the post process render passes and render to screen.
     */
    protected void renderToScreen() {
        // post process
        int i = boundTextures;
        for (RenderPass postProcessPass : this.postProcessPasses) {
            postProcessPass.begin();
            screenQuad.draw(postProcessPass.getShader());
            postProcessPass.end();

            String texNameBase = postProcessPass.getName();
            Map<String, Texture> postProcessPassTextures = postProcessPass.getFramebuffer().getTextures();
            for(String texType : postProcessPassTextures.keySet()) {
                String texName = texNameBase + "Texture";                  
                ShaderLibrary.Get().setUniform(texName, i);
                postProcessPassTextures.get(texType).bind(i);
                i++;
            }
        }
        // render to screen
        window.beginFrame(false);
        screenQuad.draw(ShaderLibrary.Get().getShader("screen"));

        // ui
        scene.renderUI();
    }

    /** Unbind all textures */
    public void unbindAllRenderPassTextures() {
        for (RenderPass rPass : this.renderPasses) {
            Map<String, Texture> rPassTextures = rPass.getFramebuffer().getTextures();
            for(Texture tex : rPassTextures.values()) {
                tex.unbind();
            }
        }

        for (RenderPass rPass : this.postProcessPasses) {
            Map<String, Texture> rPassTextures = rPass.getFramebuffer().getTextures();
            for(Texture tex : rPassTextures.values()) {
                tex.unbind();
            }
        }
        boundTextures = 0;
    }

    /**
     * Update the camera
     */
    public void updateCamera() {
        Vector2i size = this.window.getSize();
        this.camera.resize(size.x(), size.y());
        ShaderLibrary.Get().setMat4("uView", this.camera.getViewMat());
        ShaderLibrary.Get().setMat4("uProj", this.camera.getProjectionMat());
    }

    /**
     * Returns the current scene of this pipeline
     * @return the current scene of this pipeline
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Sets the current scene to render
     * @param scene the scene to render
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    /**
     * Returns the active camera of this pipeline
     * @return the active camera of this pipeline
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Returns the attached window of this pipeline
     * @return the attached window of this pipeline
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Returns the time data of this pipeline
     * @return the time data of this pipeline
     */
    public Time getTimeData() {
        return timeData;
    }

    /**
     * Delete the pipeline
     */
    public void delete() {
        ShaderLibrary.Get().destroyAll();
        window.destroy();
    }
}

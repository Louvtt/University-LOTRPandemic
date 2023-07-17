package pandemic.graphics.render;

import java.util.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import pandemic.game.Log;
import pandemic.graphics.exception.LWJGLException;

/**
 * Shader Library
 */
public class ShaderLibrary {
    /** Map of registered shaders */
    protected Map<String, Shader> shaders;
    /** Path to shader asset directory */
    protected static String assetsDir = "assets/shaders/";
    /** Unique Shader Libary instance (Singleton) */
    protected static ShaderLibrary instance = null;
    
    /**
     * Returns the current ShaderLibrary instance
     * @return the current ShaderLibrary instance
     */
    public static ShaderLibrary Get() {
        if(instance == null) instance = new ShaderLibrary();
        return instance;
    }

    /**
     * Load a shader from the shader assets directory
     * @param vertexShaderName vertex shader name
     * @param fragShaderName fragment shader name
     * @return the loaded shader
     */
    public static Shader loadShader(String vertexShaderName, String fragShaderName)
    {
        try {
            // scene shader
            Shader shader = new Shader(assetsDir+vertexShaderName+".vert", assetsDir+fragShaderName+".frag");
            ShaderLibrary.Get().registerShader(fragShaderName, shader);
            return shader;
        } catch (LWJGLException e) { 
            Log.Get().error(e.getMessage()); 
            System.exit(404);
            return null; 
        }
    }

    /**
     * Change the assets directory path
     * @param assetsDir new assets directory path
     */
    public static void setAssetsDir(String assetsDir) {
        ShaderLibrary.assetsDir = assetsDir;
    }

    /**
     * Create a Shader Library 
     */
    protected ShaderLibrary() {
        shaders = new HashMap<>();
    }

    /**
     * Register a shader into the library
     * @param name name of the shader to register
     * @param shader shader to register
     */
    public void registerShader(String name, Shader shader) {
        this.shaders.put(name, shader);
    }

    /**
     * Returns the corresponding shader (or null if not found)
     * @param name name of the shader
     * @return the corresponding shader (or null if not found)
     */
    public Shader getShader(String name) {
        return this.shaders.get(name);
    }

    /**
     * Set the uniform for all registered shaders 
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setMat4(String name, Matrix4f value) {
        for(Shader shader : this.shaders.values()) {
            shader.setUniform(name, value);
        }
    }

    /**
     * Set the uniform for all registered shaders 
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, int value) {
        for(Shader shader : this.shaders.values()) {
            shader.setUniform(name, value);
        }
    }
    
    /**
     * Set the uniform for all registered shaders 
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, Vector2f value) {
        for(Shader shader : this.shaders.values()) {
            shader.setUniform(name, value);
        }
    }

    /**
     * Destroy all registered shaders
     */
    public void destroyAll() {
        for(Shader shader : this.shaders.values()) {
            shader.delete();
        }
    }

}

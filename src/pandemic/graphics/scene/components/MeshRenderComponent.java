package pandemic.graphics.scene.components;

import pandemic.graphics.render.Mesh;
import pandemic.graphics.render.Shader;
import pandemic.graphics.scene.Component;

/**
 * Mesh render component
 */
public class MeshRenderComponent implements Component {
    /** Mesh */
    protected Mesh mesh;
    /** Mesh shader */
    protected Shader shader;

    /**
     * Create a mesh render component
     * @param mesh mesh
     * @param shader shader
     */
    public MeshRenderComponent(Mesh mesh, Shader shader) {
        this.mesh = mesh;
        this.shader = shader;
    }

    public void onRender() {
        this.mesh.draw(this.shader);
    }

    @Override
    public void delete() {
        mesh.delete();
    }
}

package pandemic.graphics.scene.components;

import java.security.InvalidParameterException;

import pandemic.graphics.render.Model;
import pandemic.graphics.render.Shader;
import pandemic.graphics.scene.Component;

/**
 * Model component
 */
public class ModelComponent implements Component {

    /** Model */
    protected Model model;

    /**
     * Create a model component
     * @param model model
     */
    public ModelComponent(Model model) {
        this.model = model;
        if(this.model == null) {
            throw new InvalidParameterException("[ModelComponent] model is null.");
        }
    }

    public void onRender() {
        this.model.draw(Shader.GetActiveShader());        
    }

    @Override
    public void delete() {}
}

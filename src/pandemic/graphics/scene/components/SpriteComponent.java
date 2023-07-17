package pandemic.graphics.scene.components;

import pandemic.graphics.scene.Component;
import pandemic.graphics.render.*;

/**
 * Sprite component
 */
public class SpriteComponent implements Component {
    /** Sprite */
    protected Sprite sprite;

    /**
     * Create a sprite component
     * @param sprite sprite
     */
    public SpriteComponent(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void onRenderUI() {
        this.sprite.getShader().setUniform("uAlpha", 1f);
        this.sprite.draw();
    }

    @Override
    public void delete() {
        this.sprite.delete();
    }
}

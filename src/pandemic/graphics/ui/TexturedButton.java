package pandemic.graphics.ui;

import org.joml.Vector2f;
import org.joml.Vector2i;

import pandemic.graphics.core.Window;
import pandemic.graphics.render.*;

/**
 * Textured button
 */
public class TexturedButton extends Button {
    /** Sprite of the button */
    protected Sprite sprite;

    /**
     * Create a textured button
     * @param texture texture of the button
     * @param position position of the button (screen UV)
     */
    public TexturedButton(Texture texture, Vector2f position) {
        super();
        this.sprite = new Sprite(texture, position);

        this.position = position;
        final Vector2i winSize = Window.Get().getSize();
        this.size = new Vector2f(texture.getSize().x(), texture.getSize().y).div(winSize.x(), winSize.y());
    }

    @Override
    public void draw() {
        this.sprite.getShader().setUniform("uAlpha", (this.hovered?.8f:1f));
        this.sprite.draw();
    }

    @Override
    public void delete() {
        this.sprite.delete();
    }
}

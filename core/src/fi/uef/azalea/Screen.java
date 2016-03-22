package fi.uef.azalea;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class Screen {

	public abstract void init();
	public abstract void render(SpriteBatch spriteBatch, DecalBatch decalBatch);
	public abstract void resize(int width, int height);
	public abstract void dispose();
	public abstract void pause();
	public abstract void resume();
	public abstract void handleTouchPoint(Vector2 tp2);
	
}

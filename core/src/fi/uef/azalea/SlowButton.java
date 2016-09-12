package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SlowButton extends TextButton {
	
	private float longpress = 0;
	
	public SlowButton (String text, Skin skin) {
		super(text, skin);
	}

	public SlowButton (String text, Skin skin, String styleName) {
		super(text, skin, styleName);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(isPressed()){
			batch.setColor(1, 1, 1, 0.75f);
			getSkin().getPatch("button_warn").draw(batch, getX(), getY(), 30+(longpress/Statics.MAX_PRESS_DELAY)*(getWidth()-30), getHeight());
			longpress += Gdx.graphics.getDeltaTime();
			if(longpress > Statics.MAX_PRESS_DELAY){
				longpress = Statics.MAX_PRESS_DELAY;
			}
		} else {
			longpress = 0;
		}
	}
	
	@Override
	public boolean fire (Event event) {
		
		if(longpress >= Statics.MAX_PRESS_DELAY){
			return super.fire(event);
		}
		
		return false;
	}

}

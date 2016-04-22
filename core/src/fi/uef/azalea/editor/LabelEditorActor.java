package fi.uef.azalea.editor;

import java.awt.Color;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import fi.uef.azalea.Statics;

public class LabelEditorActor extends Actor {

	public boolean editing = false;
	private Texture labelEditTexture;
	private Pixmap labelTexturePixmap;
	private boolean erase = false;

	public LabelEditorActor(){

		labelTexturePixmap = new Pixmap(512, 128, Format.RGBA8888);
		labelEditTexture = new Texture(labelTexturePixmap);
		//setBounds(getX(),getY(),titleTexture.getWidth(),titleTexture.getHeight());

		this.addListener(new InputListener() {

			float lx = 0, ly = 0;

			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				lx = trX(x);
				ly = trY(y);
				return true;
			}

			public void touchDragged(InputEvent event, float x, float y, int pointer){
				Pixmap.setBlending(Blending.SourceOver);
				
				if(erase){
					labelTexturePixmap.setColor(0,0,0,0);
				} else {
					labelTexturePixmap.setColor(1,1,1,0.5f);
				}

				x = trX(x);
				y = trY(y);

				for(float r=0; r < Math.PI*2; r+=0.2f){
					if(erase){
						labelTexturePixmap.drawLine((int)(lx + Math.sin(r)*Statics.ERASER_RADIUS), (int)(ly + Math.cos(r)*Statics.ERASER_RADIUS), (int)(x + Math.sin(r)*Statics.ERASER_RADIUS), (int)(y + Math.cos(r)*Statics.ERASER_RADIUS));
					} else {
						labelTexturePixmap.drawLine((int)(lx + Math.sin(r)*Statics.BRUSH_RADIUS), (int)(ly + Math.cos(r)*Statics.BRUSH_RADIUS), (int)(x + Math.sin(r)*Statics.BRUSH_RADIUS), (int)(y + Math.cos(r)*Statics.BRUSH_RADIUS));
					}
				}

				lx = x;
				ly = y;

				labelEditTexture.draw(labelTexturePixmap, 0,0);
			}
		});
		
		
		
	}
	
	private float trX(float x){
		return (x/getWidth())*labelTexturePixmap.getWidth();
	}
	
	private float trY(float y){
		return ((getHeight()-y)/getHeight())*labelTexturePixmap.getHeight();
	}
	
	public void setErase(boolean erase){
		this.erase = erase;
	}
	
	public void clearTexture(){
		labelTexturePixmap.setColor(0, 0, 0, 0);
		labelTexturePixmap.fillRectangle(0, 0, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		labelEditTexture.draw(labelTexturePixmap, 0,0);
	}
	
	public Pixmap getLabelPixmap(){
		return labelTexturePixmap;
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(labelEditTexture, getX(), getY(), getWidth(), getHeight());
	}

}

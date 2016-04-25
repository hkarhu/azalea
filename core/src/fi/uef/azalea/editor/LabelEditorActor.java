package fi.uef.azalea.editor;

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

		labelTexturePixmap = new Pixmap(1024, 256, Format.RGBA8888);
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
				
				
				if(erase){
					Pixmap.setBlending(Blending.None);
					labelTexturePixmap.setColor(0,0,0,0);
				} else {
					Pixmap.setBlending(Blending.SourceOver);
					labelTexturePixmap.setColor(1,1,1,0.7f);
				}

				x = trX(x);
				y = trY(y);

				for(float r=0; r < Math.PI*2; r+=0.1f){
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

	public void setLabel(Pixmap p) {
		labelTexturePixmap.setColor(0, 0, 0, 0);
		labelTexturePixmap.fillRectangle(0, 0, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		labelTexturePixmap.drawPixmap(p, 0, 0, p.getWidth(), p.getHeight(), 0, 0, labelEditTexture.getWidth(), labelEditTexture.getHeight());
		labelEditTexture.draw(labelTexturePixmap, 0,0);
	}

}

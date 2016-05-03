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
	private boolean edit = true;
	private boolean erase = false;
	private boolean newTexture = true;

	public LabelEditorActor(){

		labelTexturePixmap = new Pixmap(1024, 256, Format.RGBA8888);
		labelEditTexture = new Texture(labelTexturePixmap);
		//setBounds(getX(),getY(),titleTexture.getWidth(),titleTexture.getHeight());
		
		this.addListener(new InputListener() {

			float lx = 0, ly = 0;

			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(!edit) return true;
				newTexture = false;
				lx = trX(x);
				ly = trY(y);
				return true;
			}

			public void touchDragged(InputEvent event, float x, float y, int pointer){
				
				if(!edit) return;
				
				if(erase){
					Pixmap.setBlending(Blending.None);
					labelTexturePixmap.setColor(0,0,0,0);
				} else {
					Pixmap.setBlending(Blending.SourceOver);
					labelTexturePixmap.setColor(0,0,0,0.7f);
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
	
	private boolean isTextureEmpty(){
		
		for(int y=0; y < labelTexturePixmap.getHeight(); y += Statics.BRUSH_RADIUS)
		for(int x=0; x < labelTexturePixmap.getWidth(); x += Statics.BRUSH_RADIUS){
			if(labelTexturePixmap.getPixel(x, y) > 0) return false;
		}
		
		return true;
	}
	
	public void setEdit(boolean edit){
		this.edit = edit;
	}
	
	public void setErase(boolean erase){
		this.erase = erase;
	}
	
	public void clearTexture(){
		newTexture = true;
		Pixmap.setBlending(Blending.None);
		labelTexturePixmap.setColor(0, 0, 0, 0);
		labelTexturePixmap.fillRectangle(0, 0, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		labelEditTexture.draw(labelTexturePixmap, 0,0);
	}
	
	public Pixmap getLabelPixmap(){
		return labelTexturePixmap;
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		if(newTexture) batch.draw(Statics.TEX_TITLE_HELP, getX()+getWidth()*0.5f, getY());
		batch.draw(labelEditTexture, getX(), getY(), getWidth(), getHeight());
	}
	

	public void setEditableLabel(Pixmap p) {
		System.out.println("Editable label set " + p);
		Pixmap.setBlending(Blending.None);
		labelTexturePixmap.setColor(0,0,0,1);
		labelTexturePixmap.drawPixmap(p, 0, 0, p.getWidth(), p.getHeight(), 0, 0, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		labelEditTexture.draw(labelTexturePixmap, 0,0);
		newTexture = isTextureEmpty();
	}

}

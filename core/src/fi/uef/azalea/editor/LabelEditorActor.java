package fi.uef.azalea.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.ScreenUtils;

import fi.uef.azalea.Statics;

public class LabelEditorActor extends Actor {

	public boolean editing = false;
	private Texture labelEditTexture;
	private Pixmap labelTexturePixmap;
	private boolean edit = true;
	private boolean erase = false;
	private boolean emptyTexture = true;
	
	private TextureRegion background;

	public LabelEditorActor(){

		background = new TextureRegion(Statics.TEX_LISTBG);
		
		labelTexturePixmap = new Pixmap(1024, 256, Format.RGBA8888);
		labelEditTexture = new Texture(labelTexturePixmap);
		//setBounds(getX(),getY(),titleTexture.getWidth(),titleTexture.getHeight());
		
		this.addListener(new InputListener() {

			float lx = 0, ly = 0;

			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(!edit) return true;
				emptyTexture = false;
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
	


	public void stampText(String text){

		//TODO: night time copypaste code, check, possible refactor
		SpriteBatch sp = new SpriteBatch();
		BitmapFont bmf = new BitmapFont(Gdx.files.internal("large-font.fnt"), Statics.SKIN.getFont("large-font").getRegion(), true);
		bmf.getData().setScale(labelTexturePixmap.getHeight()*0.01f,  labelTexturePixmap.getWidth()*0.01f);
		FrameBuffer m_fbo = new FrameBuffer(Format.RGBA8888, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight(), false);

		m_fbo.begin();
			Gdx.gl.glClearColor(0,0,0,0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			sp.begin();
				bmf.setColor(0, 0, 0, 1);
				bmf.draw(sp, text, labelTexturePixmap.getWidth()*0.025f, bmf.getXHeight()*0.5f);
			sp.end();
			Pixmap p = ScreenUtils.getFrameBufferPixmap(0,0,labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		m_fbo.end();

		m_fbo.dispose();
		sp.dispose();
		bmf.dispose();	
		labelTexturePixmap.drawPixmap(p, 0, 0, p.getWidth(), p.getHeight(), 0, 0, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		
		/* Works only on desktop ;-;
		BufferedImage bi = new BufferedImage(labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)bi.getGraphics();
		int fontsize=130;
		g.setFont(new Font("Arial", Font.BOLD, fontsize));
		g.setColor(Color.white);
		g.drawString(text, 30, labelTexturePixmap.getHeight() - (fontsize/2));
		for(int y=0; y < labelTexturePixmap.getHeight(); y++)
		for(int x=0; x < labelTexturePixmap.getWidth(); x++){
			if(bi.getRGB(x, y) < 0) labelTexturePixmap.drawPixel(x, y, 0x000000FF);
		}
		*/
		
		labelEditTexture.draw(labelTexturePixmap, 0,0);
		emptyTexture = false;
	}
	
	public void setEdit(boolean edit){
		this.edit = edit;
	}
	
	public void setErase(boolean erase){
		this.erase = erase;
	}
	
	public void clearTexture(){
		emptyTexture = true;
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
		batch.draw(Statics.TEX_LISTBG, getX(), getY(), getWidth(), getHeight(), 0f, 0f, getWidth()*0.008f, getHeight()*0.008f);
		if(emptyTexture) batch.draw(Statics.TEX_TITLE_HELP, getX()+getWidth()*0.5f, getY(), Statics.TEX_TITLE_HELP.getWidth()*(Gdx.graphics.getWidth()*0.0006f), Statics.TEX_TITLE_HELP.getHeight()*(Gdx.graphics.getHeight()*0.001f));
		batch.draw(labelEditTexture, getX(), getY(), getWidth(), getHeight());
	}
	

	public void setEditableLabel(Pixmap p) {
		System.out.println("Editable label set " + p);
		Pixmap.setBlending(Blending.None);
		Pixmap.setFilter(Filter.NearestNeighbour);
		labelTexturePixmap.setColor(0,0,0,1);
		labelTexturePixmap.drawPixmap(p, 0, 0, p.getWidth(), p.getHeight(), 0, 0, labelTexturePixmap.getWidth(), labelTexturePixmap.getHeight());
		labelEditTexture.draw(labelTexturePixmap, 0,0);
		emptyTexture = isTextureEmpty();
	}

	public boolean isLabelEmpty() {
		return emptyTexture;
	}

}

package fi.uef.azalea;

public abstract class Screen {

	protected Screen nextScreen;
	public boolean ready = false;
	
	public Screen getNextScreen(){
		return nextScreen;
	}
	
	public abstract void init(); //Init gets called every time screen is changed
	
	public abstract void render(); //Render loop calls this
	
	public abstract void resize(int width, int height); //Called when resizing the canvas is over
	
	public abstract void dispose(); //Called when program gets derezzed
	
	//These are used when app gets switched
	public abstract void pause();
	public abstract void resume();
	
	//Input handling abstraction
	public abstract void touchDown(float x, float y, int id);
	public abstract void touchUp(float x, float y, int id);
	
}

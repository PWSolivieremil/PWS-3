package om.entities.pws;

//import om.game.pws.Game;
import om.graphics.pws.Screen; //Importeren van de benodigede classes en libraries
import om.level.pws.Level;

public class Entity {

	public int x;	// Dit geeft de x-co�rdinaat	
	public int y;	//Dit geeft de y-co�rdinaat
	
	protected Level level;
	
	public Entity(Level level) {
		init(level);
	}
	
	public final void init(Level level) {
		this.level = level;
	}
	
	//public abstract void tick() {
	//	System.out.print("Marco" + " ");
		//if (System.currentTimeMillis() - Game.lastSpawn >= 1000){
			//Game.lastSpawn += 1000;
		//	System.out.println("Polo");
	//		Game.Ai = new AI(this);
		//	Game.bots = new Bots(level, 0, 0, Ai);
		//	level.addEntity(bots);
			
	//	}
	//}		
	//Als er mobs zijn dan worden ze hier in gezet.
	
	public void tick() {
		
	}
	
	public void render(Screen screen) {	
		
	}
}
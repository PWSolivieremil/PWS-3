package om.entities.pws;

import om.game.pws.Game;			//Importeren van de benodigede classes en libraries
import om.graphics.pws.Colours;
import om.graphics.pws.Screen;
import om.level.pws.Level;			

public class Bots  extends Mob{

	private int colour = Colours.get(-1, 111, 300, 051);
	
	public Bots(Level level, int x, int y, int speed) {
		super(level, "zombie", x, y, speed);
	}
	
	public void tick() {			//Looprichting
		int xa = 0;
		int ya = 0;
		
		
//		if (Entity.playerx() > x) {
//			xa = 1;
//	}	
//		if (Player.x < x) {
//			xa = -1;
//		}
//		if (Player.y > y) {
//			xa = 1;
//		}
//		if (Player.y < y) {
//			xa = -1;
//		}
//		
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}

	}
	
	
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 26;
		int walkingSpeed = 4;		// dit is de snelheid waarop de player animatie beweegt
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		
		if (movingDir == 1) {		//nieuwe sprite die de game moet gebruiken
			xTile += 2;
			
		}else if (movingDir > 1){
			xTile += 4  + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			flipBottom = (movingDir - 1) % 2;
				
		}

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;
		
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop); // upper body part 1
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop); // upper body part 2
		screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom); // lower body part 1
		screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32,colour , flipBottom); // lower body part 2
	}

}

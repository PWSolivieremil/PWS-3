package om.entities.pws;

import om.level.pws.Level; //Importeren van de benodigede classes en libraries
import om.level.tile.pws.Tile;

public class Mob extends Entity{

	// Dit zijn eigenschappen die een Entity kan hebben.
	// -naam
	// -snelheid
	// -aantal stappen
	// -aan het bewegen
	// -bewegingsrichting
	// -grootte

	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;

	public Mob(Level level, String name, int x, int y, int speed) {		//Dit is wat een Mob zijn eigenschappen geeft.
		super(level);													//Het staat altijd in het level "level"
		this.name = name;												//Hier wordt een naam gegeven.
		this.x = x;														//Dit wordt de x coördinaat.
		this.y = y;														//Dit wordt de y coördinaat.
		this.speed = speed;												//Dit is de snelheid van de mob.
		
	}
	
	public void move(int xa, int ya) {				//Beweging naar een plek
		if (xa != 0 && ya != 0) {					//ze mogen maar een kant per keer bewegen
			move(xa, 0);							//Bewegen
			move(0, ya);
			numSteps -= 1;
			return;
		}
		
		// Dit deel van de code kan het karakter verplaatsen door middel van keybinds
	 	// Dit staat in de inputhandler en zorgt ervoor dat je het poppetje zelf kan besturen
		
		numSteps += 1;
		if (!hasCollided(xa, ya)){
			if (ya < 0) {			//Up
				movingDir = 0;
			}
			if (ya > 0) {			//Down
				movingDir = 1;
			}
			if (xa < 0) {			//Left
				movingDir = 2;
			}
			if (xa > 0) {			//Right
				movingDir = 3;
			}
			x += xa * speed;
			y += ya * speed;
		}
	}

	public boolean hasCollided(int xa, int ya) {
		return false;
	}

	//Deze boolean bepaald of de dichtsbijzijnde tegel solid is.
	protected boolean isSolidTile (int xa, int ya, int x, int y) {
		if(level == null) { return false;}												//Als het niet in "level" staat dan is het nooit een solid
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);			//Dit kijkt wat de tegel is waar je nu op staat.
		Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);	//Dit kijkt wat de tegel is waar je op wil gaan staan.
		if(!lastTile.equals(newTile) && newTile.isSolid()) {							//Dit bepaald of de tegel solid is door hem te vergelijken met de oude tegel.
			return true;																//Als dit wel zo is dan is de tegel solid
		}
		return false;																	//Als dit niet zo is dan is hij niet solid
		}
	
	public String getName() {
		return name;
	}
	
}
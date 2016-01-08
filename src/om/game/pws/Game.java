package om.game.pws;

import java.awt.BorderLayout;  //Importeren van de benodigede classes en libraries
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import om.entities.pws.Bots;
import om.entities.pws.Entity;
import om.entities.pws.Player;
import om.graphics.pws.Screen;
import om.graphics.pws.SpriteSheet;
import om.level.pws.Level;

public class Game extends Canvas implements Runnable { //Dit is onze mainclass.

	private static final long serialVersionUID = 1L;//Dit moet gedaan worden bij het maken van een frame.
	
	public static final int WIDTH = 160;			//Dit zijn eind waardes die niet veranderen. 
	public static final int HEIGHT = WIDTH / 12 * 9;//Het is op deze manier gedaan,
	public static final int SCALE = 3;				//zodat wij het in de code snel kunnen aanpassen.
	public static final String NAME = "Game";
	public static final long starttime = System.nanoTime();
	public static long lastSpawn = System.currentTimeMillis();
	private JFrame frame;							//Dit is de frame, hierin zal de game runnen.
	public boolean running = false;
	public int tickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); //Dit is wat er in het canvas komt te staan.
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();		
	private int[] colours = new int[6 * 6 * 6];
	
	private Screen screen;
	public InputHandler input;
	public Level level;
	public Entity Entity;
	public static final int levelWidth = 64;
	public static final int levelHeight = 64;
	public Player player;
	public static Bots bots;
	
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));	//Dit zijn de dimensies van het canvas
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));	//Doordat ze allemaal hetzelfde zijn, 
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));	//start het spel altijd met dezelfde grootte

		frame = new JFrame(NAME);										//hier maken we het frame

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//Het canvas sluit als jij hem sluit
		frame.setLayout(new BorderLayout());							 
		frame.add(this, BorderLayout.CENTER);
		frame.pack();													//De grootte van het canvas is nu  volgens die dimensies

		frame.setResizable(false);										//Grootte is niet aanpasbaar
		frame.setLocationRelativeTo(null);								//Waar het spel is nu in het midden van het scherm
		frame.setVisible(true);											//Vanaf nu kan het het spel zien.
	}

	public void init() {												//Dit start allerlei gegevens bij het opstarten van het spel.
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);

					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);	
		level = new Level(levelWidth, levelHeight);
		player = new Player(level, 0, 0, input);
		level.addEntity(player);
		
		
	}

	public synchronized void start() {			//Synchronized is handig als je het als applet maakt, zo kan je het runnen in browser.
		running = true;	
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();				//Bij een game is het erg belangrijk
		long lastTimer = System.currentTimeMillis();	//dat het speelbaar is.
		double nsPerTick = 1000000000D / 60D;			//Door een limiet te zetten op de snelheid
		double delta = 0;								//van while loop(ticks) loopt de game op een 
		int ticks = 0;									//controleerbare snelheid, die op elk apparaat gelijk is.
		int frames = 0;
		init();

		while (running) {								//Dit is de  functie die blijft draaien tot je de game sluit
			long now = System.nanoTime();		
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while (delta >= 1) {	
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {	//Elke seconde komt een nieuwe waarden van de frames en ticks.
				lastTimer += 1000;									//Nu kan je beter zien dat er een limiet is.
				System.out.println(ticks + " ticks , " + frames + " frames per second");
				frames = 0;
				ticks = 0;
			}
			
		}
			
	}
	
	public static int randInt(int min, int max) {					// In deze methode worden willekeurige getallen gegenereerd om te gebruiken voor de coördinaten van de zombies
	    Random rand = new Random();									
	    int randomNum = rand.nextInt((max - min) + 1) + min;		// hier wordt de integer randomNum een willekeurige waarde gegeven en gereturnt.
	    return randomNum;
	}
	public void tick() {
		tickCount++;
		level.tick();
		
		if (System.currentTimeMillis() - Game.lastSpawn >= 10000){														//Er wordt elke 10 seconden (10.000 milliseconden) een zombie gespawnt.
			Game.lastSpawn += 10000;
			Game.bots = new Bots(level, randInt(0, (levelWidth * 8 )) , randInt(0, (levelHeight * 8)), 1);				//Hij bevind zich in het level met een willekeurige x- en y-coördinaat.
			level.addEntity(bots);
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);				//Dit is de snelheid waarop het plaatje wordt gemaakt
			return;
		}

		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);

		
		level.renderTiles(screen, xOffset, yOffset);

		//for (int x = 0; x < level.width; x++) {
			//int colour = Colours.get(-1, -1, -1, 000);
			
			//if (x % 10 == 0 && x != 0) {
				//colour = Colours.get(-1, -1, -1, 500);
			//}
			//Font.render(terugveranderen, screen, 0 + 4, 0, colour); //Dit is als je wil controleren hoe groot het level is
		//}

		level.renderEntities(screen);

		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int ColourCode = screen.pixels[x + y * screen.width];
				if (ColourCode < 255) {
					pixels[x + y * WIDTH] = colours[ColourCode];

				}
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();						//De oude graphics wegdoen
		bs.show();
	}

	public static void main(String[] args) { //Dit is de main, hiermee start alles in de code.
		new Game().start();
	}
}
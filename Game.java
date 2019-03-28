import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;

class Game extends JPanel {
	
	public static int mouseX = 0; // always changes
	public static int mouseY = 0;
	public static int mouseCX = 0; // only changes when mouse is dragged
	public static int mouseCY = 0;
	public static int mousePX = 0; // only changes when mouse is clicked
	public static int mousePY = 0;
	public static boolean mClick = false;
	public static boolean mouseDragged = false;
	public static boolean mRelease = false;
	public static boolean mSelect = false;
	public static boolean setMP = false; // tracks when mPX & mPY have been changed
	public static final int limX = 30;
	public static final int limY = 30;
	public static final int limW = 924;
	public static final int limH = 601;
	
	private ArrayList<Integer> moving = new ArrayList<Integer>(); 	// tracks the levels that have moving shapes
	private ArrayList<Integer> pars = new ArrayList<Integer>();		// tracks the par for each level
	private ArrayList<MRect> mRects = new ArrayList<MRect>();
	private ArrayList<Polygon> polys = new ArrayList<Polygon>();
	private ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
	private boolean[] keys;
	private boolean paused;
	private BufferedImage back;
	private BufferedImage exit1;
	private BufferedImage exit2;
	private BufferedImage help1;
	private BufferedImage help2;
	private BufferedImage helpscreen;
	private BufferedImage pause1;
	private BufferedImage pause2;
	private BufferedImage start1;
	private BufferedImage start2;
	private BufferedImage title;
	private BufferedImage winScreen;
	private Color color = new Color(0, 0, 0);
	private int level;
	private int time;
	private int score;
	private int attempts; // tracks number of attempts for each level
	private int par; // tracks par for each level
	private Random rand = new Random();
	private Rectangle boundary;
	private Rectangle exitB;
	private Rectangle pauseB;
	private Rectangle startB;
	private Rectangle helpB;
	private String mode;
	
	public Game() {

		keys = new boolean[KeyEvent.KEY_LAST + 1];
		
		paused = false;
		
		boundary = new Rectangle(limX, limY, limW, limH);
		
		level = 0;
		time = 0;
		score = 0;
		attempts = 0;
		
		// buttons
		exitB = new Rectangle(859, 536, 75, 75);
		pauseB = new Rectangle(455, 300, 75, 75);
		startB = new Rectangle(390, 300, 200, 75);
		helpB = new Rectangle(390, 400, 200, 75);
		
		mode = "MENU";
		
		// import images
		try {
			back = ImageIO.read(new File("images/turf.png"));
			exit1 = ImageIO.read(new File("images/exit1.png"));
			exit2 = ImageIO.read(new File("images/exit2.png"));
			help1 = ImageIO.read(new File("images/help1.png"));
			help2 = ImageIO.read(new File("images/help2.png"));
			helpscreen = ImageIO.read(new File("images/helpscreen.png"));
			pause1 = ImageIO.read(new File("images/pause1.png"));
			pause2 = ImageIO.read(new File("images/pause2.png"));
			start1 = ImageIO.read(new File("images/start1.png"));
			start2 = ImageIO.read(new File("images/start2.png"));
			title = ImageIO.read(new File("images/title.png"));
			winScreen = ImageIO.read(new File("images/win.png"));
			}
		catch (IOException e) { System.out.println("Could not import images"); }
		
		// read in files for each level
		try {
			Scanner inFile = new Scanner(new File("levels/pars.txt"));
			
			while (inFile.hasNextLine()) {
				String line = inFile.nextLine();
				pars.add(Integer.valueOf(line)); } }
		catch (IOException e) { System.out.println("Could not import 'levels/pars.txt'"); }
		
		try {
			Scanner inFile = new Scanner(new File("levels/moving.txt"));
			
			while (inFile.hasNextLine()) {
				int line = Integer.valueOf(inFile.nextLine());
				moving.add(line);
			}
		}
		catch (IOException e) { System.out.println("Could not import 'levels/moving.txt'"); }
	}
	
    public void setKey(int i, boolean b) {
    	keys[i] = b;
    }
    
    public void pause() {
    	if (mode.equals("PAUSE")) {
    		mode = "GAME";
    		paused = false;
    	}
    	else if (mode.equals("GAME")) {
    		mode = "PAUSE";
    	}
    }
    
    // ------------------------ GRAPHICS -------------------------
	@Override
	public void paintComponent(Graphics g) {
		if (mode.equals("MENU")) {
			drawMenu(g);
		}
		else if (mode.equals("HELP")) {
			drawHelp(g);
		}
		else if (mode.equals("GAME")) {
			drawGame(g);
		}
		else if (mode.equals("PAUSE")) {
			drawPause(g);
		}
		else if (mode.equals("WINLEVEL")) {
			winLevel(g);
		}
		else if (mode.equals("WIN")) {
			winGame(g);
		}
	}
    // ---- Mode Game ----
    private void drawGame(Graphics g) {
		// draw walls
		g.setColor(color);
		
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(back, limX, limY, null);
		for (Rectangle r : rects) {
			g.fillRect(r.x, r.y, r.width, r.height); }
		for (MRect rect : mRects) {
			Rectangle r = rect.getBounds();
			g.fillRect(r.x, r.y, r.width, r.height); }
		for (Polygon p : polys) {
			g.fillPolygon(p); }
	
		
		// draw hole
		g.setColor(Color.BLACK);
		
		int[] pos = Hole.getPos();
		g.fillOval(pos[0], pos[1], pos[2], pos[3]);
		
		// draw dotted line
		if (mClick && Ball.isMoving() == false) {
			drawLine(g); }
		
		// draw ball
		g.setColor(Color.WHITE);
		g.fillOval((int)Math.round(Ball.x) - Ball.size/2, (int)Math.round(Ball.y) - Ball.size/2, Ball.size, Ball.size);
		
		// draw text
		int textH = 18;
		g.setColor(Color.BLACK);
		g.fillRect(115, 4, 70, 20);
		g.fillRect(794, 4, 90, 20);
		g.fillRect(915, 4, 60, 20);
		g.setColor(Color.WHITE);
		g.drawString("Score", 120, textH);
		g.drawString("" + score, 165, textH);
		g.drawString("Par", 920, textH);
		g.drawString("" + pars.get(level - 1), 952, textH);
		g.drawString("Attempts", 800, textH);
		g.drawString("" + attempts, 863, textH);
    }
    private void drawLine(Graphics g) {
    	// draws dotted line of the path of the ball
		int x = (int)(Math.round(Ball.x - mouseCX));
		int y = (int)(Math.round(Ball.y - mouseCY));
		double angle = 0;
		
		if (x == 0) { // prevents division by 0
			if (y == 0) { angle = 0; }
			if (y > 0) { angle = Math.PI/2; }
			if (y < 0) { angle = 3*Math.PI/2; } }
		else { 
			angle = Math.atan((double)y/(double)x); }
		if (x < 0) { angle += Math.PI; }
		/* since atan returns -pi<x<pi adding 
		 * pi accounts for all angles
		 */
		
		double dist = Math.hypot(x, y);
		if (dist > 300) { dist = 300; }
		
			drawLine(g, Ball.x, Ball.y, angle, dist, 6, 0);
		
    }
    private void drawLine(Graphics g, double x, double y, double angle, double dist, double size, int count) {
    	/* draws dashed line which predicts the path of the ball
    	 * and recurses off of new coordinates every time the 
    	 * path deflects
   		 */
    	g.setColor(Color.YELLOW);
    	
    	if (count > 3) { return; } // only draw 3 deflections
    	
    	boolean drewLine = false;
    	
    	for (int i = 0; i < dist; i ++) {
    		double lx = x + (double)i*Math.cos(angle);
    		double ly = y + (double)i*Math.sin(angle);
    		
    		// bounce off walls
    		if (lx < limX + 1 || lx > limX+limW - 1) {
    			if (lx < limX + 1) { lx = limX + 1; }
    			if (lx > limX+limW - 1) { lx = limX+limW - 1; }
    			angle = Math.PI - angle;
    			double newX = lx + Math.cos(angle);
    			double newY = ly + Math.sin(angle);
    			drawLine(g, newX, newY, angle, dist-i, size, count + 1);
    			break;
    		}
    		else if (ly < limY + 1 || ly > limY+limH - 1) {
    			if (ly < limY + 1) { ly = limY + 1; }
    			if (ly > limY+limH - 1) { ly = limY+limH - 1; }
    			angle = 2*Math.PI - angle;
    			double newX = lx + Math.cos(angle);
    			double newY = ly + Math.sin(angle);
    			drawLine(g, newX, newY, angle, dist-i, size, count + 1);
    			break;
    		}
    		
    		// bounce off rects
			for (Rectangle r : rects) {
				
				if (! r.contains(new Point((int)Math.round(lx), (int)Math.round(ly)))) { continue; }
				
				drewLine = true;
				
				Line edge = Calculations.nearEdge(new Point((int)Math.round(lx), (int)Math.round(ly)), r);
				
				// set new x,y and angle
				double[] vars = edge.getVar();
				
				if (vars[0] == 0) { // if slope is 0
					ly = -1*edge.getInterceptY();
	    			angle = 2*Math.PI - angle;
				}
				else {
					lx = edge.getInterceptX();
	    			angle = Math.PI - angle;
				}
    			double newX = lx + Math.cos(angle); 
    			double newY = ly + Math.sin(angle);
				
				// draw new line
    			drawLine(g, newX, newY, angle, dist-i, size, count + 1);
	    		break;
			}
			if (drewLine) { break; }
			
			// bounce off MRects
			for (MRect mr : mRects) {
				Rectangle r = mr.getBounds();
				
				if (! r.contains(new Point((int)Math.round(lx), (int)Math.round(ly)))) { continue; }
				
				drewLine = true;
				
				Line edge = Calculations.nearEdge(new Point((int)Math.round(lx), (int)Math.round(ly)), r);
				
				// set new x,y and angle
				double[] vars = edge.getVar();
				
				if (vars[0] == 0) { // if slope is 0
					ly = -1*edge.getInterceptY();
	    			angle = 2*Math.PI - angle;
				}
				else {
					lx = edge.getInterceptX();
	    			angle = Math.PI - angle;
				}
    			double newX = lx + Math.cos(angle); 
    			double newY = ly + Math.sin(angle);
				
				// draw new line
    			drawLine(g, newX, newY, angle, dist-i, size, count + 1);
	    		break;
			}
			if (drewLine) { break; }
			
			// bounce off polys
			for (Polygon p : polys) {
				
				Point point = new Point((int)Math.round(lx), (int)Math.round(ly));
				
				if (! p.contains(point)) { continue; }
				
				drewLine = true;
				
				// get edge of polygon
	    		double px = x + (double)i*Math.cos(angle); // previous pos
	    		double py = y + (double)i*Math.sin(angle);
				Line edge = Calculations.nextEdge(px, py, (int)Math.round(dist), angle, p);
				if (edge == null) { break; }
				
				// get angle of edge
				double edgeAngle = edge.getAngle();
				if (edgeAngle < 0) { edgeAngle += Math.PI; }
				
				// set new x,y
				while (p.contains(new Point((int)Math.round(lx), (int)Math.round(ly)))) {
    				lx = lx + Math.cos(angle + Math.PI);
    				ly = ly + Math.sin(angle + Math.PI);
				}
				
				// set new angle
				double inc = edgeAngle - angle; // angle of incidence
				
				angle = edgeAngle + inc;
				
				// draw new line
    			double newX = lx + 2*Math.cos(angle); 
    			double newY = ly + 2*Math.sin(angle);
    			
    			drawLine(g, newX, newY, angle, dist-i, size, count + 1);
				break;
			}
			if (drewLine) { break; }
			
    		// draw line
    		int ox = (int)Math.round(lx - (int)Math.round(size)/2);
    		int oy = (int)Math.round(ly - (int)Math.round(size)/2);
    		
    		if (i % 10 == 0) { g.fillOval(ox, oy, (int)Math.round(size), (int)Math.round(size)); }
			
			size -= (3/dist); // rate at which dots decrease in size (the smaller the distance, the higher rate)
    	}
    }
    // ---- Mode Menu ----
    private void drawHelp(Graphics g) {
    	g.setColor(new Color(0, 210, 0));
    	g.fillRect(0, 0, getWidth(), getHeight());
    	g.drawImage(helpscreen, 0, 0, null);
    	//exit button
    	if (exitB.contains(new Point(mouseX, mouseY))) {
			g.drawImage(exit2, exitB.x, exitB.y, null);
    	}
    	else {
			g.drawImage(exit1, exitB.x, exitB.y, null);
    	}
    }
    // ---- Mode Menu ----
    private void drawMenu(Graphics g) {
    	g.setColor(new Color(0, 210, 0));
    	g.fillRect(0, 0, getWidth(), getHeight());
    	
    	g.drawImage(title, 330, 15, null);
    	
    	// start button
    	if (mClick && startB.contains(new Point(mouseCX, mouseCY))) {
    		g.setColor(Color.WHITE);
			g.drawImage(start2, startB.x, startB.y, null);
    		g.drawRect(startB.x, startB.y, startB.width, startB.height);
    	}
    	else if (!mClick && startB.contains(new Point(mouseX, mouseY))) {
    		g.setColor(Color.WHITE);
			g.drawImage(start1, startB.x, startB.y, null);
    		g.drawRect(startB.x, startB.y, startB.width, startB.height);
    	}
    	else {
			g.drawImage(start1, startB.x, startB.y, null);
    	}
    	// help button
    	if (mClick && helpB.contains(new Point(mouseCX, mouseCY))) {
    		g.setColor(Color.WHITE);
			g.drawImage(help2, helpB.x, helpB.y, null);
    		g.drawRect(helpB.x, helpB.y, helpB.width, helpB.height);
    	}
    	else if (!mClick && helpB.contains(new Point(mouseX, mouseY))) {
    		g.setColor(Color.WHITE);
			g.drawImage(help1, helpB.x, helpB.y, null);
    		g.drawRect(helpB.x, helpB.y, helpB.width, helpB.height);
    	}
    	else {
			g.drawImage(help1, helpB.x, helpB.y, null);
    	}
    	//exit button
    	if (exitB.contains(new Point(mouseX, mouseY))) {
			g.drawImage(exit2, exitB.x, exitB.y, null);
    	}
    	else {
			g.drawImage(exit1, exitB.x, exitB.y, null);
    	}
    }
    // ---- Mode Pause ----
    private void drawPause(Graphics g) {
    	if (! paused) {
	    	g.setColor(new Color(255, 255, 255, 127));
	    	g.fillRect(0, 0, getWidth(), getHeight());
	    	paused = true;
    	}
    	
    	//exit button
    	if (exitB.contains(new Point(mouseX, mouseY))) {
			g.drawImage(exit2, exitB.x, exitB.y, null);
    	}
    	else {
			g.drawImage(exit1, exitB.x, exitB.y, null);
    	}
    }
    // ---- Mode WinLevel ----
    private void winLevel(Graphics g) {}
    // ---- Mode Win ----
    private void winGame(Graphics g) {
    	g.setColor(Color.WHITE);
		g.drawImage(winScreen, 0, 0, null);
    	//exit button
    	if (exitB.contains(new Point(mouseX, mouseY))) {
			g.drawImage(exit2, exitB.x, exitB.y, null);
    	}
    	else {
			g.drawImage(exit1, exitB.x, exitB.y, null);
    	}
		g.drawString("SCORE: " + score, 460, 325);
    }
    
    // ------------------------ LOGIC -------------------------
	public void refresh() {
		
		if (time >= 5 * (int)(Math.round(Golf.frameRate))) { time = 0; }
		else { time ++; }
		
		if (mode.equals("MENU")) {
			if (startB.contains(new Point(mouseX, mouseY)) && mSelect) {
				mode = "NEXT";
			}
			else if (helpB.contains(new Point(mouseX, mouseY)) && mSelect) {
				mode = "HELP";
			}
			else if (exitB.contains(new Point(mouseX, mouseY)) && mSelect) {
			    System.exit(0);
			}
			level = 0;
			score = 0;
			attempts = 0;
			Ball.setVelocity(0, 0);
		}
		else if (mode.equals("HELP")) {
			if (exitB.contains(new Point(mouseX, mouseY)) && mSelect) {
			    mode = "MENU";
			}
		}
		else if (mode.equals("NEXT")) {
			nextLevel();
		}
		else if (mode.equals("PAUSE")) {
			if (exitB.contains(new Point(mouseX, mouseY)) && mSelect) {
			    pause();
			    mode = "MENU";
			}
			
			mClick = false;
			mouseDragged = false;
			mRelease = false;
			mSelect = false;
			setMP = false;
		}
		else if (mode.equals("GAME")) {
	    	moveBall();
	    	moveShapes();
	    	collide();
	    	collideHole();
		}
		else if (mode.equals("WINLEVEL")) {
			if (level >= 18) {
				score += attempts;
				attempts = 0;
				Ball.setVelocity(0, Ball.direction);
				mode = "WIN";
			}
			else{
				score += attempts;
				attempts = 0;
				Ball.setVelocity(0, Ball.direction);
				mode = "NEXT";
			}
		}
		else if (mode.equals("WIN")) {
			if (exitB.contains(new Point(mouseX, mouseY)) && mSelect) {
			    mode = "MENU";
			}
		}
		mRelease = false;
		mSelect = false;
	}	
	// ---- Mode Next ----
    private void nextLevel() {
    	/* reads in a text file with coordinates 
    	 * and converts them to rectangles,
    	 * polygons, and the goal
    	 */
		
		level ++;
		color = new Color(rand.nextInt(16777216));
		polys = new ArrayList<Polygon>();
		rects = new ArrayList<Rectangle>();
		mRects = new ArrayList<MRect>();
		
		// read in level txt file
		try {
			Scanner inFile = new Scanner(new File("levels/level"+level+".txt"));
			
			// first line is the (x, y) of the ball
			String[] ball = inFile.nextLine().split(" ");
			Ball.set(Integer.valueOf(ball[0]), Integer.valueOf(ball[1]));
			
			// create polygons and rects
			while (inFile.hasNextLine()) {
				
				String[] line = inFile.nextLine().split(" ");
				
				// 4 numbers is a rectangle
				if (line.length == 4) {
					Rectangle r = new Rectangle(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]), Integer.valueOf(line[3]));
					rects.add(r);
				}
				// hole means the next coordinate is the pos of the hole
				if (line[0].equals("hole")) {
					line = inFile.nextLine().split(" ");
					int x = Integer.valueOf(line[0]);
					int y = Integer.valueOf(line[1]);
					Hole.x = (double)x;
					Hole.y = (double)y;
				}
				// 1 number is a polygon with that many sides, and is followed by that number of coordinates
				if (line.length == 1) {
					int count = Integer.valueOf(line[0]);
					int[] x = new int[count];
					int[] y = new int[count];
					for (int i = 0; i < count; i ++) {
						line = inFile.nextLine().split(" ");
						x[i] = Integer.valueOf(line[0]);
						y[i] = Integer.valueOf(line[1]);
					}
					Polygon p = new Polygon(x, y, count);
					polys.add(p);
				}
			}
		}
		catch (IOException e) { System.out.println("Could not import 'levels/level"+level+".txt'"); }
		
		if (moving.contains(level)) {
			try {
				Scanner inFile = new Scanner(new File("levels/move"+level+".txt"));
				
				// create polygons and rects
				while (inFile.hasNextLine()) {
					
					String[] line = inFile.nextLine().split(" ");
					
					// 2 numbers are the vx and vy
					if (line.length == 2) {
						int vx = Integer.valueOf(line[0]);
						int vy = Integer.valueOf(line[1]);
						line = inFile.nextLine().split(" ");
						int x = Integer.valueOf(line[0]);
						int y = Integer.valueOf(line[1]);
						int w = Integer.valueOf(line[2]);
						int h = Integer.valueOf(line[3]);
						Rectangle r = new Rectangle(x, y, w, h);
						MRect m = new MRect(r, vx, vy);
						mRects.add(m);
					}
				}
			}
			catch (IOException e) { System.out.println("Could not import 'levels/move"+level+".txt'"); }
		}
		mode = "GAME";
    }
    // ---- Mode Game ----
    private void moveBall() {
    	
    	if (mRelease && Ball.isMoving() == false) {
    		
    		int x = (int)(Math.round(Ball.x - mouseCX));
    		int y = (int)(Math.round(Ball.y - mouseCY));
    		double dist = Math.hypot(x, y);
    		double dir = 0;
    		
    		if (x == 0) { // prevents division by 0
    			if (y < 0) { dir = 3*Math.PI/2; }
    			if (y == 0) { dir = 0; }
    			if (y > 0) { dir = Math.PI; } }
    		else {
	    		dir = Math.atan((double)y/(double)x); }
    		if (x < 0) { dir += Math.PI; }
    		
    		if (dist > 150) { dist = 150; }
    		
			Ball.setVelocity((dist/150)*Ball.max, dir);
			/* velocity is ratio of mouse's 
			 * distance from the ball
			 */
			
			attempts ++;
    	}
    	else {
    		Ball.move();
    	}
		mRelease = false;
    }
    private void moveShapes() {
    	for (MRect rect : mRects) {
			rect.checkCollisions(rects);
    		rect.move();
    	}
    }
    private void collide() {
		/* l - left
		 * r - right
		 * t - top
		 * b - bottom
		 */
		int x = (int)Math.round(Ball.x);
		int y = (int)Math.round(Ball.y);
		int lx = (int)Math.round(Ball.x - (double)(Ball.size/2));
		int rx = (int)Math.round(Ball.x + (double)(Ball.size/2));
		int ty = (int)Math.round(Ball.y - (double)(Ball.size/2));
		int by = (int)Math.round(Ball.y + (double)(Ball.size/2));
		
		// collide into walls
		if (lx <= limX) {
			Ball.x = limX + Ball.size/2 + 1;
			Ball.direction = Math.PI - Ball.direction;
			Ball.move();
		}
		if (rx >= limX + limW) {
			Ball.x = limX + limW - Ball.size/2 - 1;
			Ball.direction = Math.PI - Ball.direction;
			Ball.move();
		}
		if (ty <= limY) {
			Ball.y = limY + Ball.size/2 + 1;
			Ball.direction = 2*Math.PI - Ball.direction;
			Ball.move();
		}
		if (by >= limY + limH) {
			Ball.y = limY + limH - Ball.size/2 - 1;
			Ball.direction = 2*Math.PI - Ball.direction;
			Ball.move();
		}
		
		// collide into rects
		boolean intersects = false;
		for (Rectangle r : rects) {
			
			for (Point p : Ball.getPoints()) {
				if (r.contains(p)) {
					intersects = true; } }
			
			if (intersects) {			
				Ellipse2D.Double ball = new Ellipse2D.Double(Ball.x - Ball.size/2, Ball.y - Ball.size/2, Ball.size, Ball.size);
				
				if (ball.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())) {
					if (r.contains(new Point(lx - 1, y))) { // ball hits left of rect
						Ball.x = r.getX() + r.getWidth() + (double)(Ball.size/2) + 1;
						Ball.direction = Math.PI - Ball.direction;
						Ball.move(); }
					else if (r.contains(new Point(rx + 1, y))) { // ball hits right of rect
						Ball.x = r.getX() - (double)(Ball.size/2) - 1;
						Ball.direction = Math.PI - Ball.direction;
						Ball.move(); }
					else if (r.contains(new Point(x, ty - 1))) { // ball hits top of rect
						Ball.y = r.getY() + r.getHeight() + (double)(Ball.size/2) + 1;
						Ball.direction = 2*Math.PI - Ball.direction;
						Ball.move(); }
					else if (r.contains(new Point(x, by + 1))) { // ball hits bottom of rect
						Ball.y = r.getY() - (double)(Ball.size/2) - 1;
						Ball.direction = 2*Math.PI - Ball.direction;
						Ball.move(); }
					break;
				}
			}
		}
		
		intersects = false;
		for (MRect mr : mRects) {
			
			Rectangle r = mr.getBounds();
			
			for (Point p : Ball.getPoints()) {
				if (r.contains(p)) {
					intersects = true; } }
			
			if (intersects) {			
				Ellipse2D.Double ball = new Ellipse2D.Double(Ball.x - Ball.size/2, Ball.y - Ball.size/2, Ball.size, Ball.size);
				
				if (ball.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())) {
					if (r.contains(new Point(lx - 1, y))) { // ball hits left of rect
						Ball.x = r.getX() + r.getWidth() + (double)(Ball.size/2) + 1;
						Ball.direction = Math.PI - Ball.direction;
						Ball.move(); }
					else if (r.contains(new Point(rx + 1, y))) { // ball hits right of rect
						Ball.x = r.getX() - (double)(Ball.size/2) - 1;
						Ball.direction = Math.PI - Ball.direction;
						Ball.move(); }
					else if (r.contains(new Point(x, ty - 1))) { // ball hits top of rect
						Ball.y = r.getY() + r.getHeight() + (double)(Ball.size/2) + 1;
						Ball.direction = 2*Math.PI - Ball.direction;
						Ball.move(); }
					else if (r.contains(new Point(x, by + 1))) { // ball hits bottom of rect
						Ball.y = r.getY() - (double)(Ball.size/2) - 1;
						Ball.direction = 2*Math.PI - Ball.direction;
						Ball.move(); }
					break;
				}
			}
		}
		
		// collide into polygons
		intersects = false;
		for (Polygon p : polys) {
			
			Ellipse2D.Double ball = new Ellipse2D.Double(Ball.x - Ball.size/2, Ball.y - Ball.size/2, Ball.size, Ball.size);
			
			// checks for intersection
			for (Point point : Ball.getPoints()) {
				if (p.contains(point)) {
					intersects = true;
					break; } }
			if (intersects) {
				// get edge and vertex of polygon
				Line edge = Calculations.nearEdge(new Point((int)Math.round(Ball.x), (int)Math.round(Ball.y)), p);
				Point vertex = Calculations.nearVertex(new Point((int)Math.round(Ball.x), (int)Math.round(Ball.y)), p);
				
				// get angle of edge
				double angle = edge.getAngle();
				
				// set new coordinates of ball
				double initialD = Ball.direction;
				double initialV = Ball.velocity;
				Ball.setVelocity(1, Ball.direction + Math.PI);
				/* move the ball back in the direction
				 * it came from until it no longer 
				 * intersects the polygon. Then 
				 * calculate the new angle of the ball
				 */
				while (intersects == true) {
					// check if it intersects
					intersects = false;
					for (Point point : Ball.getPoints()) {
						if (p.contains(point)) {
							intersects = true;
							break; } }
					// if so, move by 1
					if (intersects) {
						Ball.move(); }
				}
				Ball.setVelocity(initialV, initialD);
				
				// set new angle and speed
				double inc = angle - Ball.direction; // angle of incidence
				
				double dir = angle + inc;
				Ball.setVelocity(Ball.velocity, dir);
				
				Ball.move();
				break;
			}
		}
    }
    private void collideHole() {
    	if (Hole.contains(Ball.x, Ball.y)) {
    		mode = "WINLEVEL";
    	}
    }
}

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JViewport;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private ImagePanel board;

	int [][] matrix = new int[8][16];
	
	public GUI(){
		setSize(400, 300);
		setLocation(200,200);
		add(createPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fleds!");
        
        draw(matrix);
        
        return;
	}

	private void sendMatrix() {
		
		StringBuffer str = new StringBuffer();
		
		int r, g, b;
		
		for(int col = 0; col < matrix[0].length; col++) {
			r = 0;
			g = 0;
			b = 0;
			for(int row = 0; row < matrix.length; row++) {
				if ((matrix[row][col] & 0x04) == 1)
					r |= 1;
				if ((matrix[row][col] & 0x02) == 1)
					g |= 1;
				if ((matrix[row][col] & 0x01) == 1)
					b |= 1;
				r <<= 1;
				g <<= 1;
				b <<= 1;
				str.append(r).append(g).append(b).append('-');
			}
		}

		System.out.println(str);
		
		return;
	}
	
	private void draw(int [][] matrix) {
		
		if (matrix == null)
			return;
		
		board.setBoard(matrix);
		board.repaint();
		
		sendMatrix();
		
		return;
	}
	
	//JPanel
	private JPanel createPanel(){
		panel = new JPanel();
		
		panel.setSize(this.getSize());
		panel.setLayout(null);
		
		//Tablero
		board = new ImagePanel();
		board.setSize(16 * 20, 8 * 20);
		board.setLocation(10, 10);
		panel.add(board);
		
		board.addMouseListener(
				new MouseListener() {
				
					public void mouseReleased(MouseEvent e) {
					}
				
					public void mousePressed(MouseEvent e) {
					}
				
					public void mouseExited(MouseEvent e) {
					}
				
					public void mouseEntered(MouseEvent e) {
					}
				
					public void mouseClicked(MouseEvent e) {
						int x = e.getX() / 20;
						int y = e.getY() / 20;
						
						if (e.getButton() == MouseEvent.BUTTON1)
							matrix[y][x] = (matrix[y][x] + 1) % 8;
						else if (e.getButton() == MouseEvent.BUTTON3)
							matrix[y][x] = (matrix[y][x] + 7) % 8;
						
						// redibuja
						draw(matrix);				
					}
				}
		);

		//rnd
		JButton butRND = this.createButton("RND", 20, 180, 10, 200);
		panel.add(butRND);

		butRND.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent arg0){
						// Genera una matrix rnd
						for(int row = 0; row < matrix.length; row++) {
							for(int col = 0; col < matrix[0].length; col++) {
								matrix[row][col] = (int)(Math.random() * 8);
							}
						}
						
						draw(matrix);
					}
				}
		);
		
		return panel;
	}

	// JButton
	private JButton createButton(String text, int h, int w, int x, int y) {
		JButton but = new JButton();

		but.setText(text);
		but.setSize(w, h);
		but.setLocation(x,y);
		
		return but;
	}

	// JTextField
	private JTextField createTextField(int h, int w, int x, int y) {
		JTextField textField = new JTextField();
		
		textField.setSize(w, h);
		textField.setLocation(x,y);
		
		return textField;
	}
	
	// JLabel
	private JLabel createLabel(String text, int h, int w, int x, int y) {
		JLabel label = new JLabel();

		label.setText(text);
		label.setSize(w, h);
		label.setLocation(x,y);
		
		return label;
	}

}

class ImagePanel extends JViewport {

	private static final long serialVersionUID = 1L;

	private static BufferedImage leds[] = new BufferedImage[8];
	private int [][] ownMatrix = null;
	
	public ImagePanel() {
		try {
			leds[0] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "black.png"));
			leds[1] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "blue.png"));
			leds[2] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "green.png"));
			leds[3] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "cyan.png"));
			leds[4] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "red.png"));
			leds[5] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "pink.png"));
			leds[6] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "yellow.png"));
			leds[7] = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("resources" + File.separator + "white.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBoard(int [][] matrix) {
		ownMatrix = new int[matrix.length][matrix[0].length];
		
		for(int row = 0; row < matrix.length; row++) {
			for(int col = 0; col < matrix[0].length; col++) {
				ownMatrix[row][col] = matrix[row][col];
			}
		}
	}

	protected void paintComponent(Graphics g) {  
		//super.paintComponent(g);

		if(ownMatrix == null){
			return;
		}
		
		int dim = 20;
		
		for(int row = 0; row < ownMatrix.length; row++) {
			for(int col = 0; col < ownMatrix[0].length; col++) {
				g.drawImage(leds[ownMatrix[row][col]], col * dim, row * dim, null);
			}
		}
	} 
}
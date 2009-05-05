package fleds;

import java.awt.Font;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private ImagePanel board;
	private JTextField txtTexto;
	private JTextArea txtSalida;

	int [][] matrix = new int[8][16];
	
	public GUI(){
		setSize(800, 600);
		setLocation(200,200);
		add(createPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fleds!");
        
        draw(matrix);
        
        return;
	}

	private void sendText(String text) {

		//TODO:

		return;
	}

	private void sendMatrix() {
		
		StringBuffer str = new StringBuffer();
		
		int r, g, b, set = 1;
		
		for(int col = 0; col < matrix[0].length; col++) {
			r = 0;
			g = 0;
			b = 0;
			set = 1;
			for(int row = 0; row < matrix.length; row++) {
				if ((matrix[row][col] & 0x04) == 4)
					r |= set;
				if ((matrix[row][col] & 0x02) == 2)
					g |= set;
				if ((matrix[row][col] & 0x01) == 1)
					b |= set;
				set <<= 1;
			}
			str.append((char)(col+'@')).append(' ').append(r).append(' ').append(g).append(' ').append(b).append(' ');
		}

		txtSalida.setText(str.toString());
		
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
		int y = 0;
		panel = new JPanel();
		
		panel.setSize(this.getSize());
		panel.setLayout(null);
		
		//Tablero
		board = new ImagePanel();
		board.setSize(16 * 40, 8 * 40);
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
						int x = e.getX() / 40;
						int y = e.getY() / 40;
						
						if (e.getButton() == MouseEvent.BUTTON1)
							matrix[y][x] = (matrix[y][x] + 1) % 8;
						else if (e.getButton() == MouseEvent.BUTTON3)
							matrix[y][x] = (matrix[y][x] + 7) % 8;
						
						// redibuja
						draw(matrix);				
					}
				}
		);

		//Salida
		JLabel lblSalida = createLabel("Salida:", 20, 60, board.getX(), board.getY() + board.getHeight() + 10);
		panel.add(lblSalida);
		txtSalida = new JTextArea("");
		txtSalida.setLocation(lblSalida.getX() + lblSalida.getWidth(), lblSalida.getY());
		txtSalida.setSize(16 * 40 - lblSalida.getWidth(), 50);
		txtSalida.setFont(new Font("Monospace", 0, 12));
		txtSalida.setLineWrap(true);
		txtSalida.setWrapStyleWord(true);
		panel.add(txtSalida);
		
		//Texto
		JLabel lblTexto = createLabel("Texto:", 20, 60, 10, txtSalida.getHeight() + txtSalida.getY() + 10);
		panel.add(lblTexto);
		txtTexto = createTextField(20, 16 * 40 - lblTexto.getWidth(), lblTexto.getWidth() + 10, lblTexto.getY());
		txtTexto.setText("Trabajo Practico Sistemas Operativos");
		txtTexto.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(txtTexto);
		
		//enviaTexto
		JButton butSendText = this.createButton("Enviar", 20, 120, txtTexto.getX() + txtTexto.getWidth() + 10, txtTexto.getY());
		panel.add(butSendText);

		butSendText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent arg0){
						sendText(txtTexto.toString());
					}
				}
		);
		
		//rnd
		JButton butRND = this.createButton("Azar!", 20, 120, board.getX() + board.getWidth() + 10, y+=10);
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

		//
		JButton butScroll = this.createButton("Scroll", 20, 120, board.getX() + board.getWidth() + 10, y+=30);
		panel.add(butScroll);
		
		butScroll.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent arg0){
						//TODO:
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
		
		int dim = 40;
		
		for(int row = 0; row < ownMatrix.length; row++) {
			for(int col = 0; col < ownMatrix[0].length; col++) {
				g.drawImage(leds[ownMatrix[row][col]], col * dim, row * dim, null);
			}
		}
	} 
}
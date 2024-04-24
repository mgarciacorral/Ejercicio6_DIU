import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MusicPlayer extends JFrame
{
    private Clip clip;
    private File audio;
    private int contador = 0;
    private JButton play = new JButton();
    private JButton atras = new JButton();
    private JButton pause = new JButton();
    private JButton alante = new JButton();
    private JSlider prorgess = new JSlider();
    private JLabel segundos = new JLabel();
    private JLabel icono = new JLabel();
    private ImageIcon[] icon = new ImageIcon[4];
    private File[] audios = new File[4];
    private JPanel botones = new JPanel();
    private JPanel barraP = new JPanel();
    private JPanel imagen = new JPanel();

    public MusicPlayer()
    {
        setSize(400, 400);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTitle("Reproductor de audio");
        setIconImage(new ImageIcon("src/resources/icono.png").getImage());

        imagen.setLayout(new BorderLayout());

        imagen.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        barraP.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        imagen.add(icono, BorderLayout.CENTER);
        botones.add(atras);
        botones.add(play);
        botones.add(pause);
        botones.add(alante);
        barraP.add(prorgess);
        barraP.add(segundos);

        add(imagen);
        add(botones);
        add(barraP);

        atras.setIcon(new ImageIcon("src/resources/backward.png"));
        play.setIcon(new ImageIcon("src/resources/play.png"));
        pause.setIcon(new ImageIcon("src/resources/pause.png"));
        alante.setIcon(new ImageIcon("src/resources/forward.png"));
        prorgess.setValue(0);
        prorgess.setEnabled(false);
        segundos.setText("0:00");

        audios[0] = new File("src/resources/Nocturne in E flat major Op.9 No.2 - Frédéric François Chopin.wav");
        audios[1] = new File("src/resources/Double Violin Concerto 1st Movement - Johann Sebastian Bach.wav");
        audios[2] = new File("src/resources/Sonata No.13 - Ludwig Van Beethoven.wav");
        audios[3] = new File("src/resources/PerdioSuVidaEnElLol.wav");

        icon[0] = new ImageIcon("src/resources/Nocturne in E flat major Op.9 No.2 - Frédéric François Chopin.png");
        icon[1] = new ImageIcon("src/resources/Double Violin Concerto 1st Movement - Johann Sebastian Bach.png");
        icon[2] = new ImageIcon("src/resources/Sonata No.13 - Ludwig Van Beethoven.png");
        icon[3] = new ImageIcon("src/resources/PerdioSuVidaEnElLol.png");

        ponerClip();

        alante.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(contador < 3){
                    contador++;
                } else {
                    contador = 0;
                }

                ponerClip();
            }
        });

        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        clip.start();
                        while(clip.getMicrosecondPosition() < clip.getMicrosecondLength()){
                            prorgess.setValue((int) clip.getMicrosecondPosition());

                            if((int) clip.getMicrosecondPosition() / 1000000 % 60 < 10){
                                segundos.setText((int) clip.getMicrosecondPosition() / 1000000 / 60 + ":0" + (int) clip.getMicrosecondPosition() / 1000000 % 60);
                            }else
                            {
                                segundos.setText((int) clip.getMicrosecondPosition() / 1000000 / 60 + ":" + (int) clip.getMicrosecondPosition() / 1000000 % 60);
                            }
                        }
                    }
                }).start();
            }
        });

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clip.stop();
            }
        });

        atras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(contador > 0){
                    contador--;
                } else {
                    contador = 3;
                }

                ponerClip();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void ponerClip()
    {
        audio = audios[contador];
        icono.setIcon(icon[contador]);

        try{
            clip = AudioSystem.getClip();
            AudioInputStream sound = AudioSystem.getAudioInputStream(audio);
            clip.open(sound);
            prorgess.setMaximum((int) clip.getMicrosecondLength());
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null,
                    "ERROR\nNo hay fichero de audio", "alerta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

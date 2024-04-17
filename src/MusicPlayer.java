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
    JButton play = new JButton();
    JButton stop = new JButton();
    JButton pause = new JButton();
    JButton open = new JButton();
    JSlider prorgess = new JSlider();
    JLabel segundos = new JLabel();
    public MusicPlayer()
    {
        setSize(400, 100);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setTitle("Reproductor de audio");
        setIconImage(new ImageIcon("src/resources/play.png").getImage());

        UIManager.LookAndFeelInfo looks[];
        looks = UIManager.getInstalledLookAndFeels();
        try{
            UIManager.setLookAndFeel(looks[1].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch(Exception e) {}

        add(open);
        add(play);
        add(pause);
        add(stop);
        add(prorgess);
        add(segundos);

        open.setIcon(new ImageIcon("src/resources/abrir.png"));
        play.setIcon(new ImageIcon("src/resources/play.png"));
        pause.setIcon(new ImageIcon("src/resources/pausa.png"));
        stop.setIcon(new ImageIcon("src/resources/stop.png"));
        prorgess.setValue(0);
        segundos.setText("0:00");

        todo0();
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                todo0();
                JFileChooser file = new JFileChooser();
                file.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("WAV file", "wav"));
                file.showOpenDialog(open);
                audio = file.getSelectedFile();

                try{
                    clip = AudioSystem.getClip();
                    AudioInputStream sound = AudioSystem.getAudioInputStream(audio);
                    clip.open(sound);
                    play.setEnabled(true);
                    prorgess.setMaximum((int) clip.getMicrosecondLength());
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null,
                            "ERROR\nNo hay fichero de audio", "alerta",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                play.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(true);
                new Thread(new Runnable() {
                    public void run() {
                        clip.start();
                        while(clip.getMicrosecondPosition() < clip.getMicrosecondLength()){
                            prorgess.setValue((int) clip.getMicrosecondPosition());
                            segundos.setText((int) clip.getMicrosecondPosition() / 1000000 / 60 + ":" + (int) clip.getMicrosecondPosition() / 1000000 % 60);
                        }
                    }
                }).start();
            }
        });

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                play.setEnabled(true);
                pause.setEnabled(false);
                stop.setEnabled(true);
                clip.stop();
            }
        });

        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                play.setEnabled(true);
                pause.setEnabled(false);
                stop.setEnabled(false);
                clip.stop();
                clip.setMicrosecondPosition(0);
                prorgess.setValue(0);
                segundos.setText("0:00");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void todo0()
    {
        play.setEnabled(false);
        pause.setEnabled(false);
        stop.setEnabled(false);
        prorgess.setEnabled(false);
        prorgess.setValue(0);
    }
}

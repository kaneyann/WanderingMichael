package RPG;

import java.awt.Container;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    TitlePanel tp;
    public GameFrame() {
        //-- タイトル
        setTitle("さすらいのマイケル");
        setResizable(false);
        //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //-- パネル生成
        tp = new TitlePanel();
        Container contentPane = getContentPane();
        contentPane.add(tp);

        //-- パネルサイズに合わせてフレームサイズを設定
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
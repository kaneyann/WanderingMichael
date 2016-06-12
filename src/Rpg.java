package RPG;

import java.awt.Container;

import javax.swing.JFrame;

public class Rpg extends JFrame {
    public static boolean startFlag = false;
    public static boolean restartFlag = false;

    public Rpg() {
        // タイトルを設定
        setTitle("さすらいのマイケル");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        boolean flag = true;
        GameFrame f = null;
        Rpg r = null;
        while ( true ) {
            if ( flag == true ) {
                f = new GameFrame();
                while ( startFlag == false ) {
                    System.out.println();
                }
                r = new Rpg();
                flag = false;
            }
            System.out.println();
            if ( restartFlag == true ) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
                flag = true;
                restartFlag = false;
                startFlag = false;
                r.setVisible(false);
            }
        }
    }
}

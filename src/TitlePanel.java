package RPG;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TitlePanel extends JPanel implements ActionListener {
    private static final int WIDTH = 805;    // パネルの幅
    private static final int HEIGHT = 605;    // パネルの高さ
    private static final Color  BACK = new Color(0, 102, 0);    // パネルの背景色
    private static final Font font = new Font("MS ゴシック", Font.BOLD, 70);    // 文字のフォント
    private static final Font font2 = new Font("MS ゴシック", Font.BOLD, 22);    // 文字のフォント
    JButton start, end;

    public TitlePanel() {
        setPreferredSize(new Dimension(MainPanel.WIDTH, MainPanel.HEIGHT));    // パネルの推奨サイズを設定
        setLayout(null);    // コンポーネントを座標で張り付ける
        setBackground(BACK);
        start = new JButton("スタート");
        start.setBounds(250, 400, 200, 100);
        start.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 26));
        start.addActionListener(this);
        add(start);
        end = new JButton("閉じる");
        end.setBounds(530, 400, 200, 100);
        end.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 26));
        end.addActionListener(this);
        add(end);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = getToolkit().getImage("image/hero.jpg");
        g.drawImage(img, 40, 100, 84, 100, this);
        Image imgT = getToolkit().getImage("image/tanni.jpg");
        g.drawImage(imgT, 840, 110, 84, 84, this);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("さすらいのマイケル", 135, 100);
        g.setFont(font2);
        g.drawString("・マップにある「たんい」をゲットするゲームだよ", 260, 230);
        g.drawString("・スタートを押すとすぐに始まります", 260, 280);
        g.drawString("・キャラの移動：方向キー", 260, 330);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == end) { // ボタンクリックイベント
            System.exit(0); // 終了
        }

        if ( e.getSource() == start ) { // ボタンクリックイベント
            Rpg.startFlag = true;
            // ウインドウを閉じる
            Component c = (Component)e.getSource();
            Window w = SwingUtilities.getWindowAncestor(c);
            w.dispose();
        }
    }
}

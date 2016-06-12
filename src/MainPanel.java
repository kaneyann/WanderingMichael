package RPG;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements KeyListener, Runnable, Common {
    // パネルサイズ
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    // マップ
    private Mapp map;

    // 勇者
    private Chara hero;

    // アクションキー
    private ActionKey leftKey;
    private ActionKey rightKey;
    private ActionKey upKey;
    private ActionKey downKey;
    private ActionKey spaceKey;

    // 勇者のHP
    public int hitPoint;
    // 勇者のAP
    public int attackPoint;

    // 勇者のターン移行フラグ
    private boolean heroTurnFlag = true;
    // 敵のターン移行フラグ
    private boolean enemyTurnFlag = true;

    // ゲームループスレッド
    private Thread gameLoop;
    // タイマースレッド
    private Thread threadTime;

    // 乱数生成器
    private Random rand = new Random();

    // メッセージウィンドウ
    private MessageWindow messageWindow;
    // ウィンドウを表示する領域
    private static Rectangle WND_RECT = new Rectangle(300, 260, 356, 140);

    // ステータスウィンドウ
    private MessageWindow statusWindow;
    // ウィンドウを表示する領域
    private static Rectangle S_WND_RECT = new Rectangle(840, 0, 120, 30);

    // 命令ウィンドウ
    private MessageWindow commandWindow;
    // ウィンドウを表示する領域
    private static Rectangle C_WND_RECT = new Rectangle(350, 0, 250, 30);

    // ゲームオーバーウィンドウ
    private MessageWindow gameOverWindow;
    // ウィンドウを表示する領域
    private static Rectangle G_WND_RECT = new Rectangle(300, 260, 356, 140);

    private int number = 60;

    private boolean goalFlag = false;
    private boolean gameOverflag = false;

    public MainPanel() {
        // パネルの推奨サイズを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // パネルがキー操作を受け付けるように登録する
        setFocusable(true);
        addKeyListener(this);

        // アクションキーを生成
        leftKey = new ActionKey();
        rightKey = new ActionKey();
        upKey = new ActionKey();
        downKey = new ActionKey();
        spaceKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);

        // マップを生成
        map = new Mapp("map/map.dat", "event/event.dat", this);

        // 勇者を生成
        /*
        Random rnd = new Random();
        int ran = rnd.nextInt(18) + 1;
        int x = ran;
        ran = rnd.nextInt(28) + 1;
        int y = ran;
        map.map[x][y] = 0;
        */
        hero = new Chara(1, 1, 0, DOWN, 0, 20, 5, map);
        hitPoint = 10;
        attackPoint = 5;

        // マップにキャラクターを登録
        // キャラクターはマップに属する
        map.addChara(hero);

        // メッセージウィンドウを追加
        messageWindow = new MessageWindow(WND_RECT);
        // ステータスウィンドウを追加
        statusWindow = new MessageWindow(S_WND_RECT);
        // 命令ウィンドウを追加
        commandWindow = new MessageWindow(C_WND_RECT);
        //ゲームオーバーウィンドウを追加
        gameOverWindow = new MessageWindow(G_WND_RECT);

        // ゲームループの生成と開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);

    	// X方向のオフセット(距離)を計算
    	int offsetX = hero.getPx() - ( MainPanel.WIDTH / 2 );

    	if ( offsetX < 0 ) {
    		offsetX = 0;    // マップの左端ではスクロールしないようにする
    	}
    	if ( offsetX > map.getWidth() - MainPanel.WIDTH ) {
    		offsetX = map.getWidth() - MainPanel.WIDTH;    // マップの右端ではスクロールしないようにする
    	}

    	// Y方向のオフセット(距離)を計算
    	int offsetY = hero.getPy() - ( MainPanel.HEIGHT / 2 );

    	if ( offsetY < 0 ) {
    		offsetY = 0;    // マップの上端ではスクロールしないようにする
    	}
    	if ( offsetY > map.getHeight() - MainPanel.HEIGHT ) {
    		offsetY = map.getHeight() - MainPanel.HEIGHT;    // マップの下端ではスクロールしないようにする
    	}

    	// マップを描く
    	// キャラクターはマップを描いてくれる(キャラクターはマップに属するため)
    	map.draw(g, offsetX, offsetY);

    	if ( gameOverflag == true ) {
    	    gameOverWindow.drawGameOver(g);
    	    return;
    	}

    	if ( goalFlag == true ) {
            messageWindow.drawGoal(g);
            return;
        }

    	// メッセージウィンドウを描画
    	messageWindow.draw(g);
        // 命令ウィンドウを描画
        commandWindow.drawCommand(g);
    	// ステータスウィンドウを描画
    	if ( statusWindow.drawStatus(g, hitPoint, attackPoint) == true ) {
    	    gameOverflag = true;
    	}

    	if ( number < 0 ) {
    	    hitPoint--;
    	    if ( hitPoint == 0 ) { return; }
    	    number = 60;
    	}

    }

    // キーが押されたらキーの状態を「押された」に変える
    public void keyPressed(KeyEvent e) {
    	// 押されたキーを調べて取得
    	int KeyCode = e.getKeyCode();

    	switch ( KeyCode ) {
    	case KeyEvent.VK_LEFT :
    		leftKey.press();
    		break;
    	case KeyEvent.VK_RIGHT :
    		rightKey.press();
    		break;
    	case KeyEvent.VK_UP :
    		upKey.press();
    		break;
    	case KeyEvent.VK_DOWN :
    		downKey.press();
    		break;
        case KeyEvent.VK_SPACE :
            spaceKey.press();
            break;
    	}
    }

    // キーが離されたらキーの状態を「離された」に変える
    public void keyReleased(KeyEvent e) {
    	// 離されたキーを調べて取得
    	int KeyCode = e.getKeyCode();

    	switch ( KeyCode ) {
    	case KeyEvent.VK_LEFT :
    		leftKey.release();
    		break;
    	case KeyEvent.VK_RIGHT :
    		rightKey.release();
    		break;
    	case KeyEvent.VK_UP :
    		upKey.release();
    		break;
    	case KeyEvent.VK_DOWN :
    		downKey.release();
    		break;
        case KeyEvent.VK_SPACE :
            spaceKey.release();
            break;
    	}
    }

    public void keyTyped(KeyEvent e) {
    }

    // ゲームループ
    public void run() {
    	while ( true ) {
    	    number--;
    		// キー入力をチェックする
    		if ( messageWindow.isVisible() == true ) {    // メッセージウィンドウ表示中なら
    		    messageWindowCheckInput();
    		} else {
    		    if ( heroTurnFlag == true ) {
    		        mainWindowCheckInput();    // メイン画面のキー入力チェック
    		    }
    		}

    		if ( messageWindow.isVisible() == false ) {
    		    // 勇者の移動処理
    		    heroMove();
    		    // 勇者以外のキャラクターの移動処理
    		    charaMove();
    		}

    		// 再描画
    		repaint();

    		if ( gameOverflag == true ) {
    		    Rpg.restartFlag = true;
    		    repaint();
    		    /*
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
                */
    		    return;
    		}
    		if ( goalFlag == true ) {
    		    Rpg.restartFlag = true;
    		    repaint();
    		    /*
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
                */
    		    return;
    		}

    		// 休止
    		try {
    			Thread.sleep(20);
    		} catch (InterruptedException  e) {
    			e.printStackTrace();
    		}
    	}
    }

    // メインウィンドウでのキー入力をチェックする
    public void mainWindowCheckInput() {
    	if ( leftKey.isPressed() == true ) {     // キーが押されている
    		if ( hero.isMoving() == false ) {    // 移動中でなければ
    			hero.setDirection(LEFT);         // 方向をセットして
    			hero.setMoving(true);            // 移動開始
    		}
    	}
    	if ( rightKey.isPressed() == true ) {
    		if ( hero.isMoving() == false ) {
    			hero.setDirection(RIGHT);
    			hero.setMoving(true);
    		}
    	}
    	if ( upKey.isPressed() == true ) {
    		if ( hero.isMoving() == false ) {
    			hero.setDirection(UP);
    			hero.setMoving(true);
    		}
    	}
    	if ( downKey.isPressed() == true ) {
    		if ( hero.isMoving() == false ) {
    			hero.setDirection(DOWN);
    			hero.setMoving(true);
    		}
    	}
    	Event item = hero.search();
        if ( item != null ) {
            if ( hero.isMoving() == true ) { return; }    // 移動中は表示できない

            // しらべる
            //Event item = hero.search();
            //if ( item != null ) {
                // メッセージをセットする
                messageWindow.setMessage("しんきゅうおめでとう！");
                // メッセージウィンドウを表示
                goalFlag = true;
                repaint();
                /*
                Rpg.restartFlag = true;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
                */
                //closeFlag = true;
                // ここにアイテム入手処理を入れる
                if ( item.itemName.equals("かいふくハート") ) {
                    hitPoint += 5;
                }
                if ( item.itemName.equals("ほのおのけん") ) {
                    attackPoint += 5;
                }
                // 取得したアイテムを削除
                map.removeEvent(item);
                /*
                // 敵のターンに移行
                heroTurnFlag = false;
                enemyTurnFlag = true;
                */
                //return;    // しらべた場合ははなさない
            //}

            // はなす
            if ( messageWindow.isVisible() == false ) {
                Chara chara = hero.talkWith();
                if ( chara != null ) {
                    // メッセージをセットする
                    messageWindow.setMessage(chara.getMessage());
                } else {
                    messageWindow.setMessage("そのほうこうには　だれもいない。");
                }
                messageWindow.show();    // メッセージウィンドウを表示
            }
        }
    }

    //--- メッセージウィンドウでのキー入力をチェックする
    private void messageWindowCheckInput() {
        if ( spaceKey.isPressed() == true ) {
            if ( messageWindow.nextMessage() == true ) {    // 次のメッセージへ
                messageWindow.hide();    // 終了したら隠す
            }
        }
    }

    //--- 勇者の移動処理
    private void heroMove() {
        // 移動中なら移動する
        if ( hero.isMoving() == true && heroTurnFlag == true ) {
            if ( hero.move() == true ) {    // 移動する
                // 移動が完了した場合の処理をここに書く
                /*
                // 敵のターンに移行
                heroTurnFlag = false;
                enemyTurnFlag = true;
                */
            }
        }
    }

    //--- 勇者以外のキャラクターの移動処理
    private void charaMove() {
        // マップにいるキャラクターを取得
        Vector<Chara> charas = map.getCharas();
        for ( int i = 0; i < charas.size(); i++ ) {
            Chara chara = (Chara)charas.get(i);
            // キャラクターの移動タイプを調べる
            if ( chara.getMoveType() == 1 ) {    // 移動するタイプなら
                if ( chara.isMoving() == true ) {    // 移動中なら
                    chara.move();    // 移動する
                } else if ( enemyTurnFlag == true ) {
                    if ( rand.nextDouble() < Chara.PROB_MOVE ) {
                    // 移動していない場合はChara.PROB_MOVEの確率で移動する
                    // 移動する方向はランダムに決める
                    chara.setDirection(rand.nextInt(4));
                    chara.setMoving(true);
                    }
                    /*
                    // 勇者のターンに移行
                    enemyTurnFlag = false;
                    heroTurnFlag = true;
                    */
                }
            }
        }


    }
}

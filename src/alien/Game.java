package alien;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Game extends Application {
	private int score;
	private final static int WIDTH = 600;
	private final static int HEIGHT = 600;
	private final static int NBPINAPPLES = 400;
	
	private static Collection<Sprite> pineapples;

	public static String getRessourcePathByName(String name) {
		return Game.class.getResource('/' + name).toString();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void changeSpeed(Sprite pinapple) {
		int max = 5;
		pinapple.setSpeed(max * Math.random() - max / 2, max * Math.random() - max / 2);
	}
	
	private static void spawnPineapples(int count) {
		Sprite pinappleorig = new Sprite(getRessourcePathByName("images/pinapple.png"), 8, 12, WIDTH, HEIGHT);
		for (int i = 0; i < NBPINAPPLES; i++) {
			Sprite pinapple = new Sprite(pinappleorig);
			pinapple.setPosition(WIDTH * Math.random(), HEIGHT * Math.random());
			Game.pineapples.add(pinapple);
		}
	}

	public void start(Stage stage) {

		stage.setTitle("Alien vs Pinapples");
		stage.setResizable(false);

		Group root = new Group();
		Scene scene = new Scene(root);
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		gc.setFill(Color.BISQUE);
		gc.setStroke(Color.RED);
		gc.setLineWidth(1);

		Image space = new Image(getRessourcePathByName("images/space.jpg"), WIDTH, HEIGHT, false, false);

		Sprite spaceship = new Sprite(getRessourcePathByName("images/alien.png"), 62, 36, WIDTH, HEIGHT);
		spaceship.setPosition(WIDTH / 2 - spaceship.width() / 2, HEIGHT / 2 - spaceship.height() / 2);

		Game.pineapples = new ArrayList<Sprite>();
		Game.spawnPineapples(NBPINAPPLES);

		stage.setScene(scene);
		stage.show();

		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				spaceship.setSpeed(0, 0);
				spaceship.setPosition(e.getX() - spaceship.width() / 2, e.getY() - spaceship.height() / 2);
			}
		};

		scene.setOnMouseDragged(mouseHandler);
		scene.setOnMousePressed(mouseHandler);

		MediaPlayer mediaPlayer = null;
		try {
			mediaPlayer = new MediaPlayer(new Media(getRessourcePathByName("sounds/Engine.mp4")));// Only format allowed
																									// in the context of
																									// the project (mp4)

		} catch (MediaException e) {
			// in case of a platform without sound capabilities
		}
		MediaPlayer mediaPlayerPffft = null;
		try {
			mediaPlayerPffft = new MediaPlayer(new Media(getRessourcePathByName("sounds/Explosion.mp4")));

		} catch (MediaException e) {
			// in case of a platform without sound capabilities
		}

		final MediaPlayer mediaPlayerFinalCopy = mediaPlayer;// final copies are needed to transmit to inner classes
		final MediaPlayer mediaPlayerBoomFinalCopy = mediaPlayerPffft;

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				spaceship.changeSpeed(e.getCode());
				if (mediaPlayerFinalCopy != null) {
					mediaPlayerFinalCopy.stop();
					mediaPlayerFinalCopy.play();
				}

			    if (e.getCode() == KeyCode.A) {
			        Game.spawnPineapples(10);
			    }
			}
		});

		new AnimationTimer() {
			public void handle(long arg0) {
				gc.drawImage(space, 0, 0);

				spaceship.updatePosition();

				Iterator<Sprite> it = Game.pineapples.iterator();
				while (it.hasNext()) {
					Sprite pinapple = it.next();
					pinapple.updatePosition();
					if (pinapple.intersects(spaceship)) {
						it.remove();
						if (mediaPlayerBoomFinalCopy != null) {
							mediaPlayerBoomFinalCopy.stop();
							mediaPlayerBoomFinalCopy.play();
						}
						score += 10000;
					} else {
						pinapple.render(gc);
						if (Math.random() > 0.995) {
							changeSpeed(pinapple);
						}
					}
				}

				spaceship.render(gc);

				String txt = "Score: " + score;
				gc.fillText(txt, WIDTH - 36, 36);
				gc.strokeText(txt, WIDTH - 36, 36);
				gc.setTextAlign(TextAlignment.RIGHT);
			}
		}.start();
	}
}

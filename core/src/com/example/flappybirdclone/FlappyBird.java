package com.example.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;

	Texture background;
	Texture[] bird;

	int birdStateFlag = 0;
	float flyHeight;
	float fallingSpeed = 0f;

	int gameStateFlag = 0;
	int spaceBetweenTubes = 500;

	Texture top_tube;
	Texture bottom_tube;
	Random random;

	int tubeSpeed = 7;
	int tubesNumber = 5;
	float tubeX[] = new float[tubesNumber];
	float tubeShift[] = new float[tubesNumber];
	float distanceBetweenTubes;

	Circle birdCircle;
	Rectangle[] topTubesRectangles;
	Rectangle[] bottomTubesRectangles;
	//ShapeRenderer shapeRenderer;

	int gameScore = 0;
	int passedTubeIndex = 0;

	BitmapFont scoreFont;
	Texture gameOver;

	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("background.png");
		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		topTubesRectangles = new Rectangle[tubesNumber];
		bottomTubesRectangles = new Rectangle[tubesNumber];

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");

		top_tube = new Texture("top_tube.png");
		bottom_tube = new Texture("bottom_tube.png");
		random = new Random();
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.CYAN);
		scoreFont.getData().setScale(10);

		gameOver = new Texture("game_over.png");

		distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

		initGame();
	}

	public void initGame() {

		flyHeight = Gdx.graphics.getHeight() / 2
				- bird[0].getHeight() / 2;

		for (int i = 0; i < tubesNumber; i++) {
			tubeX[i] = Gdx.graphics.getWidth() / 2
					- top_tube.getWidth() / 2 + Gdx.graphics.getWidth() +
					i * distanceBetweenTubes * 2f;
			tubeShift[i] = (random.nextFloat() - 0.5f) *
					(Gdx.graphics.getHeight() - spaceBetweenTubes - 200);

			topTubesRectangles[i] = new Rectangle();
			bottomTubesRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth()
				, Gdx.graphics.getHeight());

		if (gameStateFlag == 1) {

			Gdx.app.log("GameScore", String.valueOf(gameScore));

			if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
				gameScore++;

				if (passedTubeIndex < tubesNumber - 1) {
					passedTubeIndex++;
				} else {
					passedTubeIndex = 0;
				}
			}

			if (Gdx.input.isTouched()){
				fallingSpeed = -20;
			}

			for (int i = 0; i < tubesNumber; i++) {

				if (tubeX[i] < 0) {
					tubeX[i] = tubesNumber * distanceBetweenTubes;
				} else {
					tubeX[i] -= tubeSpeed;
				}

				batch.draw(top_tube, tubeX[i], Gdx.graphics.getHeight() / 2 +
						spaceBetweenTubes / 2 + tubeShift[i]);
				batch.draw(bottom_tube, tubeX[i], Gdx.graphics.getHeight() / 2 -
						spaceBetweenTubes / 2 -
						bottom_tube.getHeight() + tubeShift[i]);

				topTubesRectangles[i] =
						new Rectangle(tubeX[i],
								Gdx.graphics.getHeight() / 2 +
										spaceBetweenTubes / 2 + tubeShift[i], top_tube.getWidth(),
								top_tube.getHeight());
				bottomTubesRectangles[i] =
						new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 -
								spaceBetweenTubes / 2 - bottom_tube.getHeight() +
								tubeShift[i],
								bottom_tube.getWidth(), bottom_tube.getHeight());
			}

			if (flyHeight > 0){
				fallingSpeed++;
				flyHeight -= fallingSpeed;
			} else {
				gameStateFlag = 2;
			}
		} else if (gameStateFlag == 0){
			if (Gdx.input.isTouched()){
				Gdx.app.log("Tap", "oops");
				gameStateFlag = 1;
			}
		} else if (gameStateFlag == 2) {
			batch.draw(gameOver,
					Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			if (Gdx.input.isTouched()){
				Gdx.app.log("Tap", "oops");
				gameStateFlag = 1;
				initGame();
				gameScore = 0;
				passedTubeIndex = 0;
				fallingSpeed = 0;
			}
		}

		if (birdStateFlag == 0) {
			birdStateFlag = 1;
		} else {
			birdStateFlag = 0;
		}

	batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth() / 2
					- bird[birdStateFlag].getWidth() / 2, flyHeight);

		scoreFont.draw(batch, String.valueOf(gameScore), 100, 150);

	batch.end();
	birdCircle.set(Gdx.graphics.getWidth() / 2, flyHeight +
					bird[birdStateFlag].getHeight() / 2,
					bird[birdStateFlag].getWidth() / 2);

	/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	shapeRenderer.setColor(Color.CYAN);
	shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);*/

	for (int i = 0; i < tubesNumber; i++) {

			/*shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight() / 2 +
							spaceBetweenTubes / 2 + tubeShift[i],
					top_tube.getWidth(), top_tube.getHeight());
			shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight() / 2 -
							spaceBetweenTubes / 2 -
							bottom_tube.getHeight() + tubeShift[i],
					bottom_tube.getWidth(), bottom_tube.getHeight());*/

			if (Intersector.overlaps(birdCircle, topTubesRectangles[i]) ||
					(Intersector.overlaps(birdCircle, bottomTubesRectangles[i]))) {
						Gdx.app.log("Intersected", "touch");
						gameStateFlag = 2;
			}

		}
	}
	//shapeRenderer.end();
}

package com.mathbteixeira.football;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ActionScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture ballImage;

    // Position of the ball
    private float ballX = 100;
    private float ballY = 100;

    // Speed of the ball (pixels per second)
    private float velocityX = 300;
    private float velocityY = 200;

    public ActionScreen() {
        // SpriteBatch is the tool libGDX uses to send images to your graphics card
        batch = new SpriteBatch();
        // Load the image from your assets folder
        ballImage = new Texture("ball.png");
    }

    @Override
    public void render(float delta) {
        // 1. UPDATE LOGIC (Physics)
        // delta is the time passed since the last frame (usually ~0.016 seconds)
        ballX += velocityX * delta;
        ballY += velocityY * delta;

        // Bounce off the edges of the screen
        if (ballX < 0 || ballX > Gdx.graphics.getWidth() - ballImage.getWidth()) {
            velocityX = -velocityX; // Reverse X direction
        }
        if (ballY < 0 || ballY > Gdx.graphics.getHeight() - ballImage.getHeight()) {
            velocityY = -velocityY; // Reverse Y direction
        }

        // 2. DRAWING (Rendering)
        // Clear screen to a nice grass green
        ScreenUtils.clear(0.1f, 0.6f, 0.2f, 1);

        batch.begin(); // Tell the graphics card we are about to draw
        batch.draw(ballImage, ballX, ballY); // Draw the ball at its current X and Y
        batch.end(); // Finish drawing
    }

    @Override
    public void dispose() {
        // We MUST dispose of heavy assets manually to prevent memory leaks
        batch.dispose();
        ballImage.dispose();
    }
}

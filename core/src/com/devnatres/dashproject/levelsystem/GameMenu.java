package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.animations.EAnimButton;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.levelsystem.levelscreen.LevelScreen;
import com.devnatres.dashproject.resourcestore.HyperStore;

/**
 * Represents a in-game menu. <br>
 *     <br>
 * Created by DevNatres on 31/01/2015.
 */
public class GameMenu implements IButtonExecutable {
    private final Button yesButton;
    private final Button noButton;

    private final Button resumeButton;
    private final Button resetButton;
    private final Button soundButton;
    private final Button soundSymbolButton;
    private final Agent offSymbol;

    private final LevelScreen levelScreen;
    private final GameState gameState;

    private Button confirmingButton;

    public GameMenu(LevelScreen levelScreen, HyperStore hyperStore, GameState gameState) {
        this.levelScreen = levelScreen;
        this.gameState = gameState;

        offSymbol = new Agent(hyperStore.getTexture("symbols/symbol_off.png"));

        yesButton = new Button(120, 100,
                EAnimButton.BUTTON_MENU_YES.create(hyperStore),
                null,
                0,
                this);

        noButton = new Button(360, 100,
                EAnimButton.BUTTON_MENU_NO.create(hyperStore),
                null,
                0,
                this);

        resumeButton = new Button(240, 700,
                EAnimButton.BUTTON_MENU_RESUME.create(hyperStore),
                null,
                0,
                this);

        resetButton = new Button(240, 500,
                EAnimButton.BUTTON_MENU_RESET.create(hyperStore),
                null,
                0,
                this);

        soundButton = new Button(240, 300,
                EAnimButton.BUTTON_MENU_SOUND.create(hyperStore),
                null,
                0,
                this);
        soundButton.setAutomaticSoundOff();
        soundSymbolButton = new Button(240, soundButton.getY()-offSymbol.getHeight()/2,
                EAnimButton.BUTTON_SYMBOL_SOUND.create(hyperStore),
                null,
                0,
                this);
        soundSymbolButton.setAutomaticSoundOff();
    }

    public void check(InputTranslator inputTranslator, DnaCamera camera) {
        Vector2 touchDownPointOnCamera = inputTranslator.getTouchDownPointOnCamera(camera);

        if (confirmingButton == null) {
            resumeButton.act(Time.FRAME, touchDownPointOnCamera);
            resetButton.act(Time.FRAME, touchDownPointOnCamera);
            soundButton.act(Time.FRAME, touchDownPointOnCamera);
            soundSymbolButton.act(Time.FRAME, touchDownPointOnCamera);
        } else {
            yesButton.act(Time.FRAME, touchDownPointOnCamera);
            noButton.act(Time.FRAME, touchDownPointOnCamera);
        }
    }

    public void paint(Batch batch) {
        if (confirmingButton == null) resumeButton.draw(batch);
        if (confirmingButton == null || confirmingButton == resetButton) resetButton.draw(batch);
        if (confirmingButton == null) {
            soundButton.draw(batch);
            soundSymbolButton.draw(batch);
            if (!gameState.isSoundActivated()) {
                offSymbol.setCenter(soundSymbolButton.getCenter());
                offSymbol.draw(batch);
            }
        }
        if (confirmingButton != null) {
            yesButton.draw(batch);
            noButton.draw(batch);
        }
    }

    public void execute(Button button) {
        if (button == resumeButton) {
            levelScreen.menuResume();
        } else if (button == resetButton) {
            confirmingButton = resetButton;
        } else if (button == soundButton || button == soundSymbolButton) {
            gameState.activateSound(!gameState.isSoundActivated());
            soundButton.playSound();
        } else if (button == yesButton) {
            if (confirmingButton == resetButton) {
                levelScreen.menuReset();
            }
        } else if (button == noButton) {
            confirmingButton = null;
        }
    }
}

package com.devnatres.dashproject.levelsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.agentsystem.Agent;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gameinput.Button;
import com.devnatres.dashproject.gameinput.IButtonExecutable;
import com.devnatres.dashproject.gameinput.InputTranslator;
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
    private final Button menuButton;
    private final Button exitButton;
    private final Button soundButton;
    private final Agent soundSymbol;
    private final Agent offSymbol;

    private final LevelScreen levelScreen;
    private final GameState gameState;

    private Button confirmingButton;

    public GameMenu(LevelScreen levelScreen, HyperStore hyperStore, GameState gameState) {
        this.levelScreen = levelScreen;
        this.gameState = gameState;

        yesButton = new Button(120, 100,
                EAnimation.BUTTON_MENU_YES.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        noButton = new Button(360, 100,
                EAnimation.BUTTON_MENU_NO.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        resumeButton = new Button(240, 700,
                EAnimation.BUTTON_MENU_RESUME.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        resetButton = new Button(240, 600,
                EAnimation.BUTTON_MENU_RESET.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        menuButton = new Button(240, 500,
                EAnimation.BUTTON_MENU_MENU.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        exitButton = new Button(240, 400,
                EAnimation.BUTTON_MENU_EXIT.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);

        soundButton = new Button(240, 300,
                EAnimation.BUTTON_MENU_SOUND.create(hyperStore),
                null,
                hyperStore.getSound("sounds/fail_hit.ogg"),
                0,
                this);
        soundButton.setAutomaticSoundOff();

        soundSymbol = new Agent(hyperStore.getTexture("symbols/symbol_sound.png"));
        soundSymbol.setCenter(240, 230);
        offSymbol = new Agent(hyperStore.getTexture("symbols/symbol_off.png"));
    }

    public void check(InputTranslator inputTranslator, DnaCamera camera) {
        Vector2 touchDownPointOnCamera = inputTranslator.getTouchDownPointOnCamera(camera);

        if (confirmingButton == null) {
            resumeButton.act(Time.FRAME, touchDownPointOnCamera);
            resetButton.act(Time.FRAME, touchDownPointOnCamera);
            menuButton.act(Time.FRAME, touchDownPointOnCamera);
            exitButton.act(Time.FRAME, touchDownPointOnCamera);
            soundButton.act(Time.FRAME, touchDownPointOnCamera);
        } else {
            yesButton.act(Time.FRAME, touchDownPointOnCamera);
            noButton.act(Time.FRAME, touchDownPointOnCamera);
        }
    }

    public void paint(Batch batch) {
        if (confirmingButton == null) resumeButton.draw(batch);
        if (confirmingButton == null || confirmingButton == resetButton) resetButton.draw(batch);
        if (confirmingButton == null || confirmingButton == menuButton) menuButton.draw(batch);
        if (confirmingButton == null || confirmingButton == exitButton) exitButton.draw(batch);
        if (confirmingButton == null) soundButton.draw(batch);
        if (confirmingButton == null) {
            soundSymbol.draw(batch);
            if (!gameState.isSoundActivated()) {
                offSymbol.setCenter(soundSymbol.getCenter());
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
        } else if (button == menuButton) {
            confirmingButton = menuButton;
        } else if (button == exitButton) {
            confirmingButton = exitButton;
        } else if (button == soundButton) {
            gameState.activateSound(!gameState.isSoundActivated());
            soundButton.playSound();
        } else if (button == yesButton) {
            if (confirmingButton == exitButton) {
                Gdx.app.exit();
            } else if (confirmingButton == resetButton) {
                levelScreen.menuReset();
            } else if (confirmingButton == menuButton) {
                levelScreen.menuMainMenu();
            }
        } else if (button == noButton) {
            confirmingButton = null;
        }
    }
}

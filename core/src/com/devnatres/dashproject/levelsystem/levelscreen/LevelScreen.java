package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.*;
import com.devnatres.dashproject.agentsystem.Number;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.dnagdx.GlobalAudio;
import com.devnatres.dashproject.gameconstants.EAnimation;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.levelscriptcmd.LevelScript;
import com.devnatres.dashproject.levelsystem.GameMenu;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.Score;
import com.devnatres.dashproject.sidescreens.LobbyScreen;
import com.devnatres.dashproject.sidescreens.MainMenuScreen;
import com.devnatres.dashproject.space.DirectionSelector;
import com.devnatres.dashproject.tools.Tools;

import static com.devnatres.dashproject.agentsystem.AgentRegistry.EAgentLayer;

/**
 * Represents a level screen of the game, the main game-play class. <br>
 *     <br>
 * Created by DevNatres on 04/12/2014.
 */
public class LevelScreen implements Screen {
    private static final int RESET_COUNT = 180;
    private static final float TIME_STEP = Time.FPS_TIME;
    private static final int MIN_FINAL_TIME = 120;
    private static final int MIN_READY_TIME = 15;
    private static final float TIME_BONUS = 3f;

    private int resetCountDown = RESET_COUNT;

    private LevelScreenSet set;
    private LevelScreenLevel level;
    private LevelScreenEnemy enemy;
    private LevelScreenAudio audio;
    private LevelScreenMessages messages;
    private LevelScreenVariables variables;
    Hero hero;
    AgentRegistry agentRegistry;
    GameState gameState;
    private Sprite lifePointImage;
    private DnaAnimation radar;
    private GameMenu gameMenu;
    private Score score;
    private EPlayMode playMode;

    public LevelScreen(DashGame game, LevelId levelId) {
        set = new LevelScreenSet(this, game);
        level = new LevelScreenLevel(set, levelId);

        hero = new Hero(this, set.hyperStore);

        agentRegistry = new AgentRegistry();
        agentRegistry.register(hero, AgentRegistry.EAgentLayer.TRUNK);

        gameState = game.getGameState();
        enemy = new LevelScreenEnemy(this, set, level);
        audio = new LevelScreenAudio(set);
        messages = new LevelScreenMessages(set);
        variables = new LevelScreenVariables(set.hyperStore);

        radar = EAnimation.RADAR_INDICATOR.create(set.hyperStore);
        lifePointImage = new Sprite(set.hyperStore.getTexture("heal_point.png"));

        gameMenu = new GameMenu(this, set.hyperStore, gameState);
        score = new Score(this, set.hyperStore);

        prepareGame();

        System.gc();

        if (Debug.DEBUG) Debug.resetCount();
    }

    private void prepareGame() {
        set.skipCameraAssistant = !gameState.isCameraAssistantActivated();
        variables.setTime(level.levelId.getInitialTime());
        variables.waitingTime = MIN_READY_TIME;
        playMode = EPlayMode.READY;
    }

    LevelScreenSet getSet() {
        return set;
    }

    LevelScreenLevel getLevel() {
        return level;
    }

    LevelId getLevelId() {
        return level.levelId;
    }

    public Hero getHero() {
        return hero;
    }

    public float getTime() {
        return variables.getTime();
    }

    public DnaCamera getCamera() {
        return set.mainCamera;
    }

    public LevelScript getLevelScript() {
        return level.levelScript;
    }

    public void addHorde(Horde horde) {
        agentRegistry.register(horde, EAgentLayer.FLOOR);
        enemy.getHordeGroup().addLinked(horde);
    }

    @Override
    public void render(float delta) {
        if (set.mainInputTranslator.isResetRequested() || resetCountDown == 0) {
            menuReset();
            return;
        }

        clearScreen();
        playMode.render(this);

        if (Debug.DEBUG) renderDebugger();
    }

    private void renderStandardComponents() {
        set.mainCamera.update();
        set.mainBatch.setProjectionMatrix(set.mainCamera.combined);

        set.mainBatch.begin();
        renderBackground();
        renderMap();
        renderSprites();
        renderFoeRadar();
        renderHub();
        set.mainBatch.end();
    }

    protected void renderPlayMode_Ready() {
        renderStandardComponents();

        set.drawCentered(messages.readyMessage);

        if (variables.waitingTime == 0 && set.mainInputTranslator.isTouchDown()) {
            playMode = EPlayMode.GAME_PLAY;
        } else {
            set.mainInputTranslator.clear();
            if (variables.waitingTime > 0) {
                variables.waitingTime--;
            }
        }
    }

    protected void renderPlayMode_GamePlay() {
        boolean thereIsNewScriptCmdExecuted = level.levelScript.execute();
        if (!thereIsNewScriptCmdExecuted && enemy.getHordeGroup().size() == 0) {
            renderPlayMode_GamePlay_scoreCountTransition();
        } else if (!hero.isVisible()) {
            playMode = EPlayMode.HERO_DEAD;
        } else if (!Debug.IMMORTAL && variables.getTime() == 0) {
            hero.die();
            playMode = EPlayMode.TIME_OUT;
        } else if (set.mainInputTranslator.isMenuRequested()) {
            playMode = EPlayMode.MENU;
        } else {
            renderPlayMode_GamePlay_main();
        }
        renderStandardComponents();
        renderAnimatedMessage();
    }

    private void renderPlayMode_GamePlay_main() {
        if (!isBulletTime() && variables.getTime() > 0) {
            variables.setTime(variables.getTime() - TIME_STEP);
            if (variables.getTime() < 0) variables.setTime(0);
        }
        if (enemy.isDeadHordeCountChanged()) {
            enemy.updateLastHordeCount();
            addTime(TIME_BONUS);
            Vector2 powerUpBasePos = (enemy.thereIsLastDeadFoe()) ? enemy.getLastDeadFoeCenter() : hero.getCenter();
            PowerUpGenerator.generatePowerUpIfLucky(set.hyperStore, this, powerUpBasePos);
        }
        inputForHero();
        decideToChaseHeroWithCamera();
        if (variables.bulletTime > 0f && !variables.comboCameraChasing) {
            variables.bulletTime--;
            if (variables.bulletTime == 0) deactivateBulletTime();
        }
    }

    private void renderPlayMode_GamePlay_scoreCountTransition() {
        playMode = EPlayMode.SCORE_COUNT;
        score.calculateFinalCount();
        gameState.updateCurrentLevelScore(score);
        GlobalAudio.playOnly(audio.endOkMusic);
        variables.waitingTime = MIN_FINAL_TIME;
    }

    protected void renderPlayMode_HeroDead() {
        if (resetCountDown > 0) resetCountDown--;

        renderStandardComponents();
        set.drawCentered(messages.dissipatedMessage);
    }

    protected void renderPlayMode_TimeOut() {
        if (resetCountDown > 0) resetCountDown--;

        renderStandardComponents();
        set.drawCentered(messages.timeoutMessage);
    }

    protected void renderPlayMode_ScoreCount() {
        renderStandardComponents();

        set.mainBatch.setProjectionMatrix(set.fixedCamera.combined);
        set.mainBatch.begin();
        score.renderFinalCount(set.mainBatch, set.mainFont);
        set.mainBatch.end();

        if (variables.waitingTime == 0 && set.mainInputTranslator.isTouchDown()) {
            menuReset();
        } else {
            set.mainInputTranslator.clear();
            if (variables.waitingTime > 0) variables.waitingTime--;
        }
    }

    protected void renderPlayMode_Menu() {
        set.mainBatch.setProjectionMatrix(set.fixedCamera.combined);
        gameMenu.check(set.mainInputTranslator, set.fixedCamera);
        set.mainBatch.begin();
        gameMenu.paint(set.mainBatch);
        set.mainBatch.end();
    }

    private boolean isAllFoesOutOfVisibleScope() {
        boolean allFoesOutOfVisibleScope = true;
        Horde globalHorde = getGlobalHorde();
        for (int i = 0; i < globalHorde.size(); i++){
            Foe foe = globalHorde.getFoe(i);
            if (set.mainCamera.isOnCamera(foe) && !foe.isDying() && hero.isFoeOnScope(foe)) {
                allFoesOutOfVisibleScope = false;
                break;
            }
        }
        return allFoesOutOfVisibleScope;
    }

    public void menuResume() {
        playMode = EPlayMode.GAME_PLAY;
    }

    public void menuMainMenu() {
        set.dashGame.setScreen(new MainMenuScreen(set.dashGame));
    }

    public void menuReset() {
        GlobalAudio.stopMusic();
        set.dashGame.setScreen(new LobbyScreen(set.dashGame));
    }

    private void decideToChaseHeroWithCamera() {
        if (set.skipCameraAssistant || variables.bulletTime == 0 || variables.comboCameraChasing) {
            chaseHeroWithCamera();
        } else { // We can't still decide not to chase, we must check if there aren't foes on hero's visible scope
            if (isAllFoesOutOfVisibleScope()) {
                variables.comboCameraChasing = true;
                chaseHeroWithCamera();
                if (variables.comboCameraChasing) { // Can be modified in the method above
                    activateBulletTime();
                }
            }
        }
    }

    private void chaseHeroWithCamera() {
        moveCameraTo(hero.getCenter().x, hero.getCenter().y, Time.FAST_CAMERA_SPEED);
    }

    @Override
    public void resize(int width, int height) {
    }

    public LevelMap getMap() {
        return level.map;
    }

    public HordeGroup getHordeGroup() {
        return enemy.getHordeGroup();
    }

    public Horde getGlobalHorde() {
        return enemy.getHordeGroup().getGlobalHorde();
    }

    private void moveCameraTo(float posX, float posY, float speed) {
        posX = limitCameraTargetX(posX);
        posY = limitCameraTargetY(posY);

        if (posX != set.mainCamera.position.x || posY != set.mainCamera.position.y) {
            Vector3 vTarget = new Vector3(posX, posY, 0);
            if (speed == 0 || Math.abs(vTarget.dst(set.mainCamera.position)) <= speed) {
                set.mainCamera.position.set(vTarget);
                variables.comboCameraChasing = false;
            } else {
                Vector3 vJump = vTarget.sub(set.mainCamera.position).nor().scl(speed);
                // Slow movement needs to be rounded to avoid seeing tile map flickering
                if (speed < Time.MEDIUM_CAMERA_SPEED) {
                    set.mainCamera.translate(Math.round(vJump.x), Math.round(vJump.y));
                } else {
                    set.mainCamera.translate(vJump.x, vJump.y);
                }
            }
        } else {
            variables.comboCameraChasing = false;
        }
    }

    public void processFoeDamageResult(Foe foe, FoeDamageResult foeDamageResult) {
        score.sumFoeScore(foeDamageResult.getScore());
        enemy.setLastDeadFoe(foe);
    }

    public void processHordeDamageResult(HordeDamageResult hordeDamageResult) {
        score.sumChainScore(hordeDamageResult.getComboScore());
    }

    private float limitCameraTargetX(float x) {
        final float minX = set.screenWidth/2;
        final float maxX = level.map.getMapPixelWidth() - set.screenWidth/2;
        return Math.round(Tools.limitFloat(x, minX, maxX));
    }

    private float limitCameraTargetY(float y) {
        final float minY = set.screenHeight/2;
        final float maxY = level.map.getMapPixelHeight() - set.screenHeight/2;
        return Math.round(Tools.limitFloat(y, minY, maxY));
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void setAgentMessage(Agent agent, int duration) {
        messages.agentMessage = agent;
        messages.agentMessageDuration = duration;
    }

    private void renderAnimatedMessage() {
        if (messages.agentMessage != null && messages.agentMessageDuration > 0) {
            messages.agentMessageDuration--;
            set.drawCentered(messages.agentMessage);
        }
    }

    private void renderBackground() {
    }

    private void renderMap() {
        level.map.paint(set.mainCamera);
    }

    private void renderHub() {
        set.mainBatch.setProjectionMatrix(set.fixedCamera.combined);

        score.renderActionScore(set.mainBatch, set.mainFont);

        set.mainFont.draw(set.mainBatch, variables.getTimeString(), set.screenWidth/2, set.screenHeight - 10);

        Number timeNumber = variables.getTimeNumber();
        if (timeNumber.getValue() < 2.5) {
            Agent timeHalo = variables.getTimeHalo();
            timeHalo.act(Time.FRAME);
            timeHalo.draw(set.mainBatch);
        }
        timeNumber.draw(set.mainBatch);

        int life = hero.getLife();
        float lifeWidth = lifePointImage.getWidth() + 1;
        for (int i = 1; i <= life; i++) {
            lifePointImage.setPosition(350 + (i*lifeWidth), set.screenHeight - 20);
            lifePointImage.draw(set.mainBatch);
        }

        set.mainFont.draw(set.mainBatch, enemy.getCurrentHordeCountString(), 450, set.screenHeight - 10);
    }

    private void renderSprites() {
        agentRegistry.render(Time.FRAME, set.mainBatch);

        int removedHordes = enemy.getHordeGroup().removeKilledHordes();
        if (removedHordes > 0) enemy.subtractCurrentHordeCount(removedHordes);

        score.updateScore();
    }

    private void renderFoeRadar() {
        Horde globalHorde = enemy.getHordeGroup().getGlobalHorde();
        boolean thereIsFoeOnCamera = false;
        int numberOfFoes = globalHorde.size();
        for (int i = 0; i < numberOfFoes; i++){
            Foe foe = globalHorde.getFoe(i);
            if (set.mainCamera.isOnCamera(foe) && !foe.isDying()) {
                thereIsFoeOnCamera = true;
                break;
            }
        }

        if (!thereIsFoeOnCamera) {
            HordeGroup hordeGroup = enemy.getHordeGroup();
            int numberOfHordes = hordeGroup.size();
            for (int i = 0; i < numberOfHordes; i++){
                Horde horde = hordeGroup.getHorde(i);
                if (!horde.isKilled()) {
                    radar.updateStateTime();
                    renderFoeRadar_indicator(horde.getReferencePosition());
                }
            }
        }
    }

    private void renderFoeRadar_indicator(Vector2 referencePos) {
        DirectionSelector directionSelector = set.mainCamera.getOutDirection(referencePos.x, referencePos.y);
        TextureRegion textureRegion = radar.getCurrentKeyFrame();
        float spriteLeft, spriteBottom;

        if (directionSelector.isUp()) {
            spriteBottom = set.mainCamera.getUp() - textureRegion.getRegionHeight();
            spriteLeft = renderFoeRadar_indicator_spriteLeft(directionSelector, textureRegion, referencePos);
        } else if (directionSelector.isDown()) {
            spriteBottom = set.mainCamera.getDown();
            spriteLeft = renderFoeRadar_indicator_spriteLeft(directionSelector, textureRegion, referencePos);
        } else {
            if (directionSelector.isLeft()) {
                spriteBottom = referencePos.y - textureRegion.getRegionHeight()/2f;
                spriteLeft = set.mainCamera.getLeft();
            } else { // isRight()
                spriteBottom = referencePos.y - textureRegion.getRegionHeight()/2f;
                spriteLeft = set.mainCamera.getRight() - textureRegion.getRegionWidth();
            }

            if (spriteBottom < set.mainCamera.getDown()) {
                spriteBottom = set.mainCamera.getDown();
            } else if ((spriteBottom + textureRegion.getRegionHeight()) > set.mainCamera.getUp()) {
                spriteBottom = set.mainCamera.getUp() - textureRegion.getRegionHeight();
            }
        }

        set.mainBatch.draw(textureRegion, spriteLeft, spriteBottom);
    }

    private float renderFoeRadar_indicator_spriteLeft(DirectionSelector directionSelector,
                                                      TextureRegion textureRegion,
                                                      Vector2 referencePos) {
        float spriteLeft;
        if (directionSelector.isLeft()) {
            spriteLeft = set.mainCamera.getLeft();
        } else if (directionSelector.isRight()) {
            spriteLeft = set.mainCamera.getRight() - textureRegion.getRegionWidth();
        } else {
            spriteLeft = referencePos.x - textureRegion.getRegionWidth()/2f;
            if (spriteLeft < set.mainCamera.getLeft()) {
                spriteLeft = set.mainCamera.getLeft();
            } else if ((spriteLeft+textureRegion.getRegionWidth()) > set.mainCamera.getRight()) {
                spriteLeft = set.mainCamera.getRight() - textureRegion.getRegionWidth();
            }
        }
        return spriteLeft;
    }

    private void inputForHero() {
        Vector2 touchDownPointOnCamera = set.mainInputTranslator.getTouchDownPointOnCamera(set.mainCamera);
        if (touchDownPointOnCamera != null) {
            hero.programNextPos(touchDownPointOnCamera.x, touchDownPointOnCamera.y);
        }
    }

    public void activateBulletTime() {
        variables.bulletTime = Time.BULLET_TIME;
    }

    public void deactivateBulletTime() {
        variables.bulletTime = 0;
        enemy.clearCombo();
    }

    public boolean thereAreComboLivingFoesThatContains(Foe foe) {
        return enemy.thereAreComboLivingFoesThatContains(foe);
    }

    public void removeComboLivingFoe(Foe foe) {
        enemy.removeComboLivingFoe(foe);
    }

    public void addComboFoe(Foe foe) {
        enemy.addComboFoe(foe);
    }

    public void restoreNotAttackedFoesAccordingToComboLivingFoes(Array<Foe> attackedFoes) {
        enemy.restoreNotAttackedFoesAccordingToComboLivingFoes(attackedFoes);
    }

    public boolean isFirstComboFoe(Foe foe) {
        return enemy.isFirstComboFoe(foe);
    }

    public boolean isBulletTime() {
        return variables.bulletTime > 0;
    }

    public void register(Agent agent, EAgentLayer layer) {
        agentRegistry.register(agent, layer);
    }

    public void addTime(float extraTime) {
        variables.setTime(variables.getTime() + extraTime);
    }

    private void renderDebugger() {
        set.mainShape.setProjectionMatrix(set.mainCamera.combined);
        Horde globalHorde = getGlobalHorde();
        for (int i = 0, n = globalHorde.size(); i < n; i++) {
            Foe foe = globalHorde.getFoe(i);

            Vector2 foePosition = foe.getCenter();
            boolean hidden = false;
            if (foePosition.x < hero.getX() && hero.isCoverLeft()) {
                hidden = true;
            }
            if (foePosition.x > hero.getX() && hero.isCoverRight()) {
                hidden = true;
            }
            if (foePosition.y < hero.getY() && hero.isCoverDown()) {
                hidden = true;
            }
            if (foePosition.y > hero.getY() && hero.isCoverUp()) {
                hidden = true;
            }

            if (!hidden && set.mainCamera.isOnCamera(foe)) {
                set.mainShape.begin(ShapeRenderer.ShapeType.Line);
                set.mainShape.line(foePosition.x, foePosition.y,
                        hero.getX() + hero.getWidth() / 2, hero.getY() + hero.getHeight() / 2);
                set.mainShape.end();
            }
        }
    }

    @Override
    public void show() {
        GlobalAudio.playOnly(audio.badassMusic);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        level.map.dispose();
    }
}

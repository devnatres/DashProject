package com.devnatres.dashproject.levelsystem.levelscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.devnatres.dashproject.DashGame;
import com.devnatres.dashproject.agentsystem.*;
import com.devnatres.dashproject.animations.EAnimMedley;
import com.devnatres.dashproject.debug.Debug;
import com.devnatres.dashproject.dnagdx.DnaAnimation;
import com.devnatres.dashproject.dnagdx.DnaCamera;
import com.devnatres.dashproject.gameconstants.EScroll;
import com.devnatres.dashproject.gameconstants.Time;
import com.devnatres.dashproject.gamestate.GameState;
import com.devnatres.dashproject.levelscriptcmd.LevelScript;
import com.devnatres.dashproject.levelsystem.GameMenu;
import com.devnatres.dashproject.levelsystem.LevelId;
import com.devnatres.dashproject.levelsystem.LevelMap;
import com.devnatres.dashproject.levelsystem.Score;
import com.devnatres.dashproject.nonagentgraphics.LifeBar;
import com.devnatres.dashproject.nonagentgraphics.Number;
import com.devnatres.dashproject.sidescreens.LobbyScreen;
import com.devnatres.dashproject.sidescreens.MainMenuScreen;
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
    private static final int MIN_FINAL_TIME = 60;
    private static final int MIN_READY_TIME = 15;
    private static final float TIME_BONUS = 3f;
    private static final float CRITICAL_TIME = 3f;
    private static final int EXTRA_TIME_MESSAGE_DURATION = 90;

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
    private final LifeBar lifeBar;
    private final Radar radar;
    private GameMenu gameMenu;
    private Score score;
    private EPlayMode playMode;
    private EPlayMode playMode_before_menu;

    private final DnaAnimation damageSoftFlashing;
    private final DnaAnimation damageHardFlashing;
    private int damageSoftFlashingDuration;
    private int damageHardFlashingDuration;

    private String extraTimeString;
    private float lastExtraTime;
    private int extraTimeMessageDuration;

    public LevelScreen(DashGame game, LevelId levelId) {
        set = new LevelScreenSet(game);
        level = new LevelScreenLevel(set, levelId);

        hero = new Hero(this, set.localHyperStore);

        agentRegistry = new AgentRegistry();
        agentRegistry.register(hero, AgentRegistry.EAgentLayer.TRUNK);

        gameState = game.getGameState();
        enemy = new LevelScreenEnemy(this, set, level);
        audio = new LevelScreenAudio(set);
        messages = new LevelScreenMessages(set);
        variables = new LevelScreenVariables(set.localHyperStore);

        radar = new Radar(this, set.localHyperStore);
        lifeBar = new LifeBar(set.localHyperStore);

        gameMenu = new GameMenu(this, set.localHyperStore, gameState);
        score = new Score(this, set.localHyperStore);

        damageSoftFlashing = EAnimMedley.DAMAGE_SOFT_FLASHING.create(set.localHyperStore);
        damageHardFlashing = EAnimMedley.DAMAGE_HARD_FLASHING.create(set.localHyperStore);

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

    public void setBackScroll(EScroll eScroll) {
        level.backScroll = eScroll.create(set.localHyperStore);
    }

    public void setForeScroll(EScroll eScroll) {
        level.foreScroll = eScroll.create(set.localHyperStore);
    }

    @Override
    public void render(float delta) {
        if (resetCountDown == 0) {
            menuReset();
            return;
        }

        clearScreen();
        playMode.render(this);

        //if (Debug.DEBUG) renderDebugLines();
    }

    private void renderStandardComponents() {
        set.mainCamera.update();
        set.mainBatch.setProjectionMatrix(set.mainCamera.combined);
        set.mainBatch.begin();
        renderBackgroundScroll();
        renderMap();
        renderSprites();
        renderForegroundScroll();
        renderFoeRadar();
        renderDamageFlashing();
        renderHub();
        set.mainBatch.end();
    }

    protected void renderPlayMode_Ready() {
        renderStandardComponents();

        set.drawCentered(messages.readyMessage);

        if (variables.waitingTime == 0 && set.mainInputTranslator.isTouchDown()) {
            playMode = EPlayMode.GAME_PLAY;
        } else {
            if (set.mainInputTranslator.isMenuRequested()) {
                playMode_before_menu = EPlayMode.READY;
                playMode = EPlayMode.MENU;
            }

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
            audio.globalAudio.playAlone(audio.dieMusic);
        } else if (!Debug.IMMORTAL && variables.isTimeOut()) {
            hero.die();
            playMode = EPlayMode.TIME_OUT;
            audio.globalAudio.playAlone(audio.dieMusic);
        } else if (set.mainInputTranslator.isMenuRequested()) {
            playMode_before_menu = EPlayMode.GAME_PLAY;
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

            lastExtraTime = TIME_BONUS;
            addTime(lastExtraTime);
            extraTimeMessageDuration = EXTRA_TIME_MESSAGE_DURATION;
            extraTimeString = String.valueOf("Time " + lastExtraTime);

            Vector2 powerUpBasePos = (enemy.thereIsLastDeadFoe()) ? enemy.getLastDeadFoeCenter() : hero.getCenter();
            PowerUpGenerator.generatePowerUpIfLucky(set.localHyperStore, this, powerUpBasePos);
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
        audio.globalAudio.playAlone(audio.scoreMusic);
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
        score.renderFinalCount(set.mainBatch, set.mainShadowedFont);
        set.mainBatch.end();

        if (variables.waitingTime == 0 && set.mainInputTranslator.isTouchDown()) {
            if (score.isFinished()) {
                menuReset();
            } else {
                score.runToFinish();
            }
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
        set.mainInputTranslator.clear();
        if (playMode_before_menu != null) {
            playMode = playMode_before_menu;
            playMode_before_menu = null;
        } else {
            playMode = EPlayMode.GAME_PLAY;
        }
    }

    public void menuMainMenu() {
        set.dashGame.setScreen(new MainMenuScreen(set.dashGame));
    }

    public void menuReset() {
        audio.globalAudio.stopMusic();
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
        variables.savedCameraPosition.set(set.mainCamera.position.x, set.mainCamera.position.y);

        posX = limitCameraTargetX(posX);
        posY = limitCameraTargetY(posY);

        if (posX != set.mainCamera.position.x || posY != set.mainCamera.position.y) {
            variables.cameraTarget.set(posX, posY, 0);
            if (speed == 0 || Math.abs(variables.cameraTarget.dst(set.mainCamera.position)) <= speed) {
                set.mainCamera.position.set(variables.cameraTarget);
                variables.comboCameraChasing = false;
            } else {
                Vector3 vJump = variables.cameraTarget.sub(set.mainCamera.position).nor().scl(speed);
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
        variables.cameraMovementDone.set(set.mainCamera.position.x, set.mainCamera.position.y);
        variables.cameraMovementDone.sub(variables.savedCameraPosition);
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
        messages.messageAgent = agent;
        messages.messageAgentDuration = duration;
    }

    private void renderAnimatedMessage() {
        if (messages.messageAgent != null && messages.messageAgentDuration > 0) {
            messages.messageAgentDuration--;
            set.drawCentered(messages.lineAgent);
            set.drawCentered(messages.messageAgent);
        }
    }

    private void renderBackgroundScroll() {
        if (level.backScroll != null) level.backScroll.render(set.mainBatch, variables.cameraMovementDone);
    }

    private void renderForegroundScroll() {
        if (level.foreScroll != null) level.foreScroll.render(set.mainBatch, variables.cameraMovementDone);
    }

    private void renderMap() {
        level.map.paint(set.mainCamera);
    }

    private void renderDamageFlashing() {
        if (damageHardFlashingDuration > 0) {
            damageSoftFlashingDuration = 0; // If any.
            damageHardFlashingDuration--;
            damageHardFlashing.render(set.mainBatch, set.mainCamera.getLeft(), set.mainCamera.getDown());
        } else if (damageSoftFlashingDuration > 0) {
            damageSoftFlashingDuration--;
            damageSoftFlashing.render(set.mainBatch, set.mainCamera.getLeft(), set.mainCamera.getDown());
        }
    }

    public void setDamageSoftFlashingDuration(int duration) {
        damageSoftFlashingDuration = duration;
    }

    public void setDamageHardFlashingDuration(int duration) {
        damageHardFlashingDuration = duration;
    }

    private void renderHub() {
        set.mainBatch.setProjectionMatrix(set.fixedCamera.combined);

        score.renderActionScore(set.mainBatch, set.mainShadowedFont);

        Number timeNumber = variables.getTimeNumber();
        if (timeNumber.getValue() < CRITICAL_TIME) {
            if (timeNumber.getNumberScale() == 1) timeNumber.changeNumberScale(2f);
        } else {
            if (timeNumber.getNumberScale() != 1) timeNumber.restoreNumberScale();
        }
        timeNumber.render(set.mainBatch);
        if (extraTimeMessageDuration > 0) {
            int x = (int)set.screenWidth/2 - set.mainShadowedFont.getTextWidth(extraTimeString)/2;
            int y = (int)set.screenHeight/3 - 35;
            set.mainShadowedFont.draw(set.mainBatch, extraTimeString, x, y);
            extraTimeMessageDuration--;
        }

        lifeBar.paint(set.mainBatch, hero.getLife());

        if (enemy.getHordeCountNumber().getValue() > 0) {
            enemy.getHordeCountNumber().render(set.mainBatch);
        } else {
            Texture texture = enemy.getHordesClearTexture();
            set.mainBatch.draw(texture,
                    set.screenWidth-texture.getWidth()-5,
                    set.screenHeight-texture.getHeight()-5);
        }
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

        if (numberOfFoes > 0 && !thereIsFoeOnCamera) {
            radar.act(Time.FRAME);
            HordeGroup hordeGroup = enemy.getHordeGroup();
            int numberOfHordes = hordeGroup.size();
            for (int i = 0; i < numberOfHordes; i++){
                Horde horde = hordeGroup.getHorde(i);
                if (!horde.isKilled()) {
                    radar.lookAt(horde.getReferencePosition());
                    radar.draw(set.mainBatch);
                }
            }
        }
    }

    private void inputForHero() {
        Vector2 touchDownPointOnCamera = set.mainInputTranslator.getTouchDownPointOnCamera(set.mainCamera);
        if (touchDownPointOnCamera != null) {
            touchDownPointOnCamera.sub(variables.cameraMovementDone);
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

    private void renderDebugLines() {
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
        audio.globalAudio.playAlone(audio.gameMusic);
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
        set.localHyperStore.dispose();
    }
}

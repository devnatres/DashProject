package com.devnatres.dashproject.levelscriptcmd;

import com.badlogic.gdx.utils.Array;

import static com.devnatres.dashproject.levelscriptcmd.Cmd.*;

/**
 * Represents a list of commands to process sequentially. <br>
 * If a command is terminated in a execute() call, the next command will be executed the next time.
 * If it isn't terminated, it will continue in next calls. <br>
 *     <br>
 * Created by DevNatres on 29/12/2014.
 */
public class LevelScript {
    private final Array<Cmd> levelScript;
    private int levelScriptIndex;

    public LevelScript() {
        levelScript = new Array<Cmd>();
    }

    public void addCmd(Cmd cmd) {
        levelScript.add(cmd);
    }

    /**
     * @return false if there weren't more commands to execute
     */
    public boolean execute() {
        boolean executed = false;
        if (levelScriptIndex < levelScript.size) {
            executed = true;
            ECmdState state = levelScript.get(levelScriptIndex).execute();
            if (state == ECmdState.TERMINATED) {
                levelScriptIndex++;
            }
        }
        return executed;
    }
}

package com.devnatres.dashproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * Created by DevNatres on 17/01/2015.
 *
 * All music and sound created and managed throw this class attend the audio enable/disable status.
 * It's recommend to manage the music and sound created with this class only with its methods, not directly.
 *
 * When audio is disabled, the music that is playing is "stopped".
 * However, the sounds are not stopped when playing. So the sounds should always be short
 * enough to minimize the theoretical issue of be heard if audio is disabled when a sound is playing.
 * (If you need long sounds, consider to create them as music resources.)
 *
 * Music's volume can be set when audio is disabled because
 * the new value is saved to be retrieved later when audio is enabled.
 * (This allows you, for example, to fade in/out music volume
 * regardless of whether the audio is been enabled o disabled.)
 *
 */
public class GlobalAudio {
    private static boolean isAudioEnabled = true;
    private static Array<Music> musics = new Array();
    private static Array<Float> musicVolumesWhenEnabled = new Array();

    private GlobalAudio() {}

    public static Music newMusic(String resourceFileName) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(resourceFileName));
        musics.add(music);
        musicVolumesWhenEnabled.add(music.getVolume());
        if (!isAudioEnabled) {
            music.setVolume(0f);
        }
        return music;
    }

    public static Sound newSound(String resourceFileName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(resourceFileName));
        return sound;
    }

    public static void setVolume(Music music, float volume) {
        int i = musics.indexOf(music, true);
        if (i > -1) {
            if (isAudioEnabled) {
                musics.get(i).setVolume(volume);
            }
            musicVolumesWhenEnabled.set(i, volume);
        }
    }

    public static void stopMusic() {
        for (int i = 0; i < musics.size; i++) {
            Music music = musics.get(i);
            if (music.isPlaying()) {
                music.stop();
            }
        }
    }

    public static void playOnly(Music music) {
        stopMusic();
        music.play();
    }

    public static void play(Sound sound, float volume) {
        if (isAudioEnabled) {
            sound.play(volume);
        }
    }

    public static void play(Sound sound) {
        if (isAudioEnabled) {
            sound.play();
        }
    }

    public static void dispose(Sound sound) {
        sound.dispose();
    }

    public static void dispose(Music music) {
        int i = musics.indexOf(music, true);
        if (i > -1) {
            musics.removeIndex(i);
            musicVolumesWhenEnabled.removeIndex(i);
        }
        music.dispose();
    }

    public static void disableAudio() {
        for (int i = 0; i < musics.size; i++) {
            Music music = musics.get(i);
            if (isAudioEnabled) {
                // It isn't necessary if volume was set with this class. It's just a safety step.
                musicVolumesWhenEnabled.set(i, music.getVolume());
            }
            music.setVolume(0f);
        }
        isAudioEnabled = false;
    }

    public static void enableAudio() {
        for (int i = 0; i < musics.size; i++) {
            Music music = musics.get(i);
            float volume = musicVolumesWhenEnabled.get(i);
            music.setVolume(volume);
        }
        isAudioEnabled = true;
    }
}

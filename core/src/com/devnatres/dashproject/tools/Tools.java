package com.devnatres.dashproject.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import java.util.Random;

/**
 * Helper class for a different functions. <br>
 *     <br>
 * Created by DevNatres on 03/12/2014.
 */
final public class Tools {
    private static final Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt(max-min+1)+min;
    }

    public static boolean randomBoolean(int favorableCases, int possibleCases) {
        int rnd = randomInt(1, possibleCases);
        return rnd <= favorableCases;
    }

    /**
     * A checkInput character is added to plainString.
     * The new string is encrypted a returned.
     */
    public static String encryptWithCheck(String plainString, String password) {
        int plainCheck = 0;
        String cipherString = new String();

        for (int i = 0; i < plainString.length(); i++) {
            char character = plainString.charAt(i);
            plainCheck ^= character;

            char passwordChar = password.charAt((i+1) % password.length());
            int cipherChar = character ^ passwordChar;
            cipherString += (char)cipherChar;
        }
        int cipherCheck = plainCheck ^ password.charAt(0);
        cipherString = (char)cipherCheck + cipherString;

        return cipherString;
    }

    public static String decryptCheckedCipherString(String checkedCipherString, String password) {
        char cipherCheck = checkedCipherString.charAt(0);
        int plainCheck = cipherCheck ^ password.charAt(0);
        int plainCheckCalculated = 0;
        String plainString = new String();

        for (int i = 1; i < checkedCipherString.length(); i++) {
            char character = checkedCipherString.charAt(i);

            char passwordChar = password.charAt(i % password.length());
            int plainChar = character ^ passwordChar;
            plainString += (char)plainChar;

            plainCheckCalculated ^= (char)plainChar;
        }

        if (plainCheck != plainCheckCalculated) {
            return "";
        } else {
            return plainString;
        }
    }

    public static float limitFloat(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }

        return value;
    }

    public static int limitInteger(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }

        return value;
    }

    public static int min(int a, int b) {
        return a < b ? a : b;
    }

    public static MoveToAction getMoveToAction(Vector2 sourcePosition, Vector2 targetPosition, float speed) {
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(targetPosition.x, targetPosition.y);
        moveToAction.setDuration(sourcePosition.dst(targetPosition) / speed);

        return moveToAction;
    }

    public static MoveToAction getMoveToActionNext(Vector2 sourcePosition,
                                                   Vector2 targetPosition,
                                                   float speed) {
        MoveToAction moveToAction = getMoveToAction(sourcePosition, targetPosition, speed);
        sourcePosition.set(targetPosition);

        return moveToAction;
    }

    /**
     * @param base
     * @param nonNegativeExponent must be > 0
     * @return base ^ nonNegativeExponent
     */
    public static int integerPow(int base, int nonNegativeExponent) {
        if (nonNegativeExponent == 0) return 1;

        int pow = base;
        for (int i = 2; i <= nonNegativeExponent; i++) {
            pow *= base;
        }
        return pow;
    }

    private Tools() {}
}

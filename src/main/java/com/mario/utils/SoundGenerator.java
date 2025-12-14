package com.mario.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility to generate simple .wav files for game sounds.
 * Used to provide immediate audio feedback without external assets.
 */
public class SoundGenerator {

    private static final int SAMPLE_RATE = 44100;

    public static void main(String[] args) {
        String outputDir = args.length > 0 ? args[0] : "src/main/resources/sounds";
        generateSounds(outputDir);
    }

    public static void generateSounds(String outputDir) {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            // Jump: Rising tone
            createWav(new File(dir, "jump.wav"), generateTone(400, 800, 0.2));

            // Coin: High ping
            createWav(new File(dir, "coin.wav"), generateTone(1200, 1800, 0.1));

            // Powerup: Major arpeggio
            createWav(new File(dir, "powerup.wav"), generateArpeggio(new double[] { 440, 554, 659 }, 0.1));

            // Stomp: Low thud
            createWav(new File(dir, "stomp.wav"), generateTone(150, 50, 0.1));

            // Death: Falling tone
            createWav(new File(dir, "death.wav"), generateTone(800, 200, 0.5));

            // Level Complete: Victory melody
            createWav(new File(dir, "level_complete.wav"),
                    generateArpeggio(new double[] { 523, 659, 783, 1046 }, 0.15));

            // Game Over: Sad descending
            createWav(new File(dir, "game_over.wav"), generateArpeggio(new double[] { 600, 500, 400, 300 }, 0.3));

            // Menu Select: Blip
            createWav(new File(dir, "menu_select.wav"), generateTone(800, 1000, 0.05));

            // Menu Move: Click
            createWav(new File(dir, "menu_move.wav"), generateTone(300, 400, 0.03));

            // Checkpoint: Ding
            createWav(new File(dir, "checkpoint.wav"), generateTone(1000, 1500, 0.3));

            System.out.println("Generated sound files in " + outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] generateTone(double startFreq, double endFreq, double duration) {
        int numSamples = (int) (duration * SAMPLE_RATE);
        byte[] buffer = new byte[numSamples];
        double amplitude = 60.0; // Volume

        for (int i = 0; i < numSamples; i++) {
            double progress = (double) i / numSamples;
            double freq = startFreq + (endFreq - startFreq) * progress;
            double angle = 2.0 * Math.PI * freq * i / SAMPLE_RATE;
            buffer[i] = (byte) (Math.sin(angle) * amplitude);
        }
        return buffer;
    }

    private static byte[] generateArpeggio(double[] freqs, double noteDuration) {
        int noteSamples = (int) (noteDuration * SAMPLE_RATE);
        byte[] buffer = new byte[noteSamples * freqs.length];
        double amplitude = 60.0;

        for (int n = 0; n < freqs.length; n++) {
            for (int i = 0; i < noteSamples; i++) {
                double angle = 2.0 * Math.PI * freqs[n] * i / SAMPLE_RATE;
                buffer[n * noteSamples + i] = (byte) (Math.sin(angle) * amplitude);
            }
        }
        return buffer;
    }

    private static void createWav(File file, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            writeWavHeader(fos, data.length);
            fos.write(data);
        }
    }

    private static void writeWavHeader(FileOutputStream fos, int dataSize) throws IOException {
        int sampleRate = SAMPLE_RATE;
        int bitsPerSample = 8;
        int channels = 1;
        int byteRate = sampleRate * channels * bitsPerSample / 8;
        int blockAlign = channels * bitsPerSample / 8;

        fos.write("RIFF".getBytes());
        fos.write(intToByteArray(36 + dataSize));
        fos.write("WAVE".getBytes());
        fos.write("fmt ".getBytes());
        fos.write(intToByteArray(16));
        fos.write(shortToByteArray((short) 1)); // PCM
        fos.write(shortToByteArray((short) channels));
        fos.write(intToByteArray(sampleRate));
        fos.write(intToByteArray(byteRate));
        fos.write(shortToByteArray((short) blockAlign));
        fos.write(shortToByteArray((short) bitsPerSample));
        fos.write("data".getBytes());
        fos.write(intToByteArray(dataSize));
    }

    private static byte[] intToByteArray(int i) {
        return new byte[] {
                (byte) (i & 0xff),
                (byte) ((i >> 8) & 0xff),
                (byte) ((i >> 16) & 0xff),
                (byte) ((i >> 24) & 0xff)
        };
    }

    private static byte[] shortToByteArray(short s) {
        return new byte[] {
                (byte) (s & 0xff),
                (byte) ((s >> 8) & 0xff)
        };
    }
}


package com.example.aidigitrecognizer;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class DigitRecognitionActivity extends Activity {

    private DigitCanvas digitCanvas;
    private Button btnPredict;
    private Button btnClear;
    private Button btnBackDashboard;
    private TextView txtResult;

    private Interpreter interpreter;

    private static final int IMAGE_SIZE = 28;
    private static final int NUM_CLASSES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digit_recognition);

        digitCanvas = findViewById(R.id.digitCanvas);
        btnPredict = findViewById(R.id.btnPredict);
        btnClear = findViewById(R.id.btnClear);
        btnBackDashboard = findViewById(R.id.btnBackDashboard);
        txtResult = findViewById(R.id.txtResult);

        loadModel();

        btnPredict.setOnClickListener(v -> {
            if (!digitCanvas.hasDrawing()) {
                Toast.makeText(
                        this,
                        "Please draw a digit first",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (interpreter == null) {
                txtResult.setText(
                        "AI model could not be loaded."
                );
                return;
            }

            predictDigit();
        });

        btnClear.setOnClickListener(v -> {
            digitCanvas.clearCanvas();
            txtResult.setText("Prediction: --");
        });

        btnBackDashboard.setOnClickListener(v -> finish());
    }

    private void loadModel() {
        try {
            MappedByteBuffer modelBuffer = loadModelFile();
            interpreter = new Interpreter(modelBuffer);

            // Verify the model's expected input and output shapes.
            int[] inputShape =
                    interpreter.getInputTensor(0).shape();

            int[] outputShape =
                    interpreter.getOutputTensor(0).shape();

            if (inputShape.length != 4
                    || inputShape[0] != 1
                    || inputShape[1] != IMAGE_SIZE
                    || inputShape[2] != IMAGE_SIZE
                    || inputShape[3] != 1
                    || outputShape.length != 2
                    || outputShape[0] != 1
                    || outputShape[1] != NUM_CLASSES
                    || interpreter.getInputTensor(0).dataType()
                    != org.tensorflow.lite.DataType.FLOAT32
                    || interpreter.getOutputTensor(0).dataType()
                    != org.tensorflow.lite.DataType.FLOAT32) {

                interpreter.close();
                interpreter = null;

                Toast.makeText(
                        this,
                        "Model format is different. Please check the model.",
                        Toast.LENGTH_LONG
                ).show();
            }

        } catch (Exception e) {
            interpreter = null;

            Toast.makeText(
                    this,
                    "Model loading failed: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private MappedByteBuffer loadModelFile()
            throws IOException {

        try (AssetFileDescriptor fileDescriptor =
                     getAssets().openFd("mnist.tflite");
             FileInputStream inputStream =
                     new FileInputStream(
                             fileDescriptor.getFileDescriptor())) {

            FileChannel fileChannel = inputStream.getChannel();

            return fileChannel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getDeclaredLength()
            );
        }
    }

    private void predictDigit() {
        try {
            Bitmap originalBitmap =
                    digitCanvas.getDrawingBitmap();

            if (originalBitmap == null) {
                Toast.makeText(
                        this,
                        "Could not capture drawing",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    IMAGE_SIZE,
                    IMAGE_SIZE,
                    true
            );

            float[][][][] input =
                    new float[1][IMAGE_SIZE][IMAGE_SIZE][1];

            for (int y = 0; y < IMAGE_SIZE; y++) {
                for (int x = 0; x < IMAGE_SIZE; x++) {

                    int pixel = resizedBitmap.getPixel(x, y);

                    int red = Color.red(pixel);
                    int green = Color.green(pixel);
                    int blue = Color.blue(pixel);

                    float grayscale =
                            (red + green + blue) / 3.0f;

                    input[0][y][x][0] =
                            grayscale / 255.0f;
                }
            }

            float[][] output =
                    new float[1][NUM_CLASSES];

            interpreter.run(input, output);

            int predictedDigit = 0;
            float highestScore = output[0][0];

            for (int i = 1; i < NUM_CLASSES; i++) {
                if (output[0][i] > highestScore) {
                    highestScore = output[0][i];
                    predictedDigit = i;
                }
            }

            int confidence =
                    Math.round(highestScore * 100);

            txtResult.setText(
                    "Predicted Digit: " + predictedDigit
                            + "\nConfidence: " + confidence + "%"
            );

            resizedBitmap.recycle();
            originalBitmap.recycle();

        } catch (Exception e) {
            txtResult.setText("Prediction failed.");
            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }

        super.onDestroy();
    }
}

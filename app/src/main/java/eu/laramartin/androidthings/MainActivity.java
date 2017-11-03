package eu.laramartin.androidthings;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private ImageView android;
    private boolean color = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android = findViewById(R.id.android);

        // Open a peripheral connection
        PeripheralManagerService service = new PeripheralManagerService();
        Gpio ledGpio = null;
        Gpio buttonGpio = null;
        try {
            ledGpio = service.openGpio("GPIO_37");
            // Configure the peripheral
            ledGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            buttonGpio = service.openGpio("GPIO_32");
            // Configure the peripheral
            buttonGpio.setDirection(Gpio.DIRECTION_IN);
            buttonGpio.setEdgeTriggerType(Gpio.EDGE_RISING);
            final Gpio finalLedGpio = ledGpio;
            buttonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    if (color) {
                        android.setColorFilter(Color.GREEN);
                    } else {
                        android.clearColorFilter();
                    }
                    try {
                        finalLedGpio.setValue(color);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    color = !color;
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

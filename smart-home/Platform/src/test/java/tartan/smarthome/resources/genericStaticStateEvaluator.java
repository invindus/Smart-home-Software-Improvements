package tartan.smarthome.resources;

import org.junit.jupiter.api.BeforeEach;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.Hashtable;
import java.util.Map;

public class genericStaticStateEvaluator {
    protected StringBuffer log = new StringBuffer();
    protected StaticTartanStateEvaluator evaluator = new StaticTartanStateEvaluator();
    protected Map<String, Object> init_state = new Hashtable<>();


    //setup: make a default state (made defaults whatever MSE starts as)
    @BeforeEach
    void makeState() {
        // builds based on toIOTState in TartanHomeService
        this.init_state.clear();
        this.init_state.put(IoTValues.DOOR_STATE, false);
        this.init_state.put(IoTValues.LIGHT_STATE, true);
        this.init_state.put(IoTValues.HUMIDIFIER_STATE, false);
        this.init_state.put(IoTValues.ALARM_ACTIVE, false);
        this.init_state.put(IoTValues.GIVEN_PASSCODE, "1234"); //TODO: cant be bothered to find the default passcode right now
        this.init_state.put(IoTValues.ALARM_STATE, false);
        this.init_state.put(IoTValues.ALARM_DELAY, 30);
        this.init_state.put(IoTValues.HVAC_MODE, "Heater");
        this.init_state.put(IoTValues.HEATER_STATE, true);

        this.init_state.put(IoTValues.TEMP_READING, 70);
        this.init_state.put(IoTValues.HUMIDITY_READING, 50);
        this.init_state.put(IoTValues.TARGET_TEMP, 70);
        this.init_state.put(IoTValues.CHILLER_STATE, false);
        this.init_state.put(IoTValues.ALARM_PASSCODE, "1234");
        this.init_state.put(IoTValues.AWAY_TIMER, false);

        this.init_state.put(IoTValues.INTRUDER_PROXIMITY, false);
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true);

        // New smart lock values
        this.init_state.put(IoTValues.LOCK_STATE, false);
        this.init_state.put(IoTValues.LOCK_PASSCODE_STATE, false);
        this.init_state.put(IoTValues.LOCK_PASSCODE, "1234");
        this.init_state.put(IoTValues.GIVEN_LOCK_PASSCODE, "1234");

        // this.init_state.put(IoTValues.NIGHT_BEGIN, "?");
        // this.init_state.put(IoTValues.NIGHT_END, "?");
        // this.init_state.put(IoTValues.CURRENT_TIME, "?");
    }
}

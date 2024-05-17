package tartan.smarthome.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Map;

public class NightModeTest {
    private final Map<String, Object> state = firstSetup();
    private final StaticTartanStateEvaluator eval = new StaticTartanStateEvaluator();
    private final StringBuffer logger = new StringBuffer();

    private Map<String, Object> firstSetup() {
        // need these initial settings to prevent NullPointerExceptions
        Map<String, Object> originState = new Hashtable<>();
        originState.put(IoTValues.LIGHT_STATE, true);
        originState.put(IoTValues.DOOR_STATE, false);
        originState.put(IoTValues.ALARM_STATE, false);
        originState.put(IoTValues.ALARM_ACTIVE, false);
        originState.put(IoTValues.HUMIDIFIER_STATE, false);
        originState.put(IoTValues.ALARM_PASSCODE, "123");
        originState.put(IoTValues.TEMP_READING, 70);
        originState.put(IoTValues.TARGET_TEMP, 70);
        originState.put(IoTValues.HVAC_MODE, "Heater");
        originState.put(IoTValues.RESIDENT_PROXIMITY, false);
        originState.put(IoTValues.INTRUDER_PROXIMITY, false);
        return originState;
    }

    @BeforeEach
    void reset() {
        // need these initial settings to prevent NullPointerExceptions
        this.state.put(IoTValues.LOCK_STATE, false);
        this.state.put(IoTValues.NIGHT_MODE, false);
        this.state.put(IoTValues.LOCK_PASSCODE_STATE, false);
        this.state.put(IoTValues.NIGHT_MODE_START, LocalTime.of(22, 0));
        this.state.put(IoTValues.NIGHT_MODE_END, LocalTime.of(6, 0));
    }

    @Test
    void tf3() {
        // night mode start = local time, lock on, night mode on
        this.state.put(IoTValues.NIGHT_MODE_START, LocalTime.now());
        this.state.put(IoTValues.NIGHT_MODE_END, LocalTime.now().plusHours(1));
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(true);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(true);
    }

    @Test
    void tf4() {
        // night mode start = local time + 1hr, lock off, night mode off
        this.state.put(IoTValues.NIGHT_MODE_START, LocalTime.now().plusHours(1));
        this.state.put(IoTValues.NIGHT_MODE_END, LocalTime.now().plusHours(6));
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(false);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(false);
    }

    @Test
    void tf5() {
        // night mode start = local time - 1hr, lock on, night mode on
        this.state.put(IoTValues.NIGHT_MODE_START, LocalTime.now().minusHours(1));
        this.state.put(IoTValues.NIGHT_MODE_END, LocalTime.now().plusHours(6));
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(true);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(true);
    }

    @Test
    void tf6() {
        // night mode start = local time, lock on, night mode on
        LocalTime now = LocalTime.now();
        this.state.put(IoTValues.NIGHT_MODE_START, now);
        this.state.put(IoTValues.NIGHT_MODE_END, now.plusHours(6));
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(true);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(true);
    }

    @Test
    void tf7() {
        // night mode end = local time - 1hr, lock off, night mode off
        LocalTime now = LocalTime.now();
        this.state.put(IoTValues.NIGHT_MODE_START, now.minusHours(3));
        this.state.put(IoTValues.NIGHT_MODE_END, now.minusHours(1));
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(false);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(false);
    }

    @Test
    void tf8() {
        // night mode end = local time, lock off, night mode off
        LocalTime now = LocalTime.now();
        this.state.put(IoTValues.NIGHT_MODE_START, now.minusHours(3));
        this.state.put(IoTValues.NIGHT_MODE_END, now);
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(false);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(false);
    }

    @Test
    void tf9() {
        // night mode end = local time + 1hr, lock on, night mode on
        LocalTime now = LocalTime.now();
        this.state.put(IoTValues.NIGHT_MODE_START, now.minusHours(3));
        this.state.put(IoTValues.NIGHT_MODE_END, now.plusHours(1));
        Map<String, Object> resultState = this.eval.evaluateState(this.state, this.logger);
        assert resultState.get(IoTValues.LOCK_STATE).equals(true);
        assert resultState.get(IoTValues.NIGHT_MODE).equals(true);
    }

}

package tartan.smarthome.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import tartan.smarthome.core.TartanHomeValues;

import java.util.Hashtable;
import java.util.Map;

public class StaticStateEvaluatorHeaterTests extends genericStaticStateEvaluator {

    @BeforeEach
    void makeState() {
        super.makeState();
        // need these initial settings to prevent NullPointerExceptions
        this.init_state.put(IoTValues.TEMP_READING, 70);
        this.init_state.put(IoTValues.TARGET_TEMP, 70);
        this.init_state.put(IoTValues.HVAC_MODE, TartanHomeValues.HEAT);
        this.init_state.put(IoTValues.HEATER_STATE, false);
        this.init_state.put(IoTValues.CHILLER_STATE, false);
    }

    @Test
    @DisplayName("target temp == MIN_VALUE")
    void tf1() {
        // target temp == MIN_VALUE; heater off, chiller on
        this.init_state.put(IoTValues.TARGET_TEMP, -1);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(true);
    }

    @Test
    @DisplayName("target temp == 0")
    void tf2() {
        // target temp == 0, heater off, chiller on
        this.init_state.put(IoTValues.TARGET_TEMP, 0);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(true);
    }

    @Test
    @DisplayName("target temp == 50")
    void tf3() {
        // according to docs, this is the lowest temperature that the chiller can reach
        // target temp == 50, heater off, chiller on
        this.init_state.put(IoTValues.TARGET_TEMP, 50);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(true);
    }

    @Test
    @DisplayName("target temp == current temp - 1")
    void tf4() {
        // target temp == current temp - 1, heater off, chiller on
        int currentTemp = (int) this.init_state.get(IoTValues.TEMP_READING);
        this.init_state.put(IoTValues.TARGET_TEMP, currentTemp - 1);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(true);
    }

    @Test
    @DisplayName("target temp == current temp")
    void tf5() {
        // target temp == current temp, heater off, chiller off
        int currentTemp = (int) this.init_state.get(IoTValues.TEMP_READING);
        this.init_state.put(IoTValues.TARGET_TEMP, currentTemp);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(false);
    }

    @Test
    @DisplayName("target temp == current temp + 1")
    void tf6() {
        // target temp == current temp + 1, heater on, chiller off
        int currentTemp = (int) this.init_state.get(IoTValues.TEMP_READING);
        this.init_state.put(IoTValues.TARGET_TEMP, currentTemp + 1);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(true);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(false);
    }

    @Test
    @DisplayName("target temp == 80")
    void tf7() {
        // according to docs, this is the highest temperature that the heater can reach
        // target temp == 80, heater on, chiller off
        this.init_state.put(IoTValues.TARGET_TEMP, 80);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(true);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(false);
    }

    @Test
    @DisplayName("target temp == MAX_VALUE")
    void tf8() {
        // target temp == MAX_VALUE, heater on, chiller off
        this.init_state.put(IoTValues.TARGET_TEMP, Integer.MAX_VALUE);
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(true);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(false);
    }
}

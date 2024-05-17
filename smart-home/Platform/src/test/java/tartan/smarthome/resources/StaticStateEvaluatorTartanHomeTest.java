package tartan.smarthome.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import tartan.smarthome.core.TartanHomeValues;

import java.util.Hashtable;
import java.util.Map;

public class StaticStateEvaluatorTartanHomeTest extends genericStaticStateEvaluator {

    @BeforeEach
    void makeState() {
        super.makeState();

        this.init_state.put(IoTValues.TEMP_READING, 70);
        this.init_state.put(IoTValues.TARGET_TEMP, 70);
        this.init_state.put(IoTValues.HVAC_MODE, TartanHomeValues.HEAT);
        this.init_state.put(IoTValues.HEATER_STATE, false);
        this.init_state.put(IoTValues.CHILLER_STATE, false);
    }

    @Test
    void testHeater() {
        // target temp == current temp
        this.init_state.put(IoTValues.TARGET_TEMP, this.init_state.get(IoTValues.TEMP_READING));
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);

        // target temp > current temp
        this.init_state.put(IoTValues.TARGET_TEMP, 80);
        resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(true);

        // target temp < current temp
        this.init_state.put(IoTValues.TARGET_TEMP, 60);
        resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.HEATER_STATE).equals(false);
    }

    @Test
    void testChiller() {
        // target temp == current temp
        Map<String, Object> resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(false);

        // target temp < current temp
        this.init_state.put(IoTValues.TARGET_TEMP, 60);
        resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(true);

        // target temp > current temp
        this.init_state.put(IoTValues.TARGET_TEMP, 80);
        resultState = this.evaluator.evaluateState(this.init_state, this.log);
        assert resultState.get(IoTValues.CHILLER_STATE).equals(false);
    }
}

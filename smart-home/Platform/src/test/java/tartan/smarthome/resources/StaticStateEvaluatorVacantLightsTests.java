package tartan.smarthome.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.Hashtable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticStateEvaluatorVacantLightsTests extends genericStaticStateEvaluator {

    // case: Light_State = false, Proximity_State = false
    // expected: Light_State = false
    @Test
    void evaluateStateR1Case1() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);
        this.init_state.put(IoTValues.LIGHT_STATE, false);

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.LIGHT_STATE));
    }

    // case: Light_State = true, Proximity_State = false
    // expected: Light_State = false
    @Test
    void evaluateStateR1Case2() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);
        this.init_state.put(IoTValues.LIGHT_STATE, true);

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.LIGHT_STATE));
    }

    // this case interacts with r7, which turns on the lights if the house becomes occupied
    // case: Light_State = false, Proximity_State = true
    // expected: Light_State = true
    @Test
    void evaluateStateR1Case3() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true);
        this.init_state.put(IoTValues.LIGHT_STATE, false);

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(true, result.get(IoTValues.LIGHT_STATE));
    }

    // case: Light_State = true, Proximity_State = true
    // expected: Light_State = true
    @Test
    void evaluateStateR1Case4() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true);
        this.init_state.put(IoTValues.LIGHT_STATE, true);

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(true, result.get(IoTValues.LIGHT_STATE));
    }


}

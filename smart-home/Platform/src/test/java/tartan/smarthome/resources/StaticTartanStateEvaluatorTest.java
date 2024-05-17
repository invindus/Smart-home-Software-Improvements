package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import tartan.smarthome.resources.iotcontroller.IoTValues;

class StaticTartanStateEvaluatorTest extends genericStaticStateEvaluator{

    // R1: If the house is vacant, then the light cannot be turned on.
    @Test
    void evaluateStateR1() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);
        this.init_state.put(IoTValues.LIGHT_STATE, true);

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.LIGHT_STATE));

        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true);
        result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(true, result.get(IoTValues.LIGHT_STATE));
    }

    // R3: If the house is vacant, then close the door.
    // Assumes alarm is OFF.
    @Test
    void evaluateStateR3(){
        // VACANT --> CLOSE
        this.init_state.put(IoTValues.ALARM_STATE, false);      // ALARM OFF
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);  // VACANT
        this.init_state.put(IoTValues.DOOR_STATE, false);       // CLOSE DOOR

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE));

        // VACANT --> OPEN --> FAIL
        this.init_state.put(IoTValues.DOOR_STATE, true);       // OPEN DOOR
        result = evaluator.evaluateState(this.init_state, log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE));
    }
  
    // R5: If the house is empty, then start the away timer.
    @Test
    void testR5() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false); // VACANT (House is empty)

        // Evaluate the initial state and assert the away timer state
        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(true, result.get(IoTValues.AWAY_TIMER)); // Initially, the away timer should be off

        // Change the state to reflect the house being empty (VACANT) and re-evaluate
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true); // Not Vacant (House is occupied)
        result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.AWAY_TIMER)); // Check if the away timer started
    }
    
    @Test
    void testR5case1() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true); // OCCUPIED (House is not empty)

        // Evaluate the initial state and assert the away timer state
        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.AWAY_TIMER)); // Check if the away timer started
    }
    
    @Test
    void testR5case2() {
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false); // VACANT (House is empty)

        // Evaluate the initial state and assert the away timer state
        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(true, result.get(IoTValues.AWAY_TIMER)); // Check if the away timer started
    }  
}

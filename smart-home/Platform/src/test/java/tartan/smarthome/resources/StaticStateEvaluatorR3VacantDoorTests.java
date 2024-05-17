package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticStateEvaluatorR3VacantDoorTests extends genericStaticStateEvaluator{
    // Alarm:       Enabled (1) ,   Disabled (0),   Activated (A)
    // Proximity:   Occupied (1),   Vacant (0)
    // Door:        Open (1)    ,   Closed (0)
    @Test
    // Alarm: 1, Proximity: 0, Door: 0
    //      Expected Output: Do nothing, door already closed.
    void evaluateStateR3_100(){
        this.init_state.put(IoTValues.ALARM_STATE, true);       // ALARM ENABLED
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);  // VACANT
        this.init_state.put(IoTValues.DOOR_STATE, false);       // DOOR CLOSED

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE), "Door was already closed.");
    }
    @Test
    // Alarm: 1, Proximity: 0, Door: 1
    //      Expected Output: Close door.
    void evaluateStateR3_101(){
        this.init_state.put(IoTValues.ALARM_STATE, true);       // ALARM ENABLED
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);  // VACANT
        this.init_state.put(IoTValues.DOOR_STATE, true);        // DOOR OPEN

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE), "Door should be closed.");
    }
    @Test
    // Alarm: 0, Proximity: 0, Door: 1
    //      Expected Output: After set time passed, CLOSE DOOR, turn off light, and enable alarm.
    void evaluateStateR3_001(){
        this.init_state.put(IoTValues.ALARM_STATE, false);      // ALARM DISABLED
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);  // VACANT
        this.init_state.put(IoTValues.DOOR_STATE, true);        // DOOR OPEN

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE), "Door should be closed.");
    }
    @Test
    // Alarm: 0, Proximity: 0, Door: 0
    //      Expected Output: After set time passed, close door, turn off light, and ENABLE ALARM.
    void evaluateStateR3_000(){
        this.init_state.put(IoTValues.ALARM_STATE, false);       // ALARM DISABLED
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);   // VACANT
        this.init_state.put(IoTValues.DOOR_STATE, false);        // DOOR CLOSED

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE), "Door was already closed.");
    }

    @Test
        // Alarm: A, Proximity: 0, Door: 1
        //      Expected Output: After set time passed, close door, turn off light, and ENABLE ALARM.
    void evaluateStateR3_A01(){
        this.init_state.put(IoTValues.ALARM_ACTIVE, true);       // ALARM ACTIVATED
        this.init_state.put(IoTValues.RESIDENT_PROXIMITY, false);   // VACANT
        this.init_state.put(IoTValues.DOOR_STATE, true);        // DOOR OPEN

        Map<String, Object> result = this.evaluator.evaluateState(this.init_state, this.log);
        assertEquals(false, result.get(IoTValues.DOOR_STATE), "INTRUDER. CLOSING DOOR.");
    }

    // UNUSED TEST CASES

    // Alarm: 0, Proximity: 1, Door: 1
    //      Expected Output: Alarm disabled, occupied house, door open --> do nothing

    // Alarm: 0, Proximity: 1, Door: 0
    //      Expected Output: Alarm disabled, occupied house, door closed --> do nothing

    // Alarm: 1, Proximity: 1, Door: 1
    //      Expected Output: Door opened when alarm enabled --> activate alarm.
    // MOVE TO R4: If the alarm is enabled and the house gets suddenly occupied (i.e., someone is detected by the proximity sensor), then sound the alarm.
    // TODO: What if door was accidentally opened by the system?

    // Alarm: 1, Proximity: 1, Door: 0
    //      Expected Output: Suddenly occupied --> activate alarm.
    // MOVE TO R4: If the alarm is enabled and the house gets suddenly occupied (i.e., someone is detected by the proximity sensor), then sound the alarm.
    // TODO: What if occupants wanted to set alarm while at home (i.e going to bed)?

    // POTENTIAL TESTS
    // - actuators to lock/unlock doors not used
    // - door gets blocked, system keeps trying to close


}

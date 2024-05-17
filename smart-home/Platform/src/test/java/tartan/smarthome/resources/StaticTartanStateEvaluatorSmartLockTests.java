package tartan.smarthome.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class StaticStateEvaluatorSmartLockTests extends genericStaticStateEvaluator {

	private Map<String, Object> new_state;

	@BeforeEach
	void makeState() {
		super.makeState();
	}

	@Test
	void passcodeTests() {
		// By default, the passcode is disabled
		// Test that the lock can be changed without the right passcode
		this.init_state.put(IoTValues.LOCK_STATE, false);
		this.init_state.put(IoTValues.LOCK_PASSCODE, "1234");
		this.init_state.put(IoTValues.GIVEN_LOCK_PASSCODE, "wrong");
		this.init_state.put(IoTValues.REQUESTED_LOCK_STATE, true);

		this.new_state = this.evaluator.evaluateState(this.init_state, this.log);
		assertEquals(true, this.new_state.get(IoTValues.LOCK_STATE), "Door should have been locked");

		// Enable the passcode
		// Test that the lock requires the right passcode
		this.init_state.put(IoTValues.LOCK_PASSCODE_STATE, true);

		this.new_state = this.evaluator.evaluateState(this.init_state, this.log);
		assertEquals(false, this.new_state.get(IoTValues.LOCK_STATE), "Door should not have been locked");

		// Now give the correct passcode
		this.init_state.put(IoTValues.GIVEN_LOCK_PASSCODE, "1234");

		this.new_state = this.evaluator.evaluateState(this.init_state, this.log);
		assertEquals(true, this.new_state.get(IoTValues.LOCK_STATE), "Door should have been locked");
	}

	@Test
	void residentProximityTest() {
		// Lock the door and put a nearby resident
		this.init_state.put(IoTValues.LOCK_STATE, true);
		this.init_state.put(IoTValues.RESIDENT_PROXIMITY, true);
		// this.init_state.put(IoTValues.REQUESTED_LOCK_STATE, null);

		this.new_state = this.evaluator.evaluateState(this.init_state, this.log);
		assertEquals(false, this.new_state.get(IoTValues.LOCK_STATE), "Door did not auto-unlock for the user");
	}

	@Test
	void intruderProximityTest() {
		// Unlock the door and put a nearby intruder
		this.init_state.put(IoTValues.LOCK_STATE, false);
		this.init_state.put(IoTValues.INTRUDER_PROXIMITY, true);

		this.new_state = this.evaluator.evaluateState(this.init_state, this.log);
		assertEquals(true, this.new_state.get(IoTValues.LOCK_STATE), "Door did not auto-lock when intruder was detected");
	}
}

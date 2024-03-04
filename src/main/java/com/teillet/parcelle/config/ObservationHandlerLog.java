package com.teillet.parcelle.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

// Example of plugging in a custom handler that in this case will print a statement before and after all observations take place
@Component
@Slf4j
public class ObservationHandlerLog implements ObservationHandler<Observation.Context> {
	@Override
	public void onStart(Observation.Context context) {
		log.info("Before running the observation for context [{}]", context.getName());
	}

	@Override
	public void onStop(Observation.Context context) {
		log.info("After running the observation for context [{}]", context.getName());
	}

	@Override
	public boolean supportsContext(@NotNull Observation.Context context) {
		return true;
	}
//
//	private String getUserTypeFromContext(Observation.Context context) {
//		return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
//				.filter(keyValue -> "userType".equals(keyValue.getKey()))
//				.map(KeyValue::getValue)
//				.findFirst()
//				.orElse("UNKNOWN");
//	}
}

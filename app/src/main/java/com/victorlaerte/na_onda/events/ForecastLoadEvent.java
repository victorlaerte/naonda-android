package com.victorlaerte.na_onda.events;

import com.victorlaerte.na_onda.model.CompleteForecast;
import com.victorlaerte.na_onda.model.Forecast;

/**
 * @author Victor Oliveira
 */

public class ForecastLoadEvent {

	private CompleteForecast completeForecast;

	public ForecastLoadEvent(CompleteForecast completeForecast) {

		this.completeForecast = completeForecast;
	}

	public CompleteForecast getCompleteForecast() {

		return this.completeForecast;
	}
}

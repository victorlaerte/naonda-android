package com.victorlaerte.na_onda.model;

import android.support.annotation.StringRes;

import com.victorlaerte.na_onda.R;

/**
 * @author Victor Oliveira
 */

public enum DayOfWeek {

	DOMINGO(1, R.string.sunday, R.string.sunday_acronym),
	SEGUNDA(2, R.string.monday, R.string.monday_acronym),
	TERCA(3, R.string.tuesday, R.string.tuesday_acronym),
	QUARTA(4, R.string.wednesday, R.string.wednesday_acronym),
	QUINTA(5, R.string.thursday, R.string.thursday_acronym),
	SEXTA(6, R.string.friday, R.string.friday_acronym),
	SABADO(7, R.string.saturday, R.string.saturday_acronym);

	private int dayWeek;
	private int dayWeekName;
	private int dayAcronym;

	private DayOfWeek(int dayWeek, @StringRes int dayWeekName, @StringRes int dayWeekAcronym) {

		this.dayWeek = dayWeek;
		this.dayWeekName = dayWeekName;
		this.dayAcronym = dayWeekAcronym;
	}

	public static DayOfWeek fromInt(int dayWeek) {

		DayOfWeek dayOfWeek = DayOfWeek.DOMINGO;

		switch (dayWeek) {

			case 1:
				dayOfWeek = DOMINGO;
				break;
			case 2:
				dayOfWeek = SEGUNDA;
				break;
			case 3:
				dayOfWeek = TERCA;
				break;
			case 4:
				dayOfWeek = QUARTA;
				break;
			case 5:
				dayOfWeek = QUINTA;
				break;
			case 6:
				dayOfWeek = SEXTA;
				break;
			case 7:
				dayOfWeek = SABADO;
				break;
		}

		return dayOfWeek;
	}

	public int getDayWeekName() {

		return dayWeekName;
	}

	public int getDayWeek() {
		return dayWeek;
	}

	public int getDayAcronym(){
		return dayAcronym;
	}
}

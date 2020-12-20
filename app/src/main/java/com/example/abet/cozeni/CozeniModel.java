package com.example.abet.cozeni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * Created by AbeT on 2015/03/26.
 */
public class CozeniModel {
	private static final int unitsJpy[] = {
			10000, 5000, 1000, 500, 100, 50, 10, 5, 1,
	};
	private String mCurrency;
	private Vector<Integer> mUnits;

	public CozeniModel(String currency) {
		super();
		assert (currency == "JPY"); // @todo: validate mCurrency & mUnits depending on specified currency.
		mCurrency = currency;
		mUnits = new Vector<>();
		for (int uu : unitsJpy) {
			mUnits.add(uu);
		}
	}

	public String getCurrency() {
		return mCurrency;
	}

	private void solvePrice(Integer remain, Integer payment, Vector<Integer> suggestions) {
		Vector<Integer> counts = getCounts(remain);
		for (int ii = counts.size() - 1; ii >= 0; ii--) {
			if (counts.get(ii) > 0) {
				remain -= counts.get(ii) * mUnits.get(ii);
				if (ii > 0) {
					// pay roughly.
					solvePrice(remain + mUnits.get(ii - 1), payment, suggestions);
				}

				// pay exactly.
				payment += counts.get(ii) * mUnits.get(ii);
				if (remain > 0) {
					solvePrice(remain, payment, suggestions);
				} else {
					suggestions.add(payment);
				}
				break;
			}
		}
	}

	public Vector<Integer> solvePrice(final Integer amount) {
		Vector<Integer> suggestions = new Vector<>();
		solvePrice(amount, 0, suggestions);

		// sort by weight.
		ArrayList<Integer> sortedSuggestions = new ArrayList<>(suggestions);
		Collections.sort(sortedSuggestions, new Comparator<Integer>() {
			@Override
			public int compare(Integer lhs, Integer rhs) {
				Integer weightDeltaLhs = getWeight(lhs - amount) - getWeight(lhs);
				Integer weightDeltaRhs = getWeight(rhs - amount) - getWeight(rhs);
				return weightDeltaLhs == weightDeltaRhs ? lhs.compareTo(rhs) : weightDeltaLhs.compareTo(weightDeltaRhs);
			}
		});

		// delete exact payment from suggestions.
		sortedSuggestions.remove(amount);
		return new Vector<>(sortedSuggestions);
	}

	public Integer getWeight(Integer amount) {
		Integer weight = 0;
		for (int unit : mUnits) {
			int count = amount / unit;
			weight += count;
			amount -= count * unit;
		}
		return weight;
	}

	public Vector<Integer> getCounts(Integer amount) {
		Vector<Integer> counts = new Vector<>();
		for (int unit : mUnits) {
			int count = amount / unit;
			counts.add(count);
			amount -= count * unit;
		}
		return counts;
	}
}

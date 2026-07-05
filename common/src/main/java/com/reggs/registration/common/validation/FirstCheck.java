package com.reggs.registration.common.validation;

/**
 * Validation group marker for the cheapest possible checks:
 * "is this field present, non-blank, and well-formed?"
 *
 * This group always runs FIRST in the @GroupSequence. If anything here
 * fails, none of the SecondCheck or Default-group constraints are
 * evaluated at all - see the GroupSequence concerns in the JBE study
 * guide regarding "fail fast vs report everything."
 */
public interface FirstCheck {
}

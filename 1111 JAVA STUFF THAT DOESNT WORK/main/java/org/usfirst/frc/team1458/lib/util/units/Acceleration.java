package org.usfirst.frc.team1458.lib.util.units;

import org.usfirst.frc.team1458.lib.util.annotation.Immutable;

/**
 * Convenience wrapper for acceleration.
 *
 * @author asinghani
 */
@Immutable
public interface Acceleration extends Rate<Rate<Distance>> {}

package edu.rice.starvote;

/**
 * Describes the device hardware state. This is not displayed externally; for ballot box status, see `BallotStatus`.
 *
 * @see BallotStatus
 * @author luejerry
 */
public enum DeviceStatus {
    OFFLINE, READY, BUSY, ERROR
}

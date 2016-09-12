package edu.rice.starvote.ballotbox;

/**
 * Describes the device hardware state. This is not displayed externally; for ballot box status, see `BallotStatus`.
 *
 * @see BallotStatus
 * @author luejerry
 */
public enum DeviceStatus {
    OFFLINE, READY, BUSY, ERROR
}

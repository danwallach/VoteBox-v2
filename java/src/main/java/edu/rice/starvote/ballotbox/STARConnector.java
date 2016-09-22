package edu.rice.starvote.ballotbox;

import auditorium.NetworkException;
import votebox.AuditoriumParams;
import votebox.events.*;

import java.util.concurrent.*;

/**
 * Created by luej on 9/6/16.
 */
public class STARConnector {

    private final VoteBoxAuditoriumConnector auditorium;
    private final int serial;
    private final AuditoriumParams _constants;
    private final BlockingQueue<BallotResult> resultQueue = new LinkedBlockingQueue<>(1);
    private final ScheduledExecutorService heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
    private boolean activated = false;
    private boolean connected = false;
    private int label;
    private int battery = 100;
    private int numConnections = 0;
    private String lastBID = "";

    public STARConnector(int serial, String launchCode) throws NetworkException {
        _constants = new AuditoriumParams("bs.conf");
        if (serial == -1) {
            this.serial = _constants.getDefaultSerialNumber();
            if (this.serial == -1) {
                throw new RuntimeException("No machineID provided");
            }
        } else {
            this.serial = serial;
        }
        auditorium = new VoteBoxAuditoriumConnector(serial, _constants, launchCode,
                BallotScanAcceptedEvent.getMatcher(),
                BallotScanRejectedEvent.getMatcher(),
                StartScannerEvent.getMatcher(),
                PollMachinesEvent.getMatcher());
        auditorium.connect();
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            if (connected) {
                auditorium.announce(getStatus());
            }
        }, 0, 5, TimeUnit.MINUTES);
        auditorium.addListener(new VoteBoxEventListener() {
            @Override
            public void assignLabel(AssignLabelEvent e) {
                if (e.getTargetSerial() == serial) {
                    label = e.getLabel();
                }
            }

            @Override
            public void joined(JoinEvent e) {
                numConnections++;
                connected = true;
            }

            @Override
            public void left(LeaveEvent e) {
                numConnections --;
                connected = numConnections > 0;
            }

            @Override
            public void ballotAccepted(BallotScanAcceptedEvent e) {
                if (lastBID.equals(e.getBID())) {
                    System.out.println("Ballot " + lastBID + " accepted by supervisor");
                    try {
                        resultQueue.put(BallotResult.ACCEPT);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void ballotRejected(BallotScanRejectedEvent e) {
                if (lastBID.equals(e.getBID())) {
                    System.out.println("Ballot " + lastBID + " rejected by supervisor");
                    try {
                        resultQueue.put(BallotResult.REJECT);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void scannerStart(StartScannerEvent startScannerEvent) {
                activated = true;
            }

            @Override
            public void pollMachines(PollMachinesEvent pollMachinesEvent) {
                auditorium.announce(getStatus());
            }

            @Override public void activated(ActivatedEvent e) {}
            @Override public void authorizedToCast(AuthorizedToCastEvent e) {}
            @Override public void ballotReceived(BallotReceivedEvent e) {}
            @Override public void castCommittedBallot(CastCommittedBallotEvent e) {}
            @Override public void commitBallot(CommitBallotEvent e) {}
            @Override public void lastPollsOpen(LastPollsOpenEvent e) {}
            @Override public void overrideCancel(OverrideCancelEvent e) {}
            @Override public void overrideCancelConfirm(OverrideCancelConfirmEvent e) {}
            @Override public void overrideCancelDeny(OverrideCancelDenyEvent e) {}
            @Override public void overrideCommit(OverrideCommitEvent e) {}
            @Override public void overrideCommitConfirm(OverrideCommitConfirmEvent e) {}
            @Override public void overrideCommitDeny(OverrideCommitDenyEvent e) {}
            @Override public void pollsClosed(PollsClosedEvent e) {}
            @Override public void pollsOpen(PollsOpenEvent e) {}
            @Override public void pollsOpenQ(PollsOpenQEvent e) {}
            @Override public void supervisor(SupervisorEvent e) {}
            @Override public void votebox(VoteBoxEvent e) {}
            @Override public void ballotScanner(BallotScannerEvent e) {}
            @Override public void ballotScanned(BallotScannedEvent e) {}
            @Override public void pinEntered(PINEnteredEvent event) {}
            @Override public void invalidPin(InvalidPinEvent event) {}
            @Override public void pollStatus(PollStatusEvent pollStatusEvent) {}
            @Override public void ballotPrinting(BallotPrintingEvent ballotPrintingEvent) {}
            @Override public void ballotPrintSuccess(BallotPrintSuccessEvent ballotPrintSuccessEvent) {}
            @Override public void ballotPrintFail(BallotPrintFailEvent ballotPrintFailEvent) {}
            @Override public void spoilBallot(SpoilBallotEvent spoilBallotEvent) {}
            @Override public void announceProvisionalBallot(ProvisionalBallotEvent provisionalBallotEvent) {}
            @Override public void provisionalAuthorizedToCast(ProvisionalAuthorizeEvent provisionalAuthorizeEvent) {}
            @Override public void provisionalCommitBallot(ProvisionalCommitEvent provisionalCommitEvent) {}
            @Override public void tapMachine(TapMachineEvent tapMachineEvent) {}
            @Override public void startUpload(StartUploadEvent startUploadEvent) {}
            @Override public void completedUpload(CompletedUploadEvent completedUploadEvent) {}
            @Override public void uploadBallots(BallotUploadEvent ballotUploadEvent) {}
        });
    }

    public BallotScannerEvent getStatus() {
        String status = activated ? "active" : "inactive";
        return new BallotScannerEvent(serial, label, status, battery, -1, -1);
    }

    public BallotResult validate(String BID, long millis) {
        auditorium.announce(new BallotScannedEvent(serial, BID));
        try {
            BallotResult result = resultQueue.poll(millis, TimeUnit.MILLISECONDS);
            return (result == null) ? BallotResult.TIMEOUT : result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return BallotResult.TIMEOUT;
        }
    }
}

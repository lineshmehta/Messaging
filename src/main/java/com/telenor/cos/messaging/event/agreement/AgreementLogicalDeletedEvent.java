package com.telenor.cos.messaging.event.agreement;

import com.telenor.cos.messaging.event.Agreement;

/**
 * Creates event for logical delete of agreement.
 */
public class AgreementLogicalDeletedEvent extends AgreementEvent {

    /**
     *
     */
    private static final long serialVersionUID = -7228808147230812395L;

    /**
     * Constructor 
     * @param agreement the agreement
     */
    public AgreementLogicalDeletedEvent(Agreement agreement) {
        super(agreement, ACTION.LOGICAL_DELETE);
    }

}

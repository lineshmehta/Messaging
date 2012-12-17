package com.telenor.cos.messaging.event.agreement;

import com.telenor.cos.messaging.event.Agreement;

/**
 * Creates event for New agreement.
 */
public class AgreementNewEvent extends AgreementEvent {

    /**
     *
     */
    private static final long serialVersionUID = 1626059767778018437L;

    /**
     * Constructor 
     * @param agreement the agreement
     */
    public AgreementNewEvent(Agreement agreement) {
        super(agreement, ACTION.CREATED);
    }
}

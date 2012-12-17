package com.telenor.cos.messaging.web.form;

import java.util.UUID;

public class AgreementForm {

    private Long agreementId;
    private Long custUnitNumber;
    
    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }
    
    public Long getCustUnitNumber() {
        return custUnitNumber;
    }

    public void setCustUnitNumber(Long custUnitNumber) {
        this.custUnitNumber = custUnitNumber;
    }

    /**
     * creates an xml representation of this form
     *
     * @param eventType insert, update or whatever
     * @return yes, it does sometimes
     */
    public String toNewXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
                + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n" 
                + "    <" + eventType.toString() + " schema=\"AGREEMENT_NEW\">\n" 
                + "        <values>\n"
                + "             <cell name=\"AGREEMENT_ID\" type=\"NUMERIC\">" + agreementId + "</cell>"
                + "             <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">" + custUnitNumber + "</cell>"
                + "             <cell name=\"AGR_ID\" type=\"CHAR\">NULL</cell>"
                + "             <cell name=\"AGR_TYPE_ID\" type=\"CHAR\">GA</cell>"
                + "             <cell name=\"AGR_NAME\" type=\"VARCHAR\">NULL</cell>"
                + "             <cell name=\"STATUS_ID\" type=\"CHAR\">NULL</cell>"
                + "        </values>\n" 
                + "    </" + eventType.toString() + ">\n" 
                + "</tran>";
    }
    
    /**
     * creates an xml representation of this form
     *
     * @param eventType insert, update or whatever
     * @return yes, it does sometimes
     */
    public String toUpdateXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
                + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n"
                + "    <" + eventType.toString() + " schema=\"AGREEMENT_NEW\">\n"
                + "        <values>\n"
                + "             <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell> "
                + "        </values>\n" 
                + "        <oldValues>\n"
                + "             <cell name=\"AGREEMENT_ID\" type=\"NUMERIC\">" + agreementId + "</cell>"
                + "             <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">NULL</cell>"
                + "             <cell name=\"AGR_ID\" type=\"CHAR\">NULL</cell>"
                + "             <cell name=\"AGR_TYPE_ID\" type=\"CHAR\">GA</cell>"
                + "             <cell name=\"AGR_NAME\" type=\"VARCHAR\">NULL</cell>"
                + "             <cell name=\"STATUS_ID\" type=\"CHAR\">NULL</cell>"
                + "        </oldValues>\n" 
                + "    </" + eventType.toString() + ">\n" 
                + "</tran>";
    }
    
}
